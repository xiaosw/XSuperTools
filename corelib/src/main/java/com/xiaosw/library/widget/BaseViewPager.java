package com.xiaosw.library.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import com.xiaosw.library.utils.LogUtil;

/**
 * @ClassName : {@link BaseViewPager}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-12 20:20:26
 */
public class BaseViewPager extends ViewPager implements RadioGroup.OnCheckedChangeListener {

    private HorizontalScrollViewTabHost mHorizontalScrollViewTabHost;
    private GestureDetector mGestureDetector;
    private OnTabChangeListener mOnTabChangeListener;

    public BaseViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    void initViewPager() {
        mGestureDetector = new GestureDetector(getContext(), new ViewPagerSimpleOnGestureListener());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        PagerAdapter pagerAdapter = getAdapter();
        if (null == pagerAdapter) {
            return;
        }
        int pageCount = pagerAdapter.getCount();
        int childCount = group.getChildCount();
        if (pageCount < childCount) {
            LogUtil.w("page size is not equal to tab size!!!");
            return;
        }
        for (int i = 0; i < childCount; i++) {
            if (checkedId == group.getChildAt(i).getId()) {
                if (getCurrentItem() != i) {
                    setCurrentItem(i);
                }
                break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev)
            ||super.onTouchEvent(ev);
    }

    void bindHorizontalScrollViewTabHost(HorizontalScrollViewTabHost horizontalScrollViewTabHost) {
        this.mHorizontalScrollViewTabHost = horizontalScrollViewTabHost;
        addOnPageChangeListener(mHorizontalScrollViewTabHost);
    }

    private class ViewPagerSimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        private float mDownX;

        @Override
        public boolean onDown(MotionEvent e) {
            mDownX = e.getX();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            handleTab(distanceX);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        private void handleTab(float distanceX) {
            if (mOnTabChangeListener != null) {
                PagerAdapter adapter = getAdapter();
                if (null != adapter) {
                    int pageSize = adapter.getCount();
                    int currentItem = getCurrentItem();
                    if (currentItem == pageSize - 1 && distanceX > 50) {// 最后一页，只处理滑动到下一页
                        mOnTabChangeListener.onNextTab();
                    } else if (currentItem == 0 && distanceX < -50) {
                        mOnTabChangeListener.onUpTab();
                    }
                }
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            handleTab(mDownX - e.getX());
            return super.onSingleTapUp(e);
        }
    }

    public void setOnTabChangeListener(OnTabChangeListener listener) {
        this.mOnTabChangeListener = listener;
    }

    /**
     * 用于TabHost切换
     */
    public interface OnTabChangeListener {
        public void onNextTab();

        public void onUpTab();
    }

}
