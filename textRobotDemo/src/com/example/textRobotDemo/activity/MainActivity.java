package com.example.textRobotDemo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.example.textRobotDemo.R;
import com.example.textRobotDemo.fragment.MainFragment;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_left_menubar);

        initFragment();

    }

    private void initFragment() {
        Fragment fragment = new MainFragment();
        commitFragment(R.id.content_frame, fragment);
    }


}
