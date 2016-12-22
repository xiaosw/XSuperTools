package com.xiaosw.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * <p><br/>ClassName : {@link GUIBaseViewPager}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-21 18:18:49</p>
 */
public class GUIBaseViewPager extends ViewPager {

    public GUIBaseViewPager(Context context) {
        super(context);
        init();
    }

    public GUIBaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
    }
}
