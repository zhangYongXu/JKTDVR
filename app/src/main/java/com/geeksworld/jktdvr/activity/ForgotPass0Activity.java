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
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.model.UserModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.MyTime;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.viewModel.UserViewModel;

import java.util.HashMap;

/**
 * Created by xhs on 2017/11/7.
 */

public class ForgotPass0Activity extends BaseActivity implements View.OnClickListener {
    private EditText phone;
    private EditText code;
    private TextView getCode;
    private MyTime myTime;
    private String phoneStr;
    private String msg_id;
    private boolean is_valid;
    private final int REQUEST_SUC = 100;

    private EditText pass;
    private EditText verify;

    private UserViewModel userViewModel;

    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass0);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        userViewModel = new UserViewModel(this,this);
        share = ShareKey.getShare(this);
    }

    protected void initView() {
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title_name = inTitle.findViewById(R.id.title_name);
        title_name.setText(R.string.forgot_pass_title0);
        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);


        phone = (EditText) findViewById(R.id.forgot_phone);
        code = (EditText) findViewById(R.id.forgot_code);
        getCode = (TextView) findViewById(R.id.forgot_getCode);

        getCode.setOnClickListener(this);
        findViewById(R.id.forgot_sub).setOnClickListener(this);
        myTime = new MyTime(this, getCode, 60000, 1000);

        pass = (EditText) findViewById(R.id.forgot_pass);
        verify = (EditText) findViewById(R.id.forgot_verify);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.forgot_getCode:
                phoneStr = phone.getText().toString().trim();
                if (Tool.isNull(phoneStr)) {
                    Tool.toast(this, R.string.toast_phone);
                } else if (!Tool.isMobile(phoneStr)) {
                    Tool.toast(this, R.string.toast_phone_v);
                } else {
                    getCode.setClickable(false);
                    myTime.start();
                    getCode(phoneStr);
                }
                break;

            case R.id.forgot_sub:
                phoneStr = phone.getText().toString().trim();
                String codeStr = code.getText().toString().trim();
                String passStr = pass.getText().toString().trim();
                String verifyStr = verify.getText().toString().trim();
                if (Tool.isNull(phoneStr)) {
                    Tool.toast(this, R.string.toast_phone);
                } else if (Tool.isNull(codeStr)) {
                    Tool.toast(this, R.string.toast_code);
                }else if (Tool.isNull(passStr)) {
                    Tool.toast(this, R.string.toast_pass);
                } else if (!passStr.equals(verifyStr)) {
                    Tool.toast(this, R.string.toast_verify);
                } else {
                    submit(phoneStr,passStr,codeStr);
                }

                break;
        }
    }
   private void getCode(String phone) {
       HashMap<String, Object> map = new HashMap<>();
       map.put("phoneNumber", phone);
       userViewModel.postRequestGetValidateCode(phone, new BaseViewModel.OnRequestDataComplete<String>() {
           @Override
           public void success(String s) {

           }

           @Override
           public void failed(String error) {

           }
       });

   }

    private void submit(final String phoneStr, String passStr,String validateCode) {

        UserModel userModel = new UserModel();
        userModel.setPhone(phoneStr);
        userModel.setPassword(passStr);
        userModel.setValidateCode(validateCode);
        userViewModel.postRequestUpdateUserPass(userModel, new BaseViewModel.OnRequestDataComplete<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                share.edit().putString(ShareKey.PHONE, phoneStr).commit();
                Intent intent = new Intent();
                intent.setClass(ForgotPass0Activity.this, LoginActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void failed(String error) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SUC) {
                Intent intent = new Intent();
                intent.setClass(ForgotPass0Activity.this, LoginActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myTime != null)
            myTime.cancel();
    }
}
