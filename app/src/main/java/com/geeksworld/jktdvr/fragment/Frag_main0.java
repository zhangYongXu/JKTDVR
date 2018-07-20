package com.geeksworld.jktdvr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseViewModel;

import com.geeksworld.jktdvr.model.HomeItemModel;

import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhs on 2017/10/23.
 */

public class Frag_main0 extends BaseFragment {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;


    private HomeViewModel homeViewModel;
    private View title_container;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.frag_main0, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabLayout();
        loadData();
    }
     @Override
    protected void initData() {
        super.initData();

        homeViewModel = new HomeViewModel(getActivity(),getContext());

    }

    @Override
    protected void initView(View view){
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        title_container = view.findViewById(R.id.title_container);


        RelativeLayout topStatusViewRelativeLayout = view.findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(getContext(),topStatusViewRelativeLayout);
    }


    private void initTabLayout(){
        tabLayout.setupWithViewPager(viewPager);

    }

    private void loadData(){
        if(ShareKey.isUseTestData){
            initContent();
        }
        homeViewModel.postRequestAllTagListData(new BaseViewModel.OnRequestDataComplete<List<HomeTagModel>>() {
            @Override
            public void success(List<HomeTagModel> publishTagModels) {
                initContent();

            }

            @Override
            public void failed(String error) {

            }
        });
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
        tabFragments = new ArrayList<>();

        for(HomeTagModel publishTagModel : homeViewModel.getAllTagList()){
            String title = publishTagModel.getDataDicName();
            tabIndicators.add(title);

            FragMain0TabContentFragment f = FragMain0TabContentFragment.newInstance(homeViewModel,publishTagModel);
            tabFragments.add(f);
        }

        contentAdapter = new ContentPagerAdapter(getChildFragmentManager());
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
