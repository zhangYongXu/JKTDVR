package com.geeksworld.jktdvr.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.geeksworld.jktdvr.fragment.FragMain3TabContentPicFragment;
import com.geeksworld.jktdvr.fragment.FragMain3TabContentVideoFragment;
import com.geeksworld.jktdvr.fragment.Frag_main3;
import com.geeksworld.jktdvr.fragment.MineWorkTabContentFragment;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.WebViewFontShare;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xhs on 2018/4/9.
 */

public class MineWorkActivity extends BaseActivity{
    private SharedPreferences share;

    private TabLayout tabLayout;


    private ViewPager viewPager;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private MineWorkActivity.ContentPagerAdapter contentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_work);
        initData();
        initNavigationView();
        initView();
        initContent();
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
        titleView.setText("我的VR");
    }
    @Override
    protected void initData() {
        super.initData();
        share = ShareKey.getShare(this);

        tabIndicators = new ArrayList<>();
        tabFragments = new ArrayList<>();
    }


    @Override
    protected void initView() {
        super.initView();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

    }

    private void initContent(){
        tabLayout.setupWithViewPager(viewPager);

        String titlepic = "VR图";
        tabLayout.addTab(tabLayout.newTab().setText(titlepic));
        String titlevideo = "VR视频";
        tabLayout.addTab(tabLayout.newTab().setText(titlevideo));

        tabIndicators.add(titlepic);
        tabIndicators.add(titlevideo);


        MineWorkTabContentFragment picF = MineWorkTabContentFragment.newInstance(HomeItemModel.HomeItemModelContentTypePicture);
        tabFragments.add(picF);


        MineWorkTabContentFragment videoF = MineWorkTabContentFragment.newInstance(HomeItemModel.HomeItemModelContentTypeVideo);
        tabFragments.add(videoF);

        contentAdapter = new MineWorkActivity.ContentPagerAdapter(getSupportFragmentManager());
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
