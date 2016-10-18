package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.xiaosw.library.R;

/**
 * @ClassName : {@link ColorTrackRadioButton}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-15 17:17:50
 */
public class ColorTrackRadioButton extends RadioButton {
    private static final String TAG = "ColorTrackRadioButton";
    private int mTextOriginalColor = Color.BLACK;
    private int mTextChangeColor = Color.GREEN;


    public static final int CLIP_START_BY_LEFT = 0 ;
    public static final int CLIP_START_BY_RIGHT = 1 ;


    private int mClipStart = CLIP_START_BY_LEFT;
    private Rect mTextBounds;
    private Paint mTextPaint;
    private float mProgress;

    public ColorTrackRadioButton(Context context) {
        this(context, null);
    }

    public ColorTrackRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(attrs);
    }

    public ColorTrackRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorTrackRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttrs(attrs);
    }

    private void parseAttrs(AttributeSet attrs) {
        mTextBounds = new Rect();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (null == attrs) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ColorTrackRadioButton);
        mTextOriginalColor = ta.getColor(R.styleable.ColorTrackRadioButton_text_origin_color, Color.BLACK);
        mTextChangeColor = ta.getColor(R.styleable.ColorTrackRadioButton_text_change_color, Color.GRAY);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        CharSequence textSeq = getText();
        if (null != textSeq) {
            String text = textSeq.toString();
            Paint paint = getPaint();
            paint.getTextBounds(text.toString(), 0, text.length(), mTextBounds);
            float x = (getMeasuredWidth() - mTextBounds.width()) / 2;
            float y = (getMeasuredHeight() + mTextBounds.height()) / 2;
            // draw original text
            paint.setColor(Color.BLACK);
            canvas.drawText(text.toString(), 0, text.length(), x, y, paint);

            canvas.save(Canvas.CLIP_SAVE_FLAG);
            paint.setColor(Color.RED);
            float progressTextWidth = mTextBounds.width() * mProgress;
            if (mClipStart == CLIP_START_BY_LEFT) {
                canvas.clipRect(x, 0, x + progressTextWidth, getMeasuredHeight());
            } else {
                float right = x + mTextBounds.width();
                canvas.clipRect(right - progressTextWidth, 0, right, getMeasuredHeight());
            }
            canvas.drawText(text.toString(), 0, text.length(), x, y, paint);
            canvas.restore();
        }
    }

    //    @Override
//    protected void onDraw(Canvas canvas) {
////        super.onDraw(canvas);
//        CharSequence textSeq = getText();
//        if (null != textSeq) {
//            String text = textSeq.toString();
//            Paint paint = getPaint();
////            ColorStateList colors = getTextColors();
////            paint.setColor(colors.getColorForState(getDrawableState(), 0));
//            paint.getTextBounds(text.toString(), 0, text.length(), mTextBounds);
//            canvas.drawText(text.toString(),
//                0,
//                text.length(),
//                (getMeasuredWidth() - mTextBounds.width()) / 2,
//                (getMeasuredHeight() + mTextBounds.height()) / 2,
//                getPaint());
//
//            canvas.save(Canvas.CLIP_SAVE_FLAG);
//            paint.setColor(Color.RED);
//            float x = (getMeasuredWidth() - mTextBounds.width()) / 2;
//            float y = (getMeasuredHeight() + mTextBounds.height()) / 2;
//            if (mClipStart == CLIP_START_BY_LEFT) {
//                canvas.clipRect(x, 0, x + mTextBounds.width() * mProgress, getMeasuredHeight());
//            } else {
//                canvas.clipRect(x, 0, x + mTextBounds.width() - mTextBounds.width() * mProgress, getMeasuredHeight());
//            }
//            canvas.drawText(text.toString(), 0, text.length(), x, y, getPaint());
//            canvas.restore();
//        }
//    }


    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (isChecked()) {
            mProgress = 1.0f;
        } else {
            mProgress = 0.0f;
        }
    }

    public void setClipStart(int direation) {
        this.mClipStart = direation;
    }

    public void setProgress(float progress) {
        mProgress = progress;
        invalidate();
    }
}
