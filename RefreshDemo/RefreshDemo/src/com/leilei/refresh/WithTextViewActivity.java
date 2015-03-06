package com.leilei.refresh;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
    }

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
}
