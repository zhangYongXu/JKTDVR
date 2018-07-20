package com.geeksworld.jktdvr.aBase;

import android.app.Activity;
import android.content.Context;

/**
 * Created by xhs on 2018/4/3.
 */

public class BaseViewModel {
    public interface OnRequestDataComplete<T>{
        void success(T t);
        void failed(String error);
    }

    private Context context;
    private Activity activity;

    public Context getContext() {
        return context;
    }

    public Activity getActivity() {
        return activity;
    }

    protected void initData(){

    }


    public BaseViewModel(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void requestNetData(final OnRequestDataComplete complete){
        
    }
}
