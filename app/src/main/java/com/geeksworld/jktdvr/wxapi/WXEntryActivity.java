package com.geeksworld.jktdvr.wxapi;

import com.umeng.weixin.callback.WXCallbackActivity;

//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.modelmsg.SendAuth;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
/**
 * Created by xhs on 2017/9/15.
 */

public class WXEntryActivity extends WXCallbackActivity {

}

/*
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private SharedPreferences share;

    private final int REQUEST_PAY = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share = ShareKey.getShare(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //如果没回调onResp，八成是这句没有写
        WXAPIFactory.createWXAPI(this, ShareKey.WX_ID, false).handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_SHARE:
                        Tool.toast(this, "微信分享取消");
                        finish();
                        break;
                    case RETURN_MSG_TYPE_LOGIN:
                        Tool.toast(this, "微信登录取消");
                        finish();
                        break;
                }
                break;

            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        getWXOpenId(code);
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        finish();
                        break;
                }
                break;
        }
    }

    private void getWXOpenId(String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        HttpUtil.requestPostNetWork(Url.weixinLogin, Tool.getParams(this, map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                JSONObject obj = (JSONObject) JSONObject.parse(result);
                int code = obj.getIntValue("code");
                if (1 == code) {
                    JSONObject result1 = obj.getJSONObject("result");
                    WXBackInfo wxBackInfo = JSONObject.parseObject(result1.toJSONString(), WXBackInfo.class);
                    if ("1".equals(wxBackInfo.getIsexist())) {//1已经绑定
                        SharedPreferences.Editor edit = share.edit();
                        edit.putBoolean(ShareKey.ISLOGIN, true);
                        String u_id = wxBackInfo.getU_id();
                        if (!Tool.isNull(u_id)) {
                            edit.putInt(ShareKey.UID, Integer.valueOf(u_id));
                            String deviceId = share.getString(ShareKey.DEVICEID, "");
                            if (!Tool.isNull(deviceId))
                                Common.offlinePush(WXEntryActivity.this, Integer.valueOf(u_id), deviceId);
                        }
                        edit.putString(ShareKey.HXID_MY, wxBackInfo.getHx_id());
                        edit.commit();

                        if (LoginActivity.instance != null) {
                            LoginActivity.instance.finish();
                        }
                        if (MainActivity.instance != null) {
                            MainActivity.instance.finish();
                        }

                        Intent intent = new Intent();
                        intent.setClass(WXEntryActivity.this, MainActivity.class);
                        intent.putExtra("fromWX", true);
                        startActivity(intent);
                        finish();
                    } else {//0未绑定
                        Intent intent = new Intent();
                        intent.setClass(WXEntryActivity.this, BindPhoneWXActivity.class);
                        intent.putExtra("data", result1.toString());
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void downfailed(String error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //finish();
        }
    }

}
*/