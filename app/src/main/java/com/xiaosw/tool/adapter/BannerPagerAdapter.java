package com.xiaosw.tool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaosw.library.bean.Banner;
import com.xiaosw.library.widget.adapter.AbsBannerAdapter;
import com.xiaosw.library.widget.adapter.ViewHolder;
import com.xiaosw.tool.R;

import java.util.List;

/**
 * <p><br/>ClassName : {@link BannerPagerAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-22 10:10:51</p>
 */
public class BannerPagerAdapter extends AbsBannerAdapter<Banner> {

    /**
     * @see BannerPagerAdapter#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-BannerPagerAdapter";

    public BannerPagerAdapter(Context context) {
        super(context);
    }

    public BannerPagerAdapter(Context context, int chacheSize) {
        super(context, chacheSize);
    }

    public BannerPagerAdapter(Context context, List<Banner> data) {
        super(context, data);
    }

    public BannerPagerAdapter(Context context, List<Banner> data, int chacheSize) {
        super(context, data, chacheSize);
    }

    @Override
    protected View getTargetView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.item_banner, null);
    }

    @Override
    protected void bindData(View convertView, Banner itemData, int position) {
        ImageView iv_banner = ViewHolder.getView(convertView, R.id.iv_banner);
        Glide.with(getContext())
            .load(getItemByPosition(position).getUrl())
            .into(iv_banner);

        TextView tv_title = ViewHolder.getView(convertView, R.id.tv_title);
        tv_title.setText(itemData.getTitle());
    }
}
