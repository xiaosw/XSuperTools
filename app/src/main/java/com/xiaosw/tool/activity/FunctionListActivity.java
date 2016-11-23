package com.xiaosw.tool.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.widget.BaseRecyclerView;
import com.xiaosw.library.widget.dialog.SuperToast;
import com.xiaosw.library.widget.divider.DividerItemDecoration;
import com.xiaosw.tool.R;
import com.xiaosw.tool.bean.FunctionInfo;
import com.xiaosw.tool.widget.adapter.FunctionListAdapter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * @ClassName {@link MainActivity}
 * @Description 功能列表
 *
 * @Date 2016-10-10 19:17.
 * @Author xiaoshiwang.
 */
public class FunctionListActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener,
    AdapterView.OnItemLongClickListener{

    private static final String TAG = "FunctionListActivity";

    @BindView(R.id.recycler_view)
    BaseRecyclerView mRecyclerView;

    private FunctionListAdapter mFunctionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useCustomActionBar();
        setTitle(TAG);
        setContentView(R.layout.activity_function_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setOnItemClickListener(this);
        mRecyclerView.setOnItemLongClickListener(this);
        mFunctionListAdapter = new FunctionListAdapter(this, getData());
        mRecyclerView.setAdapter(mFunctionListAdapter);
    }

    /**
     * 参考ApiDemos
     * @return
     */
    public ArrayList<FunctionInfo> getData() {
        ArrayList<FunctionInfo> datas = new ArrayList<FunctionInfo>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
        if (null == list) {
            return datas;
        }
        for (ResolveInfo info: list) {
            FunctionInfo functionInfo = new FunctionInfo();
            CharSequence labelSeq = info.loadLabel(pm);
            String label = TextUtils.isEmpty(labelSeq) ? info.activityInfo.name : labelSeq.toString();
            functionInfo.setTitle(label);

            Intent functionIntent = new Intent();
            functionIntent.setClass(this, FunctionListActivity.class);
            functionInfo.setIntent(activityIntent(info.activityInfo.packageName, info.activityInfo.name));
            datas.add(functionInfo);
        }
        Collections.sort(datas, mDisplayNameComparator);
        return datas;
    }

    private Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    private final static Comparator<FunctionInfo> mDisplayNameComparator = new Comparator<FunctionInfo>() {

        private final Collator mCollator = Collator.getInstance();

        @Override
        public int compare(FunctionInfo o1, FunctionInfo o2) {
            return mCollator.compare(o1.getTitle(), o2.getTitle());
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(mFunctionListAdapter.getObjectByPosition(position).getIntent());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        SuperToast.show(this, "position = " + position, Toast.LENGTH_SHORT);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                SuperToast.show(this, "click : " + item.getTitle(), Toast.LENGTH_SHORT);
                return true;

            default:
                // TODO: 2016/10/11
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishByAnim() {
        finish();
    }
}
