package com.xiaosw.tool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaosw.tool.R;

import java.util.List;
import java.util.Map;

/**
 * <p><br/>ClassName : {@link SimpleExpandableAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-23 16:16:58</p>
 */
public class SimpleExpandableAdapter extends BaseExpandableListAdapter {

    /**
     * @see SimpleExpandableAdapter#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-SimpleExpandableAdapter";

    private Context mContext;
    private List<String> mGroupData;
    //子项是一个map，key是group的id，每一个group对应一个ChildItem的list
    private Map<Integer, List<String>> mChildMap;
    private LayoutInflater mInflater;
    private ExpandableListView mExpandableListView;

    public SimpleExpandableAdapter(Context context, List<String> groupData, Map<Integer, List<String>> childMap) {
        mContext = context;
        this.mGroupData = groupData;
        mChildMap = childMap;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        return mGroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildMap.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildMap.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        GroupHolder holder;
        View view = convertView;
        if (null == mExpandableListView
            && null != parent
            && parent instanceof ExpandableListView) {
            mExpandableListView = (ExpandableListView) parent;
        }
        if (view == null) {
            view = mInflater.inflate(R.layout.item_group, null);
            holder = new GroupHolder();
            holder.groupText = (TextView) view.findViewById(R.id.tv_group_title);
            holder.groupImg = (ImageView) view.findViewById(R.id.iv_arrow);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
        holder.groupText.setText(mGroupData.get(groupPosition));
        holder.groupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mExpandableListView) {
                    if (mExpandableListView.isGroupExpanded(groupPosition)) {
                        mExpandableListView.collapseGroup(groupPosition);
                    } else {
                        mExpandableListView.expandGroup(groupPosition);
                    }
                }
            }
        });
        //判断是否已经打开列表
        if(isExpanded){
            holder.groupImg.setImageResource(R.mipmap.expander_close_holo_dark);
        }else{
            holder.groupImg.setImageResource(R.mipmap.expander_open_holo_dark);
        }


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_group_sub, null);
            holder = new ChildHolder();
            holder.childText = (TextView) view.findViewById(R.id.tv_group_sub_title);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        holder.childText.setText(mChildMap.get(groupPosition).get(childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * show the text on the child and group item
     */
    private class GroupHolder {
        ImageView groupImg;
        TextView groupText;
    }
    private class ChildHolder {
        TextView childText;
    }
}
