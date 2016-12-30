package com.xiaosw.tool.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.bean.BrokenLineGraph;
import com.xiaosw.library.bean.GraphData;
import com.xiaosw.library.widget.GUIBrokenLineGraphView;
import com.xiaosw.tool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * <p><br/>ClassName : {@link BrokenLineGraphActivity}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2016-12-30 20:20:28</p>
 */
public class BrokenLineGraphActivity extends BaseAppCompatActivity {

    @BindView(R.id.broken_line_graph)
    GUIBrokenLineGraphView broken_line_graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borken_graph);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle("BrokenLineGraphActivity");

        generateData();
        broken_line_graph.setBrokenLineGraphs(mBrokenLineGropDatas);
    }

    ///////////////////////////////////////////////////////////////////////////
    // gernerate data
    ///////////////////////////////////////////////////////////////////////////
    private List<String> mGroupData;
    private Map<Integer, List<String>> mChildData;

    private List<BrokenLineGraph> mBrokenLineGropDatas;
    private void generateData() {

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
//        mBrokenLineGropDatas.add(brokenLineGraph2);

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
//        mBrokenLineGropDatas.add(brokenLineGraph3);
    }

}
