package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;
import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.tool.BuildTinkerApplication;
import com.xiaosw.tool.R;

/**
 * <p><br/>ClassName : {@link BuildTinkerApplication}
 * <br/>Description : 测试Tinker使用
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-01 11:11:24</p>
 */
public class TestTinkerActivity extends BaseAppCompatActivity {

    /** @see TestTinkerActivity#getClass().getSimpleName() */
    private static final String TAG = "TestTinkerActivity";
    private String mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tinker);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
    }

    public void doOnClick(View view) {
        Log.e(TAG, "doOnClick: -----------------------> " + view.getId());
        switch (view.getId()) {
            case R.id.bt_crash:
                LogUtil.e(TAG, "doOnClick: ---------------> crash()");
                if (mText.equals("")) {

                }
                Toast.makeText(this, "HotFix!!!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_hot_fix:
                LogUtil.e(TAG, "doOnClick: ---------------> hotFix()");
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                Log.e(TAG, " --------------------------> hotFixSuccess!!!");
                break;

            case R.id.bt_clear_patch:
                LogUtil.e(TAG, "doOnClick: ---------------> clearPatch()");
                Tinker.with(getApplicationContext()).cleanPatch();
                break;

            case R.id.bt_kill_self:
                LogUtil.e(TAG, "doOnClick: ---------------> killSelf()");
                ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            
            default:
                // TODO: 2017/3/1
                Log.e(TAG, "doOnClick: ---------------> unknown");
        }
        
    }
}
