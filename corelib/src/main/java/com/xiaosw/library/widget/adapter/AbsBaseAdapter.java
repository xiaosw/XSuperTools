/**
 * @Project : SJBank
 */

package com.xiaosw.library.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @param <T>
 * @ClassName  : {@link AbsBaseAdapter}
 * @Description: 适配器基础类
 *
 * @date 2015-11-28下午7:47:43
 * @Author xiaoshiwang <xiaoshiwang@rytong.com>
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    /** 数据源 */
    protected List<T> mData;
    /** 上下文 */
    protected Context mContext;
    /** 当前view */
    private View mCurrentConvertView;

    /** 布局资源 */
    private int mLayoutRes;

    /**
     * 使用该构造方法必须使用  {@link #refresh(Collection)} 或者 {@link #append(Collection)} 方法来更新数据
     * @param context
     * @param layoutRes
     */
    public AbsBaseAdapter(Context context, int layoutRes) {
        this(context, new ArrayList<T>(), layoutRes);
    }

    public AbsBaseAdapter(Context context, List<T> data, int layoutRes) {
        mContext = context;
        mData = data;
        mLayoutRes = layoutRes;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 用新数据替换所有的旧数据
     * <p>NOTE:数据源的指向并没有改变, 只是将数据源的数据{@link List #clear()}再 {@link List #addAll(Collection)}
     * @param newData
     */
    public synchronized void refresh(Collection<? extends T> newData) {
        mData.clear();
        if(newData != null) {
            mData.addAll(newData);
        }
        notifyDataSetChanged();
    }

    /**
     * 直接将源数据替换，将新数据的指向设置给适配器
     * @param newData
     */
    public void replaceOriginData(List<T> newData) {
        mData = (List<T>) newData;
        notifyDataSetChanged();
    }


    /**
     * 在原有数据的基础上再添加数据
     * <p>NOTE:数据源的指向并没有改变，只是在原有数据源的基础上添加数据
     * @param appendData
     */
    public synchronized void append(Collection<? extends T> appendData) {
        if(appendData == null || appendData.isEmpty()) {
            return;
        }
        mData.addAll(appendData);
        notifyDataSetChanged();
    }

    /**
     * 添加一个数据
     * @param item
     */
    public synchronized void add(T item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutRes, parent, false);
        }
        mCurrentConvertView = convertView;
        bindData(convertView, position, getItem(position));
//        LogUtil.e(convertView.getClass().getSimpleName());
        return convertView;
    }

    abstract protected void bindData(View convertView, int position, T itemData);

    // ===========
    // some util method
    // ===========
    public void bindText(int textViewId, CharSequence value) {
        ((TextView)getViewFromHolder(textViewId)).setText(value);
    }

    public <K extends View> K getViewFromHolder(int viewId) {
        return ViewHolder.getView(mCurrentConvertView, viewId);
    }

}
