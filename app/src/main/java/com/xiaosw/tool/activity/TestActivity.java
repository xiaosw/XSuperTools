package com.xiaosw.tool.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.bean.BrokenLineGraph;
import com.xiaosw.library.bean.GraphData;
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

        broken_line_graph.setBrokenLineGraphs(mBrokenLineGropDatas);
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

    private List<BrokenLineGraph> mBrokenLineGropDatas;
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

        BrokenLineGraph brokenLineGraph1 = new BrokenLineGraph(Color.RED, "一季度");
        mBrokenLineGropDatas = new ArrayList<>();
        List<GraphData> graphData = new ArrayList<>();
        graphData.add(new GraphData(10));
        graphData.add(new GraphData(30));
        graphData.add(new GraphData(40));
        graphData.add(new GraphData(60));
        graphData.add(new GraphData(20));
        graphData.add(new GraphData(80));
        graphData.add(new GraphData(90));
        graphData.add(new GraphData(100));
        graphData.add(new GraphData(88));
        graphData.add(new GraphData(112));
        graphData.add(new GraphData(100));
        graphData.add(new GraphData(99));
        graphData.add(new GraphData(130));
        brokenLineGraph1.setGraphDatas(graphData);
        mBrokenLineGropDatas.add(brokenLineGraph1);

        BrokenLineGraph brokenLineGraph2 = new BrokenLineGraph(Color.BLACK, "二季度收益");
        List<GraphData> graphData2 = new ArrayList<>();
        graphData2.add(new GraphData(90));
        graphData2.add(new GraphData(100));
        graphData2.add(new GraphData(80));
        graphData2.add(new GraphData(60));
        graphData2.add(new GraphData(50));
        graphData2.add(new GraphData(90));
        graphData2.add(new GraphData(20));
        graphData2.add(new GraphData(120));
        graphData2.add(new GraphData(99));
        graphData2.add(new GraphData(103));
        graphData2.add(new GraphData(150));
        graphData2.add(new GraphData(80));
        graphData2.add(new GraphData(100));
        brokenLineGraph2.setGraphDatas(graphData2);
        mBrokenLineGropDatas.add(brokenLineGraph2);

        BrokenLineGraph brokenLineGraph3 = new BrokenLineGraph(Color.BLUE, "三季度");
        List<GraphData> graphData3 = new ArrayList<>();
        graphData3.add(new GraphData(100));
        graphData3.add(new GraphData(80));
        graphData3.add(new GraphData(120));
        graphData3.add(new GraphData(90));
        graphData3.add(new GraphData(60));
        graphData3.add(new GraphData(40));
        graphData3.add(new GraphData(35));
        graphData3.add(new GraphData(70));
        graphData3.add(new GraphData(100));
        graphData3.add(new GraphData(140));
        graphData3.add(new GraphData(132));
        graphData3.add(new GraphData(150));
        graphData3.add(new GraphData(152));
        brokenLineGraph3.setGraphDatas(graphData3);
        mBrokenLineGropDatas.add(brokenLineGraph3);
    }

}
