package com.xiaosw.library.bean;

import java.util.List;

/**
 * <p><br/>ClassName : {@link BrokenLineGraph}
 * <br/>Description : 折线图数据
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-28 14:14:47</p>
 */
public class BrokenLineGraph<T extends GraphData> {

    /** 折线图颜色 */
    private int mColor;
    /** 折线图描述 */
    private String mDescription = "";
    /** 折线图数据源 */
    private List<T> mGraphDatas;

    public BrokenLineGraph(int color, String description) {
        this.mColor = color;
        this.mDescription = description;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public List<T> getGraphDatas() {
        return mGraphDatas;
    }

    public void setGraphDatas(List<T> graphDatas) {
        mGraphDatas = graphDatas;
    }

}
