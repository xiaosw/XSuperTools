package com.xiaosw.app.recyclerview.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

/**
 * <p><br/>ClassName : {@link WrapperUtils}
 * <br/>Description : 处理RecyclerView头
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-01-16 14:14:55</p>
 */
public class WrapperUtils {

    /**
     * @see WrapperUtils#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-WrapperUtils";

    public interface SpanSizeCallback {
        int getSpanSize(GridLayoutManager layoutManager , GridLayoutManager.SpanSizeLookup oldLookup, int position);
    }

    /**
     * 作用于 {@link GridLayoutManager}
     * @param innerAdapter
     * @param recyclerView
     * @param callback
     */
    public static void onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter,
                                                RecyclerView recyclerView,
                                                final SpanSizeCallback callback) {
        innerAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    /**
     * use {@link StaggeredGridLayoutManager}
     * @param holder
     */
    public static void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
            && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

}
