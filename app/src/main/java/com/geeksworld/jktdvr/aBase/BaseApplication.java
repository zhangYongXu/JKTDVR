package com.geeksworld.jktdvr.aBase;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.activity.MainActivity;
import com.geeksworld.jktdvr.tools.DynamicTimeFormat;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONObject;

import java.util.Map;

//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by xhs on 2018/4/8.
 */

public class BaseApplication extends Application {
    private static final String TAG = BaseApplication.class.getName();

    static final String XIAOMI_ID = "2882303761517580913";
    static final String XIAOMI_KEY = "5571758094913";

    static final String MEIZU_APPID = "113477";
    static final String MEIZU_APPKEY = "4a7aab8a20bf450ebf19fa5dc252c205";



    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";

    private Handler handler;

    public Map<String,String> extraMap;


    private BaseApplication baseApplication;

    public MainActivity mainActivity;


    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;


        //上下拉刷新组件效果初始化
        initRefresh();

        initUmeng();

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initRefresh() {
        ClassicsHeader.REFRESH_HEADER_RELEASE = "极客天地VR自生成";
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.gray_ling, R.color.black33);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.gray_ling, R.color.black33);//全局设置主题颜色
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private void initUmeng(){
        UMShareAPI.get(this);//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;

        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"0e3157ad45aa58dabd6c9168480e56d0");
        UMConfigure.setLogEnabled(true);

    }
    //各个平台的配置
    {
        //微信
        PlatformConfig.setWeixin(ShareKey.WX_ID, ShareKey.WX_SECRET);
        //QQ
        //PlatformConfig.setQQZone(ShareKey.QQ_ID, "HXIjrC4lICtvdsgz");

    }


}
