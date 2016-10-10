package com.xiaosw.library.controll;

import android.app.Activity;
import android.app.Dialog;
import android.widget.PopupWindow;

import com.xiaosw.library.utils.LogUtil;

import java.util.ArrayList;

/**
 * @ClassName : {@link AlertWindowControll}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-23 11:11:57
 */
public enum  AlertWindowControll {

    INSTANCE;

    private static final String TAG = "AlertWindowControll";

    /**
     * 存储当前界面显示的弹出集合
     */
    private ArrayList<Object> mAlertWindows;

    private synchronized ArrayList<Object> getAlertWindows() {
        if (null == mAlertWindows) {
            synchronized (AlertWindowControll.this) {
                if (null == mAlertWindows) {
                    mAlertWindows = new ArrayList<Object>();
                }
            }
        }
        return mAlertWindows;
    }

    /**
     * 记录当前弹窗
     * @param alertWindow must instanceof {@link Activity} or {@link Dialog} or {@link PopupWindow}
     */
    public void show(Object alertWindow) {
        if (null == alertWindow
            || (!(alertWindow instanceof Activity)
                    && !(alertWindow instanceof Dialog)
                    && !(alertWindow instanceof PopupWindow))) {
            return;
        }

        try {
            ArrayList<Object> objects = getAlertWindows();
            if (!objects.contains(alertWindow)) {
                objects.add(alertWindow);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "show", e);
        }
    }

    /**
     * 记录弹窗被销毁
     * @param alertWindow 窗口{@link Activity} or {@link Dialog} or {@link PopupWindow}
     */
    public void dissmiss(Object alertWindow) {
        if (null == alertWindow) {
            return;
        }
        try {
           getAlertWindows().remove(alertWindow);
        } catch (Exception e) {
            LogUtil.e(TAG, "show", e);
        }
    }

    /**
     * 账户登录状态改变，清空当前所有弹窗
     */
    public synchronized void dissmissAll() {
        ArrayList<Object> objects = getAlertWindows();
        try {
            for (Object object: objects) {
                if (null == object) {
                    continue;
                }
                if (object instanceof Activity) {
                    Activity activity = (Activity) object;
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                } else if (object instanceof Dialog) {
                    Dialog dialog = (Dialog) object;
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } else if (object instanceof PopupWindow) {
                    PopupWindow popupWindow = (PopupWindow) object;
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "dissmissAll", e);
        } finally {
            objects.clear();
        }
    }

    /**
     * 最后一个activit被销毁调用
     */
    public void clear() {
        LogUtil.e(TAG, "clear()");
        try {
            getAlertWindows().clear();
        } catch (Exception e) {
            LogUtil.e(TAG, "clear()", e);
        }
    }

}
