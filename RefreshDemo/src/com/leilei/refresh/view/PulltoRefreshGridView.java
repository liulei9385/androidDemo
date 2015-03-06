package com.leilei.refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * USER: liulei
 * DATE: 2015/3/6.
 * TIME: 16:15
 */
public class PulltoRefreshGridView extends GridView implements IPullable {

    public PulltoRefreshGridView(Context context) {
        this(context, null);
    }

    public PulltoRefreshGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PulltoRefreshGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isTop() {
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean isBottom() {
        if (getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }

}