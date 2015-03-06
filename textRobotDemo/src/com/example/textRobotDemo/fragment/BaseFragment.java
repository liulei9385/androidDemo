package com.example.textRobotDemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * USER: liulei
 * DATA: 2015/1/28
 * TIME: 11:27
 */
public class BaseFragment extends Fragment {

    protected FragmentActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    public Object getSystemService(@NonNull String name) {
        if (activity == null) return null;
        return activity.getSystemService(name);
    }
}
