package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.CompatToast;
import com.xiaosw.library.utils.HanziToPinyin;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.library.widget.GUIBrokenLineGraphView;
import com.xiaosw.library.widget.GUIHorizontalLetterView;
import com.xiaosw.tool.R;
import com.xiaosw.tool.adapter.SimpleExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class TestActivity extends BaseAppCompatActivity implements GUIHorizontalLetterView.OnLatterIndexChangeListener {
    
    private static final String TAG = "TestActivity";

    @BindView(R.id.view_horizontal_letter)
    GUIHorizontalLetterView view_horizontal_letter;

    @BindView(R.id.expandable_list_view)
    ExpandableListView expandable_list_view;

    @BindView(R.id.broken_line_graph)
    GUIBrokenLineGraphView broken_line_graph;

    private SimpleExpandableAdapter mSimpleExpandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);

        generateData();
        view_horizontal_letter.setOnLatterIndexChangeListener(this);
        mSimpleExpandableAdapter = new SimpleExpandableAdapter(this, mGroupData, mChildData);
        expandable_list_view.setAdapter(mSimpleExpandableAdapter);
        expandable_list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CompatToast.makeText(getBaseContext(), "" + mSimpleExpandableAdapter.getChild(groupPosition, childPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        broken_line_graph.setData(mPointDatas);
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void onLetterChanged(String letter, int position) {
        expandable_list_view.setSelectedGroup(position );
    }

    ///////////////////////////////////////////////////////////////////////////
    // gernerate data
    ///////////////////////////////////////////////////////////////////////////
    private List<String> mGroupData;
    private Map<Integer, List<String>> mChildData;

    private List<GUIBrokenLineGraphView.BaseData> mPointDatas;
    private void generateData() {
        mGroupData = new ArrayList<String>();
        for (int i = 0; i < GUIHorizontalLetterView.LETTERS.length; i++) {
            mGroupData.add(GUIHorizontalLetterView.LETTERS[i]);
        }

        List<List<String>> childs = new ArrayList<>();
        List<String> child = new ArrayList<String>();
        for (int i = 0; i < mGroupData.size(); i++) {
            child.add(GUIHorizontalLetterView.LETTERS[i]);
            childs.add(child);
        }

        mChildData = new HashMap<Integer, List<String>>();
        for (int i = 0; i < childs.size(); i++) {
            mChildData.put(i, childs.get(i));
        }
        LogUtil.e(TAG, "长沙 = " + HanziToPinyin.getInstance().getPinyin("长沙", HanziToPinyin.PinyinType.WHOLE_PUT_TOGETHER));
        LogUtil.e(TAG, "Appaple = " + HanziToPinyin.getPinyin("Appaple", HanziToPinyin.PinyinType.FIRST_HANZI_FIRST_LETTER));
        LogUtil.e(TAG, "B啊 = " + HanziToPinyin.getPinyin("啊B"));

        mPointDatas = new ArrayList<>();
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(10));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(30));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(40));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(60));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(20));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(80));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(90));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(100));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(88));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(112));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(100));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(99));
        mPointDatas.add(new GUIBrokenLineGraphView.BaseData(130));
    }

}
