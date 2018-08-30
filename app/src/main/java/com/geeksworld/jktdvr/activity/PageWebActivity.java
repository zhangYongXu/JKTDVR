package com.geeksworld.jktdvr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.Tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by xhs on 2017/11/27.
 */

public class PageWebActivity extends BaseActivity {
    public static final String INTENT_EXTRA_URL_KEY = "url";
    public static final String INTENT_EXTRA_NAME_KEY = "name";

    private WebView webView;
    private Activity activity;

    private String url;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_web);
        initData();
        initNavigationView();
        initView();
        loadData();
    }

    //导航设置
    private void initNavigationView(){
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack())
                    webView.goBack();
                else
                    finish();
            }
        });

        inTitle.findViewById(R.id.title_img_right).setVisibility(View.VISIBLE);
        inTitle.findViewById(R.id.title_img_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.showPopShare(activity,view,activity,url);
            }
        });

        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);

        TextView titleView = inTitle.findViewById(R.id.title_name);

        if (!Tool.isNull(title)) {
            titleView.setText(title);
        }else {
            titleView.setText("详情");
        }
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        url = intent.getStringExtra(INTENT_EXTRA_URL_KEY);
        title = intent.getStringExtra(INTENT_EXTRA_NAME_KEY);
        activity = this;
    }

    protected void initView() {

        webView = (WebView) findViewById(R.id.detail_intro);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);//必须要加，否则缩放不起作用
        settings.setDisplayZoomControls(false);
//        设置内容适应屏幕大小，按比例缩放
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());


    }

    private void loadData() {
        if (url.startsWith("http"))
            webView.loadUrl(url);
        else {
            webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        }
        //webView.loadUrl("file:///android_asset/webvr/login_clause.html");
        //webView.loadUrl("file:///android_asset/sample_vrPlayer/sample_全屏尺寸.html");
        //processWebString("webvr/login_clause.html");
    }

    private void processWebString(String filepath) {
        // 加载 asset 文件
        String tpl = getFromAssets(filepath);
        webView.loadDataWithBaseURL(null, tpl, "text/html", "utf-8", null);
    }


    /*
     * 获取html文件
     */
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearHistory();
        webView.destroy();
    }
}
