package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

import com.xiaosw.library.R;

/**
 * <p><br/>ClassName : {@link SearchView}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-17 16:16:27</p>
 */
public class SearchView extends EditText {
    /**
     * @see SearchView#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-SearchView";

    ///////////////////////////////////////////////////////////////////////////
    // search icon
    ///////////////////////////////////////////////////////////////////////////
    private Drawable mSearchIcon;
    private Rect mSearchRect;
    private Rect mHintTextRect;
    private String mHintText;

    ///////////////////////////////////////////////////////////////////////////
    // delete icon
    ///////////////////////////////////////////////////////////////////////////
    private Drawable mDeleteIcon;

    private int mCX;
    private int mCY;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        mSearchIcon = getResources().getDrawable(android.R.drawable.ic_menu_search);
        mSearchRect = new Rect(0, 0, mSearchIcon.getIntrinsicWidth(), mSearchIcon.getIntrinsicHeight());
        mDeleteIcon = getResources().getDrawable(android.R.drawable.ic_menu_delete);
        mHintTextRect = new Rect();
        mHintText = getHint() + "";
        setGravity(Gravity.CENTER);
        setSingleLine();
        setBackgroundResource(R.drawable.selector_drawable_search_view);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setIcon();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setIcon();
    }

    private void setIcon() {
        Drawable[] drawables = getCompoundDrawables();
        Drawable left = drawables[0];
        if (null == left) {
            left = mSearchIcon;
        }
        if (isFocused()) {
            Drawable right = drawables[2];
            if (right == null) {
                right = mDeleteIcon;
            }
            if (TextUtils.isEmpty(getText())) {
                setCompoundDrawablesWithIntrinsicBounds(left, drawables[1], null, drawables[3]);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(left, drawables[1], right, drawables[3]);
            }
            if (getGravity() != Gravity.LEFT) {
                setGravity(Gravity.LEFT);
            }
            setHint(mHintText);
        } else if (TextUtils.isEmpty(getText())){
            setCompoundDrawablesWithIntrinsicBounds(null, drawables[1], null, drawables[3]);
            setHint(null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(left, drawables[1], null, drawables[3]);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCX = (getRight() - getPaddingRight() - getLeft() + getPaddingLeft()) / 2;
        mCY = (getBottom()  - getPaddingBottom() - getTop() + getPaddingTop()) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isFocused() && TextUtils.isEmpty(getText())) {
            calclateSearchIconRect();
            mSearchIcon.draw(canvas);

            getPaint().setColor(getCurrentHintTextColor());
            canvas.drawText(mHintText, mSearchRect.right, mCY + mHintTextRect.height() / 2 - 3, getPaint());
        }
    }

    long mDownTime;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_UP:
                Drawable[] drawables = getCompoundDrawables();
                Drawable right = drawables[2];
                if (!TextUtils.isEmpty(getText())
                    && null != right
                    && event.getX() < getMeasuredWidth() - getPaddingRight()
                    && event.getX() >  getMeasuredWidth() - getPaddingRight() - right.getIntrinsicWidth()
                    && System.currentTimeMillis() - mDownTime < 1000) {
                    setText(null);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     *  計算搜索icon位置
     */
    private void calclateSearchIconRect() {
        if (TextUtils.isEmpty(getText())) {
            String hintText = mHintText + "";
            getPaint().getTextBounds(hintText, 0, hintText.length(), mHintTextRect);
            int l = mCX - mHintTextRect.width() / 2 - mSearchRect.width();
            int t = mCY - mSearchRect.height() / 2;
            int r = l + mSearchRect.width();
            int b = t + mSearchRect.height();
            mSearchRect.set(l, t, r, b);
            mSearchIcon.setBounds(mSearchRect);
        }
    }

}
