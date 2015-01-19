package com.example.toobar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitleTextColor(Color.BLUE);
        toolbar.setTitle("hello");
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
}