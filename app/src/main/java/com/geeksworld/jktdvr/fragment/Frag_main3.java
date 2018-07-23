package com.geeksworld.jktdvr.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseViewModel;

import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.TimeUtils;

import com.geeksworld.jktdvr.viewModel.HomeViewModel;
import com.geeksworld.jktdvr.views.MyViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xhs on 2017/10/23.
 */

public class Frag_main3 extends BaseFragment {




    private TabLayout tabLayout;

    private HomeViewModel homeViewModel;

    private ViewPager viewPager;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private Frag_main3.ContentPagerAdapter contentAdapter;


    private View title_container;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.frag_main3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initContent();
        loadData();
    }


    @Override
    protected void initData() {
        super.initData();
        homeViewModel = new HomeViewModel(getActivity(),getContext());
        tabIndicators = new ArrayList<>();
        tabFragments = new ArrayList<>();
    }

    private void loadData(){
        homeViewModel.postRequestAllTagListData(false, new BaseViewModel.OnRequestDataComplete<List<HomeTagModel>>() {
            @Override
            public void success(List<HomeTagModel> homeTagModels) {

            }

            @Override
            public void failed(String error) {

            }
        });

    }




    @Override
    protected void initView(View view){
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        title_container = view.findViewById(R.id.title_container);


        RelativeLayout topStatusViewRelativeLayout = view.findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(getContext(),topStatusViewRelativeLayout);



    }


    private void initContent(){
        tabLayout.setupWithViewPager(viewPager);

        String titlepic = "制作VR图";
        tabLayout.addTab(tabLayout.newTab().setText(titlepic));
        String titlevideo = "制作VR视频";
        tabLayout.addTab(tabLayout.newTab().setText(titlevideo));

        tabIndicators.add(titlepic);
        tabIndicators.add(titlevideo);


        FragMain3TabContentPicFragment picF = FragMain3TabContentPicFragment.newInstance(homeViewModel);
        tabFragments.add(picF);

//        FragMain3TabContentPicFragment picF1 = FragMain3TabContentPicFragment.newInstance(homeViewModel);
//        tabFragments.add(picF1);

        FragMain3TabContentVideoFragment videoF = FragMain3TabContentVideoFragment.newInstance(homeViewModel);
        tabFragments.add(videoF);

        contentAdapter = new Frag_main3.ContentPagerAdapter(getChildFragmentManager());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
