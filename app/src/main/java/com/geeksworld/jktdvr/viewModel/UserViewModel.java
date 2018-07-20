package com.geeksworld.jktdvr.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.model.UserModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.HttpUtil;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by xhs on 2018/4/16.
 */

public class UserViewModel extends BaseViewModel {

    private String currentValidateCode;

    private SharedPreferences share;

    private UserModel currentUserModel;

    public UserModel getCurrentUserModel() {
        setValues();
        return currentUserModel;
    }
    private void setValues(){
        int u_id = share.getInt(ShareKey.UID,0);
        String pass = share.getString(ShareKey.PASS,"");
        String phone = share.getString(ShareKey.PHONE,"");
        String nick = share.getString(ShareKey.NICK,"");
        String email = share.getString(ShareKey.EMAIL,"");
        String company = share.getString(ShareKey.COMPANY,"");
        String img_url = share.getString(ShareKey.IMG_URL,"");
        int sex = share.getInt(ShareKey.SEX,0);
        int point = share.getInt(ShareKey.POINT,0);

        currentUserModel.setU_id(u_id);
        currentUserModel.setPassword(pass);
        currentUserModel.setPhone(phone);
        currentUserModel.setNick_name(nick);
        currentUserModel.setEmail(email);
        currentUserModel.setCompany(company);
        currentUserModel.setImg_url(img_url);
        currentUserModel.setSex(sex);
        currentUserModel.setPoint(point);
    }

    public String getCurrentValidateCode() {
        return currentValidateCode;
    }

    public SharedPreferences getShare() {
        return share;
    }

    public UserViewModel(Activity activity, Context context){
        super(activity,context);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        share = ShareKey.getShare(getActivity());
    }

    //请求验证码
    public void postRequestGetValidateCode(String phoneNumber,final OnRequestDataComplete<String> complete){
        HashMap<String, Object> map = new HashMap<>();
        map.put("phoneNumber", phoneNumber);

        HttpUtil.requestPostNetWork(Url.sendPhoneVerifyCode, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        String validateCode = obj.getJSONObject("result").getString("validateCode");
                        currentValidateCode = validateCode;
                        if(null != complete){
                            complete.success(validateCode);
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

    //请求用户注册
    public void postRequestRegisterUser(final UserModel userModel,final OnRequestDataComplete<UserModel> complete){
        HashMap<String, Object> map = new HashMap<>();
        map.put("phoneNumber", userModel.getPhone());
        map.put("password", userModel.getPassword());
        map.put("validateCode", userModel.getValidateCode());

        HttpUtil.requestPostNetWork(Url.registUser, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        JSONObject result1 = obj.getJSONObject("result");
                        String uid = result1.getString("u_id");
                        userModel.setU_id(Integer.parseInt(uid));
                        SharedPreferences.Editor edit = share.edit();
                        if (!Tool.isNull(uid)) {
                            edit.putInt(ShareKey.UID, Integer.valueOf(uid));
                            String deviceId = share.getString(ShareKey.DEVICEID, "");
                            if (!Tool.isNull(deviceId))
                                Common.offlinePush(getActivity(), Integer.valueOf(uid), deviceId);
                        }
                        edit.putString(ShareKey.PHONE, userModel.getPhone());
                        edit.putBoolean(ShareKey.ISLOGIN, true);
                        edit.commit();

                        if(null != complete){
                            currentUserModel = userModel;
                            complete.success(userModel);
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
    //请求用户登录
    public void postRequestUserLogin(final UserModel userModel,final OnRequestDataComplete<UserModel> complete) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phoneNumber", userModel.getPhone());
        map.put("password", userModel.getPassword());

        HttpUtil.requestPostNetWork(Url.login, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        JSONObject result1 = obj.getJSONObject("result");
                        String uid = result1.getString("u_id");

                        SharedPreferences.Editor edit = share.edit();

                        if (!Tool.isNull(uid)) {
                            edit.putInt(ShareKey.UID, Integer.valueOf(uid));
                            String deviceId = share.getString(ShareKey.DEVICEID, "");
                            if (!Tool.isNull(deviceId))
                                Common.offlinePush(getActivity(), Integer.valueOf(uid), deviceId);
                        }

                        edit.putString(ShareKey.PHONE, userModel.getPhone());
                        edit.putString(ShareKey.PASS, userModel.getPassword());
                        edit.putBoolean(ShareKey.ISLOGIN, true);
                        edit.commit();
                        if(null != complete){
                            currentUserModel = userModel;
                            complete.success(userModel);
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

    //请求获取用户信息
    public void postRequestUserInfo(final String uid,final OnRequestDataComplete<UserModel> complete) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("u_id", uid );
        HttpUtil.requestPostNetWork(Url.displayUserDetails, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        JSONObject result1 = obj.getJSONObject("result");
                        String user_name = result1.getString("user_name");
                        UserModel userModel = JSONArray.parseObject(result1.toString(), UserModel.class);
                        String jsonString = JSON.toJSONString(userModel, SerializerFeature.PrettyFormat,
                                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);

                        share.edit().putString(ShareKey.PHONE, user_name).commit();
                        share.edit().putString(ShareKey.NICK, userModel.getNick_name()).commit();
                        share.edit().putString(ShareKey.EMAIL, userModel.getEmail()).commit();
                        share.edit().putString(ShareKey.WORK_POSITION, userModel.getCompany()).commit();
                        share.edit().putInt(ShareKey.SEX, Integer.valueOf(userModel.getSex())).commit();
                        share.edit().putString(ShareKey.IMG_URL, userModel.getImg_url()).commit();
                        share.edit().putInt(ShareKey.POINT, Integer.valueOf(userModel.getPoint())).commit();
                        if(null != complete){
                            currentUserModel = userModel;
                            complete.success(userModel);
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
    //请求修改用户信息
    public void postRequestUpdateUserInfo(final UserModel userModel,final OnRequestDataComplete<UserModel> complete) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("u_id", "" + userModel.getU_id());
        map.put("nick_name", userModel.getNick_name());
        map.put("company", userModel.getCompany());
        map.put("sex", "" + userModel.getSex());
        map.put("email", userModel.getEmail());
        HttpUtil.requestPostNetWork(Url.completeUserDetails, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        JSONObject result1 = obj.getJSONObject("result");
                        String u_id = result1.getString("u_id");

                        SharedPreferences.Editor edit = share.edit();

                        if (!Tool.isNull(u_id)) {
                            edit.putInt(ShareKey.UID, Integer.valueOf(u_id));
                            String deviceId = share.getString(ShareKey.DEVICEID, "");
                            if (!Tool.isNull(deviceId))
                                Common.offlinePush(getActivity(), Integer.valueOf(u_id), deviceId);
                        }


                        edit.putString(ShareKey.NICK, userModel.getNick_name());
                        edit.putString(ShareKey.EMAIL, userModel.getEmail());
                        edit.putString(ShareKey.SEX, userModel.getSex()+"");
                        edit.putString(ShareKey.COMPANY, userModel.getCompany()+"");
                        edit.commit();
                        if(null != complete){
                            currentUserModel = userModel;
                            complete.success(userModel);
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
    //请求修改用户密码
    public void postRequestUpdateUserPass(final UserModel userModel,final OnRequestDataComplete<UserModel> complete) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phoneNumber", userModel.getPhone());
        map.put("password", userModel.getPassword());
        map.put("validateCode", userModel.getValidateCode());
        HttpUtil.requestPostNetWork(Url.forgetPwd, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    Tool.toast(getActivity(), message);
                    if (code == 1) {
                        if(null != complete){
                            currentUserModel = userModel;
                            complete.success(userModel);
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
