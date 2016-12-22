package com.xiaosw.library.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.xiaosw.library.widget.internal.AutoSkipViewPager;

/**
 * <p><br/>ClassName : {@link GUIBannerViewPager}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-21 18:18:46</p>
 */
public class GUIBannerViewPager extends AutoSkipViewPager {

    /** @see GUIBannerViewPager#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-GUIBannerViewPager";

    public GUIBannerViewPager(Context context) {
        super(context);
    }

    public GUIBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        super.addOnAdapterChangeListener(listener);
    }
}
