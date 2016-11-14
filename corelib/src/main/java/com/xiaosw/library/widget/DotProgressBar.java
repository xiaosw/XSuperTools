package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.xiaosw.library.R;


/**
 * <p><br/>ClassName : {@link DotProgressBar}
 * <br/>Description : 自定义圆形进度条
 * Simple：
 * <com.xiaosw.library.widget.DotProgressBar
 *     xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:xiaosw="http://schemas.android.com/apk/res-auto"
 *     xmlns:tools="http://schemas.android.com/tools"
 *     android:id="@+id/dot_progress_bar"
 *     android:layout_width="120dp"
 *     android:layout_height="120dp"
 *     android:padding="6dp"
 *     xiaosw:point_radius="3dp"
 *     xiaosw:stroke_width="2dp"
 *     xiaosw:default_color="#C0C0C0"
 *     xiaosw:progress_color="#009f00"
 *     xiaosw:start_angle="-90"
 *     android:layout_gravity="center_horizontal"
 *     android:layout_centerInParent="true"/>
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-14 10:10:06</p>
 */
public class DotProgressBar extends View {

    ///////////////////////////////////////////////////////////////////////////
    // 常量部分
    ///////////////////////////////////////////////////////////////////////////
    /** getClass().getSimpleName() */
    private static final String TAG = "xiaosw-DotProgressBar";
    /** 动点半径 */
    private final float MIN_POINT_RADIUS_PX = 6;
    /** 弧线宽度 */
    private final float MIN_STROKE_WIDTH_PX = 4f;

    ///////////////////////////////////////////////////////////////////////////
    // 自定义属性
    ///////////////////////////////////////////////////////////////////////////
    /** 动点半径 */
    private float mPointRadius;
    /** 弧线宽度 */
    private float mStrokeWidth;
    /** 默认颜色 */
    private int mDefaultColor;
    /** 当前进度颜色 */
    private int mProgressColor; 
    /** 开始绘制角度 */
    private int mStartAngle;

    ///////////////////////////////////////////////////////////////////////////
    // 绘制相关
    ///////////////////////////////////////////////////////////////////////////
    /** 圆圈进度 */
    private RectF mRectF;
    /** 进度画笔 */
    private Paint mPaint;
    /** x-coordinate of the center of the cirle to be drawn */
    private float mCx;
    /** he y-coordinate of the center of the cirle to be drawn */
    private float mCy;
    /** he radius of the cirle to be drawn */
    private float mRadius;

    /** 最大进度 */
    private int mMax;
    /** 当前进度 */
    private int mProgress;
    /** 当前角度 */
    private float mAngle;

    public DotProgressBar(Context context) {
        super(context);
        initialize(context, null);
    }

