package com.zoctan.solar.beans;

import java.io.Serializable;

/**
 * PostBean
 */
public class PostBean implements Serializable {

    private String id;
    private String user;
    private String user_img;
    private String title;
    private String digest;
    private String post_time;
    private String comments;

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getUser_img() {
        return user_img;
    }

    public String getTitle() {
        return title;
    }

    public String getDigest() {
        return digest;
    }

    public String getPost_time() {
        return post_time;
    }

    public String getComments() {
        return comments;
    }

    public void setId(String id){
        this.id=id;
    }

    public void setUser(String user){
        this.user=user;
    }

    public void setUser_img(String user_img){
        this.user_img=user_img;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setDigest(String digest){
        this.digest=digest;
    }

    public void setPost_time(String post_time){
        this.post_time=post_time;
    }

    public void setComments(String comments){
        this.comments=comments;
    }
}