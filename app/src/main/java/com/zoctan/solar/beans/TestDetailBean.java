package com.zoctan.solar.beans;

import java.io.Serializable;

/**
 * 评测详情实体类TestDetailBean
 */
public class TestDetailBean implements Serializable {

    /**
     *
     * API返回值
     * {"1":{"content":"<p>xxxx</p>","id":"1","title":"xxxx"}}
     * 评测文章id:1
     * 标题:title
     * 内容:content
     *
     */

    // 评测文章id
    private String id;
    // 标题
    private String title;
    // 内容
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
