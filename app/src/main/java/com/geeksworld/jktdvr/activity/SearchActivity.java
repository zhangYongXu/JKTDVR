package com.geeksworld.jktdvr.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.adapter.RecyclerMainFrag0ViewItemAdapter;
import com.geeksworld.jktdvr.adapter.SearchAdapter;
import com.geeksworld.jktdvr.fragment.FragMain0TabContentFragment;
import com.geeksworld.jktdvr.model.HomeItemModel;

import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.tools.Url;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by xhs on 2017/11/7.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private EditText title_search;

    private String searchContent;
    private SharedPreferences share;

    private HomeViewModel homeViewModel;

    private HomeTagModel homeTagModel;

    private RecyclerView recyclerView;
    private RecyclerMainFrag0ViewItemAdapter itemAdapter;
    private SmartRefreshLayout refreshView;

    private Activity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initData();
        initView();
        initAdapter();
    }

    @Override
    protected void initData() {
        super.initData();
        activity = this;
        share = ShareKey.getShare(this);
        homeViewModel = new HomeViewModel(this,this);
        homeTagModel = homeViewModel.getTagModelInstance();
    }

    protected void initView() {
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(this);
        inTitle.findViewById(R.id.title_right).setOnClickListener(this);

        title_search = inTitle.findViewById(R.id.title_name);

        // 列表
        recyclerView = (RecyclerView) findViewById(R.id.recycleListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.requestFocus();

        refreshView = (SmartRefreshLayout) findViewById(R.id.refreshView);
        refreshView.setOnRefreshListener(new SearchActivity.OnRefresh());
        refreshView.setOnLoadmoreListener(new SearchActivity.OnLoadMore());

        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);

        title_search.setOnEditorActionListener(this);
        title_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchContent = title_search.getText().toString();
            }
        });

    }

    private void   initAdapter() {
        itemAdapter = new RecyclerMainFrag0ViewItemAdapter(homeTagModel.getHomeTagDataModel().getDataList(),activity);
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.setItemClickListener(new RecyclerMainFrag0ViewItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeItemModel model = itemAdapter.getItem(position);

                Intent intent = new Intent();
                intent.setClass(activity, PageWebActivity.class);
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
    }



    private void searchData(String searchText){

        homeViewModel.postRequestTagDataListData(false, homeTagModel, searchText, HomeItemModel.HomeItemModelContentTypeAll, new BaseViewModel.OnRequestDataComplete<HomeTagModel>() {
            @Override
            public void success(HomeTagModel homeTagModel) {
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.title_right://搜索
                homeTagModel.getHomeTagDataModel().getDataList().clear();
                searchContent = title_search.getText().toString();
                searchData(searchContent);
                break;

            default:
                break;
        }
    }




    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        searchContent = title_search.getText().toString();
        if(searchContent == null
                || searchContent.equals("")
                || searchContent.isEmpty()){

        }else {
            searchData(searchContent);
        }
        return false;
    }


    class OnRefresh implements OnRefreshListener {

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            homeTagModel.getHomeTagDataModel().setPageNumber(1);
            searchData(searchContent);
        }
    }

    class OnLoadMore implements OnLoadmoreListener {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            homeTagModel.getHomeTagDataModel().setPageNumber(homeTagModel.getHomeTagDataModel().getPageNumber()+1);
            searchData(searchContent);
        }
    }

}
