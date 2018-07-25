package com.geeksworld.jktdvr.tools;

/**
 * Created by xhs on 2018/7/25.
 */

public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}
