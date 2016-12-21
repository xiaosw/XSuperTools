package com.xiaosw.tool.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.library.activity.fragment.BaseFragment;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.library.widget.BaseViewPager;
import com.xiaosw.library.widget.ColorTrackRadioButton;
import com.xiaosw.library.widget.HorizontalScrollViewTabHost;
import com.xiaosw.library.widget.adapter.BaseFragmentPagerAdapter;
import com.xiaosw.tool.R;
import com.xiaosw.tool.activity.TabHostViewPagerActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @ClassName : {@link TabFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-13 11:11:54
 */
public class TabFragment extends BaseFragment implements BaseViewPager.OnTabChangeListener {

    private static final String TAG = "TabFragment";
    public static final String KEY_BACKGROUND_COLOR = "background_color";
    public static final String KEY_TITLE = "mTitle";

    private String mTitle;

    @BindView(R.id.hsv_tab_host)
    HorizontalScrollViewTabHost mHorizontalScrollViewTabHost;
    @BindView(R.id.view_pager)
    BaseViewPager mBaseViewPager;

    private FragmentTabHost mFragmentTabHost;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentTabHost = ((TabHostViewPagerActivity) context).getFragmentTabHost();
    }

    @Override
    public View doCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, null);
    }

    @Override
    public void initByAttachView(View attachView) {
        super.initByAttachView(attachView);
        mHorizontalScrollViewTabHost.bindViewPager(mBaseViewPager);
        Bundle mArgs = getArguments();
        if (mArgs == null) {
            return;
        }
        if (mArgs.containsKey(KEY_BACKGROUND_COLOR)) {
            attachView.setBackgroundColor(mArgs.getInt(KEY_BACKGROUND_COLOR));
        }

        if (mArgs.containsKey(KEY_TITLE)) {
            mTitle = mArgs.getString(KEY_TITLE);
        }

        mBaseViewPager.setAdapter(new BaseFragmentPagerAdapter<BaseFragment>(getChildFragmentManager(), initFragments(4)));
        mBaseViewPager.setOnTabChangeListener(this);
        LogUtil.i(TAG, "onCreateView mTitle = " + mTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBaseViewPager.setCurrentItem(0);
    }

    @Override
    public void onUpTab() {
        if (mFragmentTabHost.getCurrentTab() > 0) {
            mFragmentTabHost.setCurrentTab(mFragmentTabHost.getCurrentTab() - 1);
        }
    }

    @Override
    public void onNextTab() {
        if (mFragmentTabHost.getCurrentTab() < mFragmentTabHost.getTabWidget().getChildCount()) {
            mFragmentTabHost.setCurrentTab(mFragmentTabHost.getCurrentTab() + 1);
        }
    }

    private void addTab(int id, CharSequence title, boolean isChecked) {
        ColorTrackRadioButton radioButton = mHorizontalScrollViewTabHost.getBasicRadioButton();
        radioButton.setPadding(24, 0, 24, 0);
        radioButton.setId(id);
        radioButton.setText(title);
        radioButton.setChecked(isChecked);
        mHorizontalScrollViewTabHost.addRadioButton(radioButton);
    }

    private ArrayList<BaseFragment> initFragments(int count) {
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PagerFragment pagerFragment = new PagerFragment();
            Bundle args = new Bundle();
            args.putString(PagerFragment.KEY_DESCRIPTION, mTitle + "---" + i);
            pagerFragment.setArguments(args);
            fragments.add(pagerFragment);
            addTab(i, mTitle + "---" + i, i == 0);
        }
        return fragments;
    }

}
