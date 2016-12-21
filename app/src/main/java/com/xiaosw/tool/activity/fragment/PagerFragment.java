package com.xiaosw.tool.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaosw.library.activity.fragment.BaseFragment;
import com.xiaosw.tool.R;

/**
 * @ClassName : {@link PagerFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-15 20:20:44
 */
public class PagerFragment extends BaseFragment {

    public static final String KEY_DESCRIPTION = "DESCRIPTION";

    @Override
    public View doCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_fragment_pager, null);
    }

    @Override
    public void initByAttachView(View attachView) {
        super.initByAttachView(attachView);
        Bundle args = getArguments();
        if (null != args) {
            if (args.containsKey(KEY_DESCRIPTION)) {
                ((TextView) attachView.findViewById(R.id.tv_page_description)).setText(args.getString(KEY_DESCRIPTION));
            }
        }

    }
}
