package com.xiaosw.library.widget.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @ClassName : {@link BaseFragmentPagerAdapter}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-15 20:20:45
 */
public class BaseFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> mFragments;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return null == mFragments ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return null == mFragments ? 0 : mFragments.size();
    }

}
