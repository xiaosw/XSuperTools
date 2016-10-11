package com.xiaosw.tool.activity;

import android.os.Bundle;

import com.xiaosw.library.activity.BaseActivity;
import com.xiaosw.tool.R;

public class ProgressBarActivity extends BaseActivity {

    private static final String TAG = "ProgressBarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setCustomActionBarTitle(TAG);
    }
}
