package com.geeksworld.jktdvr.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.views.CommonSaveImageDialog;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MyWebViewClient extends WebViewClient {
    private Activity activity;

    private CommonSaveImageDialog commonSaveImageDialog;

    public MyWebViewClient(Activity inActivity){
        activity = inActivity;
        commonSaveImageDialog = new CommonSaveImageDialog(activity, R.style.QuestionCompleteAnswerDialog);
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        view.getSettings().setJavaScriptEnabled(true);
        super.onPageFinished(view, url);
        // 获取页面内容
        view.loadUrl("javascript:window.imagelistener.processHTML('<head>'" +
                "+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

        addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
        addLongPressLinstener(view);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.getSettings().setJavaScriptEnabled(true);
        super.onPageStarted(view, url, favicon);
    }

    private void addImageClickListener(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                "    }  " +
                "}" +
                "})()");
    }

    private void addLongPressLinstener(WebView webView){
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView)v).getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                if (type == WebView.HitTestResult.UNKNOWN_TYPE)
                    return false;
                if (type == WebView.HitTestResult.EDIT_TEXT_TYPE) {

                }

                // 这里可以拦截很多类型，我们只处理图片类型就可以了
                switch (type) {
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // TODO
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                        break;
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        // 获取图片的路径
                        final String saveImgUrl = result.getExtra();

                        commonSaveImageDialog.show();
                        commonSaveImageDialog.setClickListener(new CommonSaveImageDialog.OnCommonSaveImageDialogClickListener() {
                            @Override
                            public void saveClicked() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        url2bitmap(saveImgUrl);
                                    }
                                }).start();
                            }

                            @Override
                            public void cancelClicked() {

                            }
                        });

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    public void url2bitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            if (bm != null) {
                save2Album(bm,url);
            }
        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }

    private void save2Album(Bitmap bitmap,String picUrl) {

        FileUtils.savePhoto(activity, bitmap, new FileUtils.SaveResultCallback() {
            @Override
            public void onSavedSuccess() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onSavedFailed() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
