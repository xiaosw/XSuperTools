package com.xiaosw.tool.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.tool.R;
import com.xiaosw.zxing.qrcode.BGAQRCodeUtil;
import com.xiaosw.zxing.zxing.QRCodeDecoder;
import com.xiaosw.zxing.zxing.QRCodeEncoder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <p><br/>ClassName : {@link WeatherActivity}
 * <br/>Description : 测试二维码生成及读取
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-028 12:12:24</p>
 */
public class TestZxingDecodeActivity extends BaseAppCompatActivity {

    /** @see TestZxingDecodeActivity#getClass().getSimpleName() */
    private static final String TAG = "TestZxingDecodeActivity";

    @BindView(R.id.iv_code)
    ImageView iv_code;
    @BindView(R.id.iv_code_add_color)
    ImageView iv_code_add_color;
    @BindView(R.id.iv_code_add_color_and_logo)
    ImageView iv_code_add_color_and_logo;
    @BindView(R.id.iv_code_result)
    ImageView iv_code_result;
    @BindView(R.id.tiet_code_content)
    TextInputEditText tiet_code_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_zxing_decode);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
    }

    /**
     * 生成中文二维码
     * @param view
     */
    public void onGenerateCode(View view) {
        final String text = tiet_code_content.getText().toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, List<Bitmap>>() {
            @Override
            protected List<Bitmap> doInBackground(Void... params) {
                List<Bitmap> bitmaps = new ArrayList<Bitmap>();
                bitmaps.add(QRCodeEncoder.syncEncodeQRCode(text, BGAQRCodeUtil.dp2px(TestZxingDecodeActivity.this, 150)));
                bitmaps.add(QRCodeEncoder.syncEncodeQRCode(text, BGAQRCodeUtil.dp2px(TestZxingDecodeActivity.this, 150), Color.RED));
                bitmaps.add(QRCodeEncoder.syncEncodeQRCode(text, BGAQRCodeUtil.dp2px(TestZxingDecodeActivity.this, 150),
                    Color.RED, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
                return bitmaps;
            }

            @Override
            protected void onPostExecute(List<Bitmap> bitmaps) {
                if (bitmaps != null) {
                    iv_code.setImageBitmap(bitmaps.get(0));
                    iv_code_add_color.setImageBitmap(bitmaps.get(1));
                    iv_code_add_color_and_logo.setImageBitmap(bitmaps.get(2));
                } else {
                    Toast.makeText(TestZxingDecodeActivity.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public void onDecodeCode(View view) {
        iv_code.setDrawingCacheEnabled(true);
        Bitmap code = iv_code.getDrawingCache();
        if (null == code) {
            Toast.makeText(this, "请先生成二维码！！！", Toast.LENGTH_SHORT).show();
        } else {
            iv_code_result.setImageBitmap(code);
            onDecode(code, "解析失败...");
        }
    }

    private void onDecode(final Bitmap bitmap, final String errorTip) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                Log.e(TAG, "onPostExecute: " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(TestZxingDecodeActivity.this, errorTip, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TestZxingDecodeActivity.this, result, Toast.LENGTH_SHORT).show();
                }
                iv_code.setDrawingCacheEnabled(false);
            }
        }.execute();
    }
}
