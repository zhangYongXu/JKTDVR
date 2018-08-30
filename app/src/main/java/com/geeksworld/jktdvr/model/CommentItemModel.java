package com.geeksworld.jktdvr.model;

import com.geeksworld.jktdvr.aBase.BaseModel;

import java.io.Serializable;

/**
 * Created by xhs on 2018/8/28.
 */
/*
* id（评论id）、userId（用户id）、userName（用户名称）/imgId（图片id）/content（评论内容）/createTime（评论时间）
* */
public class CommentItemModel extends BaseModel implements Serializable {
    public static final String SerializableKey = "CommentItemModel";

    private int id;
    private int userId;
    private String userName;
    private int imgId;
    private String content;
    private String createTime;
    private String headImg;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }


    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHeadImg() {
        return headImg;
    }
}
