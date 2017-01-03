package com.xiaosw.library.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.xiaosw.library.BaseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <p><br/>ClassName : {@link AppContextUtil}
 * <br/>Description : Context相关操作
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-20 13:13:42</p>
 */
public class AppContextUtil {

    /**
     * @see AppContextUtil#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-AppContextUtil";

    private static BaseApplication sApp;
    public static void init(BaseApplication app) {
        sApp = app;
    }

    public static BaseApplication getApp() {
        return sApp;
    }

    public static Resources getResources() {
        if (null == sApp) {
            return null;
        }
        return sApp.getResources();
    }

    public static int getColor(int resId) {
        if (null == sApp) {
            return -1;
        }
        return sApp.getResources().getColor(resId);
    }

    public static float getDimension(int resId) {
        if (null == sApp) {
            return -1;
        }
        return sApp.getResources().getDimension(resId);
    }

    public static int getDimensionPixelOffset(int resId) {
        if (null == sApp) {
            return -1;
        }
        return sApp.getResources().getDimensionPixelOffset(resId);
    }

    public static int getDimensionPixelSize(int resId) {
        if (null == sApp) {
            return -1;
        }
        return sApp.getResources().getDimensionPixelSize(resId);
    }

    public static Drawable getDrawable(int resId) {
        if (null == sApp) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return sApp.getBaseContext().getDrawable(resId);
        }
        return sApp.getResources().getDrawable(resId);
    }

    public static String getString(int resId) {
        if (null == sApp) {
            return null;
        }
        return sApp.getResources().getString(resId);
    }

    public static String getString(int resId, Object... objs) {
        if (null == sApp) {
            return null;
        }
        return sApp.getResources().getString(resId, objs);
    }

    public static String readTextFileFromRawResource(int resourceId) {
        StringBuilder body = new StringBuilder();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = sApp.getResources().openRawResource(resourceId);
            isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "readTextFileFromRawResource bufferedReader.close()", e);
            } finally {
                try {
                    if (null != isr) {
                        isr.close();
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "readTextFileFromRawResource inputStreamReader.close()", e);
                } finally {
                    try {
                        if (null != is) {
                            is.close();
                        }
                    } catch (Exception e) {
                        LogUtil.e(TAG, "readTextFileFromRawResource inputStream.close()", e);
                    }
                }
            }
        }
        return body.toString();
    }

    public static float dp2px(float dpValues) {
        return sApp.getResources().getDisplayMetrics().density * dpValues + 0.5f;
    }

}
