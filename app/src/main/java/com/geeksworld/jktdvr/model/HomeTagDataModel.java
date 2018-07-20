package com.geeksworld.jktdvr.model;

import com.geeksworld.jktdvr.aBase.BaseModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xhs on 2018/5/7.
 */

public class HomeTagDataModel extends BaseModel implements Serializable{
    public static final String SerializableKey = "HomeTagDataModel";

    private int pageNumber = 1;
    private int pageSize = 10;
    private List<HomeItemModel> dataList = new LinkedList<HomeItemModel>();

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<HomeItemModel> getDataList() {
        return dataList;
    }
}
