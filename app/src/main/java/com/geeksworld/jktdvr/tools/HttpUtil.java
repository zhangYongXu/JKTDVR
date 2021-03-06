package com.geeksworld.jktdvr.tools;

import android.app.Activity;
import android.os.Handler;

import com.geeksworld.jktdvr.activity.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpUtil {
    static ExecutorService fixedThreadPool;

    public interface OnNetWorkResponse {
        void downsuccess(String result);

        void downfailed(String error);
    }

    //Get网络请求
    public static void requestGetNetWork(final String path,
                                         final OnNetWorkResponse response) {
        final Handler handler = new Handler();
        if (fixedThreadPool == null) {
            fixedThreadPool = Executors.newFixedThreadPool(4);
        }
        fixedThreadPool.execute(new Runnable() {
            private ByteArrayOutputStream out;
            private InputStream is;
            boolean iswork;

            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        is = conn.getInputStream();
                        out = new ByteArrayOutputStream();
                        int len = 0;
                        byte[] b = new byte[1024];
                        while ((len = is.read(b)) != -1) {
                            out.write(b, 0, len);
                        }
                        final String result = new String(out.toByteArray());
                        out.flush();
                        iswork = true;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                response.downsuccess(result);
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (!iswork) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                response.downfailed("连接出错啦!");
                            }
                        });
                    }
                    if (out != null) {
                        try {
                            out.close();
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    //POST网络请求
    public static void requestPostNetWork(final String path, final String params,
                                          final OnNetWorkResponse response) {
        final Handler handler = new Handler();
        if (fixedThreadPool == null) {
            fixedThreadPool = Executors.newFixedThreadPool(4);
        }
        fixedThreadPool.execute(new Runnable() {
            private ByteArrayOutputStream out;
            private InputStream is;
            boolean iswork;

            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setReadTimeout(5000);
                    conn.setDoInput(true);
                    conn.setConnectTimeout(20000);
                    byte[] requestParams = params.getBytes();
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Length", String.valueOf(requestParams.length));
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(requestParams);

                    if (conn.getResponseCode() == 200) {
                        is = conn.getInputStream();
                        out = new ByteArrayOutputStream();
                        int len;
                        byte[] b = new byte[1024];
                        while ((len = is.read(b)) != -1) {
                            out.write(b, 0, len);
                        }
                        final String result = new String(out.toByteArray());
                        out.flush();
                        if (!Tool.isNull(result)) {
                            iswork = true;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    response.downsuccess(result);
                                }
                            });
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (!iswork) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                response.downfailed("请检查网络链接!");
                            }
                        });
                    }
                    if (out != null) {
                        try {
                            out.close();
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void requestPostNetWork2(final String path, final String params,
                                          final OnNetWorkResponse response) {
        final Handler handler = new Handler(MainActivity.instance.getMainLooper());
        if (fixedThreadPool == null) {
            fixedThreadPool = Executors.newFixedThreadPool(4);
        }
        fixedThreadPool.execute(new Runnable() {
            private ByteArrayOutputStream out;
            private InputStream is;
            boolean iswork;

            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setReadTimeout(5000);
                    conn.setDoInput(true);
                    conn.setConnectTimeout(20000);
                    byte[] requestParams = params.getBytes();
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Length", String.valueOf(requestParams.length));
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(requestParams);

                    if (conn.getResponseCode() == 200) {
                        is = conn.getInputStream();
                        out = new ByteArrayOutputStream();
                        int len;
                        byte[] b = new byte[1024];
                        while ((len = is.read(b)) != -1) {
                            out.write(b, 0, len);
                        }
                        final String result = new String(out.toByteArray());
                        out.flush();
                        if (!Tool.isNull(result)) {
                            iswork = true;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    response.downsuccess(result);
                                }
                            });
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (!iswork) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                response.downfailed("请检查网络链接!");
                            }
                        });
                    }
                    if (out != null) {
                        try {
                            out.close();
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}
