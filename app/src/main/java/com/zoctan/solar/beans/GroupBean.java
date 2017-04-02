package com.zoctan.solar.beans;

import java.io.Serializable;

/**
 * 类GroupBean
 */
public class GroupBean implements Serializable {

    // 分类id
    private String id;
    // 标题
    private String title;
    // 摘要
    private String brief;
    // 封面图片地址
    private String imgsrc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
}