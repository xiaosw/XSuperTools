package com.xiaosw.library.bean;

/**
 * <p><br/>ClassName : {@link Banner}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-12-21 19:19:03</p>
 */
public class Banner {

    private String url;
    private String title;

    public Banner() {
    }

    public Banner(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Banner{" +
            "url='" + url + '\'' +
            ", title='" + title + '\'' +
            '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
