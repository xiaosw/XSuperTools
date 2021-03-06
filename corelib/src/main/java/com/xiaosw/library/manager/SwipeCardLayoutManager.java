package com.xiaosw.library.manager;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.library.widget.adapter.UniversalRecyclerAdaper;

import java.util.List;

/**
 * <p><br/>ClassName : {@link SwipeCardLayoutManager}
 * <br/>Description : 仿探探滑动卡片效果
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-30 17:17:49</p>
 */

public class SwipeCardLayoutManager extends RecyclerView.LayoutManager {

    /** @see SwipeCardLayoutManager#getClass().getSimpleName() */
    private static final String TAG = "SwipeCardLayoutManager";

    /** 最大显示数量 */
    public static final int MAX_VISIBLE_COUNT = 4;
    /** 每一级Scale相差0.05f，translationY相差7dp左右 */
    public static final float SCALE_GAP = 0.05f;
    /** 垂直移动距离 */
    public static final int TRANS_Y_GAP = 50;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        super.onLayoutChildren(recycler, state);
        int itemCount = getItemCount();
        if (itemCount < 1) {
            return;
        }
        detachAndScrapAttachedViews(recycler);
        // 计算显示的最底层所在位置
        int bottomPosition = itemCount > MAX_VISIBLE_COUNT ? itemCount - MAX_VISIBLE_COUNT : 0;
        for (int position = bottomPosition; position < itemCount; position++) {
            View child = recycler.getViewForPosition(position);

            // 测量child尺寸
            measureChild(child, 0, 0);
            addView(child);

            // 计算剧中位置
            int childWidth = getDecoratedMeasuredWidth(child);
            int childHeight = getDecoratedMeasuredHeight(child);
            int left = (getWidth() - childWidth) / 2;
            int top = (getHeight() - childHeight) / 2;
            Log.e(TAG, "onLayoutChildren: " + childWidth + ", " + childHeight);
            // 摆放子view
            layoutDecorated(child, left, top, left + childWidth, top + childHeight);

            int level = itemCount - position - 1;
            if (level > 0) {
                child.setScaleX(1 - level * SCALE_GAP);
                if (level < MAX_VISIBLE_COUNT - 1) {
                    child.setScaleY(1 - level * SCALE_GAP);
                    child.setTranslationY(level * TRANS_Y_GAP);
                } else {
                    child.setScaleY(1 - (level - 1) * SCALE_GAP);
                    child.setTranslationY((level - 1) * TRANS_Y_GAP);
                }
            }
        }
    }

    /**
     * <p><br/>ClassName : {@link SwipeCardCallback}
     * <br/>Description : 定义滑动相关
     * <br/>
     * <br/>Author : xiaosw<xiaosw0802@163.com>
     * <br/>Create date : 2017-03-30 18:18:57</p>
     */

    public static class SwipeCardCallback extends ItemTouchHelper.SimpleCallback {

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
}
