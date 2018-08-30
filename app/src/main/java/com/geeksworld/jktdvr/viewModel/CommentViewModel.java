package com.geeksworld.jktdvr.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONArray;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.model.CommentItemModel;
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
 * Created by xhs on 2018/8/28.
 */

public class CommentViewModel extends BaseViewModel {
    private List<CommentItemModel> dataList = new LinkedList<CommentItemModel>();

    public SharedPreferences share;

    public List<CommentItemModel> getDataList() {
        return dataList;
    }

    public CommentViewModel(Activity activity, Context context){
        super(activity,context);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        share = ShareKey.getShare(getActivity());

    }

    //请求获取评论
    public void postRequestCommentListData(HomeItemModel homeItemModel, final OnRequestDataComplete<List<CommentItemModel>> complete ) {
        HashMap<String, Object> map = new HashMap<>();
        String imgId = homeItemModel.getId()+"";
        map.put("imgId",imgId);
        HttpUtil.requestPostNetWork(Url.getCommentList, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        String objArrayString = obj.getString("data");
                        JSONArray objArray = (JSONArray) JSONArray.parse(objArrayString);
                        List<CommentItemModel> datas = JSONArray.parseArray(Tool.parseJson(objArray), CommentItemModel.class);

                        if(null != datas){
                            getDataList().clear();
                            getDataList().addAll(datas);
                        }

                        if(null != complete){
                            complete.success(getDataList());
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
    //发布评论
    public void postRequestPublishVRComentWork(final CommentItemModel commentItemModel, final OnRequestDataComplete<CommentItemModel> complete ) {
        HashMap<String, Object> map = new HashMap<>();
        String u_id = share.getInt(ShareKey.UID, 0)+"";
        String user_name = share.getString(ShareKey.NICK,"");
        map.put("userId", u_id );
        map.put("userName", user_name );
        map.put("imgId", commentItemModel.getImgId()+"" );
        map.put("content", commentItemModel.getContent() );

        HttpUtil.requestPostNetWork(Url.commentSaveComment, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        if(null != complete){
                            complete.success(commentItemModel);
                        }
                        Tool.toast(getActivity(), message);
                    }
                    else {
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
