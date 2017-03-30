package com.xiaosw.library.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
}
