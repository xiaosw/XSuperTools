package com.xiaosw.datepicker.widget;

import android.content.Context;

import com.xiaosw.datepicker.R;

import java.util.List;

/**
 * <p><br/>ClassName : {@link DateAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-02-24 17:17:28</p>
 */
public class DateAdapter extends WheelViewAdapter.AbstractWheelTextAdapter {

    /**
     * @see DateAdapter#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-DateAdapter";

    private List<String> mDateDatas;

    public DateAdapter(Context context, List<String> dateDatas,
                       int currentItem) {
        this(context, dateDatas, currentItem, DEFAULT_MAX_TEXT_SIZE, DEFAULT_MIN_TEXT_SIZE);
    }

    public DateAdapter(Context context, List<String> dateDatas,
                                  int currentItem, int maxTextSize, int minTextSize) {
        super(context, R.layout.item_date, NO_RESOURCE, currentItem, maxTextSize, minTextSize);
        this.mDateDatas = dateDatas;
        setItemTextResource(R.id.tv_date);
    }

    @Override
    public int getItemsCount() {
        return mDateDatas.size();
    }

    @Override
    public CharSequence getItemText(int index) {
        return mDateDatas.get(index);
    }

}
