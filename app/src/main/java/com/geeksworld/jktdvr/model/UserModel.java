package com.geeksworld.jktdvr.model;

import com.geeksworld.jktdvr.aBase.BaseModel;

/**
 * Created by xhs on 2018/4/16.
 */

public class UserModel extends BaseModel {
    private int u_id;// "2",
    private String img_url;// "http://oy45jwxyl.bkt.clouddn.com/4",
    private String nick_name;// "李四",
    private String email;
    private String company;
    private String phone;// "李四",
    private String user_name;// "李四",
    private String password;
    private int sex;// "2",//性别  1为男性，2为女性
    private int point;
    private int d_id;
    private String validateCode;

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getU_id() {
        return u_id;
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
        return nick_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
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

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public void setD_id(int d_id) {
        this.d_id = d_id;
    }

    public int getD_id() {
        return d_id;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getValidateCode() {
        return validateCode;
    }
}
