package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xiaosw.library.R;

/**
 * <p><br/>ClassName : {@link GUIHorizontalLetterView}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-23 15:15:13</p>
 */
public class GUIHorizontalLetterView extends View {

    /**
     * @see GUIHorizontalLetterView#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-LetterView";

    public static final String[] LETTERS = new String[] {"A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    /** 最新宽度 */
    private final int MIN_WIDTH_PX = 1000;
    /** 最新高度 */
    private final int MIN_HEIGTH_PX = 100;
    /** 默认颜色 */
    private final int COLOR_UNCHECKED = Color.GRAY;
    /** 挤压/选中的文字颜色 */
    private final int COLOR_CHECKED = Color.WHITE;
    /** 文字大小 */
    private final int TEXT_SIZE = 42;
    private Drawable mSelectedDrawable;


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

    private OnLetterIndexChangeListener mOnLetterIndexChangeListener;

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
        mTextPaint.setStrokeWidth(4);

        mTextBounds = new Rect();
        mTextPaint.getTextBounds("M", 0, 1, mTextBounds);

        mSelectedDrawable = getContext().getResources().getDrawable(R.mipmap.ic_letter_selected);
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
        if (null != mOnLetterIndexChangeListener) {
            mOnLetterIndexChangeListener.onTouch(event);
        }
        float x = event.getX();
        for (int i = 0; i < LETTERS.length; i++) {
            if (x > mItemWidth * i
                && x < mItemWidth * (i + 1)
                && mLastSelectedIndex != i) {
                if (null != mOnLetterIndexChangeListener
                    && !mOnLetterIndexChangeListener.onLetterChanged(LETTERS[i], i)) {
                    return true; // 没有该letter对应下标
                }
                mLastSelectedIndex = i;
                invalidate();
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
                mTextPaint.setTextSize(TEXT_SIZE + 12);
                mTextPaint.setColor(mCheckedColor);
            } else {
                mTextPaint.setTextSize(TEXT_SIZE);
                mTextPaint.setColor(mUnCheckedColor);
            }
            String letter = LETTERS[i];
            mTextPaint.getTextBounds(letter, 0, 1, mTextBounds);
            float x = mItemWidth * i + mItemWidth / 2;
            float y = getMeasuredHeight()  / 2;

            if (i == mLastSelectedIndex) {
                int l = (int) x - 30;
                int t = (int) y - 36;
                int r = (int) x + 30;
                int b = (int) y + 44;
                mSelectedDrawable.setBounds(l, t, r, b);
                mSelectedDrawable.draw(canvas);
            }
            canvas.drawText(letter, x - (mTextBounds.left + mTextBounds.right) / 2, y - (mTextBounds.top + mTextBounds.bottom) / 2, mTextPaint);
//            canvas.drawLine(mItemWidth * i + mItemWidth / 2, 0, mItemWidth * i  + mItemWidth / 2, getMeasuredHeight(), mTextPaint);
//            canvas.drawLine(mItemWidth * i, getMeasuredHeight() / 2, mItemWidth * i + mItemWidth, getMeasuredHeight() / 2, mTextPaint);
        }
    }

    public void setOnLetterIndexChangeListener(OnLetterIndexChangeListener listener) {
        mOnLetterIndexChangeListener = listener;
    }

    /**
     * 设定当前选中字母
     * @param selectedIndex
     */
    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex >= 0 &&
            selectedIndex < LETTERS.length) {
            mLastSelectedIndex = selectedIndex;
            invalidate();
        } else {
            Log.w(TAG, "selectedIndex = " + selectedIndex + ", maxLen = " + LETTERS.length + ", IndexOutOfBoundsException!!!");
        }

    }

    /**
     * 设定当前选中字母
     * @see GUIHorizontalLetterView#LETTERS
     * @param letter
     */
    public void setSelectedIndexByLetter(String letter) {
        int len = LETTERS.length;
        for (int i = 0; i < len; i++) {
            String str = LETTERS[i];
            if (str.equalsIgnoreCase(letter)) {
                mLastSelectedIndex = i;
                invalidate();
                break;
            }
        }
    }

    /**
     * 字母切换监听
     */
    public interface OnLetterIndexChangeListener {

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
        boolean onLetterChanged(String letter, int position);

    }

}
