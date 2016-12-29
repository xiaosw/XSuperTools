package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaosw.library.R;

/**
 * <p><br/>ClassName : {@link GUIProgress}
 * <br/>Description :
 * <com.putao.ptx.core.widget.Progress
        android:id="@+id/pt_study_progress"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        putao:line_width="3.2"
        putao:circle_radius="12"
        putao:line_segment="top|bottom"
        putao:max="100"
        putao:progress="0"
        putao:orientation="vertical" />
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-08 10:10:28</p>
 */
public class GUIProgress extends View {

    /**
     * @see GUIProgress#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-Progress";

    /**
     * when mFillMode=rising_water,the percent Mapping the arc
     */
    private final static double[] PERCENT_TO_ARC = { 0, 0.364413d, 0.4616d,
        0.530831d, 0.586699d, 0.634474d, 0.676734d, 0.714958d, 0.750081d,
        0.782736d, 0.813377d, 0.842337d, 0.869872d, 0.896184d, 0.921432d,
        0.945747d, 0.969237d, 0.991993d, 1.01409d, 1.0356d, 1.05657d,
        1.07706d, 1.0971d, 1.11674d, 1.13601d, 1.15494d, 1.17356d,
        1.19189d, 1.20996d, 1.22779d, 1.24539d, 1.26279d, 1.27999d,
        1.29702d, 1.31389d, 1.33061d, 1.3472d, 1.36366d, 1.38d, 1.39625d,
        1.4124d, 1.42847d, 1.44446d, 1.46039d, 1.47627d, 1.49209d,
        1.50788d, 1.52364d, 1.53937d, 1.55509d, 0.5 * Math.PI, 1.58651d,
        1.60222d, 1.61796d, 1.63371d, 1.6495d, 1.66533d, 1.6812d, 1.69713d,
        1.71313d, 1.72919d, 1.74535d, 1.76159d, 1.77794d, 1.7944d,
        1.81098d, 1.8277d, 1.84457d, 1.8616d, 1.8788d, 1.8962d, 1.9138d,
        1.93163d, 1.9497d, 1.96803d, 1.98665d, 2.00558d, 2.02485d,
        2.04449d, 2.06454d, 2.08502d, 2.10599d, 2.1275d, 2.1496d, 2.17236d,
        2.19585d, 2.22016d, 2.24541d, 2.27172d, 2.29926d, 2.32822d,
        2.35886d, 2.39151d, 2.42663d, 2.46486d, 2.50712d, 2.55489d,
        2.61076d, 2.67999d, 2.77718d, Math.PI };

    /**
     * when mFillMode=rising_water,the percent Mapping the angle
     */
    private final static double[] PERCENT_TO_ANGLE = { 0.0, 20.87932689970087,
        26.447731823238804, 30.414375934709003, 33.61537654454588,
        36.352682410783395, 38.77400205300625, 40.964075929114315,
        42.9764755929523, 44.847469272952004, 46.60306925301236,
        48.26235502771122, 49.83999431660394, 51.34756086715217,
        52.794164708298474, 54.18731158715907, 55.53318944792137,
        56.837012206521074, 58.103077046421646, 59.33550926374806,
        60.53700176013739, 61.710992282360436, 62.85919970380261,
        63.984488813439555, 65.08857848465665, 66.1731875908393,
        67.24003500537289, 68.29026664384769, 69.32560137964909,
        70.34718512836734, 71.35559084779759, 72.35253741132523,
        73.33802481895025, 74.31377194405803, 75.28035174444373,
        76.23833717790248, 77.1888741600245, 78.13196269080984,
        79.0681757280536, 79.99923214514119, 80.92455898427748,
        81.8453021610527, 82.7614616754669, 83.6741834431103,
        84.58404042177803, 85.49045965367499, 86.39516001218658,
        87.29814149731276, 88.19940410905353, 89.1000937629992, 90.0,
        90.90032715530023, 91.80044385145077, 92.70227942098667,
        93.60468794831772, 94.50938830682928, 95.41638049652137,
        96.325664517394, 97.23838628503741, 98.15511875724673,
        99.07528897622683, 100.00118877315823, 100.9316722324507,
        101.86845822748958, 102.81154675827493, 103.76151078260183,
        104.71949621606056, 105.68607601644626, 106.66182314155404,
        107.64731054917908, 108.64425711270671, 109.65266283213694,
        110.67424658085521, 111.70958131665661, 112.75981295513141,
        113.82666036966499, 114.91126947584766, 116.01535914706473,
        117.1406482567017, 118.28942863593899, 119.46284620036691,
        120.66433869675623, 121.89677091408264, 123.16300764132176,
        124.4670595830395, 125.81293744380183, 127.20579784376484,
        128.65251627647018, 130.1599682354594, 131.73789400324964,
        133.3971797779485, 135.15272246222935, 137.02342966333148,
        139.03565743983094, 141.2260750906161, 143.64739473283896,
        146.3844141201789, 149.5855293215748, 153.5521161372655,
        159.12069294814196, 180.0 };

