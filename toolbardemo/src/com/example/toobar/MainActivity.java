package com.example.toobar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.toobar.adapter.ViewPager_Adapter;
import com.example.toobar.fragment.Fragment_Tab_1;
import com.example.toobar.fragment.Fragment_Tab_2;
import com.example.toobar.fragment.Fragment_Tab_3;
import com.example.toobar.view.SlidingTabLayout;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private ViewPager_Adapter viewPager_Adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findView();
    }

    //初始化控件
    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.demo_toolbar);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.demo_tab);
        viewPager = (ViewPager) findViewById(R.id.demo_vp);

        // 实例化控件
        setSupportActionBar(toolbar);
        fragments = new ArrayList<Fragment>();
        fragments.add(new Fragment_Tab_1());
        fragments.add(new Fragment_Tab_2());
        fragments.add(new Fragment_Tab_3());
        viewPager_Adapter = new ViewPager_Adapter(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(viewPager_Adapter);
        // 设置SlidingTab
        slidingTabLayout.setViewPager(viewPager);

    }

    //建立actionBar的选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SupportMenuItem menuItem = (SupportMenuItem) menu.add(0, 1, 1, "settings");
        menuItem.setShowAsAction(MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case 1:
                showToast(itemId + " ", true);

                //打开系统设置
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(final String text, boolean isLong) {
        int duration = Toast.LENGTH_SHORT;
        if (isLong)
            duration = Toast.LENGTH_LONG;

        final int duration2 = duration;
        if (Looper.getMainLooper().getThread() == Thread.currentThread())
            Toast.makeText(this, text, duration).show();
        else new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressWarnings("ResourceType")
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, duration2).show();
            }
        });
    }
}