    public DotProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs 自定义属性
     */
    private void initialize(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DotProgressBar);
        mPointRadius = ta.getDimension(R.styleable.DotProgressBar_point_radius, MIN_POINT_RADIUS_PX);
        mStrokeWidth = ta.getDimension(R.styleable.DotProgressBar_stroke_width, MIN_STROKE_WIDTH_PX);
        mDefaultColor = ta.getColor(R.styleable.DotProgressBar_default_color, Color.parseColor("#C0C0C0"));
        mProgressColor = ta.getColor(R.styleable.DotProgressBar_progress_color, Color.parseColor("#009f00"));
        mStartAngle = ta.getInt(R.styleable.DotProgressBar_start_angle, -90);
        ta.recycle();
        mMax = 100;
        mProgress = 0;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mStrokeWidth);
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCx = getMeasuredWidth() / 2.0f;
        mCy = getMeasuredHeight() / 2.0f;
        if (mCx > mCy) {
            mRadius = mCy - (getPaddingTop() + getPaddingBottom()) / 2.0f;
        } else {
            mRadius = mCx - (getPaddingLeft() + getPaddingRight()) / 2.0f;
        }
        mRectF.set(mCx - mRadius, mCy - mRadius, mCx + mRadius, mCy + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        // 默认圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mDefaultColor);
        canvas.drawArc(mRectF, 0, 360, false, mPaint);

        // 当前进度圆
        mPaint.setColor(mProgressColor);
        canvas.drawArc(mRectF, mStartAngle, mAngle, false, mPaint);

        // 圆点
        mPaint.setStyle(Paint.Style.FILL);
        float radian = angle2Radian(mStartAngle + mAngle);
        canvas.drawCircle(mCx + mRadius * (float) Math.cos(radian), mCy + mRadius *  (float) Math.sin(radian), mPointRadius, mPaint);
        canvas.restore();
    }

    /**
     * <p>Return the upper limit of this progress bar's range.</p>
     *
     * @return a positive integer
     *
     * @see #setMax(int)
     * @see #getProgress()
     */
    public int getMax() {
        return mMax;
    }

    /**
     * <p>Set the range of the progress bar to 0...<tt>max</tt>.</p>
     *
     * @param max the upper range of this progress bar
     *
     * @see #getMax()
     * @see #setProgress(int)
     */
    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != mMax) {
            mMax = max;
            postInvalidate();

            if (mProgress > max) {
                mProgress = max;
            }
        }
    }

    /**
     * <p>Get the progress bar's current level of progress. Return 0 when the
     * progress bar is in indeterminate mode.</p>
     *
     * @return the current progress, between 0 and {@link #getMax()}
     *
     * @see #setProgress(int)
     * @see #setMax(int)
     * @see #getMax()
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * <p>Set the current progress to the specified value. Does not do anything
     * if the progress bar is in indeterminate mode.</p>
     *
     * @param progress the new progress, between 0 and {@link #getMax()}
     *
     * @see #getProgress()
     */
    public boolean setProgress(int progress) {
        progress = Math.min(Math.max(0, progress), mMax);

        if (progress == mProgress) {
            // No change from current.
            return false;
        }

        mProgress = progress;
        mProgress = progress;
        mAngle = mProgress * 360.0f / (float) mMax;
        if (mProgress <= mMax) {
            invalidate();
        }
        return true;

    }

    /**
     * 获取动点半径(dp)
     * @return unit is dpi
     */
    public float getPointRadius() {
        return mPointRadius;
    }

    /**
     * 设置动点半径(dp)
     * @param pointRadius unit is dpi
     */
    public void setPointRadius(float pointRadius) {
        mPointRadius = pointRadius;
    }

    /**
     * @see #setStrokeWidth(float)
     * @return
     */
    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    /**
     * Set the width for stroking.
     * Pass 0 to stroke in hairline mode.
     * Hairlines always draws a single pixel independent of the canva's matrix.
     *
     * @param width set the paint's stroke width, used whenever the paint's
     *              style is Stroke or StrokeAndFill.
     */
    public void setStrokeWidth(float width) {
        mStrokeWidth = width;
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    /**
     * @see #setDefaultColor(int)
     * @return
     */
    public int getDefaultColor() {
        return mDefaultColor;
    }

    /**
     * 默认颜色
     * @param defaultColor
     */
    public void setDefaultColor(int defaultColor) {
        mDefaultColor = defaultColor;
    }

    /**
     * @see #setProgressColor(int)
     * @return
     */
    public int getProgressColor() {
        return mProgressColor;
    }

    /**
     * 设置当前进度画笔颜色
     * @param progressColor
     */
    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    /**
     * @see #setStartAngle(int)
     * @return
     */
    public int getStartAngle() {
        return mStartAngle;
    }

    /**
     * @param startAngle Starting angle (in degrees) where the arc begins, default is -90
     */
    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
    }

    /**
     * 角度转弧度
     * @param angle
     * @return
     */
    private float angle2Radian(double angle) {
        return (float) (Math.PI / 180 * angle);
    }

    /**
     * 弧度转角度
     * @param radian
     * @return
     */
    private float raidan2Angle(double radian) {
        return (float) (180 / Math.PI * radian);
    }

    /**
     * dp 2 px
     * @param dp dpi
     * @return px
     */
    private float dp2px(float dp) {
        return getResources().getDisplayMetrics().density * dp;
    }

}
