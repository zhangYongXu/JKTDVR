package com.geeksworld.jktdvr.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONArray;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.HttpUtil;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xhs on 2018/5/7.
 */

public class HomeViewModel extends BaseViewModel {
    private List<HomeTagModel> allTagList = new LinkedList<HomeTagModel>();

    public SharedPreferences share;

    public List<HomeTagModel> getAllTagList() {
        return allTagList;
    }

    public HomeViewModel(Activity activity, Context context){
        super(activity,context);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        share = ShareKey.getShare(getActivity());

    }
    private void addFirstTagModel(){
        HomeTagModel homeTagModel = getTagModelInstance();

        allTagList.add(homeTagModel);
    }
    public HomeTagModel getTagModelInstance(){
        HomeTagModel homeTagModel = new HomeTagModel();
        homeTagModel.setDataDicName("所有");
        homeTagModel.setDataDicValue("0");
        return homeTagModel;
    }
    //请求获取所有tag
    public void postRequestAllTagListData(final OnRequestDataComplete<List<HomeTagModel>> complete) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("dataDicValue", "worksClass" );
        String u_id = share.getInt(ShareKey.UID, 0)+"";
        map.put("u_id",u_id);
        HttpUtil.requestPostNetWork(Url.getHomeAllTag, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        String objArrayString = obj.getString("data");
                        JSONArray objArray = (JSONArray) JSONArray.parse(objArrayString);
                        List<HomeTagModel> datas = JSONArray.parseArray(Tool.parseJson(objArray), HomeTagModel.class);

                        if(null != datas){
                            allTagList.clear();
                            addFirstTagModel();
                            allTagList.addAll(datas);

                        }

                        if(null != complete){
                            complete.success(allTagList);
                        }
                    } else {
                        Tool.toast(getActivity(), message);
                        if(null != complete){
                            complete.failed(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(null != complete){
                        complete.failed(e.toString());
                    }
                }
            }

            @Override
            public void downfailed(String error) {
                if(null != complete){
                    complete.failed(error);
                }
            }
        });
    }

    //请求获取所有tag 类型的数据
    public void postRequestTagDataListData(final HomeTagModel homeTagModel,String searchTitle,int type, final OnRequestDataComplete<HomeTagModel> complete) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", homeTagModel.getHomeTagDataModel().getPageNumber()+"" );
        map.put("rows", homeTagModel.getHomeTagDataModel().getPageSize()+"");
        String u_id = share.getInt(ShareKey.UID, 0)+"";
        map.put("authorId",u_id);
        String title = searchTitle;
        if(Tool.isNull(title)){
            title = "";
        }
        map.put("type",type+"");
        map.put("title",title);
        map.put("classVal",homeTagModel.getDataDicValue());
        map.put("title",title);
        String url = Url.getHomeList;

        HttpUtil.requestPostNetWork(url, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        String objArrayString = obj.getString("data");
                        JSONArray objArray = (JSONArray) JSONArray.parse(objArrayString);
                        List<HomeItemModel> datas = JSONArray.parseArray(Tool.parseJson(objArray), HomeItemModel.class);

                        if(1 == homeTagModel.getHomeTagDataModel().getPageNumber()){
                            homeTagModel.getHomeTagDataModel().getDataList().clear();
                        }
                        if(null != datas){
                            homeTagModel.getHomeTagDataModel().getDataList().addAll(datas);
                        }

                        if(null != complete){
                            complete.success(homeTagModel);
                        }
                    } else {
                        Tool.toast(getActivity(), message);
                        if(null != complete){
                            complete.failed(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(null != complete){
                        complete.failed(e.toString());
                    }
                }
            }

            @Override
            public void downfailed(String error) {
                if(null != complete){
                    complete.failed(error);
                }
            }
        });
    }
}
