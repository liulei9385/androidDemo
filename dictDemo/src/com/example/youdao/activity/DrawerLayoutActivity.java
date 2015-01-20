package com.example.youdao.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.youdao.R;
import com.example.youdao.fragment.WebViewFragment;
import com.example.youdao.model.IntentInitParams;

import java.util.ArrayList;
import java.util.List;

/**
 * USER: liulei
 * DATA: 2015/1/20
 * TIME: 10:41
 */
public class DrawerLayoutActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private List<String> urlItems;
    private List<String> titleLists;

    private ActionBarDrawerToggle mDrawerToggle;

    private WebViewFragment tmpWebViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout_main);

        //初始化view
        initView();

        if (savedInstanceState == null)
            selectItem(0);
    }

    private void initView() {

        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        listView = (ListView) this.findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setHomeButtonEnabled(true);

        actionBar.setLogo(R.drawable.ic_launcher);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.drawable.ic_launcher);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(mDrawerToggle);

        //设置adapter
        urlItems = new ArrayList<String>();
        urlItems.add("http://www.baidu.com");
        urlItems.add("http://www.letv.com");
        urlItems.add("http://dict.youdao.com");
        urlItems.add("http://www.nba98.com");
        urlItems.add("http://www.qq.com");

        //设置title
        titleLists = new ArrayList<String>();
        titleLists.add("百度");
        titleLists.add("乐视tv");
        titleLists.add("有道词典");
        titleLists.add("NBA98");
        titleLists.add("腾讯");

        listView.setAdapter(new ArrayAdapter<String>(DrawerLayoutActivity.this,
                R.layout.listholderlayout, R.id.textView1, urlItems));
        listView.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    };

    private void selectItem(int positon) {

        if (tmpWebViewFragment == null) {
            tmpWebViewFragment = new WebViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IntentInitParams.URL_SCHEMA, urlItems.get(positon));
            tmpWebViewFragment.setArguments(bundle);
        } else {
            tmpWebViewFragment.setHttpUrl(urlItems.get(positon));
            tmpWebViewFragment.loadUrl();
        }

        if (!tmpWebViewFragment.isAdded()) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, tmpWebViewFragment).commit();
        }

        listView.setItemChecked(positon, true);
        setTitle(titleLists.get(positon));
        drawerLayout.closeDrawer(listView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.title:
                System.out.println("DrawerLayoutActivity.onOptionsItemSelected##");
                if (drawerLayout.isDrawerOpen(Gravity.START))
                    mDrawerToggle.onDrawerClosed(listView);
                else mDrawerToggle.onDrawerClosed(listView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (drawerLayout != null && listView != null) {
                if (!drawerLayout.isDrawerOpen(listView))
                    drawerLayout.openDrawer(listView);
                else drawerLayout.closeDrawer(listView);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }
}