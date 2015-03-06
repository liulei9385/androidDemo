package com.leilei.refresh;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.leilei.refresh.view.IPullable;
import com.leilei.refresh.view.PulltoRefreshListView;
import com.leilei.refresh.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * USER: liulei
 * DATE: 2015/3/5.
 * TIME: 15:58
 */
public class WithListViewActivity extends Activity {

    private RefreshLayout refreshLayout;
    private PulltoRefreshListView listView;
    private ListAdapter adapter;

    private Handler handler = new Handler();
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withlistview);

        refreshLayout = (RefreshLayout) this.findViewById(R.id.refreshView);
        listView = (PulltoRefreshListView) this.findViewById(R.id.listView);

        List<String> strings = new ArrayList<String>();
        for (int i = 0; i < 25; i++)
            strings.add("TEST" + i);
        index = strings.size();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
                , android.R.id.text1, strings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                String text = "NULL";
                if (o != null) {
                    text = (String) o;
                }
                Toast.makeText(WithListViewActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onRefresh() {
                ((ArrayAdapter<String>) adapter).clear();
                if (Build.VERSION.SDK_INT < 11) {
                    for (String str : prepareArrayData())
                        ((ArrayAdapter<String>) adapter).add(str);
                } else
                    ((ArrayAdapter<String>) adapter).addAll(prepareArrayData());
                ((ArrayAdapter<String>) adapter).notifyDataSetChanged();
                hideView(true, 2 * 1000);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onLoadMore() {
                ((ArrayAdapter<String>) adapter).add("testOdd" + (index++));
                ((ArrayAdapter<String>) adapter).notifyDataSetChanged();
                hideView(false, 2 * 1000);
            }
        });

    }

    private List<String> prepareArrayData() {
        List<String> strings = new ArrayList<String>();
        for (int i = 0; i < 25; i++)
            strings.add("TEST" + i);
        return strings;
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
}
