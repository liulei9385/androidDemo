package com.leilei.refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * USER: liulei
 * DATE: 2015/3/5.
 * TIME: 10:07
 */
public class PulltoRefreshTextView extends TextView implements IPullable {

    public PulltoRefreshTextView(Context context) {
        this(context, null);
    }

    public PulltoRefreshTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PulltoRefreshTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isTop() {
        return true;
    }

    @Override
    public boolean isBottom() {
        return true;
    }
}
