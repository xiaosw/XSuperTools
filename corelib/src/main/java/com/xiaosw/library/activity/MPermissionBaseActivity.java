package com.xiaosw.library.activity;

import com.xiaosw.library.utils.MPermissionCompat;

import java.util.ArrayList;

/**
 * @ClassName {@link MPermissionBaseActivity}
 * @Description 动态权限处理基类
 *
 * @Date 2016-10-10 19:21.
 * @Author xiaoshiwang.
 */
public abstract class MPermissionBaseActivity extends BaseAppCompatActivity
        implements MPermissionCompat.OnRequstPermissionListener {


    ///////////////////////////////////////////////////////////////////////////
    // 权限操作部分
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onNotNeedToApplyDynamic() {
        // TODO: 16/10/10 无需动态申请权限
    }

    @Override
    public void onHaveAuthorized() {
        // TODO: 16/10/10 已授权
    }

    @Override
    public void onAlertTip(ArrayList<String> shouldShowRequestPermissions) {
        // TODO: 16/10/10 被用户点击拒绝且不再提醒
    }

}
