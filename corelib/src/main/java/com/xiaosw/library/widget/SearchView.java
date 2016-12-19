package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ConfigurationHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaosw.library.R;

/**
 * <p><br/>ClassName : {@link SearchView}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-17 16:16:27</p>
 */
public class SearchView extends AutoCompleteTextView implements TextView.OnEditorActionListener {
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

    private OnSearchListener mOnSearchListener;

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
        setSingleLine();
        setGravity(Gravity.CENTER_VERTICAL);
        setEllipsize(TextUtils.TruncateAt.END);
        setBackgroundResource(R.drawable.selector_drawable_search_view);

        setThreshold(1);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        setOnEditorActionListener(this);
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        launchQuerySearch(v, actionId);
        return true;
    }

    @Override
    public void dismissDropDown() {
        if (isPopupShowing()) {
            super.dismissDropDown();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCX = (getRight() - getPaddingRight() - getLeft() + getPaddingLeft()) / 2;
        mCY = (getBottom()  - getPaddingBottom() - getTop() + getPaddingTop()) / 2;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            getSearchViewTextMinWidthDp(), metrics));
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

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    clearFocus();
                    hideSoftInputFromWindow(this);
                    return true;
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    /**
     * We override this method to avoid replacing the query box text when a
     * suggestion is clicked.
     */
//    @Override
//    protected void replaceText(CharSequence text) {
//    }

    /**
     * We override this method to avoid an extra onItemClick being called on
     * the drop-down's OnItemClickListener by
     * {@link AutoCompleteTextView#onKeyUp(int, KeyEvent)} when an item is
     * clicked with the trackball.
     */
    @Override
    public void performCompletion() {
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

    protected void launchQuerySearch(TextView v, int actionId) {
        if ((null == mOnSearchListener || !mOnSearchListener.onSearch(v, actionId))
            && !TextUtils.isEmpty(getText())) {

            clearOwnFocus();
            hideSoftInputFromWindow(v);
        }
        Toast.makeText(getContext(), getText().toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 取消当前控件焦点
     */
    protected void clearOwnFocus() {
        ViewParent viewParent = getParent();
        if (null != viewParent) {
            ViewGroup viewGroup = (ViewGroup) viewParent;
            viewGroup.setFocusable(true);
            viewGroup.setFocusableInTouchMode(true);
            viewGroup.requestFocus();
        }
    }

    /**
     * 隐藏软键盘
     * @param targetView 用于获取token，The token of the window that is making the request,
     * as returned by {@link View#getWindowToken() View.getWindowToken()}.
     */
    protected void hideSoftInputFromWindow(View targetView) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
        }
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
            setHint(mHintText);
        } else if (TextUtils.isEmpty(getText())){
            setCompoundDrawablesWithIntrinsicBounds(null, drawables[1], null, drawables[3]);
            setHint(null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(left, drawables[1], null, drawables[3]);
        }

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

    /**
     * Get minimum width of the search view text entry area.
     */
    private int getSearchViewTextMinWidthDp() {
        final Configuration config = getResources().getConfiguration();
        final int widthDp = ConfigurationHelper.getScreenWidthDp(getResources());
        final int heightDp = ConfigurationHelper.getScreenHeightDp(getResources());

        if (widthDp >= 960 && heightDp >= 720
            && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 256;
        } else if (widthDp >= 600 || (widthDp >= 640 && heightDp >= 480)) {
            return 192;
        }
        return 160;
    }

    public OnSearchListener getOnSearchListener() {
        return mOnSearchListener;
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        mOnSearchListener = onSearchListener;
    }

    public interface OnSearchListener {
        boolean onSearch(TextView v, int actionId);
    }

}
