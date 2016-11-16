package android.support.v4.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.os.CancellationSignal;
import android.support.v4.os.OperationCanceledException;
import android.util.Log;

import com.xiaosw.library.utils.LogUtil;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p><br/>ClassName : {@link AbsListLoader}
 * <br/>Description : 加载数据集合
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-16 15:15:56</p>
 */
public abstract class AbsListLoader<T> extends AsyncTaskLoader<List<T>> {

    /** getClass().getSimpleName() */
    private static final String TAG = "xiaosw-AbsListLoader";
    protected final ForceLoadContentObserver mObserver;

    Uri mUri;
    String[] mProjection;
    String mSelection;
    String[] mSelectionArgs;
    String mSortOrder;

    List<T> mData;
    CancellationSignal mCancellationSignal;

    /* Runs on a worker thread */
    @Override
    public List<T> loadInBackground() {
        mData = null;
        LogUtil.d(TAG, "loadInBackground()");
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            mCancellationSignal = new CancellationSignal();
        }
        try {
            Cursor cursor = ContentResolverCompat.query(getContext().getContentResolver(),
                mUri, mProjection, mSelection, mSelectionArgs, mSortOrder,
                mCancellationSignal);
            if (cursor != null) {
                mData = new ArrayList<>();
                try {
                    // Ensure the cursor window is filled.
                    cursor.getCount();
                    cursor.registerContentObserver(mObserver);
                    while (cursor.moveToNext()) {
                        mData.add(parseDataByCursor(cursor));
                    }
                    List<T> otherResult = queryOther(mSortOrder, mCancellationSignal);
                    if (null != otherResult && otherResult.size() > 0) {
                        mData.addAll(otherResult);
                        resort(mData);
                    }
                } catch (RuntimeException ex) {
                    cursor.close();
                    throw ex;
                }
            }
            return mData;
        } finally {
            synchronized (this) {
                mCancellationSignal = null;
            }
        }
    }

    @Override
    public void cancelLoadInBackground() {
        LogUtil.d(TAG, "cancelLoadInBackground()");
        super.cancelLoadInBackground();

        synchronized (this) {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(List<T> data) {
        LogUtil.d(TAG, "deliverResult() data = " + data);
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (data != null) {
                data = null;
            }
            return;
        }
        List<T> oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            oldData.clear();
            oldData = null;
        }
    }

    /**
     * Creates an empty unspecified SimpleCursorLoader.  You must follow this with
     * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc
     * to specify the query to perform.
     */
    public AbsListLoader(Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
    }

    /**
     * Creates a fully-specified SimpleCursorLoader.  See
     * {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)
     * ContentResolver.query()} for documentation on the meaning of the
     * parameters.  These will be passed as-is to that call.
     */
    public AbsListLoader(Context context, Uri uri, String[] projection, String selection,
                         String[] selectionArgs, String sortOrder) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mUri = uri;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p/>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        LogUtil.d(TAG, "onStartLoading() mData = " + mData);
        if (mData != null) {
            deliverResult(mData);
        }
        if (takeContentChanged() || mData == null || mData.size() == 0) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        LogUtil.d(TAG, "onStopLoading()");
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(List<T> data) {
        LogUtil.d(TAG, "onCanceled()");
        if (data != null) {
            data.clear();
            data = null;
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        LogUtil.d(TAG, "onReset()");
        // Ensure the loader is stopped
        onStopLoading();

        if (mData != null) {
            mData.clear();
        }
        mData = null;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public String[] getProjection() {
        return mProjection;
    }

    public void setProjection(String[] projection) {
        mProjection = projection;
    }

    public String getSelection() {
        return mSelection;
    }

    public void setSelection(String selection) {
        mSelection = selection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        mSelectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(String sortOrder) {
        mSortOrder = sortOrder;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        Log.d(TAG, "dump: dump");
        writer.print(prefix);
        writer.print("mUri=");
        writer.println(mUri);
        writer.print(prefix);
        writer.print("mProjection=");
        writer.println(Arrays.toString(mProjection));
        writer.print(prefix);
        writer.print("mSelection=");
        writer.println(mSelection);
        writer.print(prefix);
        writer.print("mSelectionArgs=");
        writer.println(Arrays.toString(mSelectionArgs));
        writer.print(prefix);
        writer.print("mSortOrder=");
        writer.println(mSortOrder);
        writer.print(prefix);
        writer.print("mData=");
        writer.println(mData);
        writer.print(prefix);
        writer.print("mContentChanged=");
        writer.println(mContentChanged);
    }

    /**
     * 查询(not UI thread)
     * @return
     */
    public abstract List<T> queryOther(String orderBy, CancellationSignal cancellationSignal);

    /**
     * 重新排序，如果{@link AbsListLoader#queryOther(String, CancellationSignal)}返回有数据
     * @param data
     */
    public void resort(List<T> data) {}

    /**
     * 根据Cursor解析对应数据
     * @param cursor
     * @return
     */
    public abstract T parseDataByCursor(Cursor cursor);

    public enum OrderBy {
        ASC,
        DESC
    }
}