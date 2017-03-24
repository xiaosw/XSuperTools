package com.xiaosw.tool.activity;

import android.os.Bundle;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.tool.R;

/**
 * <p><br/>ClassName : {@link WeatherActivity}
 * <br/>Description : 测试仿华为天气view使用
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-01 11:11:24</p>
 */
public class WeatherActivity extends BaseAppCompatActivity {

    /** @see WeatherActivity#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-WeatherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
    }
}
