package com.xiaosw.library.widget.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * @ClassName : {@link SuperToast}
 * @Description : 处理高频率显示toast，一直弹toast
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 14:14:01
 */
public class SuperToast {

    private static Toast mToast;

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the mMessage.  Either {@link Toast#LENGTH_SHORT} or
     *                 {@link Toast#LENGTH_LONG}
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static void show(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        show(context, context.getResources().getText(resId), duration);
    }


    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the mMessage.  Either {@link Toast#LENGTH_SHORT} or
     *                 {@link Toast#LENGTH_LONG}
     *
     */
    public static void show(Context context, CharSequence text, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, duration);
        mToast.show();
    }
}
