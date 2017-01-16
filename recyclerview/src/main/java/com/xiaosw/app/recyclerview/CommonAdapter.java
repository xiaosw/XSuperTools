package com.xiaosw.app.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import com.xiaosw.app.recyclerview.base.ItemViewDelegate;
import com.xiaosw.app.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * <p><br/>ClassName : {@link CommonAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-01-16 15:15:33</p>
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    /**
     * @see CommonAdapter#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-CommonAdapter";

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position) {
                return true;
            }

            @Override
            public void bindData(ViewHolder holder, T t, int position) {
                CommonAdapter.this.bindData(holder, t, position);
            }
        });
    }

    protected abstract void bindData(ViewHolder holder, T t, int position);

}
