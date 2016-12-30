package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.xiaosw.library.R;
import com.xiaosw.library.utils.LogUtil;

/**
 * <p><br/>ClassName : {@link GUIBaseView}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-29 15:15:34</p>
 */
public abstract class GUIBaseView extends View {

    /**
     * @see GUIBaseView#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-GUIBaseView";

    /** 最小高 */
    int mMinWidth;
    /** 最小宽 */
    int mMinHeight;

    /** 真是宽 除去padding */
    int mRealWidth;
    /** 真是高 除去padding */
    int mRealHeight;

    public GUIBaseView(Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public GUIBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GUIBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 模板方法，先解析基础属性，然后初始化view
     * @param attrs
     */
    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        parseAttrs(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * 子类初始化View相关属性
     */
    protected void initView(){}

    /**
     * 解析Attrs {@link com.xiaosw.library.R.styleable#GUIBaseView}
     * @param attrs
     */
    private void parseAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (null != attrs) {
            final TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.GUIBaseView, defStyleAttr, defStyleRes);
            try {
                mMinWidth = typedArray.getDimensionPixelSize(R.styleable.GUIBaseView_android_minWidth, mMinWidth);
                mMinHeight = typedArray.getDimensionPixelSize(R.styleable.GUIBaseView_android_minHeight, mMinHeight);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            } finally {
                if (null != typedArray) {
                    typedArray.recycle();
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        switch (widthMode) {
//            case MeasureSpec.AT_MOST:
//            case MeasureSpec.UNSPECIFIED:
//                LogUtil.e(TAG, "" + (MeasureSpec.AT_MOST == widthMode) + ", " + (MeasureSpec.UNSPECIFIED == widthMode));
//                width = mMinWidth;
//                break;
//
//            default:
//                // TODO: 2016/12/8
//        }
//
//        switch (heightMode) {
//            case MeasureSpec.AT_MOST:
//            case MeasureSpec.UNSPECIFIED:
//                height = mMinHeight;
//                break;
//
//            default:
//                // TODO: 2016/12/8
//        }
//        setMeasuredDimension(width, height);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mRealHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getMinWidth() {
        return mMinWidth;
    }

    public int getMinHeight() {
        return mMinHeight;
    }

    public int getRealWidth() {
        return mRealWidth;
    }

    public int getRealHeight() {
        return mRealHeight;
    }
}
