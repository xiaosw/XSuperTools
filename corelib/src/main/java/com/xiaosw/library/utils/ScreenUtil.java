/**
 * @Project : XToolLib-1.0.0
 */

package com.xiaosw.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/** 
 * @ClassName  : {@link ScreenUtil}
 * @Description: 
 *
 * @date 2015-12-24下午8:16:06
 * @Author xiaoshiwang <xiaoshiwang@rytong.com>
 */
public class ScreenUtil {
    
    private static final String TAG = "ScreenUtil";

    /**
     * @Method {@link #getScreenWH}
     * @Description:     获取屏幕宽高
     * @param context
     * @return
     * @return int[]
     *
     * @date 2015-12-24 下午8:17:09
     * @Author xiaoshiwang
     */
    public static int[] getScreenWH(Context context) {
        int[] wh = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        wh[0] = outMetrics.widthPixels;
        wh[1] = outMetrics.heightPixels;
        return wh;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param activity
     *
     * @return 实际屏幕分辨率 "width*height"格式
     */
    public static String getScreenResolution(Activity activity) {
        String screenResolution = "";
        if (activity != null) {
            int[] screenWH = getScreenWH(activity);
            screenResolution = screenWH[0] + "*" + screenWH[1];
        }
        return screenResolution;
    }

    /**
     * @Method getStatusHeight
     * @Description:     获取状态栏的高度
     * @param context
     * @return int 状态栏高度
     *
     * @version 1.0
     * @date 2015-7-20 下午8:27:43
     * @Author xiaosw
     */
    public static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
        return statusHeight;
    }

    public static float dip2px(Context context, float dip) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return px;
    }

    public static float px2dip(Context context, float px) {
        float dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
        return dip;
    }

    /**
     * 设置全屏显示
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 取消全屏显示
     * @param activity
     */
    public static void cancelFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(params);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 获取是否存在NavigationBar
     * @param context
     * @return true|false
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "checkDeviceHasNavigationBar", e);
        }
        return hasNavigationBar;

    }

    /**获取虚拟功能键高度 */
    public static int getVirtualBarHeigh(Context context) {
        int vh = 0;
        if (checkDeviceHasNavigationBar(context)) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            try {
                @SuppressWarnings("rawtypes")
                Class c = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vh;
    }

}
