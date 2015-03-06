package com.leilei.refresh;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import com.leilei.refresh.view.PulltoRefreshGridView;
import com.leilei.refresh.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * USER: liulei
 * DATE: 2015/3/6.
 * TIME: 16:17
 */
public class WithGridViewActivity extends Activity {

    private RefreshLayout refreshLayout;
    private PulltoRefreshGridView gridView;
    private ArrayAdapter<String> adapter;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withgridview);

        gridView = (PulltoRefreshGridView) this.findViewById(R.id.gridView);
        List<String> stringList = createListData();
        index = stringList.size();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
                , android.R.id.text1, stringList);
        gridView.setAdapter(adapter);

        refreshLayout = (RefreshLayout) this.findViewById(R.id.refreshView);
        refreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hideView(true);
                adapter.clear();
                List<String> stringList = createListData();
                if (Build.VERSION.SDK_INT < 11)
                    for (String str : stringList)
                        adapter.add(str);
                else adapter.addAll(stringList);
                index = stringList.size();
            }

            @Override
            public void onLoadMore() {
                hideView(false);
                for (int i = 0; i < 5; i++) {
                    adapter.add("Item" + index++);
                }
            }
        });
    }

    private List<String> createListData() {
        List<String> stringList = new ArrayList<String>();
        for (int i = 0; i < 12; i++)
            stringList.add("Item" + i);
        return stringList;
    }

    private Handler handler = new Handler();

    private void hideView(final boolean isTop) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isTop)
                    refreshLayout.refreshComplete();
                else
                    refreshLayout.onLoadMoreComplete();
            }
        }, 500);

    }
}
