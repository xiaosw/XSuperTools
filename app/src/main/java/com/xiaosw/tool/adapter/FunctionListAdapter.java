package com.xiaosw.tool.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xiaosw.library.widget.adapter.BaseRecyclerAdapter;
import com.xiaosw.library.widget.adapter.BaseViewHolder;
import com.xiaosw.tool.R;
import com.xiaosw.tool.bean.FunctionInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @ClassName : {@link FunctionListAdapter}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-11 10:10:42
 */
public class FunctionListAdapter extends BaseRecyclerAdapter<FunctionInfo, FunctionListAdapter.FunctionListViewHolder> {

    public FunctionListAdapter(Context context, List<FunctionInfo> data) {
        super(context, data);
    }

    @Override
    public FunctionListViewHolder onCreateViewHolder() {
        return new FunctionListViewHolder(View.inflate(getContext(), R.layout.item_function_list, null));
    }

    @Override
    public void onBindViewHolder(FunctionListViewHolder holder, int position) {
        FunctionInfo functionInfo = getObjectByPosition(position);
        holder.tv_function_description.setText(functionInfo.getTitle());
    }

    class FunctionListViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_function_description)
        TextView tv_function_description;

        public FunctionListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
