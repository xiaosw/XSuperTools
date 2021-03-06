package com.xiaosw.library;

import android.support.multidex.MultiDexApplication;

import com.xiaosw.library.utils.AppContextUtil;

/**
 * @ClassName : {@link BaseApplication}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-11 10:10:18
 */
public abstract class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContextUtil.init(this);
    }
}
