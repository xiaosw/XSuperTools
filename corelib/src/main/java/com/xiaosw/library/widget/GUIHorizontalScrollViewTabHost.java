package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xiaosw.library.R;
import com.xiaosw.library.utils.LogUtil;

/**
 * @ClassName : {@link GUIHorizontalScrollViewTabHost}
 * @Description : 字母指示器
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-12 20:20:32
 */
public class GUIHorizontalScrollViewTabHost extends HorizontalScrollView
    implements ViewPager.OnPageChangeListener {

    private RadioGroup mRadioGroup;
    private GUITabViewPager mViewPager;

    public GUIHorizontalScrollViewTabHost(Context context) {
        this(context, null);
    }

    public GUIHorizontalScrollViewTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialized();
    }

    public GUIHorizontalScrollViewTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialized();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIHorizontalScrollViewTabHost(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialized();
    }

    void initialized() {
        mRadioGroup = (RadioGroup) inflate(getContext(), R.layout.view_radio_group, null);
        mRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        mRadioGroup.setGravity(Gravity.CENTER);
        addView(mRadioGroup, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        GUIColorTrackRadioButton left = null;
        GUIColorTrackRadioButton right = null;
        if (positionOffset > 0 && position < mRadioGroup.getChildCount() - 1) {
            left = (GUIColorTrackRadioButton) mRadioGroup.getChildAt(position);
            right = (GUIColorTrackRadioButton) mRadioGroup.getChildAt(position + 1);
        } else if (positionOffset < 0 && position > 0) {
            left = (GUIColorTrackRadioButton) mRadioGroup.getChildAt(position-1);
            right = (GUIColorTrackRadioButton) mRadioGroup.getChildAt(position);
        }
        if (null != left) {
            left.setClipStart(GUIColorTrackRadioButton.CLIP_START_BY_RIGHT);
            right.setClipStart(GUIColorTrackRadioButton.CLIP_START_BY_LEFT);
            left.setProgress(1 - positionOffset);
            right.setProgress(positionOffset);
        }
        if (null != right) {

        }
    }

    @Override
    public void onPageSelected(int position) {
        int childCount = mRadioGroup.getChildCount();
        if (position > childCount) {
            LogUtil.w("page size is not equal RadioButton size");
            return;
        }
        int checkedId = mRadioGroup.getChildAt(position).getId();
        if (mRadioGroup.getCheckedRadioButtonId() != checkedId) {
            mRadioGroup.check(checkedId);
        }
    }

    public GUIColorTrackRadioButton getBasicRadioButton() {
        return (GUIColorTrackRadioButton) inflate(getContext(), R.layout.view_radio_button, null);
    }

    public void addRadioButton (RadioButton radioButton) throws IllegalArgumentException {
        addRadioButton(radioButton, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void addRadioButton(int viewId) {
        addRadioButton((RadioButton)inflate(getContext(), viewId, null));
    }

    public void addRadioButton (RadioButton radioButton, FrameLayout.LayoutParams params) throws IllegalArgumentException {
        View childView = getChildAt(0);
        if (!(childView instanceof RadioGroup)) {
            new IllegalArgumentException(childView + " not cast RaidoGroup!!!");
        }
        ((RadioGroup) childView).addView(radioButton, params);
    }

    public void bindViewPager(GUITabViewPager viewPager) {
        this.mViewPager = viewPager;
        mRadioGroup.setOnCheckedChangeListener(mViewPager);
        mViewPager.bindHorizontalScrollViewTabHost(this);
    }

}
