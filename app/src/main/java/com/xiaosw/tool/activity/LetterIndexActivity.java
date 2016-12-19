package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.view.MotionEvent;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.library.widget.LetterIndexView;
import com.xiaosw.tool.R;

import butterknife.BindView;

public class LetterIndexActivity extends BaseAppCompatActivity implements
    LetterIndexView.OnIndexChangedListener {
    
    /** @see LetterIndexActivity#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-LetterIndexActivity";

    @BindView(R.id.letter_index_view)
    LetterIndexView letter_index_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_index);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle("LetterIndexActivity");
        letter_index_view.setOnIndexChangedListener(this);
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void onIndexChanged(char charAt, int index) {
        LogUtil.i(TAG, "onIndexChanged charAt = " + charAt + ", index = " + index);
    }
}
