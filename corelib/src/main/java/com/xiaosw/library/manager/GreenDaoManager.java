package com.xiaosw.library.manager;

import com.xiaosw.library.config.BaseAppConfig;
import com.xiaosw.library.db.dao.DaoMaster;
import com.xiaosw.library.db.dao.DaoSession;
import com.xiaosw.library.utils.AppContextUtil;

/**
 * <p><br/>ClassName : {@link GreenDaoManager}
 * <br/>Description : GreenDao相关操作
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-20 13:13:51</p>
 */
public class GreenDaoManager {

    /**
     * @see GreenDaoManager#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-GreenDaoManager";

    private static class Instance {
        private static GreenDaoManager INSTANCE = new GreenDaoManager();
    }

    public static GreenDaoManager getInstance() {
        return Instance.INSTANCE;
    }

    /** 以一定的模式管理Dao类的数据库对象 */
    private DaoMaster mDaoMaster;
    /** 管理制定模式下的所有可用Dao对象 */
    private DaoSession mDaoSession;
    public GreenDaoManager() {
        if (null == mDaoMaster) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(AppContextUtil.getApp(),
                BaseAppConfig.ENCRYPTED ? BaseAppConfig.DEFAULT_GREEN_DAO_DB_NAME_ENCRYPTED : BaseAppConfig.DEFAULT_GREEN_DAO_DB_NAME,
                null);
            mDaoMaster = new DaoMaster(BaseAppConfig.ENCRYPTED ? devOpenHelper.getEncryptedReadableDb("super-secret") : devOpenHelper.getWritableDb());
        }
        if (null == mDaoSession) {
            mDaoSession = mDaoMaster.newSession();
        }
    }
    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    public DaoSession getNewSession(String name) {
        mDaoSession = mDaoMaster.newDevSession(AppContextUtil.getApp(), name);
        return mDaoSession;
    }

}
