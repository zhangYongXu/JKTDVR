package com.geeksworld.jktdvr.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.util.Util;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.views.MyWidget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

//import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by xhs on 2017/12/3.
 */

public class Common {


    /**
     * 分享到微信
     */

    public static void shareToWX(final Activity activity,final Context context, final String url, final String title, final String decribe, String imgUrl, final boolean isTimeLineCb) {
        //UmengShareUtils.shareWeb(activity, url, title, decribe, imgUrl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN);
    }
    /**
     * 分享到微信朋友圈
     */
    public static void shareToWXCircle(final Activity activity,final Context context, final String url, final String title, final String decribe, String imgUrl, final boolean isTimeLineCb) {
        //UmengShareUtils.shareWeb(activity, url, title, decribe, imgUrl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    public static final int IMAGE_LENGTH_LIMIT = 6291456;

    private static ByteArrayOutputStream getWXThumb(Bitmap resource) {
        Bitmap thumb = createScaledBitmap(resource, 100, true);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int quality = 90;
        int realLength = Util.getBitmapByteSize(resource.getWidth(), resource.getHeight(), Bitmap.Config.ARGB_8888);
        if (realLength > IMAGE_LENGTH_LIMIT) {
            quality = (int) (IMAGE_LENGTH_LIMIT * 1f / realLength * 100);
        }
        if (quality < 75) {
            quality = 75;
        }
        resource.compress(Bitmap.CompressFormat.PNG, quality, output);
        output.reset();
        thumb.compress(Bitmap.CompressFormat.PNG, 85, output);
        return output;
    }

    /*分享到QQ*/
    public static void shareToQQ(Activity activity,Context context, String url, String title, String describe, String imgUrl) {
        //UmengShareUtils.shareWeb(activity, url, title, describe, imgUrl, R.mipmap.ic_launcher, SHARE_MEDIA.QQ);
    }

    /*展现分享选择  Common.showPopShare(view, this, "url", "title", "describe", "imgUrl");*/
//    public static void showPopShare(Activity activity,View view, final Context context, final String url1, final String title1, final String describe1, final String imgUrl1) {
//        final String url = "http://geeksworld.cn/";
//        final String title = "教育大数据";
//        final String describe = "这是教育大数据的分享";
//        final String imgUrl = "http://ozdzph0kd.bkt.clouddn.com/搞笑视频71511579990.jpg";
//       // getShareData(view, context);
//
//        //测试
//        share(activity,context,view,url,title,describe,imgUrl);
//
//    }

    public static void showPopShare(Activity activity, View view, final Context context, HomeItemModel homeItemModel){
         String url = "http://jydsj.chinaecdc.com/home/downLoadShare";
         final String title = "教育大数据";
         String describe = "欢迎使用教育大数据APP，点击进入下载页!";
         final String imgUrl = "http://jydsj.chinaecdc.com/static/package/shareImage.png";

        share(activity,context,view,url,title,describe,imgUrl,homeItemModel);
    }

    private static void getShareData(final View view, final Context context, final Activity activity) {
        HashMap<String, Object> map = new HashMap<>();
        SharedPreferences share = ShareKey.getShare(context);
        map.put("u_id", share.getInt(ShareKey.UID, 0));
        HttpUtil.requestPostNetWork("", Tool.getParams(context, map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                JSONObject obj = (JSONObject) JSONObject.parse(result);
                int code = obj.getIntValue("code");
                if (1 == code) {
                    JSONObject object = obj.getJSONObject("result");
                    if (object == null) {
                        Tool.toast(context, R.string.unknown);
                        return;
                    }
                    final String imgUrl = object.getString("shareimgurl");
                    final String title = object.getString("sharetitle");
                    final String url = object.getString("shareurl");
                    final String describe = object.getString("sharetext");

                    final PopupWindow popShare = MyWidget.getPopupWindow(context, view, R.layout.pop_share, false);
                    View contentView = popShare.getContentView();
                    contentView.findViewById(R.id.pop_back).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popShare.dismiss();
                        }
                    });
                    contentView.findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.shareToWX(activity,context, url, title, describe, imgUrl, false);
                            popShare.dismiss();
                        }
                    });
                    contentView.findViewById(R.id.share_wx2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.shareToWX(activity,context, url, title, describe, imgUrl, true);
                            popShare.dismiss();
                        }
                    });
                    contentView.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Common.shareToQQ(context, url, title, describe, imgUrl);
                            popShare.dismiss();
                        }
                    });

                }
            }

            @Override
            public void downfailed(String error) {

            }
        });
    }

    public static void share(final Activity activity, final Context context, View view, final String url, final String title, final String describe, final String imgUrl, final HomeItemModel homeItemModel){
        final PopupWindow popShare = MyWidget.getPopupWindow(context, view, R.layout.pop_share, false);
        View contentView = popShare.getContentView();
        contentView.findViewById(R.id.pop_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popShare.dismiss();
            }
        });
        contentView.findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.shareToWX(activity,context, url, title, describe, imgUrl, false);
                popShare.dismiss();
            }
        });
        contentView.findViewById(R.id.share_wx2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(homeItemModel.getContype() == HomeItemModel.HomeItemModelContentTypeQuestion){
//                    Common.shareToWXCircle(activity,context, url, title, describe, imgUrl, true);
//                }else {
                    Common.shareToWXCircle(activity,context, url, describe, title, imgUrl, true);
//                }
//
                popShare.dismiss();
            }
        });
        contentView.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.shareToQQ(activity,context, url, title, describe, imgUrl);
                popShare.dismiss();
            }
        });

    }



    public static Bitmap createScaledBitmap(Bitmap src, int dstSize, boolean filter) {
        Matrix matrix = new Matrix();
        final int width = src.getWidth();
        final int height = src.getHeight();

        float xScale = width * 1f / dstSize;
        float yScale = height * 1f / dstSize;
        float scale = Math.min(xScale, yScale);

        final int dstWidth = (int) (width / scale);
        final int dstHeight = (int) (height / scale);

        matrix.setScale(scale, scale);
        return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter);
    }


    public static void offlinePush(Context context, int u_id, String device_id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("u_id", u_id);
        map.put("device_id", device_id);
        map.put("clientOs", 1);
        HttpUtil.requestPostNetWork("", Tool.getParams(context, map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {

            }

            @Override
            public void downfailed(String error) {

            }
        });
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    //判断是否有刘海
    public static boolean hasNotchInScreen(Context context){
        boolean isNotch = hasNotchInHuaWei(context);
        if(!isNotch){
            isNotch = hasNotchInOppo(context);
        }
        return isNotch;
    }
    //判断华为手机是否有刘海
    public static boolean hasNotchInHuaWei(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }
    //判断opp手机是否有刘海
    public static boolean hasNotchInOppo(Context context){
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static void hasNotchHandleNavigationStatusBg(Context context,RelativeLayout topStausViewRelativeLayout){
        if (Build.VERSION.SDK_INT <= 20 ) { //当手机屏幕不支持沉浸式
            topStausViewRelativeLayout.setVisibility(View.GONE);
        }
        if(Common.hasNotchInScreen(context)){//当有刘海屏幕时
            topStausViewRelativeLayout.setVisibility(View.GONE);
        }
    }

    public static File uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

}
