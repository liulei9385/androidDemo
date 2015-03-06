package com.example.youdao.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.youdao.R;
import com.example.youdao.model.IntentInitParams;
import com.example.youdao.pulltorefresh.PullToRefreshLayout;

/**
 * USER: liulei
 * DATA: 2015/1/20
 * TIME: 10:50
 */
public class WebViewFragment extends Fragment {

    private WebView mWebView;
    private String httpUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWebView = (WebView) getActivity().findViewById(R.id.webView1);
        initView();

        httpUrl = "http://www.baidu.com";
        Bundle argument = getArguments();
        if (argument != null)
            httpUrl = argument.getString(IntentInitParams.URL_SCHEMA);
        mWebView.loadUrl(httpUrl);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        PullToRefreshLayout pullToRefreshLayout = (PullToRefreshLayout) getActivity().findViewById(R.id.refresh_view);
        pullToRefreshLayout.setOnRefreshListener(refreshListener);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    private PullToRefreshLayout.OnRefreshListener refreshListener = new PullToRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            if (mWebView != null) {
                mWebView.loadUrl("about:blank");
                mWebView.loadUrl(getHttpUrl());
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        }
    };

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public void loadUrl() {
        if (mWebView != null) {
            mWebView.loadUrl(getHttpUrl());
        }
    }
}
