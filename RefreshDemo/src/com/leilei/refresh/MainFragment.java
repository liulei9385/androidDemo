package com.leilei.refresh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        webView = (WebView) rootView.findViewById(R.id.webView);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WithTextViewActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button button2 = (Button) rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WithListViewActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button button3 = (Button) rootView.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WithGridViewActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
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
            if (newProgress >= 20)
                refreshLayout.refreshComplete();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
