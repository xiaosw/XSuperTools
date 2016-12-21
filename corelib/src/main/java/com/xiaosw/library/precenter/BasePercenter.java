package com.xiaosw.library.precenter;

import android.content.Context;

import com.xiaosw.library.biz.IBaseBiz;

import java.io.Serializable;

/**
 * <p><br/>ClassName : {@link BasePercenter}
 * <br/>Description : Percenter
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-12 17:17:47</p>
 */
public abstract class BasePercenter<V, M extends IBaseBiz> implements Serializable {

    Context mContext;
    /** View */
    V v;
    /** Model */
    M m;

    public BasePercenter(Context context, V v, M m) {
        this.mContext = context;
        this.v = v;
        this.m = m;
    }

    public void cancel(){
        if (m != null) {
            m.cancel();
        }
    }

    public void onCreate(){}

    public void onStart(){};

    public void onRestart(){}

    public void onResume(){}

    public void onPause(){}

    public void onStop(){}

    public void onDestroy(){}

    public Context getContext() {
        return mContext;
    }

    public V getV() {
        return v;
    }

    public M getM() {
        return m;
    }
}
