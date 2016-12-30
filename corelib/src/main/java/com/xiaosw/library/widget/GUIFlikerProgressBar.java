package com.xiaosw.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

import com.xiaosw.library.R;
import com.xiaosw.library.utils.LogUtil;

/**
 * <p><br/>ClassName : {@link GUIFlikerProgressBar}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-30 15:15:28</p>
 */
public class GUIFlikerProgressBar extends AbsProgressBar implements Runnable {
    
    /** @see GUIFlikerProgressBar#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-GUIFlikerProgressBar";
    
    private PorterDuffXfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    /** 最小高度 */
    private final int DEFAULT_HEIGHT_PX = 70;
    /** 滑块刷新时间 毫秒 */
    private final int DEFAULT_DELAY_MILLIS = 20;
    private int mBorderWidth;
    private Paint mBackgroundPaint;
    private Paint mProgressPaint;
    private String mProgressText;
    private RectF mBackgroundRectF;
    private Handler mHandler;

    /** 左右来回移动的滑块 */
    private Bitmap mFlikerBitmap;
    /** 滑块移动最左边位置，作用是控制移动 */
    private float mFlickerLeft;
    /** 进度条 bitmap ，包含滑块 */
    private Bitmap mProgressBitmap;
    private Canvas mProgressCanvas;
    private volatile boolean isFinish;
    private volatile boolean isStop;

    /** 下载中颜色 */
    private int mLoadingColor;
    /** 暂停时颜色 */
    private int mStopColor;
    /** 进度文本、边框、进度条颜色 */
    private int mProgressColor;
    private int mRadius;

    BitmapShader mBitmapShader;

    /** 不同状态提示文字 */
    private String mTextDownloading;
    private String mTextDownloadPause;
    private String mTextDownloadSuccess;

    public GUIFlikerProgressBar(Context context) {
        super(context);
    }

    public GUIFlikerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GUIFlikerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GUIFlikerProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onInitDefault() {
        mHandler = new Handler(Looper.getMainLooper());
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.FILL);

        mBackgroundRectF = new RectF();

