package com.xiaosw.library.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : {@link MPermissionCompat}
 * @Description : 运行时权限处理
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-06 14:14:56
 */
public class MPermissionCompat {

    /**
     * 处理运行时权限
     * @param object {@link Activity} {@link Fragment}
     * @param requestCode
     * @param permissions
     * @param listener
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissionsCompat(Object object, int requestCode, String[] permissions, OnRequstPermissionListener listener) {
        if (null == object) {
            throw new NullPointerException("object must not null!!!");
        }
        Fragment fragment = null;
        Activity activity;
        if (object instanceof Activity) {
            activity = (Activity) object;
        } else if (object instanceof Fragment) {
            fragment = (Fragment) object;
            activity = fragment.getActivity();
        } else {
            throw  new IllegalArgumentException("object must instanceof Activity or Fragment");
        }

        if (checkNeedDynamicAuth()) { // target >= 23时才需要动态申请
            List<String> deniedPermissions = findDeniedPermissions(activity, permissions);
            if (deniedPermissions.isEmpty()) { // 待申请的权限已被授权
                if (null != listener) {
                    listener.onHaveAuthorized();
                }
            } else {
                ArrayList<String> shouldShowRequestPermissions = new ArrayList<String>();
                for (String permission : deniedPermissions) { // 检测待授权的权限是否含有已被用户拒绝且被设置为不再提醒
                    if (activity.shouldShowRequestPermissionRationale(permission)) {
                        shouldShowRequestPermissions.add(permission);
                    }
                }
                if (shouldShowRequestPermissions.isEmpty()) {
                    if (fragment != null) {
                        fragment.requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
                    } else {
                        activity.requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
                    }
                } else {
                    if (listener != null) {
                        listener.onAlertTip(shouldShowRequestPermissions);
                    }
                }
            }
        } else {
            if (null != listener) {
                listener.onNotNeedToApplyDynamic();
            }
        }
    }

    /**
     * 检查待请求的权限是否被拒绝
     * @param context
     * @param permission
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static List<String> findDeniedPermissions(Context context, String... permission){
        List<String> denyPermissions = new ArrayList<>();
        for(String value : permission){
            if(context.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED){
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }


    /**
     * 检测是否需要动态授权 target >= 23
     * @return
     */
    private static boolean checkNeedDynamicAuth() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 权限请求回调
     */
    public interface OnRequstPermissionListener extends ActivityCompat.OnRequestPermissionsResultCallback  {
        /**
         * 无需动态申请
         */
        public void onNotNeedToApplyDynamic();

        /**
         * 已授权
         */
        public void onHaveAuthorized();

        /**
         * 已拒绝且被用户标记为不再提示
         */
        public void onAlertTip(ArrayList<String> shouldShowRequestPermissions);
    }

}
