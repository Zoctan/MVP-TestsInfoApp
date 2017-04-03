package com.zoctan.solar.beans;

import java.io.Serializable;

/**
 * 评测实体类TestBean
 */
public class TestBean implements Serializable {

    /**
     *
     * API返回值
     * {"1":[{"id":"xx","title":"xxx","digest":"xxxx","imgsrc":"http://www.xx.com/xxx.jpg"}]}
     * 分类:1(手机)
     * 评测文章:id
     * 标题:title
     * 摘要:digest
     * 封面图片链接:imgsrc
     *
     */

    // 分类id
    private String id;
    // 标题
    private String title;
    // 摘要
    private String digest;
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

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
}
