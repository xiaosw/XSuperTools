package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.xiaosw.library.activity.BaseActivity;
import com.xiaosw.library.widget.BaseRecyclerView;
import com.xiaosw.library.widget.dialog.SuperToast;
import com.xiaosw.library.widget.divider.DividerItemDecoration;
import com.xiaosw.tool.R;
import com.xiaosw.tool.widget.adapter.FunctionListAdapter;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @ClassName {@link MainActivity}
 * @Description 功能列表
 *
 * @Date 2016-10-10 19:17.
 * @Author xiaoshiwang.
 */
public class FunctionListActivity extends BaseActivity implements AdapterView.OnItemClickListener,
    AdapterView.OnItemLongClickListener{

    @Bind(R.id.recycler_view)
    BaseRecyclerView mRecyclerView;
    private ArrayList<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setOnItemClickListener(this);
        mRecyclerView.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(new FunctionListAdapter(this, initData()));
    }

    public ArrayList<String> initData() {
        if (null == mData) {
            mData = new ArrayList<>();
        }
        mData.clear();
        for (int i = 0; i < 50; i++) {
            mData.add("功能描述" + i);
        }
        return mData;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SuperToast.show(this, "position = " + position, Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        SuperToast.show(this, "position = " + position, Toast.LENGTH_SHORT);
        return true;
    }
}
