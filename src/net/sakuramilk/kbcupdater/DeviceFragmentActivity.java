/*
 * Copyright (C) 2012 sakuramilk <c.sakuramilk@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sakuramilk.kbcupdater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class DeviceFragmentActivity extends PreferenceFragment implements Preference.OnPreferenceClickListener {

	private Context mContext;
	private PreferenceManager mPrefManager;
	private PreferenceScreen mRootPref;
	private List<EntryItem> mItemList;
	
	public void update(final String src) {
        final String url = this.getArguments().getString("url");
        final Preference.OnPreferenceClickListener listener = this;
        
        mRootPref.removeAll();
        
        final ProgressDialog dlg = new ProgressDialog(mContext);
        dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlg.setTitle(R.string.app_name);
        dlg.setMessage(getText(R.string.wait_for_result));
        dlg.show();

        final Handler handler = new Handler() {
			@Override
            public void handleMessage(Message msg) {
    			dlg.dismiss();

    			HashMap<String, ArrayList<Preference>> categoryList = new HashMap<String, ArrayList<Preference>>();
    			int updateCount = 0;

    			for (EntryItem item : mItemList) {
    				ArrayList<Preference> list = categoryList.get(item.category);
    				if (list == null) {
    					list = new ArrayList<Preference>();
    					PreferenceCategory prefCategory = new PreferenceCategory(mContext);
    					prefCategory.setTitle(item.category);
    					list.add(prefCategory);
    					categoryList.put(item.category, list);
    				}

    				EntryItemPreference pref = new EntryItemPreference(mContext);
    				pref.setEntryItem(item);
    				pref.setOnPreferenceClickListener(listener);
    				list.add(pref);
    				if (item.update) {
    					updateCount++;
    				}
    			}

    			ArrayList<Map.Entry<String, ArrayList<Preference>>> entries = new ArrayList<Entry<String, ArrayList<Preference>>>(categoryList.entrySet());
    			Collections.sort(entries, new Comparator<Object>(){
					@SuppressWarnings("rawtypes")
					public int compare(Object obj1, Object obj2){
    			        Map.Entry ent1 =(Map.Entry)obj1;
    			        Map.Entry ent2 =(Map.Entry)obj2;
    			        String val1 = (String) ent1.getKey();
    			        String val2 = (String) ent2.getKey();
    			        return val1.compareTo(val2);
    			    }
    			});

    			for (Entry<String, ArrayList<Preference>> list : entries) {
    				for (Preference pref : list.getValue()) {
    					mRootPref.addPreference(pref);
    				}
    			}
    			
    			UpdatePreference preference = new UpdatePreference(mContext);
    			preference.setTitle(R.string.update_list);
    			preference.setOnPreferenceClickListener(listener);
    			mRootPref.addPreference(preference);
    			
    			if ("update".equals(src)) {
	    			Toast.makeText(mContext,
	    					String.valueOf(updateCount) + mContext.getText(R.string.update_count),
	    					Toast.LENGTH_LONG).show();
    			}
            }
        };

        Thread thread = new Thread(new Runnable() {
			@Override
            public void run() {
				mItemList = EntryItemManager.load(mContext, url, src);
				handler.sendEmptyMessage(0);
            }
        });
        thread.start();
		
        setPreferenceScreen(mRootPref);		
	}
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = getActivity();
        mPrefManager = this.getPreferenceManager();
        mRootPref = mPrefManager.createPreferenceScreen(mContext);
        
        update("cache");
    }

	@Override
	public boolean onPreferenceClick(Preference preference) {
		
		if (preference instanceof EntryItemPreference) {
			final String url = ((EntryItemPreference)preference).getEnteryItem().url;
			if (Misc.isNullOfEmpty(url)) {
				return false;
			}
	
			if (url.indexOf("www1.axfc.net") > 0) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			} else {
	            final ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
	            confirmDialog.setResultListener(new ConfirmDialog.ResultListener() {
	                @Override
	                public void onYes() {
	        			String fileName = url.substring(url.lastIndexOf("/") + 1);
	
	        			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
	        			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
	        			request.allowScanningByMediaScanner();
	        			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	
	        			DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
	        			dm.enqueue(request);
	                }
	            });
	            confirmDialog.show(R.string.comfirm_download_title, R.string.comfirm_download_summary);
			}
		} else if (preference instanceof UpdatePreference) {
			update("update");
		}

		return false;
	}
}
