package net.sakuramilk.kbcupdater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
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

@SuppressLint("HandlerLeak")
public class DeviceFragmentActivity extends PreferenceFragment implements Preference.OnPreferenceClickListener {

	private PreferenceManager mPrefManager;
	private PreferenceScreen mRootPref;
	private List<EntryItem> mItemList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getActivity();
        final String url = this.getArguments().getString("url");
        final Preference.OnPreferenceClickListener listener = this;

        mPrefManager = this.getPreferenceManager();
        mRootPref = mPrefManager.createPreferenceScreen(context);

        final ProgressDialog dlg = new ProgressDialog(context);
        dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlg.setTitle(R.string.app_name);
        dlg.setMessage(getText(R.string.wait_for_result));
        dlg.show();

        final Handler handler = new Handler() {
    		@Override
            public void handleMessage(Message msg) {
    			dlg.dismiss();
    			
    			HashMap<String, ArrayList<Preference> > categoryList = new HashMap<String, ArrayList<Preference> >();
                
    			for (EntryItem item : mItemList) {
    				ArrayList<Preference> list = categoryList.get(item.category);
    				if (list == null) {
    					list = new ArrayList<Preference>();
    					PreferenceCategory prefCategory = new PreferenceCategory(context);
    					prefCategory.setTitle(item.category);
    					list.add(prefCategory);
    					categoryList.put(item.category, list);
    				}

    				EntryItemPreference pref = new EntryItemPreference(context);
    				pref.setEntryItem(item);
    				pref.setOnPreferenceClickListener(listener);
    				list.add(pref);
    			}
    			
    			ArrayList<Map.Entry<String, ArrayList<Preference>> > entries = new ArrayList<Entry<String, ArrayList<Preference>>>(categoryList.entrySet());
    			Collections.sort(entries, new Comparator(){
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
            }
        };

        Thread thread = new Thread(new Runnable() {
			@Override
            public void run() {
				WikiPageParser parser = new WikiPageParser();
				parser.parse(url);
				mItemList = parser.getEntryItemList();
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();

        setPreferenceScreen(mRootPref);
    }

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String url = ((EntryItemPreference)preference).getEnteryItem().url;
		
		if (url.indexOf("www1.axfc.net") != 0) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			
		} else {
			String fileName = url.substring(url.lastIndexOf("/") + 1);
	
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	
			DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
			dm.enqueue(request);
		}

		return false;
	}
}
