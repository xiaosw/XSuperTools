package android.support.v4.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.os.CancellationSignal;

import com.xiaosw.library.bean.MediaItem;
import com.xiaosw.library.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p><br/>ClassName : {@link SimpleMediaStoreDataLoader}
 * <br/>Description : load metadata from the media store for images and videos
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-16 15:15:03</p>
 */
public class SimpleMediaStoreDataLoader extends AbsListLoader<MediaItem> {
    /** getClass().getSimpleName() */
    private static final String TAG = "xiaosw-SimpleMediaStoreDataLoader";

    ///////////////////////////////////////////////////////////////////////////
    // 数据库排序
    ///////////////////////////////////////////////////////////////////////////
    public static final String ORDER_BY_DESC = " DESC ";
    public static final String ORDER_BY_ASC = " ASC ";

    ///////////////////////////////////////////////////////////////////////////
    // 查询条件限制
    ///////////////////////////////////////////////////////////////////////////
    public static final String FOLDER_CAMERA = "Camera";
    public static final String FOLDER_SCREENSHOTS = "Screenshots";
    public static final String FOLDER_VIDEO = "Video";
    public static final String FOLDER_BAMBOO_PAPER = "BambooPaper";
    public static final String FOLDER_DOWNLOAD_UP = "Download";
    public static final String FOLDER_DOWNLOAD_DOWN = "download";

    ///////////////////////////////////////////////////////////////////////////
    // 查询字段
    ///////////////////////////////////////////////////////////////////////////
    /** 媒体库对应字段下标 */
    final int INDEX_ID = 0;
    final int INDEX_MIME_TYPE = 1;
    final int INDEX_LATITUDE = 2;
    final int INDEX_LONGITUDE = 3;
    final int INDEX_DATE_TAKEN = 4;
    final int INDEX_DATA = 5;
    final int INDEX_SIZE = 6;
    final int INDEX_BUCKET_ID = 7;
    final int INDEX_BUCKET_DISPLAY_NAME = 8;
    final int INDEX_TITLE = 9;

    /** 图片 */
    static final String[] IMAGE_PROJECTION =  {
        MediaStore.Images.ImageColumns._ID,                 // 0
        MediaStore.Images.ImageColumns.MIME_TYPE,           // 1
        MediaStore.Images.ImageColumns.LATITUDE,            // 2
        MediaStore.Images.ImageColumns.LONGITUDE,           // 3
        MediaStore.Images.ImageColumns.DATE_TAKEN,          // 4
        MediaStore.Images.ImageColumns.DATA,                // 5
        MediaStore.Images.ImageColumns.SIZE,                // 6
        MediaStore.Images.ImageColumns.BUCKET_ID,           // 7
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, // 8
        MediaStore.Images.ImageColumns.TITLE                // 9
    };

    /** 所有照片过滤条件 */
    static final String FILTER_ALL_PHOTO_WHERE =
        MediaStore.Images.ImageColumns._ID + " > ? and ( " + // 安全模式下起作用, 默认-1
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? or " + // Camera
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? or " + // Video
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? or " + // Screenshorts
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? or " + // Bampoo Paper
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? or " + // Download
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? )";    // download

    /** 参考LocalAlbum mOrderClause */
    public static final String IMAGE_ORDER_CLAUSE_DESC = MediaStore.Images.ImageColumns.DATE_TAKEN.concat(ORDER_BY_DESC).concat(", ")
        + MediaStore.Images.ImageColumns._ID.concat(ORDER_BY_DESC);

    /** 所有照片过滤参数 */
    final static String[] FILTER_ALL_PHOTO_ARGS = new String[]{
        String.valueOf(-1),
        FOLDER_CAMERA,
        FOLDER_VIDEO,
        FOLDER_SCREENSHOTS,
        FOLDER_BAMBOO_PAPER,
        FOLDER_DOWNLOAD_UP,
        FOLDER_DOWNLOAD_DOWN};

    ///////////////////////////////////////////////////////////////////////////
    // 排序
    ///////////////////////////////////////////////////////////////////////////
    public static final Comparator<MediaItem> sDateTakenComparator =
        new DateTakenComparator();

    private static class DateTakenComparator implements Comparator<MediaItem> {
        @Override
        public int compare(MediaItem item1, MediaItem item2) {
//            return Long.compare(item1.getDateTaken(), item2.getDateTaken());
            return (item1.getDateTaken() > item2.getDateTaken()) ? -1 : ((item1.getDateTaken() == item2.getDateTaken()) ? 0 : 1);
        }
    }

    public SimpleMediaStoreDataLoader(Context context) {
        super(context);
    }

    public SimpleMediaStoreDataLoader(Context context, OrderBy orderBy) {
        this(context,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGE_PROJECTION,
            FILTER_ALL_PHOTO_WHERE,
            FILTER_ALL_PHOTO_ARGS,
            null == orderBy ? IMAGE_ORDER_CLAUSE_DESC  : MediaStore.Images.ImageColumns.DATE_TAKEN
                                                            .concat(" ")
                                                            .concat(orderBy.name()).concat(", ")
                                                            .concat(MediaStore.Images.ImageColumns._ID)
                                                            .concat(" ")
                                                            .concat(orderBy.name()));
    }

    public SimpleMediaStoreDataLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public List<MediaItem> queryOther(String orderBy, CancellationSignal cancellationSignal) {
        List<MediaItem> newData = new ArrayList<>();
        try {
            Cursor cursor = ContentResolverCompat.query(getContext().getContentResolver(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION,
                FILTER_ALL_PHOTO_WHERE,
                FILTER_ALL_PHOTO_ARGS,
                orderBy,
                cancellationSignal);
            if (cursor != null) {
                try {
                    // Ensure the cursor window is filled.
                    cursor.getCount();
                    cursor.registerContentObserver(mObserver);
                    while (cursor.moveToNext()) {
                        newData.add(parseDataByCursor(cursor));
                    }
                } catch (RuntimeException ex) {
                    cursor.close();
                    throw ex;
                }
            }
            return newData;
        } catch (Exception e) {
            LogUtil.e(TAG, TAG.concat("queryOther:"), e);
        }
        return null;
    }

    @Override
    public void resort(List<MediaItem> data) {
        Collections.sort(data, sDateTakenComparator);
    }

    @Override
    public MediaItem parseDataByCursor(Cursor cursor) {
        MediaItem mediaItem = new MediaItem();
        mediaItem.setId(cursor.getInt(INDEX_ID));
        mediaItem.setMimeType(cursor.getString(INDEX_MIME_TYPE));
        mediaItem.setLatitude(cursor.getDouble(INDEX_LATITUDE));
        mediaItem.setLongitude(cursor.getDouble(INDEX_LONGITUDE));
        mediaItem.setDateTaken(cursor.getLong(INDEX_DATE_TAKEN));
        mediaItem.setData(cursor.getString(INDEX_DATA));
        mediaItem.setSize(cursor.getInt(INDEX_SIZE));
        mediaItem.setBucktId(cursor.getInt(INDEX_BUCKET_ID));
        mediaItem.setBucketDisplayName(cursor.getString(INDEX_BUCKET_DISPLAY_NAME));
        mediaItem.setTitle(cursor.getString(INDEX_TITLE));
        return mediaItem;
    }

}
