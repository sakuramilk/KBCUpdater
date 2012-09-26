package net.sakuramilk.kbcupdater;

import android.os.Bundle;
import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

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
        bar.setTitle(getText(R.string.app_name) + " V0.1.0");

        mPagerAdapter = new TabsFragmentAdapter(this, mViewPager);
        
        Bundle args = new Bundle();
        args.putString("url", "https://github.com/kbc-developers/release/wiki/sc06d");
        mPagerAdapter.addTab(bar.newTab().setText("SC-06D"),
                DeviceFragmentActivity.class, args);
        
        /*
        args = new Bundle();
        args.putString("url", "https://github.com/kbc-developers/release/wiki/sc04d");
        mPagerAdapter.addTab(bar.newTab().setText("SC-04D"),
                DeviceFragmentActivity.class, args);
        */
        
        args = new Bundle();
        args.putString("url", "https://github.com/kbc-developers/release/wiki/sc02c");
        mPagerAdapter.addTab(bar.newTab().setText("SC-02C"),
                DeviceFragmentActivity.class, args);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }
}
