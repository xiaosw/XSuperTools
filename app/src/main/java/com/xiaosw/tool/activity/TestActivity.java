package com.xiaosw.tool.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xiaosw.tool.R;

public class TestActivity extends Activity {
    
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void request(View v) {
    }
}
