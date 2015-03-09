package com.leilei.refresh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.leilei.refresh.utils.RandomUtils;
import com.leilei.refresh.view.RefreshLayout;

import java.util.Random;

/**
 * USER: liulei
 * DATE: 2015/3/7.
 * TIME: 17:57
 */
public class MainFragment extends Fragment {

    private WebView webView;
    private RefreshLayout refreshLayout;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        webView = (WebView) rootView.findViewById(R.id.webView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        configWebview(webView);

        final String[] httpUrls = new String[]
                {
                        "http://sina.cn",
                        "http://www.baidu.com",
                        "http://www.qiushibaike.com/",
                        "http://www.qq.com"
                };

        webView.loadUrl(httpUrls[new Random().nextInt(httpUrls.length)]);

        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refreshView);
        refreshLayout.setContentMoved(true);
        refreshLayout.setMoveRatio(0.65f);
        refreshLayout.setHeadBackGroundColor(Color.rgb(RandomUtils.betweentInt(0, 255),
                RandomUtils.betweentInt(0, 255), RandomUtils.betweentInt(0, 255)));
        refreshLayout.setBottomBackGroundColor(Color.rgb(RandomUtils.betweentInt(0, 255),
                RandomUtils.betweentInt(0, 255), RandomUtils.betweentInt(0, 255)));

        refreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Random random = new Random();
                        try {
                            String httpUrl = httpUrls[random.nextInt(3)];
                            webView.loadUrl(httpUrl);
                        } catch (Exception ignored) {
                        }
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "LoadMore", Toast.LENGTH_SHORT).show();
                        refreshLayout.onLoadMoreComplete();
                    }
                }, 1000);
            }
        });

        Button button = (Button) rootView.findViewById(R.id.button1);
        setBgDrawable(button, createDrawable());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WithTextViewActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button button2 = (Button) rootView.findViewById(R.id.button2);
        setBgDrawable(button2, createDrawable());
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WithListViewActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button button3 = (Button) rootView.findViewById(R.id.button3);
        setBgDrawable(button3, createDrawable());
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WithGridViewActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button button4 = (Button) rootView.findViewById(R.id.button4);
        setBgDrawable(button4, createDrawable());
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SWipeRefreshLayoutActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button button5 = new Button(getActivity());
        setBgDrawable(button5, createDrawable());
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = Color.rgb(RandomUtils.betweentInt(0, 255),
                        RandomUtils.betweentInt(0, 255), RandomUtils.betweentInt(0, 255));
                refreshLayout.setHeadBackGroundColor(color);
                refreshLayout.setBottomBackGroundColor(color);
            }
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.button4);
        layoutParams.setMargins(0, 20, 0, 0);
        button5.setLayoutParams(layoutParams);
        button5.setText("Update RefreshLayout BackGround Color.");
        ((ViewGroup) rootView).addView(button5);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configWebview(WebView webView) {

        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebChromeClient(new MyWebChromeClieht());
        webView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebChromeClieht extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress >= 85)
                refreshLayout.refreshComplete();
            updateProgressUI(newProgress <= 85);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    public void loadUrl(String url) {
        if (url.startsWith("http"))
            this.webView.loadUrl(url);
    }

    private void updateProgressUI(boolean visizable) {
        int visibility = visizable ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(visibility);
    }

    private Drawable createDrawable() {
        GradientDrawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFFF0000,
                0xFF00FF00, 0xFF0000FF});
        drawable.setCornerRadius(8);
        drawable.setStroke(2, Color.BLUE);
        drawable.setDither(true);
        //set solid color
        //drawable.setColor(Color.parseColor("#ffff0000"));
        return drawable;
    }

    private void setBgDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16)
            view.setBackgroundDrawable(drawable);
        else view.setBackground(drawable);
    }

}
