package com.xiaosw.library.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.xiaosw.library.R;

/**
 * @ClassName {@link MPermissionBaseActivity}
 * @Description FragmentTabHost基类
 *
 * @Date 2016-10-10 19:21.
 * @Author xiaoshiwang.
 */
public abstract class FragmentTabHostActivity extends BaseAppCompatActivity {

    private FragmentTabHost mFragmentTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_fragment_tab_host, null);
        super.setContentView(rootView);
        mFragmentTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        setupTabView();
    }

    private void setupTabView() {
        mFragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mFragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
//                LogUtil.e("onTabChanged-----------------------------> " + tabId);
            }
        });
    }

    public void addTab(View tab, Class<? extends Fragment> fragmentClass, String tag) {
        addTab(tab, fragmentClass, tag, null);
    }

    public void addTab(View tab, Class<? extends Fragment> fragmentClass, String tag, Bundle args) {
        TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(null == tag ? "" : tag).setIndicator(tab);
        mFragmentTabHost.addTab(tabSpec, fragmentClass, args);
    }

    public FragmentTabHost getFragmentTabHost() {
        return mFragmentTabHost;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

}
