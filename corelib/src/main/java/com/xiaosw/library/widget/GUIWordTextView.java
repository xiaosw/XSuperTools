package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xiaosw.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <p><br/>ClassName : {@link GUIWordTextView}
 * <br/>Description : 虚线view 用于英文显示
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-01-09 17:17:13</p>
 */
public class GUIWordTextView extends TextView {

    /**
     * @see GUIWordTextView#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-DashLineTextView";

    /** 实线宽度 */
    public static final float DEFAULT_DASH_WIDTH = 30;
    /** 虚线宽度 */
    public static final float DEFAULT_DASH_GAP = 30;
    /** 起始偏移量 */
    public static final float DEFAULT_DASH_OFFSET = 0;
    /** 虚线厚度 */
    public static final float DEFAULT_DASH_STORK_WIDTH = 4;
    /** 虚线颜色 */
    public static final int DEFAULT_DASH_COLOR = Color.GRAY;

    ///////////////////////////////////////////////////////////////////////////
    // attrs
    ///////////////////////////////////////////////////////////////////////////
    private float mDashWidth;
    private float mDashGap;
    private float mDashOffset;
    private float mStorkWidth;
    private int mDashColor;

    ///////////////////////////////////////////////////////////////////////////
    // cavans
    ///////////////////////////////////////////////////////////////////////////
    private Path mDashLinePath;
    private Paint mDashLinePaint;
    private List<Line> mLines;
    private Rect mTextRect;
    private int mCenterSpanHeight;
    private float mTextY;

    public GUIWordTextView(Context context) {
        this(context, null);
    }

    public GUIWordTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GUIWordTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIWordTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray ta =  context.obtainStyledAttributes(attrs, R.styleable.GUIWordTextView);
        mDashWidth = ta.getDimension(R.styleable.GUIWordTextView_android_dashWidth, DEFAULT_DASH_WIDTH);
        mDashGap = ta.getDimension(R.styleable.GUIWordTextView_android_dashGap, DEFAULT_DASH_GAP);
        mDashOffset = ta.getDimension(R.styleable.GUIWordTextView_dashOffset, DEFAULT_DASH_OFFSET);
        mStorkWidth = ta.getFloat(R.styleable.GUIWordTextView_android_strokeWidth, DEFAULT_DASH_STORK_WIDTH);
        mDashColor = ta.getColor(R.styleable.GUIWordTextView_dashColor, DEFAULT_DASH_COLOR);
        ta.recycle();

        setMaxLines(1);
        setEllipsize(TextUtils.TruncateAt.END);

        PathEffect effects = new DashPathEffect(new float[]{mDashWidth, mDashGap}, mDashOffset);
        mDashLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashLinePaint.setStyle(Paint.Style.STROKE);
        mDashLinePaint.setStrokeWidth(mStorkWidth);
        mDashLinePaint.setColor(mDashColor);
        mDashLinePaint.setPathEffect(effects);

        mDashLinePath = new Path();

        mLines = new ArrayList<>();

        calculateTextSize();
//        Log.e(TAG, "init: " + textRect.left + ", " + textRect.top + ", " + textRect.right + ", " + textRect.bottom + ", " + textRect.width() + ", " + textRect.height());
    }

    private void calculateTextSize() {
        String text = getText().toString();
        if (!TextUtils.isEmpty(text)) {
            if(null == mTextRect) {
                mTextRect = new Rect();
            }
            getPaint().getTextBounds("e", 0, 1, mTextRect);
            mCenterSpanHeight = Math.abs(mTextRect.bottom - mTextRect.top);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        calculateTextSize();
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        calculateTextSize();
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        calculateTextSize();
    }

    @Override
    public void setTextScaleX(float size) {
        super.setTextScaleX(size);
        calculateTextSize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        if (height > 0) {
            mLines.clear();
            int startX = getCompoundPaddingLeft();
            int endX = getMeasuredWidth() - getCompoundPaddingRight();
            float centerY = (getMeasuredHeight() - getCompoundPaddingBottom() + getCompoundPaddingTop()) / 2f;

            // top line
            int y = (int) (centerY - mCenterSpanHeight / 2 - 2);
            mLines.add(new Line(new Point(startX, y), new Point(endX, y)));
            mLines.add(new Line(new Point(startX, y), new Point(endX, y)));

            // bottom line
            y = (int) (centerY + mCenterSpanHeight / 2 + mStorkWidth);
            mLines.add(new Line(new Point(startX, y), new Point(endX, y)));
            mLines.add(new Line(new Point(startX, y), new Point(endX, y)));

            mTextY = centerY + mCenterSpanHeight / 2f;

            String text = getText().toString();
            if (!TextUtils.isEmpty(text)
                    && getPaint().measureText(getText().toString()) > endX - startX) {
                // 超出一行
                int textLen = text.length();
                String resultText = "...";
                for (int i = 0; i < textLen; i++) {
                    String tempText = text.substring(0, i).concat("...");
                    if (getPaint().measureText(tempText) > endX - startX) {
                        break;
                    }
                    resultText = tempText;
                }
                setText(resultText);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDashLine(canvas);
        canvas.drawText(getText().toString(), getCompoundPaddingLeft(), mTextY, getPaint());
        // super draw text is not center
//        super.onDraw(canvas);
    }

    private void drawDashLine(Canvas canvas) {
        int lineSize = mLines.size();
        if (lineSize == 0
            || lineSize % 2 != 0) {
            return;
        }
        for (int i = 0; i < lineSize; i++) {
            mDashLinePath.reset();
            Line line = mLines.get(i);
            mDashLinePath.moveTo(line.startPoint.x, line.startPoint.y);
            mDashLinePath.lineTo(line.endPoint.x, line.endPoint.y);
            canvas.drawPath(mDashLinePath, mDashLinePaint);
        }
    }

    private class Line {
        Point startPoint;
        Point endPoint;

        public Line(Point startPoint, Point endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
        }
    }
}
