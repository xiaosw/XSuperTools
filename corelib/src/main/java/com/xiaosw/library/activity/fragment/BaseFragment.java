package com.xiaosw.library.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.library.precenter.BasePercenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p><br/>ClassName : {@link BaseFragment}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-09 11:11:23</p>
 */
public abstract class BaseFragment<T extends BasePercenter> extends Fragment {
    private Unbinder mUnbinder;
    View mContentPannel;
    T mPercenter;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mContentPannel = doCreateView(inflater, container, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, mContentPannel);
        initByAttachView(mContentPannel);
        if (null != mPercenter) {
            mPercenter.onCreate();
        }
        return mContentPannel;
    }

    /**
     * {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View doCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initByAttachView(View contentPannel) {

    }

    public View getContentPannel() {
        return mContentPannel;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (null != mPercenter) {
            mPercenter.onCreate();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        if (null != mPercenter) {
            mPercenter.onStart();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        if (null != mPercenter) {
            mPercenter.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (null != mPercenter) {
            mPercenter.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (null != mPercenter) {
            mPercenter.cancel();
            mPercenter.onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (null != mPercenter) {
            mPercenter.onDestroy();
        }
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && null != mPercenter) {
            mPercenter.cancel();
        }
    }

    public T getPercenter() {
        return mPercenter;
    }

    public void setPercenter(T percenter) {
        mPercenter = percenter;
    }

    protected void setGone(View targetView) {
        if (null == targetView) {
            return;
        }
        if (targetView.getVisibility() != View.GONE) {
            targetView.setVisibility(View.GONE);
        }
    }

    protected void setVisible(View targetView) {
        if (null == targetView) {
            return;
        }
        if (targetView.getVisibility() != View.VISIBLE) {
            targetView.setVisibility(View.GONE);
        }
    }

    protected void setInvisible(View targetView) {
        if (null == targetView) {
            return;
        }
        if (targetView.getVisibility() != View.INVISIBLE) {
            targetView.setVisibility(View.INVISIBLE);
        }
    }
}
