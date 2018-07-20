package com.geeksworld.jktdvr.model;

import com.geeksworld.jktdvr.aBase.BaseModel;

import java.io.Serializable;

/**
 * Created by xhs on 2018/5/7.
 */

/*
* {id,dataDicName(名称),dataDicValue(数据字典值)}
用于上传页面获取分类信息，上传信息保存需要的是分类信息的i
* */

public class HomeTagModel extends BaseModel implements Serializable{
    public static final String SerializableKey = "HomeTagModel";


    private int id;
    private String dataDicName;
    private String dataDicValue;
    private int pid;
    private String remark;
    private HomeTagDataModel homeTagDataModel;

    public HomeTagModel(){
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDataDicName(String dataDicName) {
        this.dataDicName = dataDicName;
    }

    public String getDataDicName() {
        return dataDicName;
    }

    public void setDataDicValue(String dataDicValue) {
        this.dataDicValue = dataDicValue;
    }

    public String getDataDicValue() {
        return dataDicValue;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public HomeTagDataModel getHomeTagDataModel() {
        if(null == this.homeTagDataModel){
            this.homeTagDataModel = new HomeTagDataModel();
        }
        return homeTagDataModel;
    }




}
