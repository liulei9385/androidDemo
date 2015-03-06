package com.leilei.refresh.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * USER: liulei
 * DATA: 2015/3/4
 * TIME: 15:38
 */
public class PulltoRefreshWebView extends WebView implements IPullable {

    private float scale = 1.0f;

    public PulltoRefreshWebView(Context context) {
        this(context, null);
    }

    public PulltoRefreshWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PulltoRefreshWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isTop() {
        return getScrollY() == 0;
    }

    @Override
    public boolean isBottom() {
        float height = getContentHeight() * getScale() - getHeight();
        return getScrollY() >= height;
    }
}
