package com.example.toobar.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * USER: liulei
 * DATA: 2015/1/19
 * TIME: 11:22
 */

public class ViewPager_Adapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public ViewPager_Adapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab_" + position;
    }
}
