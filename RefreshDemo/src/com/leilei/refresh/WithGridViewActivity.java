package com.leilei.refresh;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.leilei.refresh.view.PulltoRefreshGridView;
import com.leilei.refresh.view.RefreshLayout;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * USER: liulei
 * DATE: 2015/3/6.
 * TIME: 16:17
 */
public class WithGridViewActivity extends Activity {

    private RefreshLayout refreshLayout;
    private PulltoRefreshGridView gridView;
    private ArrayAdapter<String> adapter;

    private CheckBox isContendMoveCheck;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withgridview);

        gridView = (PulltoRefreshGridView) this.findViewById(R.id.gridView);
        List<String> stringList = createListData();
        index = stringList.size();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
                , android.R.id.text1, stringList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                Random random = new Random();
                textView.setTextColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                textView.setTextSize(12 + random.nextInt(10));
                return view;
            }
        };
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

        isContendMoveCheck = (CheckBox) this.findViewById(R.id.checkBox);
        boolean chekched = isContendMoveCheck.isChecked();
        refreshLayout.setContentMoved(chekched);
        isContendMoveCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                refreshLayout.setContentMoved(isChecked);
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
