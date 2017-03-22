package com.zoctan.solar.beans;

import java.io.Serializable;
import java.util.List;

/**
 *  Post详情实体类PostDetailBean
 */

public class PostDetailBean implements Serializable {

    private String post_id;
    private String post_user;
    private String post_title;
    private String post_content;
    private String post_time;
    private String post_comments;

    public List<PostCommentBean> postCommentBeen;

    public String getPost_id() {
        return post_id;
    }
    public String getPost_user() {
        return post_user;
    }
    public String getPost_title() {
        return post_title;
    }
    public String getPost_content() {
        return post_content;
    }
    public String getPost_time() {
        return post_time;
    }
    public String getPost_comments() {
        return post_comments;
    }
    public void setPost_id(String post_id){
        this.post_id=post_id;
    }
    public void setPost_user(String post_user){
        this.post_user=post_user;
    }
    public void setPost_title(String post_title){
        this.post_title=post_title;
    }
    public void setPost_content(String post_content){
        this.post_content=post_content;
    }
    public void setPost_time(String post_time){
        this.post_time=post_time;
    }
    public void setPost_comments(String post_comments){
        this.post_comments=post_comments;
    }


}

