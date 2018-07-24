package com.geeksworld.jktdvr.model;

import com.geeksworld.jktdvr.aBase.BaseModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xhs on 2018/3/30.
 */
/*
字段：id(作为本条内容的id)、
authorId：作者id，
authorName：作者名称、
title：标题、
classVal：分类id（要获取名称格式化，根据id去数据库取）、
imgUrl：图片地址、
videoUrl：视频地址、
videoTextarea：视频描述信息、
type：1图片 2
* */
public class HomeItemModel extends BaseModel implements Serializable{
    public static final String SerializableKey = "homeItemModel";

    public static final int HomeItemModelContentTypeAll = 0; //所有
    public static final int HomeItemModelContentTypePicture = 1; //图片
    public static final int HomeItemModelContentTypeVideo = 2;

    private int id;
    private int authorId;
    private String authorName;
    private String title;
    private int classVal;
    private String dataDicName;

    private int type;

    private String imgUrl;
    private String videoUrl;
    private String videoTextarea;



    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setClassVal(int classVal) {
        this.classVal = classVal;
    }

    public int getClassVal() {
        return classVal;
    }

    public void setDataDicName(String dataDicName) {
        this.dataDicName = dataDicName;
    }

    public String getDataDicName() {
        return dataDicName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoTextarea(String videoTextarea) {
        this.videoTextarea = videoTextarea;
    }

    public String getVideoTextarea() {
        return videoTextarea;
    }
}
