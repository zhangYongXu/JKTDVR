package com.geeksworld.jktdvr.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseApplication;
import com.geeksworld.jktdvr.aBase.BaseFragActivity0;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.adapter.MainAdapter;
import com.geeksworld.jktdvr.fragment.Frag_main0;
import com.geeksworld.jktdvr.fragment.Frag_main1;
import com.geeksworld.jktdvr.fragment.Frag_main2;
import com.geeksworld.jktdvr.fragment.Frag_main3;
import com.geeksworld.jktdvr.fragment.Frag_main4;
import com.geeksworld.jktdvr.model.HomeItemModel;


import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends BaseFragActivity0 implements View.OnClickListener{

    public static Activity instance;
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private MainAdapter adapter;
    private final int REQUEST_PERMISSIONS = 101;
    private String[] array_permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};
    private Frag_main0 frag_main0;
    private Frag_main1 frag_main1;
    private Frag_main2 frag_main2;
    private Frag_main3 frag_main3;
    private Frag_main4 frag_main4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        initData();
        initView();
        loadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initData(){

    }
    private void loadData(){

    }
    protected void initView() {
        viewPager = findViewById(R.id.viewPager);
        radioGroup = findViewById(R.id.main_rg);
        adapter = new MainAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new OnPageChanged());
        findViewById(R.id.main_rd0).setOnClickListener(this);
        findViewById(R.id.main_rd1).setOnClickListener(this);
        findViewById(R.id.main_rd2).setOnClickListener(this);
        findViewById(R.id.main_rd3).setOnClickListener(this);
        findViewById(R.id.main_rd4).setOnClickListener(this);

        addFragments();

        if (getIntent().getBooleanExtra("fromWX", false))
            viewPager.setCurrentItem(5);


        Application app = getApplication();
        if(app instanceof BaseApplication) {
            BaseApplication bApp = (BaseApplication) app;
            bApp.mainActivity = this;
        }



    }



    @Override
    protected void onRestart() {

        super.onRestart();
    }

    private void addFragments() {
        frag_main0 = new Frag_main0();
        frag_main1 = new Frag_main1();
        frag_main2 = new Frag_main2();
        frag_main3 = new Frag_main3();
        frag_main4 = new Frag_main4();
        fragments.add(frag_main0);
        fragments.add(frag_main1);
        fragments.add(frag_main2);
        fragments.add(frag_main3);
        fragments.add(frag_main4);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int position = 0;
        switch (view.getId()) {
            case R.id.main_rd0:
                //frag_main0.scrollToTop();
                position = 0;
                break;
            case R.id.main_rd1:
                //frag_main1.scrollToTop();
                position = 1;
                break;
            case R.id.main_rd2:
                //frag_main2.scrollToTop();
                position = 2;
                break;
            case R.id.main_rd3:
                //frag_main3.scrollToTop();
                position = 3;
                break;
            case R.id.main_rd4:
                position = 4;
                break;
        }
        viewPager.setCurrentItem(position);
    }

    private class OnPageChanged implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            RadioButton childAt = (RadioButton) radioGroup.getChildAt(position);
            childAt.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    /*覆写可以使Fragment的回调执行*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*当Activity加载好了再获取组件*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
