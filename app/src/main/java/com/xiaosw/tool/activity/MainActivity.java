package com.xiaosw.tool.activity;

import android.os.Bundle;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.tool.R;

/**
 * @ClassName {@link MainActivity}
 * @Description 主界面
 *
 * @Date 2016-10-10 19:17.
 * @Author xiaoshiwang.
 */
public class MainActivity extends BaseAppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
        getSupportActionBar().collapseActionView();
    }
}
