package com.xiaosw.library.widget.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.xiaosw.library.widget.adapter.AbsBannerAdapter;

/**
 * <p><br/>ClassName : {@link GUIAutoSkipViewPager}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-22 11:11:04</p>
 */
public class GUIAutoSkipViewPager extends ViewPager implements Handler.Callback,
    AbsBannerAdapter.OnNotifyDataSetChangedListener {

    /**
     * @see GUIAutoSkipViewPager#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-AutoSkipViewPager";

    private static final int WHAT_NORMAL_SKIP = 0x0;
    private static final int WHAT_FAST_SKIP = 0x2;

    /** 正常跳转时间 */
    private final int NORMAL_SKIP_TIME = 4000;
    /** 快速跳转时间 */
    private final int FAST_SKIP_TIME = 2000;

    private OnSingleTapListener mOnSingleTapListener;
    private GestureDetectorCompat mGestureDetectorCompat;
    private Handler mSkipHandler;
    private int mSkipTime;

    public GUIAutoSkipViewPager(Context context) {
        this(context, null);
    }

    public GUIAutoSkipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetectorCompat = new GestureDetectorCompat(context, mOnGestureListener);
        mSkipHandler = new Handler(Looper.getMainLooper(), this);
        mSkipTime = NORMAL_SKIP_TIME;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == WHAT_FAST_SKIP) {
            fastSkip();
        } else if (msg.what == WHAT_NORMAL_SKIP){
            nextPage();
            normalSkip();
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanSkip()) {
            requestDisallowInterceptTouchEvent(true);
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                removeAllSkip();
            } else if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL) {
                fastSkip();
            }
        }
        return mGestureDetectorCompat.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        startSkip();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopSkip();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startSkip();
    }

    /**
     * 启动自动跳转
     */
    public void startSkip() {
        normalSkip();
    }

    /**
     * 停止自动跳转
     */
    public void stopSkip() {
        removeAllSkip();
    }

    /**
     * 快速跳转
     */
    private void fastSkip() {
        skip(WHAT_NORMAL_SKIP, FAST_SKIP_TIME);
    }

    /**
     * 正常跳转
     */
    private void normalSkip() {
        skip(WHAT_NORMAL_SKIP, mSkipTime);
    }

    private void skip(int what, int delayMillis) {
        removeAllSkip();
        if (isCanSkip()) {
            mSkipHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
    }

    /**
     * 取消跳转
     */
    private void removeAllSkip() {
        mSkipHandler.removeMessages(WHAT_FAST_SKIP);
        mSkipHandler.removeMessages(WHAT_NORMAL_SKIP);
    }

    /**
     * 跳转至下一页
     */
    public void nextPage() {
        int currentItem = getCurrentItem();
        ++currentItem;
        setCurrentItem(currentItem >= getCount() ? 0 : currentItem, true);
    }

    /**
     * 如果使用的是 {@link AbsBannerAdapter} 则返回的就是 {@link AbsBannerAdapter#getRealCount()}
     * 否则返回 {@link PagerAdapter#getCount()}
     * @return
     */
    public int getRealItemCount() {
        PagerAdapter adapter = getAdapter();
        if (null != adapter && adapter instanceof AbsBannerAdapter) {
            return ((AbsBannerAdapter) adapter).getRealCount();
        }
        return getCount();
    }

    /**
     * 是否可以跳转
     * @return
     */
    public boolean isCanSkip() {
        return getRealItemCount() > 1;
    }

    public int getCount() {
        return getAdapter().getCount();
    }

    public void setSkipTime(int skipTime) {
        mSkipTime = skipTime;
    }

    public void setOnSingleTapListener(OnSingleTapListener onSingleTapListener) {
        mOnSingleTapListener = onSingleTapListener;
    }

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mOnSingleTapListener != null) {
                return mOnSingleTapListener.onSingleTap();
            }
            return super.onSingleTapUp(e);
        }
    };

    public static interface OnSingleTapListener {
        /**
         * 处理了该单击监听，则返回true，否则返回false
         * @return
         */
        boolean onSingleTap();
    }

    @Override
    public void onNotifyDataSetChanged() {
        removeAllSkip();
        startSkip();
    }
}
