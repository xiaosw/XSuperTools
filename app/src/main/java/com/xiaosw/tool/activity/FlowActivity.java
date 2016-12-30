package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.widget.GUIFlowRadioGroup;
import com.xiaosw.tool.R;

import java.util.Random;

import butterknife.BindView;

/**
 * <p><br/>ClassName : {@link FlowActivity}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2016-12-30 20:20:28</p>
 */
public class FlowActivity extends BaseAppCompatActivity {

    /**
     * @see FlowActivity#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-FlowActivity";

    @BindView(R.id.flow)
    GUIFlowRadioGroup mGUIFlowRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle("FlowActivity");
    }

    public void add(View view) {
        LinearLayout.LayoutParams mFlowRadionButtonParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

        mFlowRadionButtonParams.setMargins(2, 18, 2, 18);
        mGUIFlowRadioGroup.addView(generateRadioButton(), mFlowRadionButtonParams);
    }

    private RadioButton generateRadioButton() {
        RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.view_radio_button_flow, null);
        radioButton.setText(new Random().nextLong() + "");
        return radioButton;
    }
}
