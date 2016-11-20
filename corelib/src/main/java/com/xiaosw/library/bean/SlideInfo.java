package com.xiaosw.library.bean;

import com.xiaosw.library.db.dao.DaoSession;
import com.xiaosw.library.db.dao.SlideInfoDao;
import com.xiaosw.library.db.dao.SlideMediaItemDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * <p><br/>ClassName : {@link SlideInfo}
 * <br/>Description : 幻灯片集信息
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-20 12:12:50</p>
 */
@Entity(nameInDb = "tb_slide_one")
public class SlideInfo {

    /**
     * @see SlideInfo#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-SlideInfo";

    @Id(autoincrement = true)
    private Long id; // id
    @Property(nameInDb = "title")
    private String title; // 标题
    @Property(nameInDb = "date_added")
    private long dateAdded; // 添加时间
    @Property(nameInDb = "date_modified")
    private long dateModified; // 修改时间
    @Property(nameInDb = "datetaken")
    private long datetaken;
    @Property(nameInDb = "description")
    private String description; // 描述
    @Property(nameInDb = "data_0")
    private String data0;
    @Property(nameInDb = "data_1")
    private String data1;
    @Property(nameInDb = "data_2")
    private String data2;
    @Property(nameInDb = "data_3")
    private String data3;
    @Property(nameInDb = "data_4")
    private String data4;
    @Property(nameInDb = "data_5")
    private String data5;
    @Property(nameInDb = "data_6")
    private String data6;
    @Property(nameInDb = "data_7")
    private String data7;

    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "pkId")})
    private List<SlideMediaItem> mSlideMediaItems;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1787204799)
    private transient SlideInfoDao myDao;

    @Generated(hash = 783664498)
    public SlideInfo(Long id, String title, long dateAdded, long dateModified,
            long datetaken, String description, String data0, String data1,
            String data2, String data3, String data4, String data5, String data6,
            String data7) {
        this.id = id;
        this.title = title;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.datetaken = datetaken;
        this.description = description;
        this.data0 = data0;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
    }

    @Generated(hash = 204250667)
    public SlideInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateModified() {
        return this.dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public long getDatetaken() {
        return this.datetaken;
    }

    public void setDatetaken(long datetaken) {
        this.datetaken = datetaken;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData0() {
        return this.data0;
    }

    public void setData0(String data0) {
        this.data0 = data0;
    }

    public String getData1() {
        return this.data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return this.data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return this.data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData4() {
        return this.data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return this.data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public String getData6() {
        return this.data6;
    }

    public void setData6(String data6) {
        this.data6 = data6;
    }

    public String getData7() {
        return this.data7;
    }

    public void setData7(String data7) {
        this.data7 = data7;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 421487301)
    public List<SlideMediaItem> getMSlideMediaItems() {
        if (mSlideMediaItems == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SlideMediaItemDao targetDao = daoSession.getSlideMediaItemDao();
            List<SlideMediaItem> mSlideMediaItemsNew = targetDao
                    ._querySlideInfo_MSlideMediaItems(id);
            synchronized (this) {
                if (mSlideMediaItems == null) {
                    mSlideMediaItems = mSlideMediaItemsNew;
                }
            }
        }
        return mSlideMediaItems;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 237450549)
    public synchronized void resetMSlideMediaItems() {
        mSlideMediaItems = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 165731345)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSlideInfoDao() : null;
    }

    @Override
    public String toString() {
        return "SlideInfo{" +
            "data7='" + data7 + '\'' +
            ", data6='" + data6 + '\'' +
            ", data5='" + data5 + '\'' +
            ", data4='" + data4 + '\'' +
            ", data3='" + data3 + '\'' +
            ", data2='" + data2 + '\'' +
            ", data1='" + data1 + '\'' +
            ", data0='" + data0 + '\'' +
            ", description='" + description + '\'' +
            ", datetaken=" + datetaken +
            ", dateModified=" + dateModified +
            ", dateAdded=" + dateAdded +
            ", title='" + title + '\'' +
            ", id=" + id +
            '}';
    }
}
