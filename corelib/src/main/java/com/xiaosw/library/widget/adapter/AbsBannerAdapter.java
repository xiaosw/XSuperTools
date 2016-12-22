package com.xiaosw.library.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * <p><br/>ClassName : {@link AbsBannerAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-21 19:19:02</p>
 */
public abstract class AbsBannerAdapter<T> extends BasePagerAdapter<T> {

    public static final int MIN_CHACHE_SIZE = 3;

    private LayoutInflater mInflater;
    private ViewRecycler mViewRecycler;
    private List<OnNotifyDataSetChangedListener> mOnNotifyDataSetChangedListeners;

    public AbsBannerAdapter(Context context) {
        this(context, new ArrayList<T>(), MIN_CHACHE_SIZE);
    }

    public AbsBannerAdapter(Context context, int chacheSize) {
        this(context, new ArrayList<T>(), chacheSize);
    }

    public AbsBannerAdapter(Context context, List<T> data) {
        this(context, data, MIN_CHACHE_SIZE);
    }

    public AbsBannerAdapter(Context context, List<T> data, int chacheSize) {
        super(context, data);
        mInflater = LayoutInflater.from(mContext);
        mViewRecycler = new ViewRecycler(Math.max(MIN_CHACHE_SIZE, chacheSize));
    }

    @Override
    public int getCount() {
        int realCount = getRealCount();
        return realCount > 1 ? Integer.MAX_VALUE : realCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = mViewRecycler.pop();
        if (convertView == null) {
            convertView = getTargetView(mInflater);
        }
        int realPosition = getRealPosition(position);
        bindData(convertView, getItemByPosition(realPosition), realPosition);
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            View childView = (View) object;
            container.removeView(childView);
            mViewRecycler.add(childView);
        }
    }

    /**
     * 获取需要展示的View
     * @param inflater
     * @return
     */
    protected abstract View getTargetView(LayoutInflater inflater);

    protected abstract void bindData(View convertView, T itemData, int position);

    /**
     * 根据当前position获取真实position
     * @param positon
     * @return position % getRealCout()
     */
    public int getRealPosition(int positon) {
        return positon % getRealCount();
    }

    public int getRealCount() {
        return mData.size();
    }

    public void addOnNotifyDataSetChangedListener(OnNotifyDataSetChangedListener listener) {
        if (mOnNotifyDataSetChangedListeners == null) {
            mOnNotifyDataSetChangedListeners = new ArrayList<>();
        }
        mOnNotifyDataSetChangedListeners.add(listener);
    }

    public void removeOnNotifyDataSetChangedListener(OnNotifyDataSetChangedListener listener) {
        if (mOnNotifyDataSetChangedListeners != null) {
            mOnNotifyDataSetChangedListeners.remove(listener);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mOnNotifyDataSetChangedListeners != null) {
            for (OnNotifyDataSetChangedListener onNotifyDataSetChangedListener : mOnNotifyDataSetChangedListeners) {
                onNotifyDataSetChangedListener.onNotifyDataSetChanged();
            }
        }
    }

    public interface OnNotifyDataSetChangedListener {
        void onNotifyDataSetChanged();
    }
}
