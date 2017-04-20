package io.sunc.mj.api.model;

/**
 * Description:
 * Date: 2017-04-12 16:22
 * Author: suncheng
 */
public class Music extends BaseBean {
    private String url;
    private long id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
