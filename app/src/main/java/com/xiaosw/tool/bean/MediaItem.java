package com.xiaosw.tool.bean;

/**
 * <p><br/>ClassName : {@link MediaItem}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-22 20:20:37</p>
 */
public class MediaItem {

    /**
     * @see MediaItem#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-MediaItem";

    private String data;
    private String mimeType;
    private int width;
    private int height;
    private long datetaken;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getDatetaken() {
        return datetaken;
    }

    public void setDatetaken(long datetaken) {
        this.datetaken = datetaken;
    }
}
