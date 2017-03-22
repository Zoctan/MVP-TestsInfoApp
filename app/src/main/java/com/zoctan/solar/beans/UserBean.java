package com.zoctan.solar.beans;

/**
 * 用户实体类UserBean
 */
public class UserBean {

    // 操作状态
    private  String status;
    // 用户id
    private String id;
    // 用户名
    private String name;
    // 密码
    private String password;
    // 头像链接
    private String imgsrc;
    // 加入小组id
    private String group_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
