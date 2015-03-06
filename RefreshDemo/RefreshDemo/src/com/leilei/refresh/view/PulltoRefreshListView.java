package com.leilei.refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * USER: liulei
 * DATE: 2015/3/5.
 * TIME: 15:52
 */
public class PulltoRefreshListView extends ListView implements IPullable {

    public PulltoRefreshListView(Context context) {
        this(context, null);
    }

    public PulltoRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PulltoRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isTop() {
        if (getCount() == 0) {
            return true;
        } else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean isBottom() {
        if (getCount() == 0) {
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }
}
