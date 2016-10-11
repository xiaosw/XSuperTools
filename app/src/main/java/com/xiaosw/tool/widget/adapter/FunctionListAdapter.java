package com.xiaosw.tool.widget.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xiaosw.library.widget.adapter.BaseRecyclerAdapter;
import com.xiaosw.library.widget.adapter.BaseViewHolder;
import com.xiaosw.tool.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @ClassName : {@link FunctionListAdapter}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-11 10:10:42
 */
public class FunctionListAdapter extends BaseRecyclerAdapter<String, FunctionListAdapter.FunctionListViewHolder> {

    public FunctionListAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public FunctionListViewHolder onCreateViewHolder() {
        return new FunctionListViewHolder(View.inflate(getContext(), R.layout.item_function_list, null));
    }

    @Override
    public void onBindViewHolder(FunctionListViewHolder holder, int position) {
        String functionDescription = getDataByPosition(position);
        holder.tv_function_description.setText(functionDescription);
    }

    class FunctionListViewHolder extends BaseViewHolder {

        @Bind(R.id.tv_function_description)
        TextView tv_function_description;

        public FunctionListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
