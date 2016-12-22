package com.xiaosw.library.widget.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaosw.library.R;

/**
 * <p><br/>ClassName : {@link GUIPageIndeicator}
 * <br/>Description : 页面指示器控件（e.g: 广告方指示器）
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-22 13:13:09</p>
 */
public class GUIPageIndeicator extends LinearLayout {

    /**
     * @see GUIPageIndeicator#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-GUIPageIndeicator";

    private Drawable mIndeicatorItemBackgroud;

    public GUIPageIndeicator(Context context) {
        this(context, null);
    }

    public GUIPageIndeicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public GUIPageIndeicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIPageIndeicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
    }

    private Drawable getCompatDrawable(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getContext().getDrawable(resId);
        } else {
            return getResources().getDrawable(resId);
        }
    }

    public void setIndicatorCount(int count) {
        setIndicatorCount(count, R.layout.item_page_indicator);
    }

    public void setIndicatorCount(int count, int indicatorItemLayout) {
        removeAllViews();
        if (count <= 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            View view = LayoutInflater.from(getContext()).inflate(indicatorItemLayout, this);
            if (null != mIndeicatorItemBackgroud) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getChildAt(i).setBackground(mIndeicatorItemBackgroud);
                } else {
                    getChildAt(i).setBackgroundDrawable(mIndeicatorItemBackgroud);
                }
            }
        }
    }

    public void setCurrentSelectIndicator(int pos) {
        final int childCount = getChildCount();
        if (childCount < 1) {
            return;
        }
        int index = pos % childCount;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setEnabled(i != index);
        }
    }

    /**
     * @see GUIPageIndeicator#setIndeicatorItemBackgroud(Drawable)
     * @param resId
     */
    public void setIndeicatorItemBackgroud(int resId) {
        setIndeicatorItemBackgroud(getCompatDrawable(resId));
    }

    /**
     * 必须在调用{@link GUIPageIndeicator#setIndicatorCount(int, int)}前调用才会生效
     * @param indeicatorItemBackgroud
     */
    public void setIndeicatorItemBackgroud(Drawable indeicatorItemBackgroud) {
        mIndeicatorItemBackgroud = indeicatorItemBackgroud;
    }
}
