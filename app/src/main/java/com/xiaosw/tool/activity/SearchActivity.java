package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.widget.SearchView;
import com.xiaosw.tool.R;

import butterknife.BindView;

public class SearchActivity  extends BaseAppCompatActivity implements SearchView.OnSearchListener {

    private static final String TAG = "SearchActivity";
    @BindView(R.id.id_search) SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
        mSearchView.setOnSearchListener(this);
        mSearchView.setAutoLinkMask(1);
        String [] arr={"aa","aab","aac"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr);
        mSearchView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onSearch(TextView v, int actionId) {
        return false;
    }
}
