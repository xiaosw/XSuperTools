package com.xiaosw.library.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xiaosw.library.bean.SlideInfo;
import com.xiaosw.library.bean.SlideMediaItem;

import com.xiaosw.library.db.dao.SlideInfoDao;
import com.xiaosw.library.db.dao.SlideMediaItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig slideInfoDaoConfig;
    private final DaoConfig slideMediaItemDaoConfig;

    private final SlideInfoDao slideInfoDao;
    private final SlideMediaItemDao slideMediaItemDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        slideInfoDaoConfig = daoConfigMap.get(SlideInfoDao.class).clone();
        slideInfoDaoConfig.initIdentityScope(type);

        slideMediaItemDaoConfig = daoConfigMap.get(SlideMediaItemDao.class).clone();
        slideMediaItemDaoConfig.initIdentityScope(type);

        slideInfoDao = new SlideInfoDao(slideInfoDaoConfig, this);
        slideMediaItemDao = new SlideMediaItemDao(slideMediaItemDaoConfig, this);

        registerDao(SlideInfo.class, slideInfoDao);
        registerDao(SlideMediaItem.class, slideMediaItemDao);
    }
    
    public void clear() {
        slideInfoDaoConfig.clearIdentityScope();
        slideMediaItemDaoConfig.clearIdentityScope();
    }

    public SlideInfoDao getSlideInfoDao() {
        return slideInfoDao;
    }

    public SlideMediaItemDao getSlideMediaItemDao() {
        return slideMediaItemDao;
    }

}
