package com.geeksworld.jktdvr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.fragment.MineWorkTabContentFragment;
import com.geeksworld.jktdvr.fragment.VRWorkEditTabContentPicFragment;
import com.geeksworld.jktdvr.fragment.VRWorkTabContentVideoFragment;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xhs on 2018/4/9.
 */

public class VRWorkEditActivity extends BaseActivity{

    private SharedPreferences share;

    private TabLayout tabLayout;


    private ViewPager viewPager;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private VRWorkEditActivity.ContentPagerAdapter contentAdapter;

    private HomeViewModel homeViewModel;
    private HomeItemModel homeItemModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_work_edit);
        initData();
        initNavigationView();
        initView();
        loadData();

    }
    //导航设置
    private void initNavigationView(){
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);

        TextView titleView = inTitle.findViewById(R.id.title_name);
        titleView.setText("编辑VR");
    }
    @Override
    protected void initData() {
        super.initData();
        share = ShareKey.getShare(this);

        tabIndicators = new ArrayList<>();
        tabFragments = new ArrayList<>();

        homeViewModel = new HomeViewModel(this,this);

        homeItemModel = (HomeItemModel)getIntent().getSerializableExtra(HomeItemModel.SerializableKey);

    }
    private void loadData(){
        homeViewModel.postRequestAllTagListData(false, new BaseViewModel.OnRequestDataComplete<List<HomeTagModel>>() {
            @Override
            public void success(List<HomeTagModel> homeTagModels) {
                initContent();
                selectPage();
            }

            @Override
            public void failed(String error) {

            }
        });

    }

    @Override
    protected void initView() {
        super.initView();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);



    }
    private void selectPage(){
        if(homeItemModel.getType() == HomeItemModel.HomeItemModelContentTypePicture){
            viewPager.setCurrentItem(0);
        }else {
            viewPager.setCurrentItem(1);
        }

    }
    private void initContent(){
        tabLayout.setupWithViewPager(viewPager);

        String titlepic = "VR图编辑";
        tabLayout.addTab(tabLayout.newTab().setText(titlepic));
        String titlevideo = "VR视频编辑";
        tabLayout.addTab(tabLayout.newTab().setText(titlevideo));

        tabIndicators.add(titlepic);
        tabIndicators.add(titlevideo);


        VRWorkEditTabContentPicFragment picF = VRWorkEditTabContentPicFragment.newInstance(homeViewModel,homeItemModel);
        tabFragments.add(picF);


        VRWorkTabContentVideoFragment videoF = VRWorkTabContentVideoFragment.newInstance(homeViewModel,homeItemModel);
        tabFragments.add(videoF);

        contentAdapter = new VRWorkEditActivity.ContentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(contentAdapter);
        contentAdapter.notifyDataSetChanged();
    }
    class ContentPagerAdapter extends FragmentPagerAdapter {
        public ContentPagerAdapter(FragmentManager fm) { super(fm); }
        @Override public Fragment getItem(int position) {
            return tabFragments.get(position);
        }
        @Override public int getCount() { return tabIndicators.size(); }
        @Override public CharSequence getPageTitle(int position) { return tabIndicators.get(position); }
    }

}
