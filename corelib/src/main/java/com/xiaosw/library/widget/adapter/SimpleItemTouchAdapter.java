package com.xiaosw.library.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xiaosw.library.helper.SimpleItemTouchHelperCallback;

import java.util.Collections;
import java.util.List;

/**
 * <p><br/>ClassName : {@link SimpleItemTouchAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-29 16:16:41</p>
 */
public abstract class SimpleItemTouchAdapter<T, BVH extends RecyclerView.ViewHolder> extends BaseRecyclerAdapter<T, BVH>
    implements SimpleItemTouchHelperCallback.OnItemHelperOperationListener {

    private OnStartDragListener mOnStartDragListener;

    public SimpleItemTouchAdapter(Context context, List<T> data) {
        super(context, data);
    }

    /**
     * @see SimpleItemTouchAdapter#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-SimpleItemTouchAdapter";

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(getData(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemove(int position) {
        getData().remove(position);
        notifyItemRemoved(position);
    }

    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {
        mOnStartDragListener = onStartDragListener;
    }

    public OnStartDragListener getOnStartDragListener() {
        return mOnStartDragListener;
    }

    /**
     * View触发拖拽
     */
    public interface OnStartDragListener {
        public boolean onStartDrag(RecyclerView.ViewHolder holder);
    }
}
