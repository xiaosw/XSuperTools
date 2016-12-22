package com.xiaosw.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.AdapterView;

import com.xiaosw.library.widget.adapter.BaseRecyclerAdapter;

/**
 * @ClassName : {@link GUIBaseRecyclerView}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-11 13:13:13
 */
public class GUIBaseRecyclerView extends RecyclerView {

    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    public GUIBaseRecyclerView(Context context) {
        this(context, null);
    }

    public GUIBaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GUIBaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof BaseRecyclerAdapter) {
            BaseRecyclerAdapter baseRecyclerAdapter = (BaseRecyclerAdapter) adapter;
            baseRecyclerAdapter.setOnItemClickListener(mOnItemClickListener);
            baseRecyclerAdapter.setOnItemLongClickListener(mOnItemLongClickListener);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        if (getAdapter() != null && getAdapter() instanceof BaseRecyclerAdapter) {
            ((BaseRecyclerAdapter) getAdapter()).setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
        if (getAdapter() != null && getAdapter() instanceof BaseRecyclerAdapter) {
            ((BaseRecyclerAdapter) getAdapter()).setOnItemLongClickListener(onItemLongClickListener);
        }
    }
}
