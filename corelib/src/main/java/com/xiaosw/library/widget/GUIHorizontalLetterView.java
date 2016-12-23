package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xiaosw.library.utils.LogUtil;

/**
 * <p><br/>ClassName : {@link GUIHorizontalLetterView}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-23 15:15:48</p>
 */
public class GUIHorizontalLetterView extends View {

    /**
     * @see GUIHorizontalLetterView#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-GUIHorizontalLetterView";

    public static final String[] LETTERS = new String[] {"A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    /** 最新宽度 */
    private final int MIN_WIDTH_PX = 1000;
    /** 最新高度 */
    private final int MIN_HEIGTH_PX = 100;
    /** 默认颜色 */
    private final int COLOR_UNCHECKED = Color.parseColor("#0176FF");
    /** 挤压/选中的文字颜色 */
    private final int COLOR_CHECKED = Color.BLACK;
    /** 文字大小 */
    private final int TEXT_SIZE = 32;


    ///////////////////////////////////////////////////////////////////////////
    // Paint
    ///////////////////////////////////////////////////////////////////////////
    private Paint mTextPaint;
    private int mCheckedColor;
    private int mUnCheckedColor;
    private int mTextSize;
    private int mLastSelectedIndex;
    private Rect mTextBounds;

    private int mRealWidth;
    private int mRealHeight;
    private float mItemWidth;

    private OnLatterIndexChangeListener mOnLatterIndexChangeListener;

    public GUIHorizontalLetterView(Context context) {
        super(context);
        init(context, null);
    }

    public GUIHorizontalLetterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GUIHorizontalLetterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIHorizontalLetterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mCheckedColor = COLOR_CHECKED;
        mUnCheckedColor = COLOR_UNCHECKED;
        mTextSize = TEXT_SIZE;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);

        mTextBounds = new Rect();
        mTextPaint.getTextBounds("M", 0, 1, mTextBounds);
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
        mItemWidth = mRealWidth / (float) LETTERS.length;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mOnLatterIndexChangeListener) {
            mOnLatterIndexChangeListener.onTouch(event);
        }
        float x = event.getX();
        for (int i = 0; i < LETTERS.length; i++) {
            if (x > mItemWidth * i
                && x < mItemWidth * (i + 1)
                && mLastSelectedIndex != i) {
                mLastSelectedIndex = i;
                invalidate();
                if (null != mOnLatterIndexChangeListener) {
                    mOnLatterIndexChangeListener.onLetterChanged(LETTERS[mLastSelectedIndex], mLastSelectedIndex);
                }
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLetter(canvas);
    }

    /**
     * 绘制字母指示器
     * @param canvas
     */
    private void drawLetter(Canvas canvas) {
        int size = LETTERS.length;
        for (int i = 0; i < size; i++) {
            if (i == mLastSelectedIndex) {
                mTextPaint.setColor(mCheckedColor);
            } else {
                mTextPaint.setColor(mUnCheckedColor);
            }
            String letter = LETTERS[i];
            float x = mItemWidth * i + (mItemWidth - mTextBounds.width()) / 2;
            float y = (mRealHeight + mTextBounds.height()) / 2;
            canvas.drawText(letter, x, y, mTextPaint);
        }
    }

    public void setOnLatterIndexChangeListener(OnLatterIndexChangeListener listener) {
        mOnLatterIndexChangeListener = listener;
    }

    /**
     * 字母切换监听
     */
    public interface OnLatterIndexChangeListener {

        /**
         * @see GUIHorizontalLetterView#dispatchTouchEvent(MotionEvent)
         * @param event
         */
        void onTouch(MotionEvent event);

        /**
         * 字母指示器变化
         * @param letter 当前字母
         * @param position 当前下标
         */
        void onLetterChanged(String letter, int position);

    }
}
