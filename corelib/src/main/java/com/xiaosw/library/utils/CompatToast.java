package com.xiaosw.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * <p><br/>ClassName : {@link CompatToast}
 * <br/>Description : 处理连续点击时重复显示
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-06 14:14:14</p>
 */
public class CompatToast extends Toast {

    /**
     * @see CompatToast#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-CompatToast";

    /**
     * 最后一次显示的toast
     */
    private static Toast mLastToast;

    public CompatToast(Context context) {
        super(context);
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        cancelLastIfNeeded();
        mLastToast = Toast.makeText(context, text, duration);
        return mLastToast;
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static Toast makeText(Context context, @StringRes int resId, int duration)
        throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    /**
     * 取消上一次提示
     */
    private static void cancelLastIfNeeded() {
        if (mLastToast != null) {
            mLastToast.cancel();
            mLastToast = null;
        }
    }
}
