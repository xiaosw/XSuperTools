package com.xiaosw.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.xiaosw.library.R;

/**
 * <p><br/>ClassName : {@link GUILetterIndexView}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-19 18:18:21</p>
 */
public class GUILetterIndexView extends View {

    /**
     * @see GUILetterIndexView#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-LetterIndexView";

    private String TEXT_String = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // index letter

    /** 默认文字大小 */
    public static final int LETTER_SIZE_DEFAULT = 30;
    /** 默认文字颜色 */
    public static final int LETTER_COLOR_DEFAULT = Color.BLACK;
    /** 默认挤压文字颜色 */
    public static final int LETTER_COLOR_PRESS = Color.WHITE;
    /** 默认文字选中背景 */
    public static final int LETTER_COLOR_PRESS_BACKGROUD = Color.GREEN;

    /** 动画执行时间(ms) */
    public static final int LETTER_ANIM_RUNING_DURATION = 300;

    private ValueAnimator mAnimator; // control point animator
    private boolean isToOrigin = true; // judge if it animators to origin

    private float distance = 500.0f; // sin interval size
    private int mWidth = 0; // 本View的宽高
    private int mHeight = 0;
    private float perHeight = 0.0f; // 一个字母所占用的空间
    private float recordY; // 当前绘制字母的位置
    private float pointX = 0.0f; // 当前手指的位置
    private float pointY = Float.MAX_VALUE;
    private float pointYBefore = pointY - distance; // 正弦区间
    private float pointYAfter = pointY + distance;
    private float sinH = 0.0f; // control y
    private float baseX = 0.0f; // letter x position

    private Paint mPaint;
    private Paint mCirclePaint;
    private int padding = 10;

    private int mHighLightPosition = -1;
    private boolean isAutoWidth = false;

    private float trans = 0.0f;
    private float sinMax = 80.0f;
    private float circleMax = 0.0f;
    private float circleR = 0.0f;

    /** 默认文字颜色 */
    private int mDefaultTextColor;
    /** 按下文字颜色 */
    private int mPressTextColor;
    /** 默认文字选中背景 */
    private int mCircleColor;
    /** 默认文字选中背景 */
    private float mTextSize;

    private OnIndexChangedListener mIndexChangeListener;

    public GUILetterIndexView(Context context) {
        super(context);
        init(null);
    }

