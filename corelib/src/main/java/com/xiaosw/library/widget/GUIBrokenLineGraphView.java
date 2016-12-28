package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.xiaosw.library.bean.BrokenLineGraph;
import com.xiaosw.library.bean.GraphData;
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
public class GUIBrokenLineGraphView extends View {

    /**
     * @see GUIBrokenLineGraphView#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-GUIBrokenLineGraphView";

    /** 最小宽度 */
    private final int MIN_WIDTH_PX = 800;
    /** 最小高度 */
    private final int MIN_HEIGTH_PX = 800;

    /** left到X轴起点的距离 */
    private final int BACKGROUND_MARGIN = 20;
    /** X轴起点到原点的距离 */
    private final int BACKGROUND_PADDING = 120;
    /** X、Y轴箭头长度 */
    private final int BACKGROUND_ARROW_LEN = 10;

    /** 背景线颜色 */
    private final int BACKGROUD_LINE_COLOR = Color.GRAY;
    /** 线宽 */
    private final float LINE_WIDTH_PX = 6;
    /** 文字颜色 */
    private final int TEXT_COLRO = BACKGROUD_LINE_COLOR;

    /** Y轴刻度数 */
    private final int SCALE_MARK_COUNT = 7;

    /** 实际宽度 */
    private float mRealWidth;
    /** 实际高度 */
    private float mRealHeight;
    /** 图标绘制区宽度 */
    private float mBackgroundWidth;
    /** 图标绘制区高度 */
    private float mBackgroundHeight;
    /** 折线数据 */
    private List<BrokenLineGraph> mLineDatas;
    /** 根据数据计算出的坐标点 */
    private List<List<Point>> mLinePoints;
    /** X轴刻度(屏幕尺寸) */
    private double mSclaeIntervalX;
    /** Y轴刻度(屏幕尺寸) */
    private double mSclaeIntervalY;
    /** 统计图中最大值 */
    private double mMax;
    /** 格式化数据显示 */
    private DecimalFormat mDecimalFormat;
    /** 线、点等 */
    private Paint mContentPaint;
    /** 坐标刻度 */
    private Paint mBackgroundPaint;
    /** 存储文字尺寸相关信息 */
    private Rect mTextBound;

    /** 绘制这线示意图 */
    private RectF mOriginalIndicatorRectF;
    private RectF mDrawIndicatorRectF;
    private RectF mContentRectF;

    private Path mPath;
    private LinearGradient mLinearGradient;

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

    /**
     * 绘制折线背景相关
     * @param canvas
     */
    private void drawBackgroud(Canvas canvas) {
        mBackgroundPaint.setColor(BACKGROUD_LINE_COLOR);
        // Y轴
        float startX = BACKGROUND_MARGIN;
        float startY = mRealHeight - BACKGROUND_MARGIN - BACKGROUND_PADDING;
        float stopX = mRealWidth - BACKGROUND_MARGIN;
        float stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, mBackgroundPaint);
        // Y轴箭头
        canvas.drawLine(stopX - BACKGROUND_ARROW_LEN, stopY - BACKGROUND_ARROW_LEN, stopX, stopY, mBackgroundPaint);
        canvas.drawLine(stopX - BACKGROUND_ARROW_LEN, stopY + BACKGROUND_ARROW_LEN, stopX, stopY, mBackgroundPaint);

        // X轴
        startX = BACKGROUND_MARGIN + BACKGROUND_PADDING;
        startY = mRealHeight - BACKGROUND_MARGIN;
        stopX = startX;
        stopY = BACKGROUND_MARGIN;
        canvas.drawLine(startX, startY, stopX, stopY, mBackgroundPaint);
        // X轴箭头
        canvas.drawLine(stopX - BACKGROUND_ARROW_LEN, stopY + BACKGROUND_ARROW_LEN, stopX, stopY, mBackgroundPaint);
        canvas.drawLine(stopX + BACKGROUND_ARROW_LEN, stopY + BACKGROUND_ARROW_LEN, stopX, stopY, mBackgroundPaint);

