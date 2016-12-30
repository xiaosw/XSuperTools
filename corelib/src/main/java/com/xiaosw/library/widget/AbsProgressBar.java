package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.xiaosw.library.R;

import java.text.DecimalFormat;

/**
 * <p><br/>ClassName : {@link AbsProgressBar}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2016-12-30 15:15:48</p>
 */
abstract class AbsProgressBar extends ProgressBar {

    /**
     * @see AbsProgressBar#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-BaseProgressBar";

    final DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

    final int DEFAULT_TEXT_SIZE_PX = 32;

    ///////////////////////////////////////////////////////////////////////////
    // size
    ///////////////////////////////////////////////////////////////////////////
    /** 控件宽度 */
    int mWidth;
    /** 控件高 */
    int mHeight;
    /** 实际绘制宽度 */
    int mRealWidth;
    /** 实际绘制高度 */
    int mRealHeight;
    /** 控件X轴中心坐标 */
    float mCenterX;
    /** 控件Y轴中心坐标 */
    float mCenterY;

    ///////////////////////////////////////////////////////////////////////////
    // attrs
    ///////////////////////////////////////////////////////////////////////////
    /** 最小宽度 */
    int mMinWidth;
    /** 最小高度 */
    int mMinHeight;
    /** 文字大小 */
    int mTextSize;
    /** 其实角度 */
    int mStartAngle;
    /** 默认颜色 */
    int mColorDefault;
    int mDuration;

    ///////////////////////////////////////////////////////////////////////////
    // draw
    ///////////////////////////////////////////////////////////////////////////
    /** 绘制文字 */
    Paint mTextPaint;
    /** 测量文字尺寸信息 */
    Rect mTextBound;

    /**
     * @see View#onAttachedToWindow()
     * @see View#onDetachedFromWindow()
     */
    private volatile boolean isAttachedToWindow;

    public AbsProgressBar(Context context) {
        this(context, null);
    }

    public AbsProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.GUIProgressBar);
    }

    public AbsProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbsProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, 0);
    }

    /**
     * init
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        initDefault();
        parseAttrs(context, attrs, defStyleAttr, defStyleRes);
        applyAttrs();
    }

    /**
     * init defualt
     * call before {@link AbsProgressBar#parseAttrs(Context, AttributeSet, int, int)}
     */
    private void initDefault() {
        mDuration = 4000;
        mMinWidth = 0;
        mMinHeight = 0;
        mTextSize = DEFAULT_TEXT_SIZE_PX;
        mStartAngle = 0;
        mColorDefault = Color.GRAY;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextBound = new Rect();
        onInitDefault();
    }

    /**
     * parse custom attrs, call before {@link AbsProgressBar#initDefault()}, after {@link AbsProgressBar#applyAttrs()}
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    private void parseAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        onParseAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * apply attrs, call after {@link AbsProgressBar#parseAttrs(Context, AttributeSet, int, int)}
     */
    private void applyAttrs() {
        mTextPaint.setTextSize(mTextSize);
       onApplyAttrs();
    }

    /**
     * @see AbsProgressBar#initDefault()
     */
    protected abstract void onInitDefault();

    /**
     * @see AbsProgressBar#parseAttrs(Context, AttributeSet, int, int)
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    protected abstract void onParseAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes);

    /**
     * @see AbsProgressBar#applyAttrs()
     */
    protected abstract void onApplyAttrs();

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.AT_MOST
            && widthMode != MeasureSpec.UNSPECIFIED
            && heightMode != MeasureSpec.AT_MOST
            && heightMode != MeasureSpec.UNSPECIFIED) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            switch (widthMode) {
                case MeasureSpec.AT_MOST:
                case MeasureSpec.UNSPECIFIED:
                    width = mMinWidth;
                    break;

                default:
                    // TODO: 2016/12/8
            }

            switch (heightMode) {
                case MeasureSpec.AT_MOST:
                case MeasureSpec.UNSPECIFIED:
                    height = mMinHeight;
                    break;

                default:
                    // TODO: 2016/12/8
            }
            setMeasuredDimension(width, height);
        }
        calulateSize();
    }

    void calulateSize() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRealWidth = mWidth - getPaddingTop() - getPaddingBottom();
        mRealHeight = mHeight - getPaddingLeft() - getPaddingRight();
        mCenterX = getPaddingLeft() + (float) mRealWidth / 2;
        mCenterY = getPaddingTop() + (float) mRealHeight / 2;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
    }

    public float getSweepAngle() {
        return (getProgress() * 360f / getMax());
    }

    public String getPercentage() {
        return mDecimalFormat.format((float) getProgress() * 100 / getMax()).concat("%");
    }

    /**
     * 角度转弧度
     * @param angle
     * @return
     */
    public float angle2Radian(double angle) {
        return (float) (Math.PI / 180 * angle);
    }

    /**
     * 弧度转角度
     * @param radian
     * @return
     */
    public float raidan2Angle(double radian) {
        return (float) (180 / Math.PI * radian);
    }

    /**
     * dp 2 px
     * @param dp dpi
     * @return px
     */
    public float dp2px(float dp) {
        return getResources().getDisplayMetrics().density * dp;
    }
    /**
     * 是否绑定到window，兼容低版本
     * @return
     */
    public boolean isAttachedToWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return super.isAttachedToWindow();
        }
        return isAttachedToWindow;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        invalidate();
    }

    public int getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
        invalidate();
    }

    public int getColorDefault() {
        return mColorDefault;
    }

    public void setColorDefault(int colorDefault) {
        mColorDefault = colorDefault;
        invalidate();
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }
}
