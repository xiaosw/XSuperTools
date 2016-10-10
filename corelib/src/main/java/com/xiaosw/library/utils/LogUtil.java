package com.xiaosw.library.utils;

import android.util.Log;

import com.xiaosw.library.config.BaseAppConfig;


/**
 * @ClassName: LogUtil
 * @Description: Log工具，类似android.util.Log。
 * tag自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 *
 * @version 1.0
 * @date 2015-5-19
 * @Author xiaoshiwang
 */
public class LogUtil {

    public static String tag = "%s.%s:L%d";
    public static String customTagPrefix;
    public static boolean isDebulg = BaseAppConfig.DEBUG;
    public static boolean printV = BaseAppConfig.DEBUG;
    public static boolean printI = BaseAppConfig.DEBUG;
    public static boolean printD = BaseAppConfig.DEBUG;
    public static boolean printW = BaseAppConfig.DEBUG;
    public static boolean printE = BaseAppConfig.DEBUG;
    public static boolean printWtf = BaseAppConfig.DEBUG;

    @SuppressWarnings("unused")
    private void openLog() {
        isDebulg = true;
        printV = true;
        printI = true;
        printD = true;
        printW = true;
        printE = true;
        printWtf = true;
    }

    @SuppressWarnings("unused")
    private void closeLog() {
        isDebulg = false;
        printV = false;
        printI = false;
        printD = false;
        printW = false;
        printE = false;
        printWtf = false;
    }

    private static String getTag() {
        StackTraceElement mStackTraceElement = Thread.currentThread().getStackTrace()[4];
        // 全类名
        String className = mStackTraceElement.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        // 方法名
        String methodName = mStackTraceElement.getMethodName();
        // 调用处所在行
        int lineNumber = mStackTraceElement.getLineNumber();
        tag = String.format(tag, className, methodName, lineNumber);
        return null == customTagPrefix ? tag : customTagPrefix + "--->" + tag;
    }

    public static void printMsg(String msg) {
        if(isDebulg) {
            System.out.println(getTag() + ":" + msg);
        }
    }

    public static void printException(Throwable t) {
        if(isDebulg) {
            t.printStackTrace();
        }
    }

    public static void v(String msg) {
        if(printV) {
            Log.v(getTag(), msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if(printV) {
            Log.v(getTag(), msg, tr);
        }
    }

    public static void i(String msg) {
        if(printI) {
            Log.i(getTag(), msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if(printI) {
            Log.i(getTag(), msg, tr);
        }
    }

    public static void d(String msg) {
        if(printD) {
            Log.d(getTag(), msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if(printD) {
            Log.d(getTag(), msg, tr);
        }
    }

    public static void w(String msg) {
        if(printW) {
            Log.w(getTag(), msg);
        }
    }

    public static void w(String tag, String msg) {
        if(printW) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if(printW) {
            Log.w(tag, msg, throwable);
        }
    }

    public static void w(Throwable tr) {
        if(printW) {
            Log.w(getTag(), tr);
        }
    };

    public static void w(String msg, Throwable tr) {
        if(printW) {
            Log.w(getTag(), msg, tr);
        }
    }

    public static void e(String msg) {
        e(getTag(), msg);
    };

    public static void e(String tag, String msg) {
        if(printE) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        e(getTag(), msg, tr);
    };

    public static void e(String tag, String msg, Throwable tr) {
        if(printE) {
            Log.e(tag, msg, tr);
        }
    }

    public static void wtf(String msg) {
        if(printWtf) {
            Log.wtf(getTag(), msg);
        }
    }

    public static void wtf(Throwable tr) {
        if(printWtf) {
            Log.wtf(getTag(), tr);
        }
    }

    public static void wtf(String msg, Throwable tr) {
        if(printWtf) {
            Log.wtf(getTag(), msg, tr);
        }
    }

    private LogUtil() {
    }

}
