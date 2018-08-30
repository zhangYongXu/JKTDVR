package com.geeksworld.jktdvr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.adapter.RecyclerCommentViewItemAdapter;
import com.geeksworld.jktdvr.adapter.RecyclerMainFrag0ViewItemAdapter;
import com.geeksworld.jktdvr.fragment.Frag_main1;
import com.geeksworld.jktdvr.model.CommentItemModel;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.viewModel.CommentViewModel;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * Created by xhs on 2018/8/28.
 */

public class CommentListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerCommentViewItemAdapter itemAdapter;
    private SmartRefreshLayout refreshView;


    private CommentViewModel commentViewModel;

    private HomeItemModel homeItemModel;

    private Activity activity;

    private int REQUESTCODE_REFRESH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        initNavigationView();
        initData();
        initView();
        initAdapter();
        loadData();
    }

    @Override
    protected void initData() {
        super.initData();
        homeItemModel = (HomeItemModel) getIntent().getSerializableExtra(HomeItemModel.SerializableKey);
        commentViewModel = new CommentViewModel(this,this);
        activity = this;
    }

    @Override
    protected void initView() {
        super.initView();

        // 列表
        recyclerView = (RecyclerView) findViewById(R.id.recycleListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.requestFocus();



        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout)findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);
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
        TextView rightTextView =  inTitle.findViewById(R.id.title_right);
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText("发表评论");
        rightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(activity, PublishCommentActivity.class);
                intent.putExtra(HomeItemModel.SerializableKey,homeItemModel);
                startActivityForResult(intent,REQUESTCODE_REFRESH);
            }
        });

        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);

        TextView titleView = inTitle.findViewById(R.id.title_name);
        titleView.setText("社区评论");
    }
    private void initAdapter(){
        itemAdapter = new RecyclerCommentViewItemAdapter(commentViewModel.getDataList(),this);
        recyclerView.setAdapter(itemAdapter);

    }

    private void loadData(){
        commentViewModel.postRequestCommentListData(homeItemModel, new BaseViewModel.OnRequestDataComplete<List<CommentItemModel>>() {
            @Override
            public void success(List<CommentItemModel> commentItemModels) {

                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void failed(String error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_REFRESH) {
                loadData();
            }
        }
    }
}
