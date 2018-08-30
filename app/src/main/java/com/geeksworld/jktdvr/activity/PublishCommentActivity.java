package com.geeksworld.jktdvr.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.model.CommentItemModel;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.viewModel.CommentViewModel;

/**
 * Created by xhs on 2018/8/28.
 */

public class PublishCommentActivity extends BaseActivity {
    private EditText contentEditText;
    private Button saveButton;
    private Activity activity;
    private HomeItemModel homeItemModel;
    private CommentViewModel commentViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_comment);
        initNavigationView();
        initData();
        initView();
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
        titleView.setText("发表评论");
    }

    @Override
    protected void initData() {
        super.initData();
        activity = this;
        homeItemModel = (HomeItemModel) getIntent().getSerializableExtra(HomeItemModel.SerializableKey);
        commentViewModel = new CommentViewModel(this,this);
    }

    @Override
    protected void initView() {
        super.initView();

        contentEditText = (EditText)findViewById(R.id.contentEditText);
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publisthComment();
            }
        });
    }

    private boolean validate(){
        if(Tool.isNull(contentEditText.getText().toString())){
            Tool.toast(activity,"请输入内容");
            return false;
        }
        return true;
    }

    private void  publisthComment(){
        if(!validate()){
            return;
        }

        CommentItemModel model = new CommentItemModel();
        model.setContent(contentEditText.getText().toString());
        model.setImgId(homeItemModel.getId());

        commentViewModel.postRequestPublishVRComentWork(model, new BaseViewModel.OnRequestDataComplete<CommentItemModel>() {
            @Override
            public void success(CommentItemModel commentItemModel) {
                Intent intent = new Intent(PublishCommentActivity.this, CommentListActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void failed(String error) {

            }
        });
    }
}