        // 滑块
        mFlikerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_progress_bar_flicker);
        mFlickerLeft = -mFlikerBitmap.getWidth();

        mTextDownloading = getContext().getResources().getString(R.string.str_downloading);
        mTextDownloadPause = getContext().getResources().getString(R.string.str_download_pause);
        mTextDownloadSuccess = getContext().getResources().getString(R.string.str_download_success);

        isStop = false;
    }

    @Override
    protected void onParseAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GUIFlikerProgressBar);
        try {
            mTextSize = (int) a.getDimension(R.styleable.GUIFlikerProgressBar_android_textSize, 20);
            mLoadingColor = a.getColor(R.styleable.GUIFlikerProgressBar_loadingColor, Color.parseColor("#33ccff"));
            mStopColor = a.getColor(R.styleable.GUIFlikerProgressBar_stopColor, Color.parseColor("#ff9900"));
            mRadius = (int) a.getDimension(R.styleable.GUIFlikerProgressBar_android_radius, 0);
            mBorderWidth = (int) a.getDimension(R.styleable.GUIFlikerProgressBar_borderWidth, 1);
        } catch (Exception e) {
            LogUtil.e(TAG, "onParseAttrs", e);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onApplyAttrs() {
        mBackgroundPaint.setStrokeWidth(mBorderWidth);
        if(isStop){
            mProgressColor = mStopColor;
        } else{
            mProgressColor = mLoadingColor;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler.postDelayed(this, DEFAULT_DELAY_MILLIS);
    }

    @Override
    protected void onDetachedFromWindow() {
        setStop(true);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        switch (heightSpecMode){
            case MeasureSpec.AT_MOST:
                height = DEFAULT_HEIGHT_PX;
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                height = heightSpecSize;
                break;
        }
        setMeasuredDimension(widthSpecSize, height);

        calulateSize();

        float l = mCenterX - mRealWidth / 2 + mBorderWidth;
        float t = mCenterY - mRealHeight / 2 + mBorderWidth;
        float r = mCenterX + mRealWidth / 2 - mBorderWidth;
        float b = mCenterY + mRealHeight / 2 - mBorderWidth;
        mBackgroundRectF.set(l, t, r, b);
        if(mProgressBitmap == null){
            mProgressBitmap = Bitmap.createBitmap(mRealWidth - mBorderWidth, mRealHeight - mBorderWidth, Bitmap.Config.ARGB_8888);
            mProgressCanvas = new Canvas(mProgressBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景
        drawBackground(canvas);

        //进度
        drawProgress(canvas);

        //进度text
        drawProgressText(canvas);

        //变色处理
        drawColorProgressText(canvas);
    }

    /**
     * 边框
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        mBackgroundPaint.setColor(mProgressColor);
        //left、top、right、bottom不要贴着控件边，否则border只有一半绘制在控件内,导致圆角处线条显粗
        canvas.drawRoundRect(mBackgroundRectF, mRadius, mRadius, mBackgroundPaint);
    }

    /**
     * 进度
     */
    private void drawProgress(Canvas canvas) {
        mProgressPaint.setColor(mProgressColor);
        float right = (getProgress() * mRealWidth / (float) getMax()) ;
        mProgressCanvas.save(Canvas.CLIP_SAVE_FLAG);
        mProgressCanvas.clipRect(0, 0, right, mHeight);
        mProgressCanvas.drawColor(mProgressColor);
        mProgressCanvas.restore();

        if(!isStop){
            mProgressPaint.setXfermode(mXfermode);
            mProgressCanvas.drawBitmap(mFlikerBitmap, mFlickerLeft, 0, mProgressPaint);
            mProgressPaint.setXfermode(null);
        }

        //控制显示区域
        mBitmapShader = new BitmapShader(mProgressBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mProgressPaint.setShader(mBitmapShader);
        canvas.drawRoundRect(mBackgroundRectF, mRadius, mRadius, mProgressPaint);
    }

    /**
     * 进度提示文本
     * @param canvas
     */
    private void drawProgressText(Canvas canvas) {
        mTextPaint.setColor(mProgressColor);
        mProgressText = getProgressText();
        adjustTextSize(mProgressText);
        int tWidth = mTextBound.width();
        int tHeight = mTextBound.height();
        float xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        float yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        canvas.drawText(mProgressText, xCoordinate, yCoordinate, mTextPaint);
    }

    private void adjustTextSize(String text) {
        mTextPaint.getTextBounds(text, 0, text.length(), mTextBound);
        if (mTextBound.height() > mRealHeight
            || mTextBound.width() > mRealWidth) {
            mTextPaint.setTextSize(mTextPaint.getTextSize() - 1);
            adjustTextSize(text);
        }
    }

    /**
     * 变色处理
     * @param canvas
     */
    private void drawColorProgressText(Canvas canvas) {
        mTextPaint.setColor(Color.WHITE);
        int tWidth = mTextBound.width();
        int tHeight = mTextBound.height();
        float xCoordinate = (mRealWidth - tWidth) / 2;
        float yCoordinate = (mRealHeight + tHeight) / 2;
        float progressWidth = getProgress() * mRealWidth / (float) getMax();
        if(progressWidth > xCoordinate){
            canvas.save(Canvas.CLIP_SAVE_FLAG);
            float right = Math.min(progressWidth, xCoordinate + tWidth * 1.1f);
            canvas.clipRect(xCoordinate, 0, right, mHeight);
            canvas.drawText(mProgressText, xCoordinate, yCoordinate, mTextPaint);
            canvas.restore();
        }
    }

    /**
     * 停止刷新
     * @param stop
     */
    public void setStop(boolean stop) {
        isStop = stop;
        mHandler.removeCallbacks(this);
        if (isAttachedToWindow()) {
            if(isStop){
                mProgressColor = mStopColor;
            } else {
                mProgressColor = mLoadingColor;
                mHandler.postDelayed(this, DEFAULT_DELAY_MILLIS);
            }
        }
        invalidate();
    }

    public void finishLoad() {
        isFinish = true;
        setStop(true);
    }

    public void toggle(){
        if(!isFinish){
            if(isStop){
                setStop(false);
            } else {
                setStop(true);
            }
        }
    }

    @Override
    public void run() {
        mHandler.removeCallbacks(this);
        if (!isAttachedToWindow()) {
            return;
        }
        int width = mFlikerBitmap.getWidth();
        mFlickerLeft += 10;
        float progressWidth = getProgress() * mRealWidth / (float) getMax();
        if(mFlickerLeft >= progressWidth){
            mFlickerLeft = -width;
        }
        invalidate();
        mHandler.postDelayed(this, DEFAULT_DELAY_MILLIS);
    }

    /**
     * 重置
     */
    public void reset(){
        setStop(true);
        setProgress(0);
        isFinish = false;
        isStop = false;
        mProgressColor = mLoadingColor;
        mProgressText = "";
        mFlickerLeft = -mFlikerBitmap.getWidth();
    }

    public boolean isStop() {
        return isStop;
    }

    public boolean isFinish() {
        return isFinish;
    }

    private String getProgressText() {
        if(!isFinish){
            if(!isStop){
                return String.format(mTextDownloading, getPercentage());
            } else {
                return mTextDownloadPause;
            }
        } else{
            return mTextDownloadSuccess;
        }
    }
}
