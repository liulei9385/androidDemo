package com.leilei.refresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * USER: liulei
 * DATE: 2015/3/9.
 * TIME: 12:00
 */
public class SWipeRefreshLayoutActivity extends Activity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiperefresh);

        textView = (TextView) this.findViewById(R.id.text1);
        swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swiptRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        textView.setText("Refreshing_" + new Random().nextInt(100));
                    }
                }, 2 * 1000);

            }
        });


    }
}
