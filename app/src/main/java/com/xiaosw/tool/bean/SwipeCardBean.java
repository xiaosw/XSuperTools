package com.xiaosw.tool.bean;

/**
 * <p><br/>ClassName : {@link SwipeCardBean}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-30 11:11:17</p>
 */

public class SwipeCardBean {

    private int position;
    private String url;
    private String description;

    public SwipeCardBean() {
    }

    public SwipeCardBean(int position, String url, String description) {
        this.position = position;
        this.url = url;
        this.description = description;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SwipeCardBean{" +
            "position=" + position +
            ", url='" + url + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
