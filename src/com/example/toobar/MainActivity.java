package com.example.toobar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.demo_toolbar);
        toolbar.setTitle("hello world!");
    }
}