        // 网格
        startY = (mRealHeight - BACKGROUND_MARGIN - BACKGROUND_PADDING);
        startX = BACKGROUND_MARGIN + BACKGROUND_PADDING;
        stopX = mRealWidth - BACKGROUND_MARGIN - BACKGROUND_ARROW_LEN;
//        canvas.drawCircle(startX, startY, LINE_WIDTH_PX * 4, mBackgroundPaint);
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
        if (mLinePoints.size() == 1) {
            canvas.save(Canvas.CLIP_SAVE_FLAG);
            canvas.clipRect(mContentRectF);
        }
        mDrawIndicatorRectF.set(mOriginalIndicatorRectF.left,
            mOriginalIndicatorRectF.top,
            mOriginalIndicatorRectF.right,
            mOriginalIndicatorRectF.bottom);
        mColorRectLeftOffset = 0;
        for (int i = 0; i < mLinePoints.size(); i++) { // 折线数量
            List<Point> points = mLinePoints.get(i);
            int pointsSize = points.size();
            BrokenLineGraph brokenLineGraph = mLineDatas.get(i);
            if (i == 0 && mLineDatas.size() == 1) {
                mContentPaint.setColor(Color.parseColor("#E0000000"));
                mContentPaint.setShader(mLinearGradient);
                canvas.drawPath(mPath, mContentPaint);
                startAnim();
            }
            mContentPaint.setShader(null);
            mContentPaint.setColor(brokenLineGraph.getColor());
            // 折线图描述
            drawLineDescription(canvas, brokenLineGraph);
            for (int j = 0; j < pointsSize - 1; j++) { // 折线
                Point point = points.get(j);
                Point nextPoint = points.get(j + 1);
                // 折线
                canvas.drawLine(point.x, point.y, nextPoint.x, nextPoint.y, mContentPaint);
                // 折点
                canvas.drawCircle(point.x, point.y, LINE_WIDTH_PX * 1.6f, mContentPaint);
                if (j == pointsSize - 2) {
                    canvas.drawCircle(nextPoint.x, nextPoint.y, LINE_WIDTH_PX * 2f, mContentPaint);
                }

            }
        }
        if (mLinePoints.size() == 1) {
            canvas.restore();
        }
    }

    private void startAnim() {
        if (mContentRectF.right <= (BACKGROUND_MARGIN + BACKGROUND_PADDING + mRealWidth)) {
            getHandler().postDelayed(mAnimRunnable, 5);
        }
    }

    private Runnable mAnimRunnable = new Runnable() {
        @Override
        public void run() {
            float r = mContentRectF.right + 12;
            mContentRectF.right = r;
            invalidate();
        }
    };

    /**
     * 绘制折线图描述（如红色代表什么，蓝色代表什么）
     * @param canvas
     * @param mColorRectLeftOffset
     * @param lineDescription
     * @return
     */
    private float mColorRectLeftOffset = 0;
    private void drawLineDescription(Canvas canvas, BrokenLineGraph brokenLineGraph) {
        String description = brokenLineGraph.getDescription();
        if (!TextUtils.isEmpty(description)) {
            // 颜色快
            int rectAndTextGap = 10;
            float l = mDrawIndicatorRectF.left + mColorRectLeftOffset;
            float r = l + mDrawIndicatorRectF.width();
            mDrawIndicatorRectF.left = l;
            mDrawIndicatorRectF.right = r;
            canvas.drawRect(mDrawIndicatorRectF, mContentPaint);
            mContentPaint.getTextBounds(description, 0, description.length(), mTextBound);

            // 色块描述
            // 计算文字居中
            float textOffset = (mDrawIndicatorRectF.height() - mTextBound.height()) / 2;
            canvas.drawText(description, mDrawIndicatorRectF.right + rectAndTextGap, mDrawIndicatorRectF.bottom - textOffset, mContentPaint);
            mColorRectLeftOffset = mDrawIndicatorRectF.width() + rectAndTextGap * 2 + mTextBound.width();
        }
    }

    /**
     * 初始化数据（画笔，数据等）
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        // 折线
        mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContentPaint.setColor(Color.RED);
        mContentPaint.setTextSize(22);
        mContentPaint.setStrokeWidth(LINE_WIDTH_PX);

        // 坐标刻度
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.BLACK);
        mBackgroundPaint.setTextSize(28);
        mBackgroundPaint.setStrokeWidth(LINE_WIDTH_PX / 3);
        mTextBound = new Rect();
        mOriginalIndicatorRectF = new RectF();
        mDrawIndicatorRectF = new RectF();
        mContentRectF = new RectF();

        // 保留三位小数
        mDecimalFormat = new DecimalFormat("#.000");

        mLineDatas = new ArrayList<>();
        mLinePoints = new ArrayList<>();

        mPath = new Path();
    }

    /**
     * 设置基础数据
     * @param datas
     */
    public void setBrokenLineGraphs(List<BrokenLineGraph> datas) {
        mLineDatas.clear();
        if (null != datas) {
            mLineDatas = datas;
        }
        handleData(true);
    }

    /**
     * 添加基础数据
     * @param brokenLineGraph
     */
    public void addBrokenLineGraph(BrokenLineGraph brokenLineGraph) {
        if (null == brokenLineGraph) {
            return;
        }
        mLineDatas.add(brokenLineGraph);
        handleData(true);
    }

    /**
     * 添加基础数据
     * @param datas
     */
    public void addBrokenLineGraphs(List<BrokenLineGraph> datas) {
        mLineDatas.addAll(datas);
        handleData(true);
    }

    /**
     * 重制表格数据
     */
    public void reset() {
        mLineDatas.clear();
        handleData(true);
    }

    /**
     * 根据数据计算相关坐标
     * @param needInvalidate
     */
    private void handleData(boolean needInvalidate) {
        mLinePoints.clear();
        mPath.reset();
        if (mLineDatas != null && mLineDatas.size() > 0) {
            // 计算最大值
            int maxSpanCount = 0;
            for (BrokenLineGraph lineData : mLineDatas) {
                List graphDatas = lineData.getGraphDatas();
                for (int i = 0; i < graphDatas.size(); i++) {
                    GraphData graphData = (GraphData) graphDatas.get(i);
                    mMax = Math.max(mMax, graphData.getSize() + 10); // +10防止最大值达到顶刻度
                }
                maxSpanCount = Math.max(maxSpanCount, graphDatas.size());
            }
            // 计算实际绘图区大小
            mBackgroundWidth = mRealWidth - BACKGROUND_MARGIN * 2 - BACKGROUND_PADDING - BACKGROUND_ARROW_LEN;
            mSclaeIntervalX = mBackgroundWidth / maxSpanCount;

            mBackgroundHeight = mRealHeight - BACKGROUND_MARGIN * 2 - BACKGROUND_PADDING - BACKGROUND_ARROW_LEN * 10;
            mSclaeIntervalY = mBackgroundHeight / SCALE_MARK_COUNT;

            mContentRectF.set(0, 0, BACKGROUND_MARGIN + BACKGROUND_PADDING, getMeasuredHeight());

            // 计算数据在坐标轴中对应坐标位置
            double yScale = mBackgroundHeight / mMax;

            for (BrokenLineGraph lineData : mLineDatas) {
                List<Point> tempPoints = new ArrayList<>();
                List graphDatas = lineData.getGraphDatas();
                for (int i = 0; i < graphDatas.size(); i++) {
                    GraphData grapData = (GraphData) graphDatas.get(i);
                    tempPoints.add(new Point((int) (BACKGROUND_MARGIN + BACKGROUND_PADDING + mSclaeIntervalX * i),
                        (int) (mRealHeight - yScale * grapData.getSize() - BACKGROUND_MARGIN - BACKGROUND_PADDING)));
                }
                mLinePoints.add(tempPoints);
            }

            float l = BACKGROUND_MARGIN + BACKGROUND_PADDING + 10;
            float t = mRealHeight - BACKGROUND_MARGIN - 40;
            float r = l + 40;
            float b = t + 40;
            mOriginalIndicatorRectF.set(l, t, r, b);

            // 单个图标时，绘制渐变色
            if (mLinePoints.size() == 1) {
                List<Point> points = mLinePoints.get(0);
                Point firstPoint = points.get(0);
                mPath.moveTo(firstPoint.x, firstPoint.y);
                // 不需要覆盖在X、Y轴
                float strokeWidthOffset = mBackgroundPaint.getStrokeWidth() / 2;
                int pointSize = points.size();
                for (int i = 1; i < pointSize; i++) {
                    Point point = points.get(i);
                    if (i < pointSize - 1) {
                        mPath.lineTo(point.x, point.y);
                    } else {
                        mPath.lineTo(point.x + strokeWidthOffset, point.y - strokeWidthOffset);
                    }

                }
                Point lastPoint = points.get(points.size() - 1);


                mPath.lineTo(lastPoint.x + strokeWidthOffset, mRealHeight - BACKGROUND_MARGIN - BACKGROUND_PADDING - strokeWidthOffset);
                mPath.lineTo(BACKGROUND_MARGIN + BACKGROUND_PADDING + strokeWidthOffset,
                    mRealHeight - BACKGROUND_MARGIN - BACKGROUND_PADDING - strokeWidthOffset);
                mPath.lineTo(firstPoint.x + strokeWidthOffset, firstPoint.y);
            }
        }

        mLinearGradient = new LinearGradient(mRealWidth / 2, 0,
            mRealWidth / 2, mRealHeight,
            new int[] {Color.parseColor("#CC6600"), Color.parseColor("#CCCC00")},
            new float[] {0.2f, 1},
            Shader.TileMode.CLAMP);

        if (needInvalidate) {
            invalidate();
        }
    }

}
