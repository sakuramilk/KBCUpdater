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

import android.os.Bundle;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainFragmentActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private TabsFragmentAdapter mPagerAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewpager);
        setContentView(mViewPager);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        bar.setTitle(R.string.app_name);

        mPagerAdapter = new TabsFragmentAdapter(this, mViewPager);
        
        Bundle args = new Bundle();
        args.putString("url", Constant.LIST_URL_SC06D);
        mPagerAdapter.addTab(bar.newTab().setText("SC-06D"),
                DeviceFragmentActivity.class, args);
        
        /*
        args = new Bundle();
        args.putString("url", "https://github.com/kbc-developers/release/wiki/sc04d");
        mPagerAdapter.addTab(bar.newTab().setText("SC-04D"),
                DeviceFragmentActivity.class, args);
        */
        
        args = new Bundle();
        args.putString("url", Constant.LIST_URL_SC02C);
        mPagerAdapter.addTab(bar.newTab().setText("SC-02C"),
                DeviceFragmentActivity.class, args);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Update list");
        MenuItem actionItem = menu.add("Update list");
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        actionItem.setIcon(android.R.drawable.ic_menu_manage);
     
        return true;
    }
     
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = new Intent(this, SettingPreferenceActivity.class);
    	startActivity(intent);
    	
        return true;
    }
}
