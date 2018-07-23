package com.geeksworld.jktdvr.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.aBase.BaseViewModel;

import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.tools.WebViewFontShare;


/**
 * Created by xhs on 2018/4/9.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private SharedPreferences share;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initData();
        initNavigationView();
        initView();
    }
    //导航设置
    private void initNavigationView(){
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);

        TextView titleView = inTitle.findViewById(R.id.title_name);
        titleView.setText("设置");
    }
    @Override
    protected void initData() {
        super.initData();
        share = ShareKey.getShare(this);

    }


    @Override
    protected void initView() {
        super.initView();

        String name = WebViewFontShare.getSetting_WebView_FontSize_name(this);

        findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        findViewById(R.id.set_check_version_layout).setOnClickListener(this);
        findViewById(R.id.set_about_layout).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.set_check_version_layout:{
                loadVersionUpdateData();
            }break;
            case R.id.set_about_layout:{
                Intent intent = new Intent();
                intent.setClass(this, PageWebActivity.class);
                String url = "http://geeksworld.cn/";
                intent.putExtra(PageWebActivity.INTENT_EXTRA_URL_KEY,url);
                intent.putExtra(PageWebActivity.INTENT_EXTRA_NAME_KEY,"关于极客天地");
                startActivity(intent);
            }break;

        }
    }
    private void loadVersionUpdateData(){
        Tool.toast(this,"已经是最新版本");
    }

    private void logout(){
        if (share.getBoolean(ShareKey.ISLOGIN, false)) {
            new AlertDialog.Builder(this).setMessage("确认退出登录!")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            share.edit().clear().commit();
                            share.edit().putBoolean(ShareKey.NONEWER, true).commit();
                            Intent intent = new Intent();
                            intent.setClass(SettingActivity.this, MainActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }
    }
}
