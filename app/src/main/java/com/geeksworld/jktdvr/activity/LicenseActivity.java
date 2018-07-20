package com.geeksworld.jktdvr.activity;

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


/**
 * Created by jktd-wbr on 2017/9/7.
 */

public class LicenseActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private TextView title_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_web);
        initView();
    }

    protected void initView() {
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(this);
        title_name = inTitle.findViewById(R.id.title_name);

        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);

        webView = (WebView) findViewById(R.id.detail_intro);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);//必须要加，否则缩放不起作用
        settings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());

        loadData();

    }

    private void loadData() {
        if (getIntent().getBooleanExtra("secret", false)) {
            title_name.setText(R.string.login_secret2);
            webView.loadUrl("file:///android_asset/login_secret.html");
        } else {
            title_name.setText(R.string.login_clause2);
            webView.loadUrl("file:///android_asset/login_clause.html");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearHistory();
        webView.destroy();
    }
}
