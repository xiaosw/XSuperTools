package com.xiaosw.tool.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.xiaosw.library.activity.FragmentTabHostActivity;
import com.xiaosw.tool.R;
import com.xiaosw.tool.activity.fragment.TabFragment;

/**
 * 实现TabHost，ViewPager组合功能
 */
public class TabHostViewPagerActivity extends FragmentTabHostActivity {

    private String[] mTitles;
    private int[] mBackgrounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppThemeBlack);
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }
        addTabs();
    }

    private void addTabs() {
        initData();
        int len = mTitles.length;
        for (int i = 0; i < len; i++) {
            View tab1 = getLayoutInflater().inflate(R.layout.item_fragment_tab_widget_item, null);
            ((TextView) tab1.findViewById(R.id.tv_tab_widget_title)).setText(mTitles[i]);
            Bundle args = new Bundle();
            args.putInt(TabFragment.KEY_BACKGROUND_COLOR, mBackgrounds[i]);
            args.putString(TabFragment.KEY_TITLE, mTitles[i]);
            addTab(tab1, TabFragment.class, String.valueOf(i), args);
        }
    }

    private void initData() {
        mTitles = new String[] {
            "资讯",
            "商城",
            "论坛",
            "我"
        };

        mBackgrounds = new int[] {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.GRAY
        };
    }

}
