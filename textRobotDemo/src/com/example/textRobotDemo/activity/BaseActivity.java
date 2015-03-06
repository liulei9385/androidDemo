package com.example.textRobotDemo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

/**
 * USER: liulei
 * DATA: 2015/1/28
 * TIME: 11:40
 */
public class BaseActivity extends ActionBarActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
    }

    /**
     * @param fragmenREsId 布局id
     * @param fragment     fragment
     */
    public void commitFragment(int fragmenREsId, Fragment fragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmenREsId, fragment);
        transaction.commit();
    }


}
