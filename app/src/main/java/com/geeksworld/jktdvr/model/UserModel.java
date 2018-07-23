package com.geeksworld.jktdvr.model;

import com.geeksworld.jktdvr.aBase.BaseModel;
import com.geeksworld.jktdvr.tools.Tool;

/**
 * Created by xhs on 2018/4/16.
 */

/*
* id（用户id）、
* user_name（用户名称）、
* sex（性别）
* /birth（出生）
* /tel（手机号）
* /email（邮箱号）
* /regist_time（注册时间）
* */

public class UserModel extends BaseModel {
    private int id;// "2",
    private String img_url;// "http://oy45jwxyl.bkt.clouddn.com/4",
    private String nick_name;// "李四",
    private String email;
    private String regist_time;
    private String phone;// "李四",
    private String tel;
    private String user_name;// "李四",
    private String birth;// "李四",
    private String password;
    private String sex;//

    private String validateCode;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getNick_name() {
        if(Tool.isNull(nick_name) && !Tool.isNull(user_name)){
            return getUser_name();
        }
        if(Tool.isNull(nick_name) && Tool.isNull(user_name)){
            return getPhone();
        }
        return nick_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setRegist_time(String regist_time) {
        this.regist_time = regist_time;
    }

    public String getRegist_time() {
        return regist_time;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        if(Tool.isNull(phone) && !Tool.isNull(tel)){
            return tel;
        }
        return phone;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBirth() {
        return birth;
    }
}
