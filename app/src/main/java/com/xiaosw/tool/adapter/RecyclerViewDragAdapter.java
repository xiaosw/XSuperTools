package com.xiaosw.tool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaosw.library.helper.SimpleItemTouchHelperCallback;
import com.xiaosw.library.widget.adapter.BaseViewHolder;
import com.xiaosw.library.widget.adapter.SimpleItemTouchAdapter;
import com.xiaosw.tool.R;

import java.util.List;

import butterknife.BindView;

/**
 * <p><br/>ClassName : {@link RecyclerViewDragAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-29 15:15:52</p>
 */
public class RecyclerViewDragAdapter extends SimpleItemTouchAdapter<String, RecyclerViewDragAdapter.DragHolderView> {

    /**
     * @see RecyclerViewDragAdapter#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-RecyclerViewDragAdapter";

    public RecyclerViewDragAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public DragHolderView onCreateViewHolder() {
        return new DragHolderView(View.inflate(getContext(), R.layout.item_recycler_view_drag, null));
    }

    @Override
    public void onBindViewHolder(final DragHolderView holder, int position) {
        holder.tv_description.setText(getData().get(position));
        if (getOnStartDragListener() != null) {
            holder.iv_handle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                        return getOnStartDragListener().onStartDrag(holder);
                    }
                    return false;
                }
            });
        }
    }

    public class DragHolderView extends BaseViewHolder implements SimpleItemTouchHelperCallback.OnItemTouchHelperViewHolder {

        @BindView(R.id.tv_text)
        TextView tv_description;
        @BindView(R.id.iv_handle)
        ImageView iv_handle;
        public DragHolderView(View itemView) {
            super(itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

}
