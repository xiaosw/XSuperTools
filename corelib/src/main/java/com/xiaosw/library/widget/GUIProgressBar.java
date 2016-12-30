package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.xiaosw.library.R;

import java.text.DecimalFormat;

import static com.xiaosw.library.widget.GUIProgressBar.GUIProgressBarMode.MODE_RING;
import static com.xiaosw.library.widget.GUIProgressBar.GUIProgressBarMode.MODE_WATER_RISING;

/**
 * <p><br/>ClassName : {@link GUIProgressBar}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-29 15:15:34</p>
 */
public class GUIProgressBar extends ProgressBar {

    /**
     * @see GUIProgressBar#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-GUIRoundProgress";

    private final int DEFAULT_TEXT_SIZE_PX = 32;
    private final int DEFAULT_MIN_WIDTH_PX = 120;
    private final int DEFAULT_MIN_HEIGTH_PX = 120;
    private final DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

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

    ///////////////////////////////////////////////////////////////////////////
    // size
    ///////////////////////////////////////////////////////////////////////////
    int mWidth;
    int mHeight;
    int mRealWidth;
    int mRealHeight;
    float mCenterX;
    float mCenterY;
    float mCircleRadius;
    RectF mMaxRectF;

    ///////////////////////////////////////////////////////////////////////////
    // attrs
    ///////////////////////////////////////////////////////////////////////////
    private int mDuration;
    private int mCircleMode;
    private int mCircleColor;
    private int mDefaultColor;
    private float mStrokeWidth;
    int mMinWidth;
    int mMinHeight;
    int mTextSize;
    private int mAngleOffset;

    ///////////////////////////////////////////////////////////////////////////
    // draw
    ///////////////////////////////////////////////////////////////////////////
    private Paint mTextPaint;
    private Paint mCirclePaint;
    private Rect mTextBound;
    private Path mPath;

    int mRecordProgress;
    long mDelayMillis;

    public GUIProgressBar(Context context) {
        this(context, null);
    }

    public GUIProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.GUIProgressBar);
    }

    public GUIProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        initProgressBar();
        if (null != attrs) {
            final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.GUIProgressBar, defStyleAttr, defStyleRes);
            mCircleMode = a.getInt(R.styleable.GUIProgressBar_circleMode, MODE_RING.value());
            mDuration = a.getInt(R.styleable.GUIProgressBar_indeterminateDuration, mDuration);
            mStrokeWidth = a.getDimension(R.styleable.GUIProgressBar_strokeWidth, 1.0f);
            mAngleOffset = a.getInt(R.styleable.GUIProgressBar_angleOffset, mAngleOffset);
            mDefaultColor = a.getColor(R.styleable.GUIProgressBar_defaultColor, Color.GRAY);
            mTextSize = a.getDimensionPixelSize(R.styleable.GUIProgressBar_android_textSize, mTextSize);
            mMinWidth = a.getDimensionPixelSize(R.styleable.GUIProgressBar_android_minWidth, mMinWidth);
            mMinHeight = a.getDimensionPixelSize(R.styleable.GUIProgressBar_android_minHeight, mMinHeight);
            a.recycle();
        }

        mTextPaint.setTextSize(mTextSize);
        if (mCircleMode == GUIProgressBarMode.MODE_PIE_CHART.value()
            || mCircleMode == MODE_WATER_RISING.value()) {
            mCirclePaint.setStyle(Paint.Style.FILL);
        } else {
            mCirclePaint.setStyle(Paint.Style.STROKE);
        }
        mCirclePaint.setStrokeWidth(mStrokeWidth);

    }

    /**
     * 默认初始化操作
     */
    private void initProgressBar() {
        mDuration = 4000;
        mCircleMode = MODE_RING.value();
        mCircleColor = Color.parseColor("#61269E");
        mAngleOffset = 0;
        mDelayMillis = 10;
        mMinWidth = 0;
        mMinHeight = 0;
        mTextSize = DEFAULT_TEXT_SIZE_PX;

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextBound = new Rect();
        mMaxRectF = new RectF();
        mPath = new Path();
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
                    width = mMinWidth > 0 ? mMinWidth : DEFAULT_MIN_WIDTH_PX;
                    break;

                default:
                    // TODO: 2016/12/8
            }

