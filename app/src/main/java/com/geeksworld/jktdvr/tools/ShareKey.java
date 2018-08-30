package com.geeksworld.jktdvr.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xhs on 2017/10/25.
 */

public class ShareKey {

    public static SharedPreferences getShare(Context context) {
        return context.getSharedPreferences("EduBigData", Context.MODE_PRIVATE);
    }

    public static final String WX_ID = "wx7685ef5fca572bf4";
    public static final String WX_SECRET = "a3cee2bb4efc6e9fe1346c625d99e989";
    public static final String QQ_ID = "1106110659";
    public static final String HX_Appkey = "1444171124068855#kefuchannelapp49752";
    public static final String HX_Client_ID = "YXA6MzcooND0Eee1fV0zmX8NNQ";
    public static final String HX_Client_Secret = "YXA6LZYYUHgkgcjiQMxTKnLxsghVF7M";
    public static final String HX_IM = "kefuchannelimid_222955";
    public static final String HX_ZUZHI = "1444171124068855";
    public static final String HX_NAME = "kefuchannelapp49752";
    public static final String HX_ID = "70436";
    public static final String UPDATE = "update";

    public static final String UID = "u_id";
    public static final String DID = "d_id";
    public static final String HXID_MY = "hx_id";
    public static final String POINT = "point";
    public static final String PHONE = "phone";
    public static final String TEL = "Tel";
    public static final String PASS = "pass";
    public static final String NICK = "nick";
    public static final String EMAIL = "email";
    public static final String WORK_POSITION = "work_position";
    public static final String SEX = "sex";
    public static final String BIRTH = "birth";
    public static final String IMG_URL = "img_url";
    public static final String ISLOGIN = "isLogin";
    public static final String NONEWER = "newUser";
    public static final String DEVICEID = "deviceId";

    public static final String HIS_MAIN = "his_main";
    public static final String HIS_GUEST = "his_guest";
    public static final String HIS_PAGE1 = "his_page1";
    public static final String HIS_PAGE2 = "his_page2";
    public static final String HIS_PAGE3 = "his_page3";

    public static final String TODAY = "today";



    //是否使用测试数据
    public static final boolean isUseTestData = false;
}
