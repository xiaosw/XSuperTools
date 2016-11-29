package com.xiaosw.library.helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.xiaosw.library.utils.LogUtil;

/**
 * <p><br/>ClassName : {@link SimpleItemTouchHelperCallback}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-29 16:16:05</p>
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    /**
     * @see SimpleItemTouchHelperCallback#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-SimpleItemTouchHelperCallback";
    private OnItemHelperOperationListener mOnItemHelperOperationListener;

    public SimpleItemTouchHelperCallback(OnItemHelperOperationListener onItemHelperOperationListener) {
        mOnItemHelperOperationListener = onItemHelperOperationListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        LogUtil.i(TAG, "getMovementFlags()");
        // 0表示禁止该操作
        int dragFlags = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
//        LogUtil.i(TAG, "getMoveThreshold()");
        return super.getMoveThreshold(viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        LogUtil.i(TAG, "onSwiped() position = " + viewHolder.getAdapterPosition());
        if (null != mOnItemHelperOperationListener) {
            mOnItemHelperOperationListener.onItemRemove(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (null != mOnItemHelperOperationListener) {
            mOnItemHelperOperationListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        LogUtil.i(TAG, "onSwiped() from = " + viewHolder.getAdapterPosition() + ", to = " + target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (null != viewHolder
            && viewHolder instanceof OnItemTouchHelperViewHolder
            && actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ((OnItemTouchHelperViewHolder) viewHolder).onItemSelected();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (null != viewHolder
            && viewHolder instanceof OnItemTouchHelperViewHolder) {
            ((OnItemTouchHelperViewHolder) viewHolder).onItemClear();
        }
        super.clearView(recyclerView, viewHolder);
    }

//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            float width = (float) viewHolder.itemView.getWidth();
//            float alpha = 1.0f - Math.abs(dX) / width;
//            viewHolder.itemView.setAlpha(alpha);
//            viewHolder.itemView.setTranslationX(dX);
//        } else {
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
//                actionState, isCurrentlyActive);
//        }
//    }

    /**
     * {@link com.xiaosw.library.widget.adapter.BaseRecyclerAdapter}拖拽监听
     */
    public interface OnItemHelperOperationListener {
        /**
         * 拖拽
         * @see android.support.v7.widget.helper.ItemTouchHelper.Callback#onMove(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder)
         * @param fromPosition
         * @param toPosition
         */
        void onItemMove(int fromPosition, int toPosition);

        /**
         * 删除
         * @see android.support.v7.widget.helper.ItemTouchHelper.Callback#onSwiped(RecyclerView.ViewHolder, int)
         * @param position
         */
        void onItemRemove(int position);
    }

    public interface OnItemTouchHelperViewHolder {
        /**
         * Called when the {@link android.support.v7.widget.helper.ItemTouchHelper} first registers an
         * item as being moved or swiped.
         * Implementations should update the item view to indicate
         * it's active state.
         */
        void onItemSelected();
        /**
         * Called when the {@link android.support.v7.widget.helper.ItemTouchHelper} has completed the
         * move or swipe, and the active item state should be cleared.
         */
        void onItemClear();

    }
}
