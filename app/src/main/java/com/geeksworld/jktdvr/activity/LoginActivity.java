package com.geeksworld.jktdvr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.model.UserModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.viewModel.UserViewModel;


//import com.tencent.mm.opensdk.modelmsg.SendAuth;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by xhs on 2017/10/24.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private String TAG = this.getClass().getSimpleName();

    private static final int REQUESTCODE_REGISTER = 1;
    private static final int REQUESTCODE_PASS = 2;
    private EditText phone;
    private EditText pass;
    private ImageView login_head;
    private SharedPreferences share;
    public static LoginActivity instance;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
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
        TextView title_name = inTitle.findViewById(R.id.title_name);
        inTitle.findViewById(R.id.title_back).setOnClickListener(this);
        inTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
        title_name.setText(R.string.login);

        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);


        findViewById(R.id.login_wx).setOnClickListener(this);
        findViewById(R.id.login_qq).setOnClickListener(this);
        findViewById(R.id.login_sub).setOnClickListener(this);
        findViewById(R.id.login_forgetPass).setOnClickListener(this);
        findViewById(R.id.login_phone_register).setOnClickListener(this);
        findViewById(R.id.login_pass_visible).setOnTouchListener(this);

        phone = (EditText) findViewById(R.id.login_phone);
        pass = (EditText) findViewById(R.id.login_pass);
        login_head = (ImageView) findViewById(R.id.login_head);

        String phoneStr = share.getString(ShareKey.PHONE, "");
        if (!Tool.isNull(phoneStr))
            phone.setText(phoneStr);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.login_sub:
                String phoneStr = phone.getText().toString().trim();
                String passStr = pass.getText().toString().trim();
                if (Tool.isNull(phoneStr)) {
                    Tool.toast(this, R.string.toast_phone);
                } else if (Tool.isNull(passStr)) {
                    Tool.toast(this, R.string.toast_pass);
                } else if (!Tool.isMobile(phoneStr)) {
                    Tool.toast(this, R.string.toast_phone_v);
                } else {
                    loginSub(phoneStr, passStr);
                }
                break;

            case R.id.login_forgetPass:
                intent.setClass(this, ForgotPass0Activity.class);
                startActivityForResult(intent, REQUESTCODE_PASS);
                break;

            case R.id.login_phone_register:
                intent.setClass(this, RegisterActivity.class);
                startActivityForResult(intent, REQUESTCODE_REGISTER);
                break;

            case R.id.login_wx:
                wx_login();
                break;

            case R.id.login_qq:
                qq_login();
                break;

            default:
                break;
        }
    }

    private void loginSub(final String phoneStr, final String passStr) {
        UserModel userModel = new UserModel();
        userModel.setPhone(phoneStr);
        userModel.setPassword(passStr);
        userViewModel.postRequestUserLogin(userModel, new BaseViewModel.OnRequestDataComplete<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void failed(String error) {

            }
        });

    }




    private void wx_login() {
//        if(!Common.isWeixinAvilible(this)){
//            Toast.makeText(this,"未安装微信",Toast.LENGTH_LONG);
//            return;
//        }
//        authorization(SHARE_MEDIA.WEIXIN);

    }

    private void qq_login() {
//        if(!Common.isQQClientAvailable(this)){
//            Toast.makeText(this,"未安QQ",Toast.LENGTH_LONG);
//            return;
//        }
//        authorization(SHARE_MEDIA.QQ);

    }
/*
    public void qq_logout() {
        mTencent.logout(this);
    }
    */
//授权
//private void authorization(SHARE_MEDIA share_media) {
//    UMShareAPI.get(this).getPlatformInfo(this, share_media, new MyUMAuthListener(this));
//}

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUESTCODE_REGISTER) {
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this, MainActivity.class);
//                setResult(RESULT_OK, intent);
//                finish();
//            } else if (requestCode == REQUESTCODE_PASS) {
//                String phoneStr = share.getString(ShareKey.PHONE, "");
//                if (!Tool.isNull(phoneStr))
//                    phone.setText(phoneStr);
//            }
//        }

        //UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    /*当Activity加载好了再获取组件*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (phone != null)
                phone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (i == 10) {//如果输完手机号，根据手机号请求头像
                            //showUserPortrait(charSequence.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