    /** use at {@link MeasureSpec#AT_MOST} or {@link MeasureSpec#UNSPECIFIED} */
    private static final int DEFAULT_WIDTH_PX = 120;
    /** use at {@link MeasureSpec#AT_MOST} or {@link MeasureSpec#UNSPECIFIED} */
    private static final int DEFAULT_HEIGHT_PX = 120;

    private static final int DEFAULT_CIRCLE_COLOR_OUT = Color.GRAY;
    private static final int DEFAULT_CIRCLE_COLOR_CENTER = Color.WHITE;
    private static final int DEFAULT_CIRCLE_COLOR_IN = Color.GREEN;
    private static final int DEFAULT_LINE_COLOR = Color.GRAY;

    private static final float DEFAULT_LINE_WIDTH_PX = 4.0f;
    private static final float DEFAULT_CIRCEL_RADIUS_PX = 12.0f;
    ///////////////////////////////////////////////////////////////////////////
    // 田
    //          top
    //
    //  left   none  right
    //
    //         bottom
    ///////////////////////////////////////////////////////////////////////////
    public static final int LINE_SEGMENT_LEFT = 1;
    public static final int LINE_SEGMENT_TOP = 100;
    public static final int LINE_SEGMENT_RIGHT = 1000;
    public static final int LINE_SEGMENT_BOTTOM = 10000;
    public static final int DEFAULT_LINE_SEGMENT_NONE = -1;
    public static final int DEFAULT_MAX = 100;

    ///////////////////////////////////////////////////////////////////////////
    // attrs
    ///////////////////////////////////////////////////////////////////////////
    private int mLineColor;
    private int mCircleColorOut;
    private int mCircleColorCenter;
    private int mCircleColorIn;
    private int mLineSegment;
    private float mLineWidth;
    private float mCircleRadius;
    private float mProgress;
    private float mMax;
    private float mOrientation;

    ///////////////////////////////////////////////////////////////////////////
    // Canvas draw
    ///////////////////////////////////////////////////////////////////////////
    private Paint mPaint;
    private Paint mProgressPaint;
    private int mWidth;
    private int mHeight;
    private float mCx;
    private float mCy;
    private RectF mCircleRectF;
    private Path mPath;

    public GUIProgress(Context context) {
        super(context);
        initialize(null);
    }