    public GUILetterIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GUILetterIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    public void setTextSize(float size){
        mTextSize = size;
        invalidate();
    }

    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        mWidth = Math.abs(r - l);
        mHeight = Math.abs(b - t);
        perHeight = (mHeight - 2 * padding) / (float) (TEXT_String.length());
    }

    private void init(AttributeSet attrs) {
        if (null != attrs) {
            TypedArray ta =  getContext().obtainStyledAttributes(attrs, R.styleable.GUILetterIndexView);
            mDefaultTextColor = ta.getColor(R.styleable.GUILetterIndexView_text_color_def, LETTER_COLOR_DEFAULT);
            mPressTextColor = ta.getColor(R.styleable.GUILetterIndexView_text_color_press, LETTER_COLOR_PRESS);
            mCircleColor = ta.getColor(R.styleable.GUILetterIndexView_circle_color, LETTER_COLOR_PRESS_BACKGROUD);
            mTextSize = ta.getDimensionPixelSize(R.styleable.GUILetterIndexView_android_textSize, LETTER_SIZE_DEFAULT);
            ta.recycle();
        } else {
            mDefaultTextColor = LETTER_COLOR_DEFAULT;
            mPressTextColor = LETTER_COLOR_PRESS;
            mCircleColor = LETTER_COLOR_PRESS_BACKGROUD;
            mTextSize = LETTER_SIZE_DEFAULT;
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mDefaultTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        measureView(this);
        mWidth = this.getMeasuredWidth();
        mHeight = this.getMeasuredHeight();
        perHeight = (mHeight - 2 * padding) / (float) (TEXT_String.length());
        this.setWillNotDraw(false);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);

        mAnimator = new ValueAnimator();
        mAnimator.setDuration(LETTER_ANIM_RUNING_DURATION);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sinH = (Float) animation.getAnimatedValue();
                circleR = (sinH / sinMax) * circleMax;
                if (isAutoWidth) {
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = (int) (sinH + 26.0f + mTextSize + circleRCurrent / 2);
                    mWidth = params.width;
                    setLayoutParams(params);
                }
                invalidate();
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isToOrigin) {
                    sinH = 0.0f;
                    pointY = Float.MAX_VALUE;
                } else {
                    sinH = sinMax;
                }
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });

    }

    @SuppressLint("NewApi")
    private void startSinAnimator(float from, float to) {
        if (mAnimator == null) {
            return;
        }
        if (to == 0.0f) {
            isToOrigin = true;
        } else {
            isToOrigin = false;
        }
        // if (mAnimator.isRunning()) {
        // mAnimator.cancel();
        // } else {
        mAnimator.setFloatValues(from, to);
        mAnimator.start();
        // }
    }

    public void setAutoWidth(boolean isAutoWidth) {
        this.isAutoWidth = isAutoWidth;
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = (int) (25.0f + mTextSize);
        this.setLayoutParams(params);
    }

    public void setIndexString(String indexString) {
        TEXT_String = indexString;
        perHeight = (mHeight - 2 * padding) / (float) TEXT_String.length();
        this.invalidate();
    }

    public void setOnIndexChangedListener(
        OnIndexChangedListener mIndexChangeListener) {
        this.mIndexChangeListener = mIndexChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        makeDraw(canvas);
    }

    private void makeDraw(Canvas canvas) {
        if (mHeight == 0) {
            mWidth = this.getMeasuredWidth();
            mHeight = this.getMeasuredHeight();
            perHeight = (mHeight - 2 * padding)
                / (float) (TEXT_String.length());
            distance = perHeight * 11;
            baseX = mWidth - 25.0f;
            sinH = 0.0f;
            pointYBefore = 0.0f;
            pointYAfter = 0.0f;
        } else {
            baseX = mWidth - 25.0f;
            pointYBefore = pointY - distance / 2;
            pointYAfter = pointY + distance / 2;
            sinH = Math.min(sinH, sinMax);
            circleR = Math.min(circleR, circleMax);
            drawCircle(canvas);
        }
        circleMax = baseX - sinMax;
        mPaint.setColor(mDefaultTextColor);

        recordY = 0.0f;
        drawSinusoid(canvas);
    }

    private float circleRCurrent = 0.0f;

    // draw circle that we point to
    private void drawCircle(Canvas canvas) {
        circleRCurrent = mTextSize * 1.6f * (sinH / sinMax);
        float realY = Math.min(Math.max(pointY, 0), getHeight() - getPaddingBottom());
        canvas.drawCircle(baseX - sinH, realY, circleRCurrent, mCirclePaint);
    }

    private void drawSinusoid(Canvas canvas) {
        for (int i = 0; i < TEXT_String.length(); i++) {
            recordY = i * perHeight + 25.0f + padding;

            if (judgeInterval(recordY)) {
                // Sinusoid
                float disBefore = recordY - pointYBefore;
                float realY = 0.0f;
                if (disBefore <= distance / 2.0f) {
                    realY = 1 / (distance / 2.0f) * disBefore * disBefore;
                    trans = (float) (-sinH / 2 * (Math.sin(3.1416f
                        / (distance / 2.0f) * (realY) - 3.1416 / 2) + 1.0f));
                } else {
                    realY = 1 / (distance / 2.0f) * (disBefore - distance)
                        * (disBefore - distance);
                    trans = (float) (-sinH / 2 * (Math.sin(3.1416f
                        / (distance / 2.0f) * (realY) - 3.1416 / 2) + 1.0f));
                }
                mPaint.setTextSize(mTextSize * (-trans / sinMax + 1));

                if (Math.abs(recordY - pointY) < perHeight / 2.0f) {
                    if (mHighLightPosition != i || (mHighLightPosition == i && isCallTextChange)) {
                        if (mIndexChangeListener != null) {
                            mIndexChangeListener.onIndexChanged(
                                TEXT_String.charAt(i), i);
                        }
                        mHighLightPosition = i;
                        isCallTextChange = false;
                    }
                    mPaint.setColor(mPressTextColor);

                } else {
                    mPaint.setColor(mDefaultTextColor);
                }
                canvas.drawText(TEXT_String, i, i + 1, baseX + trans, recordY,
                    mPaint);
            } else {
                mPaint.setTextSize(mTextSize);
                canvas.drawText(TEXT_String, i, i + 1, baseX, recordY, mPaint);
            }
        }
    }

    private boolean judgeInterval(float recordY) {
        if (recordY >= pointYBefore && recordY <= pointYAfter) {
            return true;
        }
        return false;
    }

    private boolean isCallTextChange = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mIndexChangeListener != null) {
            mIndexChangeListener.onTouch(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointX = event.getX();
                pointY = event.getY();

                isCallTextChange = true;
                startSinAnimator(sinH, sinMax);
                break;
            case MotionEvent.ACTION_MOVE:
                pointX = event.getX();
                pointY = event.getY();
                if (pointY > 0
                    && pointY < (getHeight() - getPaddingBottom())) {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startSinAnimator(sinH, 0.0f);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
            params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
    public void  setTouchCircleColor(int color){
        mCirclePaint.setColor(color);
        invalidate();
    }

    public interface OnIndexChangedListener {

        public void onTouch(MotionEvent event);

        public void onIndexChanged(char charAt, int index);
    }

}
