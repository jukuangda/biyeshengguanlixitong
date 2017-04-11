package com.example.jkd.biyeshengguanlixitong;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.igexin.sdk.PushManager;

public class MainActivity2 extends ActionBarActivity implements ShowView2 {

    private long exitTime = 0;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        PushManager.getInstance().initialize(this.getApplicationContext());
        System.out.println(PushManager.getInstance().getClientid(getApplicationContext()));
        setContentView(R.layout.activity_main_activity2);
        actionbar = getSupportActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeButtonEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setTitle("毕业生供需管理系统");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.Thecontont);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionbar.setSelectedNavigationItem(position);
            }
        });

        actionbar.addTab(actionbar.newTab().setText("资讯").setTabListener(new MyTabsListener(new MainFragment())));
        actionbar.addTab(actionbar.newTab().setText("推送").setTabListener(new MyTabsListener(new Push())));
        actionbar.addTab(actionbar.newTab().setText("个人").setTabListener(new MyTabsListener(new PerInformation())));
        actionbar.setSelectedNavigationItem(1);
    }

    @Override
    public void showPerInformation() {
        Intent i = new Intent(this, MainActivity3.class);
        Bundle bl = new Bundle();
        bl.putString("Tag", "perinformation");
        i.putExtras(bl);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void showallpush1(String title, String time, String content,String MID) {
        Intent i = new Intent(this, MainActivity3.class);
        Bundle bl = new Bundle();
        bl.putString("Tag", "allpush1");
        bl.putString("title", title);
        bl.putString("time", time);
        bl.putString("content", content);
        bl.putString("MID",MID);
        i.putExtras(bl);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void showdz() {
        Intent i = new Intent(this, MainActivity3.class);
        Bundle bl = new Bundle();
        bl.putString("Tag", "dz");
        i.putExtras(bl);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void showPush(String pid, String cid) {
        Intent i = new Intent(this, MainActivity3.class);
        Bundle bl = new Bundle();
        bl.putString("Tag", "allpush");
        bl.putString("pid", pid);
        bl.putString("cid", cid);
        i.putExtras(bl);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    protected class MyTabsListener implements ActionBar.TabListener {
        private Fragment Fm;

        public MyTabsListener(Fragment Fm) {
            this.Fm = Fm;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(Fm);
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new Push();
                case 2:
                    return new PerInformation();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case 0:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
