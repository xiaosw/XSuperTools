package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.tool.R;
import com.xiaosw.zxing.qrcode.QRCodeView;

import butterknife.BindView;

public class TestZxingScanActivity extends BaseAppCompatActivity implements QRCodeView.Delegate {
    
    /** @see TestZxingScanActivity#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-TestZxingScanActivity";

    @BindView(R.id.scal_view)
    QRCodeView mQRCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zbar);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG.substring(7));
        mQRCodeView.setDelegate(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
    }

    @Override
    public void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtil.e(TAG, "onScanQRCodeSuccess: -----------------------> result = " + result);
        vibrate();
        Toast.makeText(this, "" + result, Toast.LENGTH_SHORT).show();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtil.e(TAG, "onScanQRCodeOpenCameraError: ------------------> onScanQRCodeOpenCameraError");
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
