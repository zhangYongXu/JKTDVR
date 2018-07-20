package com.geeksworld.jktdvr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
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

/**
 * Created by xhs on 2017/10/24.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private TextView phone;
    private TextView code;
    private TextView getCode;
    private EditText pass;
    private MyTime myTime;
    private String msg_id;
    private SharedPreferences share;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        share = ShareKey.getShare(this);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        userViewModel = new UserViewModel(this,this);
    }

    protected void initView() {
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title_name = inTitle.findViewById(R.id.title_name);
        title_name.setText(R.string.register);



        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);


        phone = (TextView) findViewById(R.id.login_phone);
        code = (TextView) findViewById(R.id.login_code);
        getCode = (TextView) findViewById(R.id.login_getCode);
        pass = (EditText) findViewById(R.id.login_pass);

        getCode.setOnClickListener(this);
        findViewById(R.id.login_sub).setOnClickListener(this);
        findViewById(R.id.login_clause).setOnClickListener(this);
        findViewById(R.id.login_secret).setOnClickListener(this);
        findViewById(R.id.login_pass_visible).setOnTouchListener(this);

        myTime = new MyTime(this, getCode, 60000, 1000);
    }

    @Override
    public void onClick(View view) {
        String phoneStr;
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.title_right:
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.login_getCode:
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

            case R.id.login_sub:
                phoneStr = phone.getText().toString().trim();
                String codeStr = code.getText().toString().trim();
                String passStr = pass.getText().toString().trim();
                if (Tool.isNull(phoneStr)) {
                    Tool.toast(this, R.string.toast_phone);
                } else if (Tool.isNull(codeStr)) {
                    Tool.toast(this, R.string.toast_code);
                } else if (Tool.isNull(passStr)) {
                    Tool.toast(this, R.string.toast_pass);
                }else{
                    registerSub(phoneStr,codeStr,passStr);
                }
                break;

            case R.id.login_clause:
                intent.setClass(this, LicenseActivity.class);
                startActivity(intent);
                break;

            case R.id.login_secret:
                intent.putExtra("secret", true);
                intent.setClass(this, LicenseActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    //获取验证码
    private void getCode(String phone) {
        userViewModel.postRequestGetValidateCode(phone, new BaseViewModel.OnRequestDataComplete<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failed(String error) {

            }
        });
    }


    private void registerSub(final String phoneStr,String validateCode ,final String passStr) {
        UserModel userModel = new UserModel();
        userModel.setPhone(phoneStr);
        userModel.setPassword(passStr);
        userModel.setValidateCode(validateCode);
        userViewModel.postRequestRegisterUser(userModel, new BaseViewModel.OnRequestDataComplete<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void failed(String error) {

            }
        });
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.login_pass_visible) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    String text = pass.getText().toString();
                    if (!Tool.isNull(text))
                        pass.setSelection(text.length());
                    break;

                case MotionEvent.ACTION_UP:
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    String text1 = pass.getText().toString();
                    if (!Tool.isNull(text1))
                        pass.setSelection(text1.length());
                    break;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myTime != null)
            myTime.cancel();
    }

}
