package com.geeksworld.jktdvr.tools;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import internal.org.apache.http.entity.mime.HttpMultipartMode;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * @author a
 *         传递单张图片
 *         登陆注册以及头像修改
 */
public class UploadVideo {

    public interface OnNetWorkResponse {
        public void downsuccess(String result);

        public void downfailed(String error);

        public void uploadProgress(long currentBytes, long contentLength,boolean isUploadComplete);
    }



    public static void postVideoForm(final String url, final Map<String, String> param, final String videoPath, final OnNetWorkResponse onResponse) {
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



                     File   f = new File(videoPath);
                    // 添加文件参数

                    if (f != null && f.exists()) {
                        if (f != null && f.exists()) {
                            try {
                                FileOutputStream out = new FileOutputStream(f);
                                entity.addPart("fileName", new FileBody(f));
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
    public static void postVideoForm2(final String url, final Map<String, String> param, final String videoPath, final OnNetWorkResponse onResponse) {
        File   file = new File(videoPath);
        // 添加文件参数

        if (file != null && file.exists()) {
            postFile(url, new ProgressListener() {
                @Override
                public void onProgress(long currentBytes, long contentLength, boolean done) {
                    String pstr =  "cb:"+currentBytes + "cl："+contentLength + "d:"+done;
                    Log.d("postVideoForm2", "onProgress: "+pstr);
                    onResponse.uploadProgress(currentBytes,contentLength,done);
                }
            }, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onResponse.downfailed(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    int code =  response.code();
                    ResponseBody body = response.body();
                    if(code == 200){
                       try {
                           String bodyString = body.string();
                           onResponse.downsuccess(bodyString);
                       }catch (IOException e){
                           e.printStackTrace();

                       }
                    }else {
                        onResponse.downfailed(response.toString());
                    }

                }
            }, file);
        }

    }
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20000, TimeUnit.MILLISECONDS)
            .readTimeout(20000,TimeUnit.MILLISECONDS)
            .writeTimeout(20000, TimeUnit.MILLISECONDS).build();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    public static void postFile(String url, final ProgressListener listener, okhttp3.Callback callback, File...files){

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.i("huang","files[0].getName()=="+files[0].getName());
        //第一个参数要与Servlet中的一致
        builder.addFormDataPart("fileName",files[0].getName(), RequestBody.create(MediaType.parse("application/octet-stream"),files[0]));

        MultipartBody multipartBody = builder.build();

        Request request  = new Request.Builder().url(url).post(new ProgressRequestBody(multipartBody,listener)).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


}
