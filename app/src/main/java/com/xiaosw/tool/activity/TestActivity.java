package com.xiaosw.tool.activity;

import android.graphics.Color;
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

        broken_line_graph.setLineDatas(mPointDatas, mLineDescriptions);
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

    private List<List<GUIBrokenLineGraphView.LineData>> mPointDatas;
    private List<GUIBrokenLineGraphView.LineDescription> mLineDescriptions;
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

        mPointDatas = new ArrayList<List<GUIBrokenLineGraphView.LineData>>();
        mLineDescriptions = new ArrayList<GUIBrokenLineGraphView.LineDescription>();
        List<GUIBrokenLineGraphView.LineData> ponits1 = new ArrayList<>();
        ponits1.add(new GUIBrokenLineGraphView.LineData(10));
        ponits1.add(new GUIBrokenLineGraphView.LineData(30));
        ponits1.add(new GUIBrokenLineGraphView.LineData(40));
        ponits1.add(new GUIBrokenLineGraphView.LineData(60));
        ponits1.add(new GUIBrokenLineGraphView.LineData(20));
        ponits1.add(new GUIBrokenLineGraphView.LineData(80));
        ponits1.add(new GUIBrokenLineGraphView.LineData(90));
        ponits1.add(new GUIBrokenLineGraphView.LineData(100));
        ponits1.add(new GUIBrokenLineGraphView.LineData(88));
        ponits1.add(new GUIBrokenLineGraphView.LineData(112));
        ponits1.add(new GUIBrokenLineGraphView.LineData(100));
        ponits1.add(new GUIBrokenLineGraphView.LineData(99));
        ponits1.add(new GUIBrokenLineGraphView.LineData(130));
        mPointDatas.add(ponits1);
        mLineDescriptions.add(new GUIBrokenLineGraphView.LineDescription(Color.RED, "一季度"));


        List<GUIBrokenLineGraphView.LineData> ponits2 = new ArrayList<>();
        ponits2.add(new GUIBrokenLineGraphView.LineData(90));
        ponits2.add(new GUIBrokenLineGraphView.LineData(100));
        ponits2.add(new GUIBrokenLineGraphView.LineData(80));
        ponits2.add(new GUIBrokenLineGraphView.LineData(60));
        ponits2.add(new GUIBrokenLineGraphView.LineData(50));
        ponits2.add(new GUIBrokenLineGraphView.LineData(90));
        ponits2.add(new GUIBrokenLineGraphView.LineData(20));
        ponits2.add(new GUIBrokenLineGraphView.LineData(120));
        ponits2.add(new GUIBrokenLineGraphView.LineData(99));
        ponits2.add(new GUIBrokenLineGraphView.LineData(103));
        ponits2.add(new GUIBrokenLineGraphView.LineData(150));
        ponits2.add(new GUIBrokenLineGraphView.LineData(80));
        ponits2.add(new GUIBrokenLineGraphView.LineData(100));
        mPointDatas.add(ponits2);
        mLineDescriptions.add(new GUIBrokenLineGraphView.LineDescription(Color.BLACK, "二季度收益"));

        List<GUIBrokenLineGraphView.LineData> ponits3 = new ArrayList<>();
        ponits3.add(new GUIBrokenLineGraphView.LineData(100));
        ponits3.add(new GUIBrokenLineGraphView.LineData(80));
        ponits3.add(new GUIBrokenLineGraphView.LineData(120));
        ponits3.add(new GUIBrokenLineGraphView.LineData(90));
        ponits3.add(new GUIBrokenLineGraphView.LineData(60));
        ponits3.add(new GUIBrokenLineGraphView.LineData(40));
        ponits3.add(new GUIBrokenLineGraphView.LineData(35));
        ponits3.add(new GUIBrokenLineGraphView.LineData(70));
        ponits3.add(new GUIBrokenLineGraphView.LineData(100));
        ponits3.add(new GUIBrokenLineGraphView.LineData(140));
        ponits3.add(new GUIBrokenLineGraphView.LineData(132));
        ponits3.add(new GUIBrokenLineGraphView.LineData(150));
        ponits3.add(new GUIBrokenLineGraphView.LineData(152));
        mPointDatas.add(ponits3);
        mLineDescriptions.add(new GUIBrokenLineGraphView.LineDescription(Color.BLUE, "三季度"));
    }

}
