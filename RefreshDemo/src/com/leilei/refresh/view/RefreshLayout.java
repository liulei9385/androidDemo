package com.leilei.refresh.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.RotateDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.leilei.refresh.R;

/**
 * 下拉刷新上拉加载 参照 {@see https://github.com/jingchenUSTC/PullToRefreshAndLoad}
 * USER: liulei
 * DATA: 2015/2/12
 * TIME: 16:13
 */
public class RefreshLayout extends RelativeLayout {

    private View headView;
    private ImageView arrowView;
    private ProgressBar progressBar;

    private View footView;
    private ProgressBar footProgressBar;
    private TextView refreshView;
    private View contentView;

    private float lastY;
    private float lastX;
    private float moveY;

    private float ratio = 0.78f;
    private int maxMoveY = 180;
    private int moveMinInstance = 8;

    private int headViewHeight = 0;
    private int footViewHeight = 0;

    private boolean firstLayout = true;

    private boolean canPullUp = false;
    private boolean canPullDown = false;

    private boolean isHeadRefreshing = false;
    private boolean isFooterRefreshing = false;

    private boolean isRotateRecover = false;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("InflateParams")
    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        maxMoveY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                120, getResources().getDisplayMetrics());

        headView = layoutInflater.inflate(R.layout.layout_pullrefresh, null);
        arrowView = (ImageView) headView.findViewById(R.id.arrow);
        refreshView = (TextView) headView.findViewById(R.id.refresh_text);
        progressBar = (ProgressBar) headView.findViewById(R.id.progressBar);
        addView(headView, 1);

        footView = layoutInflater.inflate(R.layout.layout_loadmore, null);
        footProgressBar = (ProgressBar) footView.findViewById(R.id.footProgressBar);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.
                MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        footView.setLayoutParams(layoutParams);
        addView(footView, 2);

        //屏蔽单击事件.防止点到contentView的单击事件
        headView.setClickable(true);
        footView.setClickable(true);

        if (contentView instanceof AbsListView) {
            final AbsListView listView = (AbsListView) contentView;
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                        if (((IPullable) contentView).isBottom()) {
                            autoBottomRefresh();
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                }
            });
        }
    }

    private boolean isPullDown = false;
    private boolean isPullUp = false;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //state = DONE;
                lastX = ev.getX();
                lastY = ev.getY();
                moveY = 0;
                canPullDown = canPullUp = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (state == REFRESHING)
                    lastY = ev.getY();
                moveY = (ev.getY() - lastY) * ratio;
                //下拉
                if (state != REFRESHING && contentView instanceof IPullable) {
                    canPullDown = ((IPullable) contentView).isTop();
                    canPullUp = ((IPullable) contentView).isBottom();
                }

                if (state != REFRESHING) {
                    if (isPullDown && moveY >= -moveMinInstance) {
                        state = DONE;
                        requestLayout();
                    }
                    if (isPullUp && moveY <= moveMinInstance) {
                        state = DONE;
                        requestLayout();
                    }
                }

                final boolean canProcessMove = (Math.abs(ev.getX() - lastX) < Math.abs(moveY));

                if (!isPullUp && canProcessMove && moveY >= moveMinInstance && canPullDown && state != REFRESHING
                        && !isHeadRefreshing && !isFooterRefreshing) {
                    state = moveY < headViewHeight ? PULL_TO_REFRESH : RELEASE_TO_REFERSH;
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    requestLayout();
                    isPullDown = true;
                    if (moveY > maxMoveY) moveY = maxMoveY;
                }

                //上拉
                if (!isPullDown && canProcessMove && moveY < -moveMinInstance && canPullUp && state != REFRESHING
                        && !isFooterRefreshing && !isHeadRefreshing) {
                    state = moveY >= -footViewHeight ? PULL_TO_REFRESH : RELEASE_TO_REFERSH;
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    requestLayout();
                    isPullUp = true;
                    if (moveY <= -maxMoveY) moveY = -maxMoveY;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (state == RELEASE_TO_REFERSH) {
                    if (ev.getY() - lastY >= 0)
                        isHeadRefreshing = true;
                    else if (ev.getY() - lastY < 0)
                        isFooterRefreshing = true;
                    state = REFRESHING;
                    updateProgressbar();
                    if (refreshListener != null && isHeadRefreshing && canPullDown)
                        refreshListener.onRefresh();
                    if (refreshListener != null && isFooterRefreshing && canPullUp)
                        refreshListener.onLoadMore();
                }
                if (state == PULL_TO_REFRESH) {
                    state = DONE;
                }
                requestLayout();
                isPullDown = isPullUp = false;
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    private static final int PULL_TO_REFRESH = 0;
    private static final int RELEASE_TO_REFERSH = 1;
    private static final int REFRESHING = 2;
    private static final int DONE = 3;
    private static final int INIT = 4;
    private int state = INIT;

    private void hideHeadView() {
        headView.layout(0, 0, 0, 0);
    }

    private void hideFootView() {
        footView.layout(0, 0, 0, 0);
    }


    private boolean isAutorRefresh = false;

    private void updateLayout(int state) {
        if (!isAutorRefresh) {
            canPullDown = moveY >= 0 && canPullDown;
            canPullUp = moveY < 0 && canPullUp;
        }

        switch (state) {
            case PULL_TO_REFRESH:
                if (isPullUp && isPullDown)
                    return;
                if (!isPullUp && canPullDown && !isFooterRefreshing) {
                    isPullDown = true;
                    hideFootView();
                    //恢复原先的状态
                    if (!isRotateRecover) {
                        //避免多次刷新
                        isRotateRecover = true;
                        arrowView.startAnimation(createRotateAnimation(180, 360));
                    }
                    refreshView.setText(getContext().getString(R.string.pull_to_refesh));
                    headView.layout(0, 0, headView.getMeasuredWidth(), (int) moveY);
                    if (moveY <= moveMinInstance)
                        handler.sendEmptyMessage(0);
                    if (isContentMoved()) {
                        contentView.layout(0, (int) moveY, contentView.getMeasuredWidth()
                                , (int) moveY + contentView.getMeasuredHeight());
                    }
                }
                if (!isPullDown && canPullUp && !isHeadRefreshing) {
                    isPullUp = true;
                    hideHeadView();
                    footView.layout(0, (int) (contentView.getMeasuredHeight() + moveY), footView.getMeasuredWidth(),
                            contentView.getMeasuredHeight());
                    if (moveY >= -moveMinInstance)
                        handler.sendEmptyMessage(0);
                    if (isContentMoved()) {
                        contentView.layout(0, (int) moveY, contentView.getMeasuredWidth()
                                , (int) moveY + contentView.getMeasuredHeight());
                    }
                }
                break;
            case RELEASE_TO_REFERSH:
                if (isPullUp && isPullDown)
                    return;
                if (!isPullUp && canPullDown && !isFooterRefreshing) {
                    isPullDown = true;
                    hideFootView();
                    refreshView.setText(getContext().getString(R.string.release_to_refresh_text));
                    if (arrowView.getAnimation() == null || !arrowView.getAnimation().hasStarted()) {
                        isRotateRecover = false;
                        arrowView.startAnimation(createRotateAnimation(0, 180));
                    }
                    headView.layout(0, 0, headView.getMeasuredWidth(), (int) moveY);
                    if (isContentMoved()) {
                        contentView.layout(0, (int) moveY, contentView.getMeasuredWidth()
                                , (int) moveY + contentView.getMeasuredHeight());
                    }
                }
                if (!isPullDown && canPullUp && !isHeadRefreshing) {
                    isPullUp = true;
                    hideHeadView();
                    footView.layout(0, (int) (contentView.getMeasuredHeight() + moveY), footView.getMeasuredWidth(),
                            contentView.getMeasuredHeight());
                    if (isContentMoved()) {
                        contentView.layout(0, (int) moveY, contentView.getMeasuredWidth()
                                , (int) moveY + contentView.getMeasuredHeight());
                    }
                }
                break;
            case REFRESHING:
                if ((canPullDown && !isFooterRefreshing && !isPullUp) || isHeadRefreshing) {
                    hideFootView();
                    refreshView.setText(getContext().getString(R.string.refreshing));
                    headView.layout(0, 0, headView.getMeasuredWidth(), headView.getMeasuredHeight());
                    if (isContentMoved()) {
                        contentView.layout(0, headViewHeight, contentView.getMeasuredWidth()
                                , headViewHeight + contentView.getMeasuredHeight());
                    }
                }
                if ((canPullUp && !isHeadRefreshing && !isPullDown) || isFooterRefreshing) {
                    hideHeadView();
                    footView.layout(0, contentView.getMeasuredHeight() - footView.getMeasuredHeight(),
                            footView.getMeasuredWidth(), contentView.getMeasuredHeight());
                    if (isContentMoved()) {
                        contentView.layout(0, -footView.getMeasuredHeight(), contentView.getMeasuredWidth()
                                , -footView.getMeasuredHeight() + contentView.getMeasuredHeight());
                    }
                }
                break;
            case INIT:
                headView.layout(0, 0, 0, 0);
                arrowView.clearAnimation();
                progressBar.setVisibility(GONE);

                footView.layout(0, 0, 0, 0);
                footProgressBar.setVisibility(GONE);
                break;
            case DONE:
                progressBar.setVisibility(GONE);
                footProgressBar.setVisibility(GONE);
                headView.layout(0, 0, 0, 0);
                arrowView.clearAnimation();
                footView.layout(0, 0, 0, 0);
                if (isContentMoved())
                    contentView.layout(0, 0, contentView.getMeasuredWidth()
                            , contentView.getMeasuredHeight());
                //isPullDown = isPullUp = false;
                break;
        }
    }

    //更新状态信息
    private void updateProgressbar() {
        if (canPullUp && state == REFRESHING) {
            footProgressBar.setVisibility(VISIBLE);
        } else footProgressBar.setVisibility(GONE);
        if (canPullDown && state == REFRESHING) {
            progressBar.setVisibility(VISIBLE);
        } else progressBar.setVisibility(GONE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (firstLayout) {
            contentView = getChildAt(0);
            initView();
            firstLayout = false;
            headView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            footView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            headViewHeight = headView.getMeasuredHeight();
            footViewHeight = footView.getMeasuredHeight();

            if (contentView instanceof IPullable) {
                canPullDown = ((IPullable) contentView).isTop();
                canPullUp = ((IPullable) contentView).isBottom();
            }
        }
        updateLayout(state);
    }

    /*旋转动画*/
    private Animation createRotateAnimation(float fromDegrees, float toDegrees) {
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(300);
        animation.setFillAfter(true);
        return animation;
    }

    /*平移动画*/
    private Animation createTranslateAnimation() {
        if (contentView != null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,
                    contentView.getMeasuredHeight(), 0);
            translateAnimation.setInterpolator(new LinearInterpolator());
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(200);
            return translateAnimation;
        }
        return null;
    }

    /*缩放动画*/
    private Animation createScaleAnimation() {
        /*敢于思考，总有解决办法的额*/
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    public void refreshComplete() {
        state = DONE;
        isHeadRefreshing = isAutorRefresh = false;
        requestLayout();
    }

    public void onLoadMoreComplete() {
        state = DONE;
        isFooterRefreshing = isAutorRefresh = false;
        requestLayout();
    }

    /*自动下拉*/
    public void autoTopRefresh() {
        if (!isHeadRefreshing && !isFooterRefreshing) {
            isHeadRefreshing = true;
            isFooterRefreshing = false;
            isAutorRefresh = true;
            canPullDown = true;
            state = REFRESHING;
            if (!isContentMoved())
                headView.startAnimation(createScaleAnimation());
            updateProgressbar();
            if (refreshListener != null && canPullDown)
                refreshListener.onRefresh();
            requestLayout();
        }
    }

    /*自动上拉*/
    public void autoBottomRefresh() {
        if (!isHeadRefreshing && !isFooterRefreshing) {
            isHeadRefreshing = false;
            isFooterRefreshing = true;
            isAutorRefresh = true;
            state = REFRESHING;
            canPullUp = true;
            Animation animation = createTranslateAnimation();
            if (animation != null && !isContentMoved())
                footView.startAnimation(animation);
            updateProgressbar();
            if (refreshListener != null && canPullUp)
                refreshListener.onLoadMore();
            requestLayout();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (moveY > 0)
                        moveY--;
                    else if (moveY < 0)
                        moveY++;
                    if (moveY == 0)
                        handler.removeMessages(0);
                    requestLayout();
                    sendEmptyMessageDelayed(0, 20);
                    break;
            }
        }
    };

    private OnRefreshListener refreshListener;

    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();
    }

    public void setRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * 设置滑动系数
     *
     * @param newRatio 比例
     */
    public void setMoveRatio(float newRatio) {
        if (newRatio <= 0)
            throw new IllegalArgumentException("ration must be larger than zero.");
        this.ratio = newRatio;
    }

    private boolean isContentMoved = false;

    /*设置主view是否移动*/
    public void setContentMoved(boolean isContentMoved) {
        this.isContentMoved = isContentMoved;
    }

    public boolean isContentMoved() {
        return isContentMoved;
    }

}