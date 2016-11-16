package com.xiaosw.library.bean;

/**
 * <p><br/>ClassName : {@link MediaItem}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-16 15:15:14</p>
 */
public class MediaItem {

    /** @see android.provider.MediaStore.Images.ImageColumns#_ID */
    private int id;
    /** @see android.provider.MediaStore.Images.ImageColumns#MIME_TYPE */
    private String mimeType;
    /** @see android.provider.MediaStore.Images.ImageColumns#LATITUDE 经度 */
    private double latitude;
    /** @see android.provider.MediaStore.Images.ImageColumns#LONGITUDE 纬度 */
    private double longitude;
    /** @see android.provider.MediaStore.Images.ImageColumns#DATE_TAKEN */
    private long dateTaken;
    /** @see android.provider.MediaStore.Images.ImageColumns#DATA */
    private String data;
    /** @see android.provider.MediaStore.Images.ImageColumns#SIZE */
    private int size;
    /** @see android.provider.MediaStore.Images.ImageColumns#BUCKET_ID 计算方式为---> "sdcard/xxx/xxx".toLowerCase().hashCode() */
    private int bucktId;
    /** @see android.provider.MediaStore.Images.ImageColumns#BUCKET_DISPLAY_NAME 所在文件夹名称 */
    private String bucketDisplayName;
    /** @see android.provider.MediaStore.Images.ImageColumns#TITLE 标题 */
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBucktId() {
        return bucktId;
    }

    public void setBucktId(int bucktId) {
        this.bucktId = bucktId;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
