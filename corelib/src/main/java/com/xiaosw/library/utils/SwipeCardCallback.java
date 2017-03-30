package com.xiaosw.library.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.xiaosw.library.widget.adapter.UniversalRecyclerAdaper;

import java.util.List;

/**
 * <p><br/>ClassName : {@link SwipeCardCallback}
 * <br/>Description : 定义滑动相关
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-30 18:18:57</p>
 */

public class SwipeCardCallback extends ItemTouchHelper.SimpleCallback {
    
    /** @see SwipeCardCallback#getClass().getSimpleName() */
    private static final String TAG = "SwipeCardCallback";

    private UniversalRecyclerAdaper mAdapter;
    private List mDatas;

    public SwipeCardCallback(UniversalRecyclerAdaper adapter) {
        super(0, ItemTouchHelper.LEFT
            | ItemTouchHelper.UP
            | ItemTouchHelper.RIGHT
            | ItemTouchHelper.DOWN);
        mAdapter = adapter;
        mDatas = mAdapter.getDatas();
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mDatas.add(0, mDatas.remove(viewHolder.getLayoutPosition()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        int childCount = recyclerView.getChildCount();
        if (childCount < 1) {
            return;
        }
        double dist = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
        double fraction = Math.min(dist / getThreshold(recyclerView, viewHolder), 1);
        for (int i = 0; i < childCount; i++) {
            int level = childCount - i - 1;
            if (level > 0) {
                View child = recyclerView.getChildAt(i);
                child.setScaleX((float) (1 - (level - fraction) * SwipeCardLayoutManager.SCALE_GAP));
                if (level < SwipeCardLayoutManager.MAX_VISIBLE_COUNT - 1) {
                    child.setScaleY((float) (1 - (level - fraction) * SwipeCardLayoutManager.SCALE_GAP));
                    child.setTranslationY((float) ((level - fraction) * SwipeCardLayoutManager.TRANS_Y_GAP));
                }
            }
        }
    }

    private float getThreshold(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return recyclerView.getWidth() * getSwipeThreshold(viewHolder);
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return .5f;
    }
}
