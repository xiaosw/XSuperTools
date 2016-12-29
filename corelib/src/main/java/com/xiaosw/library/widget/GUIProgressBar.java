package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.xiaosw.library.R;

import java.text.DecimalFormat;

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

    private final int MODE_PIE_CHART = 0;
    private final int MODE_RING = 2;
    private final int DEFAULT_TEXT_SIZE_PX = 32;
    private final DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

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
    private int mAngleOffset;

    ///////////////////////////////////////////////////////////////////////////
    // draw
    ///////////////////////////////////////////////////////////////////////////
    private Paint mTextPaint;
    private Paint mCirclePaint;
    private Rect mTextBound;

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
            mCircleMode = a.getInt(R.styleable.GUIProgressBar_circle_mode, MODE_RING);
            mDuration = a.getInt(R.styleable.GUIProgressBar_indeterminateDuration, mDuration);
            mStrokeWidth = a.getDimension(R.styleable.GUIProgressBar_strokeWidth, 1.0f);
            mAngleOffset = a.getInt(R.styleable.GUIProgressBar_angleOffset, mAngleOffset);
            mDefaultColor = a.getColor(R.styleable.GUIProgressBar_defaultColor, Color.GRAY);
            mTextPaint.setTextSize(a.getDimension(R.styleable.GUIProgressBar_textSize, DEFAULT_TEXT_SIZE_PX));
            a.recycle();
        }

        if (mCircleMode == MODE_PIE_CHART) {
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
        mCircleMode = MODE_RING;
        mCircleColor = Color.parseColor("#61269E");
        mAngleOffset = 0;
        mDelayMillis = 10;

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(DEFAULT_TEXT_SIZE_PX);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextBound = new Rect();
        mMaxRectF = new RectF();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        if (mCircleMode == MODE_PIE_CHART) {
            canvas.drawArc(mMaxRectF, mAngleOffset, calulateSweepAngle(), true, mCirclePaint);
        } else if (mCircleMode == MODE_RING) {
            canvas.drawArc(mMaxRectF, mAngleOffset, calulateSweepAngle(), false, mCirclePaint);
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

    public long getDelayMillis() {
        return mDelayMillis;
    }

    public void setDelayMillis(long delayMillis) {
        mDelayMillis = delayMillis;
    }

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
}
