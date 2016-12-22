package com.xiaosw.library.widget.adapter;

import java.util.List;

/**
 * <p><br/>ClassName : {@link IAdapter}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-22 10:10:06</p>
 */
public interface IAdapter<T> {

    /**
     * 拼接数据
     * @param t
     */
    void append(T t);

    /**
     * 拼接数据
     * @param newData
     */
    void appendAll(List<T> newData);

    /**
     * 替换单个数据
     * @param position 替换位置
     * @param newData 新数据
     */
    void replace(int position, T newData);

    /**
     * 替换数据
     * @param newData
     */
    void replaceAll(List<T> newData);

    /**
     * 清空数据
     */
    void clear();

    /**
     * 获取对应数据
     * @param position
     * @return
     */
    T getItemByPosition(int position);

}
