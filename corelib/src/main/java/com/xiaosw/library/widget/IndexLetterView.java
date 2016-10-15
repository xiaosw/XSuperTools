package com.xiaosw.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * @ClassName : {@link IndexLetterView}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-11 19:19:07
 */
public class IndexLetterView extends View {

    public static final String[] DEFAULT_LATTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
        "Y", "Z" , "#"};

    private String[] mLetters = DEFAULT_LATTERS;

    public IndexLetterView(Context context) {
        super(context);
        initialized(context);
    }

    public IndexLetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialized(context);
    }

    public IndexLetterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialized(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IndexLetterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialized(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initialized(Context context) {

    }

}
