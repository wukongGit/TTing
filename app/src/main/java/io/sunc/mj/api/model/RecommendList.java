package io.sunc.mj.api.model;

/**
 * Description:
 * Date: 2017-04-12 16:22
 * Author: suncheng
 */
public class RecommendList extends BaseBean {
    private String name;
    private long id;
    private String coverImgUrl;
    private String playCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

}
