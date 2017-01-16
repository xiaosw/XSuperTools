package com.xiaosw.app.recyclerview.base;

import android.support.v4.util.SparseArrayCompat;

/**
 * <p><br/>ClassName : {@link ItemViewDelegateManager}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-01-16 14:14:54</p>
 */
public class ItemViewDelegateManager<T> {

    /**
     * @see ItemViewDelegateManager#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-ItemViewDelegateManager";

    SparseArrayCompat<ItemViewDelegate<T>> mDelegates = new SparseArrayCompat();

    public int getItemViewDelegateCount() {
        return mDelegates.size();
    }

    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> delegate) {
        int viewType = mDelegates.size();
        if (delegate != null) {
            mDelegates.put(viewType, delegate);
        }
        return this;
    }

    public ItemViewDelegateManager<T> addDelegate(int viewType, ItemViewDelegate<T> delegate) {
        if (mDelegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                "An ItemViewDelegate is already registered for the viewType = "
                    + viewType
                    + ". Already registered ItemViewDelegate is "
                    + mDelegates.get(viewType));
        }
        mDelegates.put(viewType, delegate);
        return this;
    }

    public ItemViewDelegateManager<T> removeDelegate(ItemViewDelegate<T> delegate) {
        if (delegate == null)  {
            throw new NullPointerException("ItemViewDelegate is null");
        }
        int indexToRemove = mDelegates.indexOfValue(delegate);

        if (indexToRemove >= 0) {
            mDelegates.removeAt(indexToRemove);
        }
        return this;
    }

    public ItemViewDelegateManager<T> removeDelegate(int itemType) {
        int indexToRemove = mDelegates.indexOfKey(itemType);

        if (indexToRemove >= 0)  {
            mDelegates.removeAt(indexToRemove);
        }
        return this;
    }

    public int getItemViewType(T item, int position) {
        int delegatesCount = mDelegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewDelegate<T> delegate = mDelegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return mDelegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
            "No ItemViewDelegate added that matches position=" + position + " in data source");
    }

    public void bindData(ViewHolder holder, T item, int position) {
        int delegatesCount = mDelegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            ItemViewDelegate<T> delegate = mDelegates.valueAt(i);

            if (delegate.isForViewType(item, position)) {
                delegate.bindData(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
            "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }


    public ItemViewDelegate getItemViewDelegate(int viewType) {
        return mDelegates.get(viewType);
    }

    public int getItemViewLayoutId(int viewType) {
        return getItemViewDelegate(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(ItemViewDelegate itemViewDelegate) {
        return mDelegates.indexOfValue(itemViewDelegate);
    }

}
