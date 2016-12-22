package com.xiaosw.tool.activity;

import android.os.Bundle;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.bean.Banner;
import com.xiaosw.library.widget.GUIBannerViewPager;
import com.xiaosw.tool.R;
import com.xiaosw.tool.adapter.BannerPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BannerActivity extends BaseAppCompatActivity {

    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
                    "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
                    "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
                    "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
                    "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};

    @BindView(R.id.view_pager_banner)
    GUIBannerViewPager view_pager_banner;

    private List<Banner> mData;
    private BannerPagerAdapter mBannerPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle("BannerActivity");

        generateData();
        mBannerPagerAdapter = new BannerPagerAdapter(this, mData);
        mBannerPagerAdapter.addOnNotifyDataSetChangedListener(view_pager_banner);
        view_pager_banner.setAdapter(mBannerPagerAdapter);
    }

    ///////////////////////////////////////////////////////////////////////////
    // generate test data
    ///////////////////////////////////////////////////////////////////////////
    private void generateData() {
        mData = new ArrayList<>();
        int size = imageUrls.length;
        for (int i = 0; i < size; i++) {
            mData.add(new Banner(imageUrls[i], "title " + i));
        }
    }
}
