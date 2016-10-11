package com.xiaosw.library.activity;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xiaosw.library.R;

import butterknife.ButterKnife;

/**
 * @ClassName {@link BaseActivity}
 * @Description Activity基类
 *
 * @Date 2016-10-10 19:19.
 * @Author xiaoshiwang.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Custom ActionBar
    ///////////////////////////////////////////////////////////////////////////
    private ImageButton ib_custom_action_bar_back;
    private TextView tv_custom_action_bar_title;
    /**
     * 使用自定义ActionBar
     */
    public View useCustomActionBar() {
        //得到actionBar，注意我的是V7包，使用getSupportActionBar()
        ActionBar actionBar = getSupportActionBar();
        //在使用v7包的时候显示icon和标题需指定一下属性。
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setLogo(R.mipmap.back_black);
        actionBar.setDisplayUseLogoEnabled(true);
        // 返回箭头（默认不显示）
        actionBar.setDisplayHomeAsUpEnabled(false);
        // 左侧图标点击事件使能
        actionBar.setHomeButtonEnabled(true);
        //显示自定义的actionBar
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(null);
        View actionBarView = View.inflate(this, R.layout.view_action_bar, null);
        ib_custom_action_bar_back = (ImageButton) actionBarView.findViewById(R.id.ib_back);
        tv_custom_action_bar_title = (TextView) actionBarView.findViewById(R.id.tv_action_bar_title);
        ib_custom_action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        actionBar.setCustomView(actionBarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return actionBarView;
    }

    public void setCustomActionBarTitle(CharSequence title) {
        if (null != tv_custom_action_bar_title) {
            tv_custom_action_bar_title.setText(title);
        }
    }

    public void setCustomActionBarTitle(int resId) {
        setCustomActionBarTitle(getString(resId));
    }

    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (null != ib_custom_action_bar_back) {
            ib_custom_action_bar_back.setVisibility(showHomeAsUp ? View.VISIBLE : View.GONE);
        }
    }

}
