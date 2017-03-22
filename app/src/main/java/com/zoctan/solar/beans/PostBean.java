package com.zoctan.solar.beans;

import java.io.Serializable;

/**
 * PostBean
 */
public class PostBean implements Serializable {

    /**
     * API返回值
     * not define
     */


    // 分类id
    private String id;
    // User
    private String user;
    // 标题
    private String title;
    // 摘要
    private String digest;
    // post time
    private String post_time;
    // comment
    private String comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return this.user;
    }
    public void setUser(String user) {
        this.user = user;
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

    public String getPost_time() {
        return post_time;
    }
    public void setPost_time(String time) {
        post_time=time;
    }
    public String getComments(){
        return comments;
    }
    public void setComments(String comments){
        this.comments=comments;
    }
}