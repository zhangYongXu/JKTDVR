package com.geeksworld.jktdvr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.activity.PageWebActivity;
import com.geeksworld.jktdvr.activity.PlayerActivity;
import com.geeksworld.jktdvr.adapter.RecyclerMainFrag0ViewItemAdapter;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.Url;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created by xhs on 2017/10/23.
 */

public class Frag_main2 extends BaseFragment{

    private RecyclerView recyclerView;
    private RecyclerMainFrag0ViewItemAdapter itemAdapter;
    private SmartRefreshLayout refreshView;


    private HomeViewModel homeViewModel;

    private HomeTagModel homeTagModel;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.frag_main2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        loadData();
    }

    @Override
    protected void initData() {
        super.initData();
        homeViewModel = new HomeViewModel(getActivity(),getContext());
        homeTagModel = homeViewModel.getTagModelInstance();
    }

    @Override
    protected void initView(View view) {

        // 列表
        recyclerView = view.findViewById(R.id.recycleListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.requestFocus();

        refreshView = view.findViewById(R.id.refreshView);
        refreshView.setOnRefreshListener(new Frag_main2.OnRefresh());
        refreshView.setOnLoadmoreListener(new Frag_main2.OnLoadMore());
        RelativeLayout topStatusViewRelativeLayout = view.findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(getContext(),topStatusViewRelativeLayout);

    }
    private void initAdapter(){

        itemAdapter = new RecyclerMainFrag0ViewItemAdapter(homeTagModel.getHomeTagDataModel().getDataList(),getContext());
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.setItemClickListener(new RecyclerMainFrag0ViewItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeItemModel model = itemAdapter.getItem(position);

                if(model.getType() == HomeItemModel.HomeItemModelContentTypePicture){
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PageWebActivity.class);
                    String url = model.getShowUrl();
                    intent.putExtra(PageWebActivity.INTENT_EXTRA_URL_KEY,url);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PlayerActivity.class);
                    intent.putExtra(HomeItemModel.SerializableKey,model);
                    startActivity(intent);
                }
            }
        });
    }
    private void loadData() {

        homeViewModel.postRequestTagDataListData(false,homeTagModel,"",HomeItemModel.HomeItemModelContentTypeVideo, new BaseViewModel.OnRequestDataComplete<HomeTagModel>() {
            @Override
            public void success(HomeTagModel publishTagModel) {
                itemAdapter.notifyDataSetChanged();
                refreshView.finishRefresh();
                refreshView.finishLoadmore();
            }

            @Override
            public void failed(String error) {
                refreshView.finishRefresh();
                refreshView.finishLoadmore();
            }
        });

    }

    class OnRefresh implements OnRefreshListener {

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            homeTagModel.getHomeTagDataModel().setPageNumber(1);
            loadData();
        }
    }

    class OnLoadMore implements OnLoadmoreListener {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            homeTagModel.getHomeTagDataModel().setPageNumber(homeTagModel.getHomeTagDataModel().getPageNumber()+1);
            loadData();
        }
    }
}
