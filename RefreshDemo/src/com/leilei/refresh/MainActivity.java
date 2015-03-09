package com.leilei.refresh;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private DrawerLayout drawerLayout;
    private View drawerLeftLayout;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private MainFragment mainFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerLeftLayout = this.findViewById(R.id.left_drawer);
        listView = (ListView) drawerLayout.findViewById(R.id.left_drawer_list);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenFrame, mainFragment = new MainFragment())
                .commit();
        setUpLeftList();
    }

    private void setUpLeftList() {
        List<String> textString = new ArrayList<String>();
        //add httpUrl string etc.
        textString.add("http://tv.sohu.com/");
        textString.add("http://www.letv.com/");
        textString.add("http://www.youku.com/");
        adapter = new ArrayAdapter<String>(this, R.layout.simple_list_text, R.id.text1, textString);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                doItemClick(str);
            }
        });
    }

    private void doItemClick(String textString) {
        if (textString != null && !textString.equals("")) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            if (mainFragment != null) {
                if (mainFragment.isAdded())
                    mainFragment.loadUrl(textString);
            }
        }
    }

}