package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.xiaosw.library.utils.LogUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * <p><br/>ClassName : {@link GUIBrokenLineGraphView}
 * <br/>Description : 折线图
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-27 18:18:15</p>
 */
public class GUIBrokenLineGraphView<T extends GUIBrokenLineGraphView.BaseData> extends View {

    /**
     * @see GUIBrokenLineGraphView#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-GUIBrokenLineGraphView";

    /** 最小宽度 */
    private final int MIN_WIDTH_PX = 800;
    /** 最小高度 */
    private final int MIN_HEIGTH_PX = 800;

    /** 背景边距 */
    private final int BACKGROUND_MARGIN = 20;
    private final int BACKGROUND_PADDING = 120;
    private final int BACKGROUND_ARROW_LEN = 10;

    /** 背景线颜色 */
    private final int BACKGROUD_LINE_COLOR = Color.GRAY;
    /** 文字颜色 */
    private final int TEXT_COLRO = BACKGROUD_LINE_COLOR;

    /** Y轴刻度数 */
    private final int SCALE_MARK_COUNT = 7;

    /** 实际宽度 */
    private float mRealWidth;
    /** 实际高度 */
    private float mRealHeight;
    /** 实际高度 */
    private float mBackgroundWidth;
    private float mBackgroundHeight;
    private List<T> mData;
    private List<Point> mPoints = new ArrayList<>();
    private float mPointRadius = 6.0f;
    private double mSclaeIntervalX;
    private double mSclaeIntervalY;
    private double mMax;
    private DecimalFormat mDecimalFormat;
    /** 线、点等 */
    private Paint mContentPaint;
    /** 坐标刻度 */
    private Paint mBackgroundPaint;
    private Rect mTextBound;

    public GUIBrokenLineGraphView(Context context) {
        super(context);
        init(context, null);
    }

    public GUIBrokenLineGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GUIBrokenLineGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIBrokenLineGraphView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                LogUtil.e(TAG, "" + (MeasureSpec.AT_MOST == widthMode) + ", " + (MeasureSpec.UNSPECIFIED == widthMode));
                width = MIN_WIDTH_PX;
                break;

            default:
                // TODO: 2016/12/8
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = MIN_HEIGTH_PX;
                break;

            default:
                // TODO: 2016/12/8
        }
        setMeasuredDimension(width, height);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mRealHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        handleData(mData, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroud(canvas);
        drawBrokenLine(canvas);
    }

    private void drawBackgroud(Canvas canvas) {

        mBackgroundPaint.setColor(BACKGROUD_LINE_COLOR);
        // horizontal
        // line
        float startX = BACKGROUND_MARGIN;
        float startY = mRealHeight - BACKGROUND_MARGIN - BACKGROUND_PADDING;
        float stopX = mRealWidth - BACKGROUND_MARGIN;
        float stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, mBackgroundPaint);
        // arrow
        canvas.drawLine(stopX - BACKGROUND_ARROW_LEN, stopY - BACKGROUND_ARROW_LEN, stopX, stopY, mBackgroundPaint);
        canvas.drawLine(stopX - BACKGROUND_ARROW_LEN, stopY + BACKGROUND_ARROW_LEN, stopX, stopY, mBackgroundPaint);

        // vertical
        // line
        startX = BACKGROUND_MARGIN + BACKGROUND_PADDING;
        startY = mRealHeight - BACKGROUND_MARGIN;
        stopX = startX;
        stopY = BACKGROUND_MARGIN;
        canvas.drawLine(startX, startY, stopX, stopY, mBackgroundPaint);
        // arrow
        canvas.drawLine(stopX - BACKGROUND_ARROW_LEN, stopY + BACKGROUND_ARROW_LEN, stopX, stopY, mBackgroundPaint);
        canvas.drawLine(stopX + BACKGROUND_ARROW_LEN, stopY + BACKGROUND_ARROW_LEN, stopX, stopY, mBackgroundPaint);

        // 网格
        startY = (int) (mRealHeight - BACKGROUND_MARGIN - BACKGROUND_PADDING);
        for (int i = 0; i < SCALE_MARK_COUNT; i++) {
            startX = BACKGROUND_MARGIN + BACKGROUND_PADDING;
            startY -= mSclaeIntervalY;
            stopX = mRealWidth - BACKGROUND_MARGIN - BACKGROUND_ARROW_LEN;
            stopY = startY;
            mBackgroundPaint.setColor(BACKGROUD_LINE_COLOR);
            canvas.drawLine(startX, startY, stopX, stopY, mBackgroundPaint);

            mBackgroundPaint.setColor(TEXT_COLRO);
            String text = String.valueOf(mDecimalFormat.format(mMax / SCALE_MARK_COUNT * (i + 1)));
            mBackgroundPaint.getTextBounds(text, 0, text.length(), mTextBound);
            canvas.drawText(text, startX - mTextBound.width() - 12, startY + mTextBound.height() / 2, mBackgroundPaint);
        }
    }

    /**
     * 绘制折线图
     * @param canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        int pointSize = mPoints.size();
        for (int i = 0; i < pointSize - 1; i++) {
            Point point = mPoints.get(i);
            Point nextPoint = mPoints.get(i + 1);
            canvas.drawLine(point.x, point.y, nextPoint.x, nextPoint.y, mContentPaint);
            canvas.drawCircle(point.x, point.y, mPointRadius, mContentPaint);
            if (i == pointSize - 2) {
                canvas.drawCircle(nextPoint.x, nextPoint.y, mPointRadius, mContentPaint);
            }
        }
    }

    private void init(Context context, AttributeSet attrs) {
        //
        mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContentPaint.setColor(Color.RED);

        // 坐标刻度
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.BLACK);
        mBackgroundPaint.setTextSize(28);
        mTextBound = new Rect();

        // 保留三位小数
        mDecimalFormat = new DecimalFormat("#.000");
    }

    public void setData(List<T> data) {
        handleData(data, true);
    }

    private void handleData(List<T> data, boolean needInvalidate) {
        if (data == null || data.isEmpty()) {
            return;
        }
        this.mData = data;
        mMax = mData.get(0).getSize();
        for (int i = 1; i < mData.size(); i++) {
            double temp = mData.get(i).getSize();
            if (mMax < temp) {
                mMax = temp;
            }
        }
        mBackgroundWidth = mRealWidth - BACKGROUND_MARGIN * 2 - BACKGROUND_PADDING - BACKGROUND_ARROW_LEN;
        mSclaeIntervalX = mBackgroundWidth / data.size();

        mBackgroundHeight = mRealHeight - BACKGROUND_MARGIN * 2 - BACKGROUND_PADDING - BACKGROUND_ARROW_LEN * 10;
        mSclaeIntervalY = mBackgroundHeight / SCALE_MARK_COUNT;

        double yScale = mBackgroundHeight / mMax;
        mPoints.clear();
        if (null != mData) {
            for (int i = 0; i < mData.size(); i++) {
                mPoints.add(new Point((int) (BACKGROUND_MARGIN + BACKGROUND_PADDING + mSclaeIntervalX * i),
                    (int) (mRealHeight - yScale * mData.get(i).getSize() - BACKGROUND_MARGIN - BACKGROUND_PADDING)));
            }
        }
        if (needInvalidate) {
            invalidate();
        }
    }

    public static class BaseData {
        private double size;

        public BaseData(double size) {
            this.size = size;
        }

        public double getSize() {
            return size;
        }

        public void setSize(double size) {
            this.size = size;
        }
    }
}
