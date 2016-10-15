package com.xiaosw.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * @ClassName : {@link ViewPagerTab}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-12 20:20:26
 */
public class ViewPagerTab extends ViewPager implements ViewPager.OnPageChangeListener {

    private OnPageChangeListener mOnPageChangeListener;

    public ViewPagerTab(Context context) {
        super(context);
        initViewPager();
    }

    public ViewPagerTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    void initViewPager() {
        addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        if (null != mOnPageChangeListener) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (null != mOnPageChangeListener) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (null != mOnPageChangeListener) {
            mOnPageChangeListener.onPageSelected(position);
        }
    }

    public void registerPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

}
