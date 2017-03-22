package com.zoctan.solar.beans;

/**
 * Created by root on 3/15/17.
 */

public class PostCommentBean {
    private String comment_id;
    private String comment_user;
    private String comment_content;
    private String comment_time;

    public String getComment_id() {
        return comment_id;
    }
    public String getComment_user() {
        return comment_user;
    }
    public String getComment_content() {
        return comment_content;
    }
    public String getComment_time() {
        return comment_time;
    }
    public void setComment_id(String comment_id){
        this.comment_id=comment_id;
    }
    public void setComment_user(String comment_user){
        this.comment_user=comment_user;
    }
    public void setComment_content(String comment_content){
        this.comment_content=comment_content;
    }
    public void setComment_time(String comment_time){
        this.comment_time=comment_time;
    }
}
