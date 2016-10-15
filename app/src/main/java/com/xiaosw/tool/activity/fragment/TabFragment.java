package com.xiaosw.tool.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaosw.library.activity.fragment.BaseFragment;
import com.xiaosw.tool.R;

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
    private Bundle mArgs;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mArgs = getArguments();
    }

    @Override
    public View createAttachView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab1, null);
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
            ((TextView) attachView.findViewById(R.id.tv_title)).setText(mArgs.getString(KEY_TITLE));
        }
    }
}
