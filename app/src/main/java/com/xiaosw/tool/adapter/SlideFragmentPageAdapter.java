package com.xiaosw.tool.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xiaosw.tool.activity.fragment.SlideFragment;

import java.util.ArrayList;

/**
 * <p><br/>ClassName : {@link SlideFragmentPageAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-04 19:19:23</p>
 */
public class SlideFragmentPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<SlideFragment> mFragments;

    public SlideFragmentPageAdapter(FragmentManager fm, ArrayList<SlideFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public int getCount() {
        return null == mFragments ? 0 : mFragments.size();
    }

    @Override
    public SlideFragment getItem(int position) {
        return null == mFragments ? null : mFragments.get(position);
    }
}
