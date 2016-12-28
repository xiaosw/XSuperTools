package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
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
public class GUIBrokenLineGraphView<T extends GUIBrokenLineGraphView.LineData> extends View {

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
    /** 线宽 */
    private final int LINE_WIDTH_PX = 3;
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
    private List<List<T>> mLineDatas;
    private List<List<Point>> mLinePoints;
    private List<LineDescription> mLineDescriptions;
    private double mSclaeIntervalX;
    private double mSclaeIntervalY;
    private double mMax;
    private DecimalFormat mDecimalFormat;
    /** 线、点等 */
    private Paint mContentPaint;
    /** 坐标刻度 */
    private Paint mBackgroundPaint;
    private Rect mTextBound;
    private RectF mOriginalIndiactorRectF;
    private RectF mDrawIndiactorRectF;

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
        handleData(false);
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
        startY = (mRealHeight - BACKGROUND_MARGIN - BACKGROUND_PADDING);
        startX = BACKGROUND_MARGIN + BACKGROUND_PADDING;
        stopX = mRealWidth - BACKGROUND_MARGIN - BACKGROUND_ARROW_LEN;
        canvas.drawCircle(startX, startY, LINE_WIDTH_PX * 4, mBackgroundPaint);
        for (int i = 0; i < SCALE_MARK_COUNT; i++) {
            startY -= mSclaeIntervalY;
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
        mDrawIndiactorRectF.set(mOriginalIndiactorRectF.left,
            mOriginalIndiactorRectF.top,
            mOriginalIndiactorRectF.right,
            mOriginalIndiactorRectF.bottom);

        float leftOffset = 0;
        for (int i = 0; i < mLinePoints.size(); i++) {
            List<Point> points = mLinePoints.get(i);
            int pointsSize = points.size();

            LineDescription lineDescription = mLineDescriptions.get(i);
            mContentPaint.setColor(lineDescription.getColor());
            String description = lineDescription.getDescription();

            // 折线图描述
            if (!TextUtils.isEmpty(description)) {
                int rectAndTextGap = 10;
                float l = mDrawIndiactorRectF.left + leftOffset;
                float t = mDrawIndiactorRectF.top;
                float r = l + mDrawIndiactorRectF.width();
                float b = mDrawIndiactorRectF.bottom;
                mDrawIndiactorRectF.set(l, t, r, b);
                canvas.drawRect(mDrawIndiactorRectF, mContentPaint);
                mContentPaint.getTextBounds(description, 0, description.length(), mTextBound);

                // 计算文字剧中
                float textOffset = (mDrawIndiactorRectF.height() - mTextBound.height()) / 2;
                canvas.drawText(description, mDrawIndiactorRectF.right + rectAndTextGap, mDrawIndiactorRectF.bottom - textOffset, mContentPaint);
                leftOffset = mDrawIndiactorRectF.width() + rectAndTextGap * 2 + mTextBound.width();
            }

            for (int j = 0; j < pointsSize - 1; j++) {
                Point point = points.get(j);
                Point nextPoint = points.get(j + 1);
                canvas.drawLine(point.x, point.y, nextPoint.x, nextPoint.y, mContentPaint);
                canvas.drawCircle(point.x, point.y, LINE_WIDTH_PX * 2, mContentPaint);
                if (j == pointsSize - 2) {
                    canvas.drawCircle(nextPoint.x, nextPoint.y, LINE_WIDTH_PX * 3, mContentPaint);
                }
            }
        }

    }

    private void init(Context context, AttributeSet attrs) {
        //
        mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContentPaint.setColor(Color.RED);
        mContentPaint.setTextSize(22);
        mContentPaint.setStrokeWidth(LINE_WIDTH_PX);

        // 坐标刻度
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.BLACK);
        mBackgroundPaint.setTextSize(28);
        mBackgroundPaint.setStrokeWidth(LINE_WIDTH_PX);
        mTextBound = new Rect();
        mOriginalIndiactorRectF = new RectF();
        mDrawIndiactorRectF = new RectF();

        // 保留三位小数
        mDecimalFormat = new DecimalFormat("#.000");

        mLineDatas = new ArrayList<>();
        mLinePoints = new ArrayList<>();
        mLineDescriptions = new ArrayList<>();
    }

    public void setLineDatas(List<List<T>> datas, List<LineDescription> lineColors) {
        mLineDatas.clear();
        mLineDescriptions.clear();
        if (null != datas) {
            mLineDatas = datas;
        }
        if (null != mLineDescriptions) {
            mLineDescriptions = lineColors;
        }
        if (mLineDatas.size() != mLineDescriptions.size()) {
            throw new IllegalArgumentException("line size is " + mLineDatas.size() + ", line color size is " + mLineDescriptions.size());
        }
        handleData(true);
    }

    public void addData(List<T> data, LineDescription lineDescription) {
        if (null == data
            || data.isEmpty()) {
            return;
        }
        mLineDatas.add(data);
        mLineDescriptions.add(lineDescription);
        handleData(true);
    }

    public void reset() {
        mLineDatas.clear();
        mLineDescriptions.clear();
        handleData(true);
    }

    /**
     * 根据数据计算相关坐标
     * @param needInvalidate
     */
    private void handleData(boolean needInvalidate) {
        mLinePoints.clear();
        if (mLineDatas != null && mLineDatas.size() > 0) {
            // 计算最大值
            int maxSpanCount = 0;
            for (List<T> ts : mLineDatas) {
                for (T t : ts) {
                    mMax = Math.max(mMax, t.getSize());
                }
                maxSpanCount = Math.max(maxSpanCount, ts.size());
            }

            // 计算实际绘图区大小
            mBackgroundWidth = mRealWidth - BACKGROUND_MARGIN * 2 - BACKGROUND_PADDING - BACKGROUND_ARROW_LEN;
            mSclaeIntervalX = mBackgroundWidth / maxSpanCount;

            mBackgroundHeight = mRealHeight - BACKGROUND_MARGIN * 2 - BACKGROUND_PADDING - BACKGROUND_ARROW_LEN * 10;
            mSclaeIntervalY = mBackgroundHeight / SCALE_MARK_COUNT;

            // 计算数据在坐标轴中对应坐标位置
            double yScale = mBackgroundHeight / mMax;
            for (List<T> ts : mLineDatas) {
                List<Point> tempPoints = new ArrayList<>();
                for (int i = 0; i < ts.size(); i++) {
                    tempPoints.add(new Point((int) (BACKGROUND_MARGIN + BACKGROUND_PADDING + mSclaeIntervalX * i),
                        (int) (mRealHeight - yScale * ts.get(i).getSize() - BACKGROUND_MARGIN - BACKGROUND_PADDING)));
                }
                mLinePoints.add(tempPoints);
            }

            float l = BACKGROUND_MARGIN + BACKGROUND_PADDING + 10;
            float t = mRealHeight - BACKGROUND_MARGIN - 40;
            float r = l + 40;
            float b = t + 40;
            mOriginalIndiactorRectF.set(l, t, r, b);

        }
        if (needInvalidate) {
            invalidate();
        }
    }

    public static class LineData {
        private double size;

        public LineData(double size) {
            this.size = size;
        }

        public double getSize() {
            return size;
        }

        public void setSize(double size) {
            this.size = size;
        }
    }

    public static class LineDescription {
        private int color;
        private String description = "";

        public LineDescription() {
        }

        public LineDescription(int color, String description) {
            this.color = color;
            this.description = description;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
