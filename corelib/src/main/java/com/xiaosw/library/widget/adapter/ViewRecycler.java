package com.xiaosw.library.widget.adapter;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * <p><br/>ClassName : {@link ViewRecycler}
 * <br/>Description :视图复用器
 * <p> 用来将闲置的View记录起来，方便下次直接使用。 该类主要是用在 {@link android.support.v4.view.PagerAdapter} 中
 * <pre>
 *  public Object instantiateItem(ViewGroup container, int position) {
 *      View convertView = mViewPool.pop();
 *      if (convertView == null) {
 *          convertView = inflater.inflate(R.layout.item_autoskip_ad, container, false);
 *      }
 *      ...
 *      container.addView(convertView);
 *      return convertView;
 *  }
 *
 * public void destroyItem(ViewGroup container, int position, Object object) {
 *      if (object instanceof View) {
 *          container.removeView((View) object);
 *          mViewPool.add((View) object);
 *      }
 * }
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-21 19:19:12</p>
 */
public class ViewRecycler {

    /**
     * @see ViewRecycler#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-ViewRecycler";
    private ArrayList<WeakReference<View>> mRecycler;

    public ViewRecycler() {
        mRecycler = new ArrayList<WeakReference<View>>();
    }

    public ViewRecycler(int capacity) {
        mRecycler = new ArrayList<WeakReference<View>>(capacity);
    }

    /**
     * 将视图添加到复用器中，以便下次使用
     * @param view
     */
    public void add(View view) {
        mRecycler.add(new WeakReference<View>(view));
    }

    /**
     * 将复用器中的闲置View取出
     * @return
     */
    public View pop() {
        return mRecycler.isEmpty() ? null : mRecycler.remove(0).get();
    }

}
