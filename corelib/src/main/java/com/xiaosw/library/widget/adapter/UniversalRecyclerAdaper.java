package com.xiaosw.library.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p><br/>ClassName : {@link UniversalRecyclerAdaper}
 * <br/>Description : 通用Adapter
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-30 10:10:35</p>
 */

public abstract class UniversalRecyclerAdaper<T> extends RecyclerView.Adapter<UniversalRecyclerAdaper.ViewHolder> {

    private Context mContext;
    private List<T> mDatas;
    private int mItemLayout;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public UniversalRecyclerAdaper(Context context, List<T> data, int itemLayout) {
        this.mContext = context;
        this.mDatas = data;
        this.mItemLayout = itemLayout;
        this.mInflater = LayoutInflater.from(mContext);
        if (null == mDatas) {
            mDatas = new ArrayList<T>();
        }
    }

    @Override
    public UniversalRecyclerAdaper.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(mInflater.inflate(mItemLayout, null));
        if (null != mOnItemClickListener) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
                }
            });
        }

        if (null != mOnItemLongClickListener) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(v, viewHolder.getAdapterPosition());
                }
            });
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UniversalRecyclerAdaper.ViewHolder holder, int position) {
        bindData(holder, mDatas.get(position), position);
    }

    /**
     * @param viewHolder
     * @param t
     */
    public abstract void bindData(UniversalRecyclerAdaper.ViewHolder viewHolder, T t, int position);

    public List<T> getDatas() {
        return mDatas;
    }

    public T getData(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindText(int resId, CharSequence text) {
            ((TextView) findViewById(resId)).setText(text);
        }

        public <T extends View> T findViewById(int id) {
            SparseArray<View> holder = (SparseArray<View>) itemView.getTag();
            if(holder == null) {
                holder = new SparseArray<View>();
                itemView.setTag(holder);
            }

            View view = holder.get(id);
            if(view == null) {
                view = itemView.findViewById(id);
                holder.put(id, view);
            }
            return (T)view;
        }
    }

    public interface OnItemClickListener {
        void onItemClick( View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }
}
