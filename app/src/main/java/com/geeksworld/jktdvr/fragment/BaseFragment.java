package com.geeksworld.jktdvr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by xhs on 2017/10/23.
 */

public class BaseFragment extends Fragment {
    private LayoutInflater inflater;
    private DisplayMetrics metrics;

    public DisplayMetrics getMetrics() {
        return metrics;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        initData();
        initView(view);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    protected void initData(){

    }
    protected void initView(View view){

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
