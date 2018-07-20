package com.geeksworld.jktdvr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.HttpUtil;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by xhs on 2017/11/7.
 */

public class ForgotPass1Activity extends BaseActivity implements View.OnClickListener {
    private EditText phone;
    private String phoneStr;
    private EditText pass;
    private EditText verify;
    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass1);
        share = ShareKey.getShare(this);
        initView();
    }

    protected void initView() {
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title_name = inTitle.findViewById(R.id.title_name);
        title_name.setText(R.string.forgot_pass_title1);


        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);

        phone = (EditText) findViewById(R.id.forgot_phone);
        pass = (EditText) findViewById(R.id.forgot_pass);
        verify = (EditText) findViewById(R.id.forgot_verify);

        findViewById(R.id.forgot_sub).setOnClickListener(this);

        Intent intent = getIntent();
        String phoneStr = intent.getStringExtra("phone");
        phone.setText(phoneStr);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.forgot_sub:
                phoneStr = phone.getText().toString().trim();
                String passStr = pass.getText().toString().trim();
                String verifyStr = verify.getText().toString().trim();

                if (Tool.isNull(phoneStr)) {
                    Tool.toast(this, R.string.toast_phone);
                } else if (Tool.isNull(passStr)) {
                    Tool.toast(this, R.string.toast_pass);
                } else if (!passStr.equals(verifyStr)) {
                    Tool.toast(this, R.string.toast_verify);
                } else
                    submit(phoneStr, passStr);
                break;
        }
    }

    private void submit(final String phoneStr, String passStr) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phoneNumber", phoneStr);
        map.put("password", passStr);
        map.put("validateCode", passStr);

        HttpUtil.requestPostNetWork(Url.forgetPwd, Tool.getParams(this, map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    Tool.toast(ForgotPass1Activity.this, message);
                    if (code == 1) {
                        share.edit().putString(ShareKey.PHONE, phoneStr).commit();
                        Intent intent = new Intent();
                        intent.setClass(ForgotPass1Activity.this, ForgotPass0Activity.class);
                        setResult(RESULT_OK, intent);

                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void downfailed(String error) {

            }
        });
    }

}
