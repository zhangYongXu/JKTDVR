package com.geeksworld.jktdvr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.activity.PageWebActivity;
import com.geeksworld.jktdvr.adapter.RecyclerMainFrag0ViewItemAdapter;
import com.geeksworld.jktdvr.adapter.RecyclerMineWorkViewItemAdapter;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.Url;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created by xhs on 2018/4/2.
 */

public class MineWorkTabContentFragment extends BaseFragment {

    //列表
    private RecyclerView recyclerView;
    private RecyclerMineWorkViewItemAdapter itemAdapter;
    private SmartRefreshLayout refreshView;




    private int type ;
    private HomeViewModel homeViewModel;
    private HomeTagModel homeTagModel;



    public static MineWorkTabContentFragment newInstance(int type) {
        MineWorkTabContentFragment newFragment = new MineWorkTabContentFragment();
        newFragment.type = type;
        return newFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.frag_main_mine_work_tab_content_fragment, container, false);
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
    protected void initView(View view){
        // 列表
        recyclerView = view.findViewById(R.id.recycleListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.requestFocus();

        refreshView = view.findViewById(R.id.refreshView);
        refreshView.setOnRefreshListener(new MineWorkTabContentFragment.OnRefresh());
        refreshView.setOnLoadmoreListener(new MineWorkTabContentFragment.OnLoadMore());
    }
    private void initAdapter(){
        itemAdapter = new RecyclerMineWorkViewItemAdapter(homeTagModel.getHomeTagDataModel().getDataList(),getContext());
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.setItemClickListener(new RecyclerMineWorkViewItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeItemModel model = itemAdapter.getItem(position);

                Intent intent = new Intent();
                intent.setClass(getActivity(), PageWebActivity.class);
                String url = Url.BASE_HOST ;
                if(model.getType() == HomeItemModel.HomeItemModelContentTypePicture){
                    url = url + "pub/webvrIndex?picUrl=" + model.getImgUrl();
                }else {
                    url = url + "/pub/webvrPlayer?picUrl=" + model.getVideoUrl();
                }
                intent.putExtra(PageWebActivity.INTENT_EXTRA_URL_KEY,url);
                startActivity(intent);
            }
        });

        itemAdapter.setOnButtonClickListener(new RecyclerMineWorkViewItemAdapter.OnButtonClickListener() {
            @Override
            public void onButtonEditClick(int position) {
                Log.i("","edit");
            }

            @Override
            public void onButtonDeleteClick(int position) {
                Log.i("","deelte");
            }
        });
    }

    private void loadData() {

        homeViewModel.postRequestTagDataListData(true,homeTagModel,"",type, new BaseViewModel.OnRequestDataComplete<HomeTagModel>() {
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
