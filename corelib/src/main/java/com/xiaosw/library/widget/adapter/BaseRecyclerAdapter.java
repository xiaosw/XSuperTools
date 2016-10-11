package com.xiaosw.library.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

/**
 * @ClassName : {@link BaseRecyclerAdapter}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 21:21:33
 */
public abstract class BaseRecyclerAdapter<T, BVH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<BVH> {
    private static final String TAG = "BaseRecyclerAdapter";
    private Context mContext;
    /** 数据源 */
    private List<T> mData;
    /** 与之绑定的view */
    private RecyclerView mRecyclerView;

    /** item点击事件 */
    private AdapterView.OnItemClickListener mOnItemClickListener;
    /** item长按事件 */
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public final BVH onCreateViewHolder(ViewGroup parent, int viewType) {
        mRecyclerView = (RecyclerView) parent;
        BVH viewHolder = onCreateViewHolder();
        if (viewHolder instanceof BaseViewHolder) {
            BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
            baseViewHolder.setOnItemClickListener(mOnItemClickListener);
            baseViewHolder.setOnItemLongClickListener(mOnItemLongClickListener);
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public abstract BVH onCreateViewHolder();

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public List<T> getData() {
        return mData;
    }

    public T getObjectByPosition(int position) {
        if (position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    public Context getContext() {
        return mContext;
    }

}
