package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * @ClassName : {@link HorizontalScrollViewTabHost}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-12 20:20:32
 */
public class HorizontalScrollViewTabHost extends HorizontalScrollView
    implements ViewPager.OnPageChangeListener{

    public HorizontalScrollViewTabHost(Context context) {
        this(context, null);
    }

    public HorizontalScrollViewTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialized();
    }

    public HorizontalScrollViewTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialized();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalScrollViewTabHost(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialized();
    }

    void initialized() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {

    }
}
