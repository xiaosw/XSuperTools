package com.xiaosw.library.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName : {@link BaseFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-13 11:11:54
 */
public abstract class BaseFragment extends Fragment {

    private View mAttachView;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAttachView = createAttachView(inflater, container, savedInstanceState);
        initByAttachView(mAttachView);
        return mAttachView;
    }

    @Override
    public void onDestroyView() {
        mAttachView = null;
        super.onDestroyView();
    }

    /**
     * {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public abstract View createAttachView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public void initByAttachView(View attachView) {

    }

    public View getAttachView() {
        return mAttachView;
    }
}
