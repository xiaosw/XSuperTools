package com.xiaosw.library.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.xiaosw.library.utils.LogUtil;

import butterknife.ButterKnife;

/**
 * @ClassName : {@link BaseViewHolder}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-11 11:11:29
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnLongClickListener {

    private static final String TAG = "BaseViewHolder";

    /** item点击事件 */
    private AdapterView.OnItemClickListener mOnItemClickListener;
    /** item长按事件 */
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onClick(View v) {
        LogUtil.d(TAG, "onClick()");
        if (null != mOnItemClickListener) {
            mOnItemClickListener.onItemClick(null, v, getAdapterPosition(), itemView.getId());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        LogUtil.d(TAG, "onLongClick()");
        if (null != mOnItemLongClickListener) {
            return mOnItemLongClickListener.onItemLongClick(null, v, getAdapterPosition(), itemView.getId());
        }
        return false;
    }

}
