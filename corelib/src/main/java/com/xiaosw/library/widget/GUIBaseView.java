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

    /** 真是宽 出去padding */
    int mRealWidth;
    /** 真是高 出去padding */
    int mRealHeight;

    public GUIBaseView(Context context) {
        super(context);
        initialize(null);
    }

    public GUIBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GUIBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GUIBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(attrs);
    }

    /**
     * 模板放放，先让子类初始化完成相关属性，然后解析基础属性
     * @param attrs
     */
    private void initialize(AttributeSet attrs) {
        initView(attrs);
        parseAttrs(attrs);
    }

    /**
     * 子类初始化View相关属性
     * @param attrs
     */
    abstract void initView(AttributeSet attrs);

    /**
     * 解析Attrs {@link com.xiaosw.library.R.styleable#GUIBaseView}
     * @param attrs
     */
    private void parseAttrs(AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = null;
            try {
                typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GUIBaseView);
//                mMinWidth = typedArray.getDimensionPixelSize(R.styleable.GUIBaseView_minWidth, mMinWidth);
//                mMinHeight = typedArray.getDimensionPixelSize(R.styleable.GUIBaseView_minHeight, mMinHeight);
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
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                LogUtil.e(TAG, "" + (MeasureSpec.AT_MOST == widthMode) + ", " + (MeasureSpec.UNSPECIFIED == widthMode));
                width = mMinWidth;
                break;

            default:
                // TODO: 2016/12/8
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = mMinHeight;
                break;

            default:
                // TODO: 2016/12/8
        }
        setMeasuredDimension(width, height);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mRealHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }

}
