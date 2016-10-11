package com.xiaosw.tool.bean;

import android.content.Intent;

/**
 * @ClassName : {@link FunctionInfo}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-11 14:14:34
 */
public class FunctionInfo {

    private String title;
    private Intent intent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
