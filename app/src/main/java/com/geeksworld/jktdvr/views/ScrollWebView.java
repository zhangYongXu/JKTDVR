package com.geeksworld.jktdvr.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by xhs on 2018/4/4.
 */

public class ScrollWebView extends WebView {
    private float startx;
    private float starty;
    private float offsetx;
    private float offsety;
    public ScrollWebView(Context context) {
        super(context);
    }
    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startx = event.getX();
                starty = event.getY();
                Log.e("MotionEvent", "webview按下");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("MotionEvent", "webview滑动");
                offsetx = Math.abs(event.getX() - startx);
                offsety = Math.abs(event.getY() - starty);
                if (offsetx > offsety) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    Log.e("MotionEvent", "屏蔽了父控件");
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    Log.e("MotionEvent", "事件传递给父控件");
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
