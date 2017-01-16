package com.xiaosw.app.recyclerview.base;

/**
 * <p><br/>ClassName : {@link ItemViewDelegate}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-01-16 14:14:54</p>
 */

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void bindData(ViewHolder holder, T t, int position);
}
