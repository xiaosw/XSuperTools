package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xiaosw.library.widget.adapter.AbsBannerAdapter;
import com.xiaosw.library.widget.internal.GUIAutoSkipViewPager;
import com.xiaosw.library.widget.internal.GUIPageIndeicator;

/**
 * <p><br/>ClassName : {@link GUIBannerView}
 * <br/>Description : 广告view
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-21 18:18:46</p>
 */
public class GUIBannerView extends RelativeLayout implements ViewPager.OnPageChangeListener,
    AbsBannerAdapter.OnNotifyDataSetChangedListener {

    /** Banner */
    private GUIAutoSkipViewPager mGUIAutoSkipViewPager;
    /** Banner 指示器 */
    private GUIPageIndeicator mGUIPageIndeicator;

    public GUIBannerView(Context context) {
        this(context, null);
    }

    public GUIBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GUIBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIBannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Banner
        mGUIAutoSkipViewPager = new GUIAutoSkipViewPager(context);
        mGUIAutoSkipViewPager.addOnPageChangeListener(this);
        addView(mGUIAutoSkipViewPager, new LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT));

        // 指示器
        mGUIPageIndeicator = new GUIPageIndeicator(context);
        RelativeLayout.LayoutParams mIndeicatorParams = new RelativeLayout.LayoutParams(
                                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                ViewGroup.LayoutParams.WRAP_CONTENT);
        mIndeicatorParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mIndeicatorParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mGUIPageIndeicator, mIndeicatorParams);
    }

    public void setAdapter(AbsBannerAdapter adapter) {
        adapter.addOnNotifyDataSetChangedListener(mGUIAutoSkipViewPager);
        mGUIAutoSkipViewPager.setAdapter(adapter);
        mGUIPageIndeicator.setIndicatorCount(adapter.getRealCount());
        mGUIPageIndeicator.setCurrentSelectIndicator(mGUIAutoSkipViewPager.getCurrentItem());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mGUIPageIndeicator.setCurrentSelectIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onNotifyDataSetChanged() {
        mGUIPageIndeicator.setIndicatorCount(mGUIAutoSkipViewPager.getRealItemCount());
    }

    public void setsetOnSingleTapListener(GUIAutoSkipViewPager.OnSingleTapListener listener) {
        mGUIAutoSkipViewPager.setOnSingleTapListener(listener);
    }

    public GUIAutoSkipViewPager getGUIAutoSkipViewPager() {
        return mGUIAutoSkipViewPager;
    }

    public GUIPageIndeicator getGUIPageIndeicator() {
        return mGUIPageIndeicator;
    }
}