    public GUIProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public GUIProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.GUIProgress);
            mLineColor = ta.getColor(R.styleable.GUIProgress_line_color, DEFAULT_LINE_COLOR);
            mCircleColorOut = ta.getColor(R.styleable.GUIProgress_circle_color_out, DEFAULT_CIRCLE_COLOR_OUT);
            mCircleColorCenter = ta.getColor(R.styleable.GUIProgress_circle_color_center, DEFAULT_CIRCLE_COLOR_CENTER);
            mCircleColorIn = ta.getColor(R.styleable.GUIProgress_circle_color_in, DEFAULT_CIRCLE_COLOR_IN);
            mLineSegment = ta.getInt(R.styleable.GUIProgress_line_segment, DEFAULT_LINE_SEGMENT_NONE);
            mLineWidth = ta.getFloat(R.styleable.GUIProgress_line_width, DEFAULT_LINE_WIDTH_PX);
            mCircleRadius = ta.getFloat(R.styleable.GUIProgress_circle_radius, DEFAULT_CIRCEL_RADIUS_PX);
            mMax = ta.getInt(R.styleable.GUIProgress_max, DEFAULT_MAX);
            mProgress = ta.getInt(R.styleable.GUIProgress_progress, 0);
            mOrientation = ta.getInt(R.styleable.GUIProgress_orientation, LinearLayout.HORIZONTAL);
            ta.recycle();
        } else {
            mLineColor = DEFAULT_LINE_COLOR;
            mCircleColorOut = DEFAULT_CIRCLE_COLOR_OUT;
            mCircleColorCenter = DEFAULT_CIRCLE_COLOR_CENTER;
            mCircleColorIn = DEFAULT_CIRCLE_COLOR_IN;
            mLineSegment = DEFAULT_LINE_SEGMENT_NONE;
            mLineWidth = DEFAULT_LINE_WIDTH_PX;
            mCircleRadius = DEFAULT_CIRCEL_RADIUS_PX;
            mMax = DEFAULT_MAX;
            mProgress = 0;
            mOrientation = LinearLayout.HORIZONTAL;
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mLineWidth);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleRectF = new RectF();
        mPath = new Path();
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
                width = DEFAULT_WIDTH_PX;
                break;

            default:
                // TODO: 2016/12/8
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = DEFAULT_HEIGHT_PX;
                break;

            default:
                // TODO: 2016/12/8
        }
        setMeasuredDimension(width, height);
        mWidth = getMeasuredWidth();
        mCx = (mWidth - getPaddingLeft() - getPaddingRight()) / 2;
        mHeight = getMeasuredHeight();
        mCy = (mHeight - getPaddingTop() - getPaddingBottom()) / 2;
        mCircleRectF.set(mCx - mCircleRadius, mCy - mCircleRadius, mCx + mCircleRadius, mCy + mCircleRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        drawLine(canvas);
        drawCircle(canvas);
    }

    private void drawLine(Canvas canvas) {
        if (mLineSegment == (DEFAULT_LINE_SEGMENT_NONE)) {
            return;
        }
        mPaint.setColor(mLineColor);
        switch (mLineSegment) {

            case LINE_SEGMENT_LEFT | LINE_SEGMENT_TOP
                 | LINE_SEGMENT_RIGHT | LINE_SEGMENT_BOTTOM: 
                drawLineSegmentLeftAndRight(canvas);
                drawLineSegmentTopAndBottom(canvas);
                break;

            case LINE_SEGMENT_LEFT | LINE_SEGMENT_TOP | LINE_SEGMENT_BOTTOM:
                drawLineSegmentTopAndBottom(canvas);
                drawLineSegmentLeft(canvas);
                break;

            case LINE_SEGMENT_LEFT | LINE_SEGMENT_TOP | LINE_SEGMENT_RIGHT:
                drawLineSegmentLeftAndRight(canvas);
                drawLineSegmentTop(canvas);
                break;

            case LINE_SEGMENT_TOP | LINE_SEGMENT_BOTTOM | LINE_SEGMENT_RIGHT :
                drawLineSegmentTopAndBottom(canvas);
                drawLineSegmentRight(canvas);
                break;

            case LINE_SEGMENT_LEFT | LINE_SEGMENT_RIGHT | LINE_SEGMENT_BOTTOM :
                drawLineSegmentLeftAndRight(canvas);
                drawLineSegmentBottom(canvas);
                break;

            case LINE_SEGMENT_LEFT | LINE_SEGMENT_TOP :
                drawLineSegmentLeft(canvas);
                drawLineSegmentTop(canvas);
                break;

            case LINE_SEGMENT_LEFT | LINE_SEGMENT_RIGHT:
                drawLineSegmentLeftAndRight(canvas);
                break;

            case LINE_SEGMENT_LEFT | LINE_SEGMENT_BOTTOM:
                drawLineSegmentLeft(canvas);
                drawLineSegmentBottom(canvas);
                break;

            case LINE_SEGMENT_TOP | LINE_SEGMENT_RIGHT:
                drawLineSegmentTop(canvas);
                drawLineSegmentRight(canvas);
                break;

            case LINE_SEGMENT_TOP | LINE_SEGMENT_BOTTOM:
                drawLineSegmentTopAndBottom(canvas);
                break;

            case LINE_SEGMENT_LEFT:
                drawLineSegmentLeft(canvas);
                break;

            case LINE_SEGMENT_TOP:
                drawLineSegmentTop(canvas);
                break;

            case LINE_SEGMENT_RIGHT:
                drawLineSegmentRight(canvas);
                break;

            case LINE_SEGMENT_BOTTOM:
                drawLineSegmentBottom(canvas);
                break;
            
            default:
                // TODO: 2016/12/8
        }
    }

    private void drawLineSegmentLeftAndRight(Canvas canvas) {
        canvas.drawLine(0, mCy, mWidth, mCy, mPaint);
    }

    private void drawLineSegmentTopAndBottom(Canvas canvas) {
        canvas.drawLine(mCx, 0, mCx, mHeight, mPaint);
    }

    private void drawLineSegmentLeft(Canvas canvas) {
        canvas.drawLine(0, mCy, mCx, mCy, mPaint);
    }

    private void drawLineSegmentTop(Canvas canvas) {
        canvas.drawLine(mCx, 0, mCx, mCy, mPaint);
    }

    private void drawLineSegmentRight(Canvas canvas) {
        canvas.drawLine(mCx, mCy, mWidth, mCy, mPaint);
    }

    private void drawLineSegmentBottom(Canvas canvas) {
        canvas.drawLine(mCx, mCy, mCx, mHeight, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(mCircleColorOut);
        canvas.drawCircle(mCx, mCy, mCircleRadius + mLineWidth * 2, mPaint);

        mPaint.setColor(mCircleColorCenter);
        canvas.drawCircle(mCx, mCy, mCircleRadius + mLineWidth , mPaint);

        drawProgress(canvas);
    }

    /**
     * 根据进度绘制
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        if (mOrientation == LinearLayout.VERTICAL) {
            canvas.rotate(180, mCx, mCy);
        } else {
            canvas.rotate(90, mCx, mCy);
        }
        mPaint.setColor(mCircleColorIn);
        mPaint.setStyle(Paint.Style.FILL);

        int percent = (int) (getProgress() * 100 / getMax());
        float startAngle;
        float rSin = (float) (mCircleRadius * Math.sin(PERCENT_TO_ARC[percent]));
        float rCos = (float) (mCircleRadius * Math.cos(PERCENT_TO_ARC[percent]));
        mPath.reset();
        if (percent < 50) {
            startAngle = (float) (90 - PERCENT_TO_ANGLE[percent]);
            canvas.drawArc(mCircleRectF, startAngle, (float) (PERCENT_TO_ANGLE[percent] * 2), true, mPaint);
            mPaint.setColor(mCircleColorCenter);
        } else {
            startAngle = (float) (450 - PERCENT_TO_ANGLE[percent]);
            canvas.drawArc(mCircleRectF, startAngle, (float) (PERCENT_TO_ANGLE[percent] * 2), true,  mPaint);
        }
        mPath.moveTo(mCx, mCy);// triangle start
        mPath.lineTo(mCx + rSin, mCy + rCos);
        mPath.lineTo(mCx - rSin, mCy + rCos);
        mPath.lineTo(mCx, mCy);
        mPath.close(); // siege of triangle
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    public int getCircleColorOut() {
        return mCircleColorOut;
    }

    public void setCircleColorOut(int circleColorOut) {
        mCircleColorOut = circleColorOut;
    }

    public int getCircleColorCenter() {
        return mCircleColorCenter;
    }

    public void setCircleColorCenter(int circleColorCenter) {
        mCircleColorCenter = circleColorCenter;
    }

    public int getCircleColorIn() {
        return mCircleColorIn;
    }

    public void setCircleColorIn(int circleColorIn) {
        mCircleColorIn = circleColorIn;
    }

    public int getLineSegment() {
        return mLineSegment;
    }

    public void setLineSegment(int lineSegment) {
        mLineSegment = lineSegment;
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
    }

    public float getCircleRadius() {
        return mCircleRadius;
    }

    public void setCircleRadius(float circleRadius) {
        mCircleRadius = circleRadius;
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    public float getMax() {
        return mMax;
    }

    public void setMax(float max) {
        mMax = max;
    }

    public float getOrientation() {
        return mOrientation;
    }

    public void setOrientation(float orientation) {
        mOrientation = orientation;
    }
}
