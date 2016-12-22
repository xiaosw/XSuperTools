package com.xiaosw.library.widget.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * <p><br/>ClassName : {@link BasePagerAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-22 09:9:54</p>
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter implements IAdapter<T> {

    Context mContext;
    List<T> mData;

    public BasePagerAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return null == mData ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void append(T t) {
        mData.add(t);
        notifyDataSetChanged();
    }

    @Override
    public void appendAll(List<T> newData) {
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public void replace (int position, T newData) {
        mData.remove(position);
        mData.add(position, newData);
        notifyDataSetChanged();
    }

    @Override
    public void replaceAll(List<T> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public T getItemByPosition(int position) {
        return mData.get(position);
    }

    public Context getContext() {
        return mContext;
    }

    public List<T> getData() {
        return mData;
    }
}
