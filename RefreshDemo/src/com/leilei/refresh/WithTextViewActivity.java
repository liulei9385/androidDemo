package com.leilei.refresh;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import com.leilei.refresh.view.RefreshLayout;

import java.util.Random;

/**
 * USER: liulei
 * DATE: 2015/3/5.
 * TIME: 10:09
 */
public class WithTextViewActivity extends Activity {

    private RefreshLayout refreshLayout;
    private TextView contentText;

    private Handler handler = new Handler();

    private int instance = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withtextview);

        contentText = (TextView) this.findViewById(R.id.contentView);

        refreshLayout = (RefreshLayout) this.findViewById(R.id.refreshView);
        refreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentText.setBackgroundColor(createRamdonColor());
                hideView(true, 3 * 1000);
            }

            @Override
            public void onLoadMore() {
                contentText.setBackgroundColor(createRamdonColor());
                hideView(false, 2 * 1000);
            }
        });

        final Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(clickListener);
        button2.setOnClickListener(clickListener);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                toX = random.nextInt(250);
                toY = random.nextInt(250);
                TranslateAnimation animation = new TranslateAnimation(fromX, toX, fromY, toY);
                fromX = toX;
                fromY = toY;
                animation.setDuration(200);
                animation.setInterpolator(new LinearInterpolator());
                animation.setFillAfter(true);
                button1.startAnimation(animation);
            }
        });
    }

    private int fromX, fromY, toX, toY;

    private void hideView(final boolean isTop, final long delays) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isTop)
                    refreshLayout.refreshComplete();
                else refreshLayout.onLoadMoreComplete();
            }
        }, delays);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }

    private int createRamdonColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    refreshLayout.autoTopRefresh();
                    break;
                case R.id.button2:
                    refreshLayout.autoBottomRefresh();
                    break;
            }
        }
    };

    private void processClickEvent() {

    }
}
