package com.geeksworld.jktdvr.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by xhs on 2018/6/11.
 */

public class WebViewFontShare {
    public static SharedPreferences getWebViewFontShare(Context context) {
        return context.getSharedPreferences("WebViewFontShareData", Context.MODE_PRIVATE);
    }

    public static final int WebView_FontSize_Smaller = 80;//小
    public static final int WebView_FontSize_Default = 100;//默认
    public static final int WebView_FontSize_middle = 130;//中
    public static final int WebView_FontSize_large = 165;//大
    public static final int WebView_FontSize_largest = 200;//特大


    public static final String Setting_WebView_FontSize_Current = "setting_webView_fontSize_current";


    public static int getSetting_WebView_FontSize(Context context){
        SharedPreferences share = getWebViewFontShare(context);
        String fontSizeStr = share.getString(Setting_WebView_FontSize_Current,"");
        if(!Tool.isNull(fontSizeStr)){
            int fontSize = Integer.parseInt(fontSizeStr);
            return fontSize;
        }
        return WebView_FontSize_Default;
    }

    public static String getSetting_WebView_FontSize_name(Context context){
        SharedPreferences share = getWebViewFontShare(context);
        String fontSizeStr = share.getString(Setting_WebView_FontSize_Current,"");
        int fontSize = WebView_FontSize_Default;
        if(!Tool.isNull(fontSizeStr)){
            fontSize = Integer.parseInt(fontSizeStr);
        }
        String name = "默认";
        switch (fontSize){
            case WebView_FontSize_Smaller:{
                name = "小";
            }
            break;

            case WebView_FontSize_middle:{
                name = "中";
            }
            break;
            case WebView_FontSize_large:{
                name = "大";
            }
            break;
            case WebView_FontSize_largest:{
                name = "特大";
            }
            break;
        }
        return name;
    }
    public static void setSetting_WebView_FontSize(Context context,int fontSize){
        SharedPreferences share = getWebViewFontShare(context);
        SharedPreferences.Editor edit = share.edit();
        edit.putString(Setting_WebView_FontSize_Current,fontSize+"");
        edit.commit();
    }

    public static void selectWebviewFontSizePop(final Context context, final WebView webView, final TextView textView){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] mItems = new String[5];
        mItems[0] = "小";
        mItems[1] = "默认";
        mItems[2] = "中";
        mItems[3] = "大";
        mItems[4] = "特大";

        builder.setTitle("选择字体");

        String name = getSetting_WebView_FontSize_name(context);

        int selectIndex = 1;
        for(int i = 0;i<mItems.length;i++){
            String cName = mItems[i];
            if(cName.equals(name)){
                selectIndex = i;
                break;
            }
        }
        builder.setSingleChoiceItems(mItems, selectIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog) dialog).getListView();
                // which表示点击的条目
                Object checkedItem = lw.getAdapter().getItem(which);
                // 既然你没有cancel或者ok按钮，所以需要在点击item后使dialog消失
                dialog.dismiss();
                // 更新你的view

                switch (which){
                    case 0:{
                        if(webView != null){
                            webView.getSettings().setTextZoom(WebView_FontSize_Smaller);
                        }
                        setSetting_WebView_FontSize(context,WebView_FontSize_Smaller);
                    }
                    break;
                    case 1:{
                        if(webView != null) {
                            webView.getSettings().setTextZoom(WebView_FontSize_Default);
                        }
                        setSetting_WebView_FontSize(context,WebView_FontSize_Default);
                    }
                    break;
                    case 2:{
                        if(webView != null) {
                            webView.getSettings().setTextZoom(WebView_FontSize_middle);
                        }
                        setSetting_WebView_FontSize(context,WebView_FontSize_middle);
                    }
                    break;
                    case 3:{
                        if(webView != null) {
                            webView.getSettings().setTextZoom(WebView_FontSize_large);
                        }
                        setSetting_WebView_FontSize(context,WebView_FontSize_large);
                    }
                    break;
                    case 4:{
                        if(webView != null) {
                            webView.getSettings().setTextZoom(WebView_FontSize_largest);
                        }
                        setSetting_WebView_FontSize(context,WebView_FontSize_largest);
                    }
                    break;
                }

                if(textView != null){
                    String name = getSetting_WebView_FontSize_name(context);
                    textView.setText(name);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
