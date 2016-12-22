package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.widget.GUISearchView;
import com.xiaosw.tool.R;

import butterknife.BindView;

public class SearchActivity  extends BaseAppCompatActivity implements GUISearchView.OnSearchListener {

    private static final String TAG = "SearchActivity";
    @BindView(R.id.id_search)
    GUISearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
        mSearchView.setOnSearchListener(this);
        String [] arr={"aa","aab","aac"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr);
        mSearchView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onSearch(TextView v, int actionId) {
        return false;
    }
}
