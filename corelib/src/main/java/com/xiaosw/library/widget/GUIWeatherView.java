package com.xiaosw.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * <p><br/>ClassName : {@link GUIWeatherView}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-24 14:14:34</p>
 */

public class GUIWeatherView extends View {
    
    /** @see GUIWeatherView#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-GUIWeatherView";

    /**
     * 默认控件大小 px
     */
    private static final int DEFAULT_SIZE_PX = 240;
    /**
     * 线宽
     */
    private static final int LINE_WIDTH = 3;

    /** 圆弧起点度数 */
    private int mStartAngle;
    /** 圆弧角度 */
    private int mSweepAngle;
    /** 圆弧分割份数 */
    private int mClipCount;
    /** 份数角度 */
    private int mGapAngle;

    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;

    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private RectF mArcRectF;


    public GUIWeatherView(Context context) {
        super(context);
        init(context);
    }

    public GUIWeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GUIWeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context) {
        mStartAngle = 120;
        mSweepAngle = 300;
        mClipCount = 60;
        mGapAngle = mSweepAngle / mClipCount;

        mArcRectF = new RectF();

        initPaint(context);
    }

    /**
     * 初始化画笔
     * @param context
     */
    private void initPaint(Context context) {
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStrokeWidth(LINE_WIDTH);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setColor(Color.RED);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(LINE_WIDTH);
        mLinePaint.setColor(Color.RED);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(60);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(DEFAULT_SIZE_PX, widthMeasureSpec);
        int height = measureDimension(DEFAULT_SIZE_PX, heightMeasureSpec);

        int realSize = Math.max(width, height);
        mCenterX = width / 2;
        mCenterY = height / 2;
        mRadius = realSize / 2 - LINE_WIDTH;

        mArcRectF.set(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);

        setMeasuredDimension(realSize, realSize);
    }

    /**
     * 测量控件尺寸
     * @param defaultSize
     * @param measureSpec
     * @return
     */
    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawArcView(canvas);
        drawShortLine(canvas);
        drawText(canvas);
    }

    /**
     * 绘制圆环
     * @param canvas
     */
    private void drawArcView(Canvas canvas) {
        canvas.drawArc(mArcRectF, mStartAngle, mSweepAngle, false, mArcPaint);
    }

    /**
     * 绘制短线
     * @param canvas
     */
    private void drawShortLine(Canvas canvas) {
        canvas.save();
        canvas.rotate(-mStartAngle - 90, mCenterX, mCenterY);
        for (int i = 0; i <= mClipCount; i++) {
            if (i % 2 == 0) {
                mLinePaint.setColor(Color.parseColor("#FFD700"));
            } else {
                mLinePaint.setColor(Color.GRAY);
            }
            if (i == 0 || i == mClipCount) {
                canvas.drawLine(mRadius + LINE_WIDTH / 2f, LINE_WIDTH / 2f, mRadius + LINE_WIDTH / 2f, 80, mLinePaint);
                Log.e(TAG, "drawShortLine: " + i);
            } else {
                canvas.drawLine(mRadius + LINE_WIDTH / 2f, LINE_WIDTH / 2f, mRadius + LINE_WIDTH / 2f, 40, mLinePaint);
            }
            canvas.rotate(-mGapAngle, mCenterX, mCenterY);
        }
        canvas.restore();
    }

    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        canvas.drawLine(mCenterX, mCenterY, mCenterX + mRadius * (float) Math.cos(45), mCenterY - mRadius * (float) Math.sin(45), mLinePaint);

        String text = "24℃";
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        float x = mCenterX - (bounds.right - bounds.left) / 2f;
        float y = mCenterY - (bounds.bottom + bounds.top) / 2f;
        canvas.drawText(text, x, y, mTextPaint);
    }
}
