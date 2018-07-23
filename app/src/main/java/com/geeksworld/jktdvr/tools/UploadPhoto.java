package com.geeksworld.jktdvr.tools;

import android.graphics.Bitmap;
import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import internal.org.apache.http.entity.mime.HttpMultipartMode;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;


/**
 * @author a
 *         传递单张图片
 *         登陆注册以及头像修改
 */
public class UploadPhoto {

    public interface OnNetWorkResponse {
        public void downsuccess(String result);

        public void downfailed(String error);
    }

    public static void postForm(final String url, final Map<String, String> param, final ArrayList<String> imgs, final OnNetWorkResponse onResponse) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpPost post = new HttpPost(url);
                    HttpClient client = new DefaultHttpClient();
                    String BOUNDARY = "*****"; // 边界标识
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY, null);
                    if (param != null && !param.isEmpty()) {

                        Iterator<Entry<String, String>> iter = param.entrySet().iterator();
                        while (iter.hasNext()) {
                            Entry<String, String> entry = iter.next();
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();
                            entity.addPart(key, new StringBody(val, Charset.forName("UTF-8")));
                        }
                    }

                    int size = imgs.size();
                    File[] file = new File[size];
                    for (int i = 0; i < imgs.size(); i++) {
                        String path = imgs.get(i);
                        file[i] = new File(path);
                    }
                    // 添加文件参数
                    for (File f : file) {
                        if (f != null && f.exists()) {
                            int degree;
                            Bitmap srcBtm;
                            Bitmap returnBtm;
                            if (f != null && f.exists()) {
                                degree = ImageUtil.getBitmapDegree(f.getAbsolutePath());
                                srcBtm = ImageUtil.getBitmapThumbnail(f.getAbsolutePath());
                                returnBtm = ImageUtil.rotateBitmapByDegree(srcBtm, degree);
                                try {
                                    FileOutputStream out = new FileOutputStream(f);
                                    entity.addPart("image", new FileBody(f));
                                    returnBtm.compress(Bitmap.CompressFormat.PNG, 40, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    post.setEntity(entity);

                    final HttpResponse response;

                    response = client.execute(post);
                    boolean iswork = false;
                    int stateCode = response.getStatusLine().getStatusCode();
                    final StringBuffer sb = new StringBuffer();
                    if (stateCode == HttpStatus.SC_OK) {
                        final HttpEntity result = response.getEntity();
                        if (result != null) {
                            InputStream is = result.getContent();
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(is));
                            String tempLine;
                            while ((tempLine = br.readLine()) != null) {
                                sb.append(tempLine);
                            }
                            iswork = true;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onResponse.downsuccess(sb.toString());
                                }
                            });
                        }
                    }
                    if (!iswork) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResponse.downfailed("没有数据啦!");
                            }
                        });
                    }
                    post.abort();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void postPicForm(final String url, final Map<String, String> param, final String picPath, final OnNetWorkResponse onResponse) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpPost post = new HttpPost(url);
                    HttpClient client = new DefaultHttpClient();
                    String BOUNDARY = "*****"; // 边界标识
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY, null);
                    if (param != null && !param.isEmpty()) {

                        Iterator<Entry<String, String>> iter = param.entrySet().iterator();
                        while (iter.hasNext()) {
                            Entry<String, String> entry = iter.next();
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();
                            entity.addPart(key, new StringBody(val, Charset.forName("UTF-8")));
                        }
                    }



                     File   f = new File(picPath);
                    // 添加文件参数

                    if (f != null && f.exists()) {
                        int degree;
                        Bitmap srcBtm;
                        Bitmap returnBtm;
                        if (f != null && f.exists()) {
                            degree = ImageUtil.getBitmapDegree(f.getAbsolutePath());
                            srcBtm = ImageUtil.getBitmapThumbnail(f.getAbsolutePath());
                            returnBtm = ImageUtil.rotateBitmapByDegree(srcBtm, degree);
                            try {
                                FileOutputStream out = new FileOutputStream(f);
                                entity.addPart("fileName", new FileBody(f));
                                returnBtm.compress(Bitmap.CompressFormat.PNG, 40, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    post.setEntity(entity);

                    final HttpResponse response;

                    response = client.execute(post);
                    boolean iswork = false;
                    int stateCode = response.getStatusLine().getStatusCode();
                    final StringBuffer sb = new StringBuffer();
                    if (stateCode == HttpStatus.SC_OK) {
                        final HttpEntity result = response.getEntity();
                        if (result != null) {
                            InputStream is = result.getContent();
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(is));
                            String tempLine;
                            while ((tempLine = br.readLine()) != null) {
                                sb.append(tempLine);
                            }
                            iswork = true;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onResponse.downsuccess(sb.toString());
                                }
                            });
                        }
                    }
                    if (!iswork) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResponse.downfailed("没有数据啦!");
                            }
                        });
                    }
                    post.abort();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
