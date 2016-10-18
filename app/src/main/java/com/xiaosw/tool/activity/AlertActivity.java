package com.xiaosw.tool.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaosw.library.activity.BaseActivity;
import com.xiaosw.library.widget.dialog.PTBaseDialog;
import com.xiaosw.tool.R;

public class AlertActivity extends BaseActivity {
    
    private static final String TAG = "AlertActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
    }

    public void showDialog(View view) {
        switch (view.getId()) {
            case R.id.bt_origin_alert:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("mTitle");
                builder.setMessage("mMessage");
                TextView textView = new TextView(this);
                textView.setText("custom view");
                builder.setView(textView);

                TextView customTitle = new TextView(this);
                textView.setText("customTitle");
                builder.setCustomTitle(customTitle);
                builder.setPositiveButton("PositiveButton", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNeutralButton("Neutral", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            break;
            case R.id.bt_custom_alert:
                PTBaseDialog.Builder ptBuilder = new PTBaseDialog.Builder(this);
                ptBuilder.create().show();
                break;

            default:
                // TODO: 2016/10/18
        }
    }
}
