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
        else
            webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
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
