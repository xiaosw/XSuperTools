package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.CompatToast;
import com.xiaosw.library.utils.SwipeCardCallback;
import com.xiaosw.library.utils.SwipeCardLayoutManager;
import com.xiaosw.library.widget.adapter.UniversalRecyclerAdaper;
import com.xiaosw.tool.R;
import com.xiaosw.tool.bean.SwipeCardBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @ClassName {@link SwipeCardActivity}
 * @Description 滑动卡片（类似探探效果）
 *
 * @Date 2016-10-10 19:19.
 * @Author xiaoshiwang.
 */
public class SwipeCardActivity extends BaseAppCompatActivity {

    /** @see SwipeCardActivity#getClass().getSimpleName() */
    private static final String TAG = "SwipeCardActivity";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<SwipeCardBean> mDatas;
    private UniversalRecyclerAdaper<SwipeCardBean> mUniversalRecyclerAdapter;
    private RequestManager mRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
        mRequestManager = Glide.with(this);

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new SwipeCardLayoutManager());
        mRecyclerView.setAdapter(mUniversalRecyclerAdapter =
            new UniversalRecyclerAdaper<SwipeCardBean>(this, mDatas = generateData(), R.layout.item_swipe_card) {

                @Override
                public void bindData(UniversalRecyclerAdaper.ViewHolder viewHolder, SwipeCardBean swipeCardBean, int position) {
                    viewHolder.bindText(R.id.tv_page_indicator, (swipeCardBean.getPosition() + 1  ) + "/" + mDatas.size());
                    viewHolder.bindText(R.id.tv_description, swipeCardBean.getDescription());
                    mRequestManager.load(swipeCardBean.getUrl()).placeholder(R.mipmap.ic_swipe_default).into((ImageView) viewHolder.findViewById(R.id.iv_icon));
                }
            });
        mUniversalRecyclerAdapter.setOnItemClickListener(new UniversalRecyclerAdaper.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CompatToast.makeText(SwipeCardActivity.this, mUniversalRecyclerAdapter.getData(position).getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
        new ItemTouchHelper(new SwipeCardCallback(mUniversalRecyclerAdapter)).attachToRecyclerView(mRecyclerView);

    }

    ///////////////////////////////////////////////////////////////////////////
    // test data
    ///////////////////////////////////////////////////////////////////////////
    private List<SwipeCardBean> generateData() {
        List<SwipeCardBean> datas = new ArrayList<>();
        int i = 0;
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201702/05/20170205220116_ihCHT.thumb.700_0.jpeg", "到此结束"));
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316142242_jUZNk.jpeg", "小清新"));
        datas.add(new SwipeCardBean(i++, "http://imgs.ebrun.com/resources/2016_03/2016_03_25/201603259771458878793312_origin.jpg", "瘦富美"));
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316142239_fSwjY.thumb.700_0.jpeg", "绿绿你懂的"));
        datas.add(new SwipeCardBean(i++, "http://p14.go007.com/2014_11_02_05/a03541088cce31b8_1.jpg", "description"));
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316143602_u5PeF.thumb.700_0.jpeg", "萌妹子"));
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316143412_KfiTX.jpeg", "xiaolb"));
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316143418_ivE8W.thumb.700_0.jpeg", "妹子"));
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316142211_iAcsM.thumb.700_0.jpeg", "白妹子"));
        datas.add(new SwipeCardBean(i++, "http://imgs.ebrun.com/resources/2016_04/2016_04_12/201604124411460430531500.jpg", "清纯"));
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316142230_VciA2.thumb.700_0.jpeg", "小青春"));
        datas.add(new SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316165211_tYvhR.thumb.700_0.jpeg", "白富美"));
        return datas;
    }
}
