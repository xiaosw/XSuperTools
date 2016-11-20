package com.xiaosw.library.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p><br/>ClassName : {@link SlideMediaItem}
 * <br/>Description : 幻灯片集item信息
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-20 12:12:50</p>
 */
@Entity(nameInDb = "tb_slide_many")
public class SlideMediaItem {

    /**
     * @see SlideMediaItem#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-SlideMediaItem";

    @Id(autoincrement = true)
    private Long id; // id
    @Property(nameInDb = "_data")
    private String data; // 文件路径
    @Property(nameInDb = "mime_type")
    private String mimeType; // 添加时间
    @Property(nameInDb = "date_taken")
    private long datetaken; // 添加时间
    @Property(nameInDb = "media_id")
    private String mediaId; // 媒体库id
    @NotNull
    private long pkId; // 外键
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
    @Generated(hash = 977820116)
    public SlideMediaItem(Long id, String data, String mimeType, long datetaken,
            String mediaId, long pkId, String data0, String data1, String data2,
            String data3, String data4, String data5, String data6, String data7) {
        this.id = id;
        this.data = data;
        this.mimeType = mimeType;
        this.datetaken = datetaken;
        this.mediaId = mediaId;
        this.pkId = pkId;
        this.data0 = data0;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
    }
    @Generated(hash = 1238921556)
    public SlideMediaItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getMimeType() {
        return this.mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public long getDatetaken() {
        return this.datetaken;
    }
    public void setDatetaken(long datetaken) {
        this.datetaken = datetaken;
    }
    public String getMediaId() {
        return this.mediaId;
    }
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
    public long getPkId() {
        return this.pkId;
    }
    public void setPkId(long pkId) {
        this.pkId = pkId;
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
}