            switch (heightMode) {
                case MeasureSpec.AT_MOST:
                case MeasureSpec.UNSPECIFIED:
                    height = mMinHeight > 0 ? mMinHeight :  DEFAULT_MIN_HEIGTH_PX;
                    break;

                default:
                    // TODO: 2016/12/8
            }
            setMeasuredDimension(width, height);
        }
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRealWidth = mHeight - getPaddingTop() - getPaddingBottom();
        mRealHeight = mHeight - getPaddingLeft() - getPaddingRight();
        mCenterX = (float) mWidth / 2;
        mCenterY = (float) getHeight() / 2;
        mCircleRadius = Math.min(mRealWidth / 2, mRealHeight / 2) - mStrokeWidth / 2;
        mMaxRectF.set(mCenterX - mCircleRadius, mCenterY - mCircleRadius, mCenterX + mCircleRadius, mCenterY + mCircleRadius);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        // defualt backgound
        mCirclePaint.setColor(mDefaultColor);
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclePaint);

        mCirclePaint.setColor(mCircleColor);
        if (GUIProgressBarMode.MODE_PIE_CHART.value() == mCircleMode) { // 扇形
            canvas.drawArc(mMaxRectF, mAngleOffset, calulateSweepAngle(), true, mCirclePaint);
        } else if (GUIProgressBarMode.MODE_RING.value() == mCircleMode) { // 圆环
            canvas.drawArc(mMaxRectF, mAngleOffset, calulateSweepAngle(), false, mCirclePaint);
        } else if (GUIProgressBarMode.MODE_WATER_RISING.value() == mCircleMode) { // 水位上升
            drawRisingWiterProgress(canvas);
        }

        drawText(canvas);

        // test
        canvas.drawLine(0, mCenterY, mWidth, mCenterY, mTextPaint);
        canvas.drawLine(mCenterX, 0, mCenterX, mHeight, mTextPaint);
    }

    private float calulateSweepAngle() {
        return (getProgress() * 360f / getMax());
    }

    private void drawText(Canvas canvas) {
        String percentageText = mDecimalFormat.format((double) getProgress() * 100 / getMax()) + "%";
        calulateTextSize(percentageText);
//        LogUtil.e(TAG, mTextBound.left + ", " + mTextBound.top + ", " + mTextBound.right + ", " + mTextBound.bottom
//            + "," + mTextBound.width() + ", " + mTextBound.height() + ", " + mTextBound.centerX() + ", " + mTextBound.centerY());
        canvas.drawText(percentageText, mCenterX - (mTextBound.left + mTextBound.right) / 2f, mCenterY - (mTextBound.top + mTextBound.bottom) / 2, mTextPaint);
    }

    /**
     * 根据宽高计算文字尺寸
     * @param percentageText
     */
    private void calulateTextSize(String percentageText) {
        mTextPaint.getTextBounds(percentageText, 0, percentageText.length(), mTextBound);
        if (Math.pow(mTextBound.width() / 2, 2) + Math.pow(mTextBound.height() / 2, 2) > Math.pow(mCircleRadius - mStrokeWidth, 2)) { // 在圆外
            mTextPaint.setTextSize(mTextPaint.getTextSize() - 1);
            calulateTextSize(percentageText);
        }
    }

    /**
     * 根据进度绘制水上升的形状
     * @param canvas
     */
    private void drawRisingWiterProgress(Canvas canvas) {
        canvas.rotate(mAngleOffset, mCenterX, mCenterY);
        mCirclePaint.setColor(mCircleColor);
        int percent = (getProgress() * 100 / getMax());
        float startAngle;
        float rSin = (float) (mCircleRadius * Math.sin(PERCENT_TO_ARC[percent]));
        float rCos = (float) (mCircleRadius * Math.cos(PERCENT_TO_ARC[percent]));
        mPath.reset();
        float offsetY = 0;// 去除path线
        if (percent < 50) {
            startAngle = (float) (90 - PERCENT_TO_ANGLE[percent]);
            canvas.drawArc(mMaxRectF, startAngle, (float) (PERCENT_TO_ANGLE[percent] * 2), true, mCirclePaint);
            mCirclePaint.setColor(mDefaultColor);
            offsetY = mCirclePaint.getStrokeWidth();
        } else {
            startAngle = (float) (450 - PERCENT_TO_ANGLE[percent]);
            canvas.drawArc(mMaxRectF, startAngle, (float) (PERCENT_TO_ANGLE[percent] * 2), true,  mCirclePaint);
            if (percent < 100) {
                offsetY = -mCirclePaint.getStrokeWidth();
            }
        }

        mPath.moveTo(mCenterX, mCenterY - offsetY);// triangle start
        mPath.lineTo(mCenterX + rSin, mCenterY + rCos - offsetY); // right
        mPath.lineTo(mCenterX - rSin, mCenterY + rCos - offsetY); // left
        mPath.lineTo(mCenterX, mCenterY - offsetY);
        mPath.close(); // siege of triangle
        canvas.drawPath(mPath, mCirclePaint);
        canvas.rotate(-mAngleOffset, mCenterX, mCenterY);
        canvas.restore();
    }

    public long getDelayMillis() {
        return mDelayMillis;
    }

    public void setDelayMillis(long delayMillis) {
        mDelayMillis = delayMillis;
    }

    /**
     * @hide
     */
    public void startAnim() {
        mRecordProgress = getProgress();
        setProgress(0);
        executeAnim();
    }

    private void executeAnim() {
        getHandler().postDelayed(mAnimTask, mDelayMillis);
    }

    private Runnable mAnimTask = new Runnable() {
        @Override
        public void run() {
            getHandler().removeCallbacks(mAnimTask);
            int increment = Math.max((int) (getMax() * mDelayMillis / mDuration), 1);
            if (getProgress() + increment <= mRecordProgress) {
                setProgress(getProgress() + increment);
                invalidate();
                executeAnim();
            } else {
                setProgress(mRecordProgress);
                invalidate();
            }
        }
    };

    /**
     * 进度条形状
     */
    public enum GUIProgressBarMode {
        /** 扇形 */
        MODE_PIE_CHART(0),
        /** 环形 */
        MODE_RING(2),
        /** 水位不断上升 */
        MODE_WATER_RISING(4);

        private int mValue;

        GUIProgressBarMode(int value) {
            this.mValue = value;
        }

        public int value() {
            return mValue;
        }
    }
}
