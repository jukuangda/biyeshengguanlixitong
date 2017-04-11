package com.example.jkd.biyeshengguanlixitong;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity3 extends ActionBarActivity {

    private ActionBar actionbar;
    private Bundle bl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_per_change);
        actionbar = getSupportActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        bl = this.getIntent().getExtras();

        String Tag = bl.getString("Tag");
        if (Tag.equals("perinformation"))
            gotoPerInformation();
        else if (Tag.equals("allpush"))
            gotoPush();
        else if (Tag.equals("allpush1"))
            gotoallpush1();
        else if (Tag.equals("dz"))
            gotodz();
    }
    public void gotodz() {
        actionbar.setTitle("");
        dingzhi dz = new dingzhi();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.son, dz, "persion")
                .commit();
    }
    public void gotoallpush1() {
        actionbar.setTitle("");
        Bundle bundle1 = new Bundle();
        bundle1.putString("title", bl.getString("title"));
        bundle1.putString("time", bl.getString("time"));
        bundle1.putString("content", bl.getString("content"));
        bundle1.putString("MID",bl.getString("MID"));
        AllPush1 allpush1 = new AllPush1();
        allpush1.setArguments(bundle1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.son, allpush1, "persion")
                .commit();
    }

    public void gotoPerInformation() {
        actionbar.setTitle("信息");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.son, new PerChange(), "persion")
                .commit();
    }

    public void gotoPush() {
        actionbar.setTitle("");
        Bundle bundle = new Bundle();
        bundle.putString("pid", bl.getString("pid"));
        bundle.putString("cid", bl.getString("cid"));
        AllPush allPush = new AllPush();
        allPush.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.son, allPush, "persion")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_per_change, menu);

        return true;
    }

    public Fragment getVisinleFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (getVisinleFragment() == getSupportFragmentManager().findFragmentByTag("persion"))
                    finish();
                else
                    getSupportFragmentManager().popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

}
