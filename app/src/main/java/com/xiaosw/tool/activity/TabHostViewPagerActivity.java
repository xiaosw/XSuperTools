package com.xiaosw.tool.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaosw.library.activity.FragmentTabHostActivity;
import com.xiaosw.tool.R;
import com.xiaosw.tool.activity.fragment.TabFragment;

/**
 * 实现TabHost，ViewPager组合功能
 */
public class TabHostViewPagerActivity extends FragmentTabHostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View tab1 = getLayoutInflater().inflate(R.layout.item_fragment_tab_widget_item, null);
        Bundle args = new Bundle();
        args.putInt(TabFragment.KEY_BACKGROUND_COLOR, Color.RED);
        addTab(tab1, TabFragment.class, "1", args);

        View tab2 = getLayoutInflater().inflate(R.layout.item_fragment_tab_widget_item, null);
        ((TextView) tab2.findViewById(R.id.tv_tab_widget_title)).setText("Tab-2");
        args = new Bundle();
        args.putInt(TabFragment.KEY_BACKGROUND_COLOR, Color.GREEN);
        addTab(tab2, TabFragment.class, "2", args);

        View tab3 = getLayoutInflater().inflate(R.layout.item_fragment_tab_widget_item, null);
        ((TextView) tab3.findViewById(R.id.tv_tab_widget_title)).setText("Tab-3");
        args = new Bundle();
        args.putInt(TabFragment.KEY_BACKGROUND_COLOR, Color.BLUE);
        addTab(tab3, TabFragment.class, "3", args);
    }

}
