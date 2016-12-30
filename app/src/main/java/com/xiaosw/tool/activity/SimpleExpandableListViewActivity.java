package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.CompatToast;
import com.xiaosw.library.utils.HanziToPinyin;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.library.widget.GUIHorizontalLetterView;
import com.xiaosw.tool.R;
import com.xiaosw.tool.adapter.SimpleExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class SimpleExpandableListViewActivity extends BaseAppCompatActivity implements GUIHorizontalLetterView.OnLetterIndexChangeListener {
    
    private static final String TAG = "SimpleExpandableListViewActivity";

    @BindView(R.id.view_horizontal_letter)
    GUIHorizontalLetterView view_horizontal_letter;

    @BindView(R.id.expandable_list_view)
    ExpandableListView expandable_list_view;

    private SimpleExpandableAdapter mSimpleExpandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);

        generateData();
        view_horizontal_letter.setOnLetterIndexChangeListener(this);
        mSimpleExpandableAdapter = new SimpleExpandableAdapter(this, mGroupData, mChildData);
        expandable_list_view.setAdapter(mSimpleExpandableAdapter);
        expandable_list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CompatToast.makeText(getBaseContext(), "" + mSimpleExpandableAdapter.getChild(groupPosition, childPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public boolean onLetterChanged(String letter, int position) {
        try {
            int groupCount = mSimpleExpandableAdapter.getGroupCount();
            String wordGroup;
            for (int i = 0; i < groupCount; i++) {
                wordGroup = (String) mSimpleExpandableAdapter.getGroup(i);
                if (wordGroup.contains(letter)) {
                    expandable_list_view.setSelectedGroup(i);
                    return true; // lettter indicator 在group中
                }
            }

            // 没有对应单词 找相邻
            // 向上找
            if (findAdjacentLetter(letter, position, groupCount, true)) { // 向上找到相邻
                return true;
            }
            // 向下找
            return findAdjacentLetter(letter, position, groupCount, false);
        } catch (Exception e) {
            Log.e(TAG, "onTouch: ", e);
        }
        return true;
    }

    /**
     *
     * @param letter find letter
     * @param position press position
     * @param groupCount group size
     * @param isUp 向上查找
     * @return
     */
    private boolean findAdjacentLetter(String letter, int position, int groupCount, boolean isUp) {
        if (isUp) {
            for (int i = position; i >= 0 ; i--) {
                if (findLetter(groupCount, GUIHorizontalLetterView.LETTERS[i])) {
                    return true;
                }
            }
        } else {
            for (int i = position; i < GUIHorizontalLetterView.LETTERS.length ; i++) {
                if (findLetter(groupCount, GUIHorizontalLetterView.LETTERS[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 匹配Group中对应position
     * @param groupCount
     * @param findLetter
     * @return
     */
    private boolean findLetter(int groupCount, String findLetter) {
        String wordGroup;
        for (int j = groupCount - 1; j >= 0; j--) {
            wordGroup = (String) mSimpleExpandableAdapter.getGroup(j);
            if (wordGroup.contains(findLetter)) {
                expandable_list_view.setSelectedGroup(j);
                return true;
            }
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // gernerate data
    ///////////////////////////////////////////////////////////////////////////
    private List<String> mGroupData;
    private Map<Integer, List<String>> mChildData;
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
    }

}
