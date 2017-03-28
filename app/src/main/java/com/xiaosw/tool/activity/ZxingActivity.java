package com.xiaosw.tool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.tool.R;

public class ZxingActivity extends BaseAppCompatActivity {

    /** @see ZxingActivity#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-ZxingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG.substring(7));
    }

    public void toDecode(View view) {
        startActivityForResult(new Intent(this, TestZxingDecodeActivity.class), 1);
    }

    public void toScan(View view) {
        startActivityForResult(new Intent(this, TestZxingScanActivity.class), 1);
    }
}
