package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.helper.SimpleItemTouchHelperCallback;
import com.xiaosw.library.widget.GUIBaseRecyclerView;
import com.xiaosw.library.widget.adapter.SimpleItemTouchAdapter;
import com.xiaosw.tool.R;
import com.xiaosw.tool.adapter.RecyclerViewDragAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecyclerViewDragActivity extends BaseAppCompatActivity implements SimpleItemTouchAdapter.OnStartDragListener {

    /** @see RecyclerViewDragActivity#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-RecyclerViewDragActivity";

    @BindView(R.id.drag_recycler_view)
    GUIBaseRecyclerView mBaseRecyclerView;

    private RecyclerViewDragAdapter mRecyclerViewDragAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_drag);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle("RecyclerViewDragActivity");
        mBaseRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerViewDragAdapter = new RecyclerViewDragAdapter(this, initData());
        mBaseRecyclerView.setAdapter(mRecyclerViewDragAdapter);
        mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mRecyclerViewDragAdapter));
        mItemTouchHelper.attachToRecyclerView(mBaseRecyclerView);
        mRecyclerViewDragAdapter.setOnStartDragListener(this);
    }

    @NonNull
    private List<String> initData() {
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < 30; i++) {
            list.add("item------------> " + i);
        }
        return list;
    }

    @Override
    public boolean onStartDrag(RecyclerView.ViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
        return true;
    }
}
