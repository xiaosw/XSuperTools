package com.xiaosw.tool.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.library.activity.fragment.BaseFragment;
import com.xiaosw.library.widget.adapter.BaseFragmentPagerAdapter;
import com.xiaosw.tool.R;

import java.util.ArrayList;

/**
 * @ClassName : {@link TabFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-13 11:11:54
 */
public class TabFragment extends BaseFragment {

    private static final String TAG = "TabFragment";
    public static final String KEY_BACKGROUND_COLOR = "background_color";
    public static final String KEY_TITLE = "title";

    private String mTitle;
    private Bundle mArgs;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mArgs = getArguments();
    }

    @Override
    public View createAttachView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, null);
    }

    @Override
    public void initByAttachView(View attachView) {
        super.initByAttachView(attachView);
        if (mArgs == null) {
            return;
        }
        if (mArgs.containsKey(KEY_BACKGROUND_COLOR)) {
            attachView.setBackgroundColor(mArgs.getInt(KEY_BACKGROUND_COLOR));
        }

        if (mArgs.containsKey(KEY_TITLE)) {
            mTitle = mArgs.getString(KEY_TITLE);
        }

        ViewPager viewPager = (ViewPager) attachView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new BaseFragmentPagerAdapter<BaseFragment>(getActivity().getSupportFragmentManager(), initFragments(4)));
    }

    private ArrayList<BaseFragment> initFragments(int count) {
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PagerFragment pagerFragment = new PagerFragment();
            Bundle args = new Bundle();
            args.putString(PagerFragment.KEY_DESCRIPTION, mTitle + "---" + i);
            pagerFragment.setArguments(args);
            fragments.add(pagerFragment);
        }
        return fragments;
    }

}
