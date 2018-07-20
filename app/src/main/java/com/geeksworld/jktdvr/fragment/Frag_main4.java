package com.geeksworld.jktdvr.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.activity.LoginActivity;

import com.geeksworld.jktdvr.activity.PersonInfoActivity;

import com.geeksworld.jktdvr.activity.SettingActivity;
import com.geeksworld.jktdvr.model.UserModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.viewModel.UserViewModel;

/**
 * Created by xhs on 2017/10/23.
 */

public class Frag_main4 extends BaseFragment implements View.OnClickListener{

    private TextView nicknameTextView;
    private ImageView headImageView;
    private TextView scoreTextView;

    private SharedPreferences share;

    private UserViewModel userViewModel;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.frag_main4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        share = ShareKey.getShare(getActivity());
        userViewModel = new UserViewModel(getActivity(),getContext());
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nicknameTextView = view.findViewById(R.id.main_page4_name);
        headImageView = view.findViewById(R.id.main_page4_head);
        scoreTextView = view.findViewById(R.id.scoreTextView);
        nicknameTextView.setOnClickListener(this);


        view.findViewById(R.id.main_page4_person_info).setOnClickListener(this);
        view.findViewById(R.id.main_page4_my_collection).setOnClickListener(this);
        view.findViewById(R.id.main_page4_settings).setOnClickListener(this);
        headImageView.setOnClickListener(this);

        RelativeLayout topStatusViewRelativeLayout = view.findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(getContext(),topStatusViewRelativeLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData(){
        boolean isLogin = share.getBoolean(ShareKey.ISLOGIN, false);
        if (!isLogin) {
            nicknameTextView.setText(R.string.unLogin);
            headImageView.setImageResource(R.mipmap.mine_head_default);
            scoreTextView.setText("");
            return;
        }else{
            nicknameTextView.setText("");
            scoreTextView.setText("");
        }
        String u_id = share.getInt(ShareKey.UID, 0)+"";
        userViewModel.postRequestUserInfo(u_id, new BaseViewModel.OnRequestDataComplete<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                if (!Tool.isNull(userModel.getImg_url()) && getActivity() != null) {
                    Glide.with(getActivity()).load(userModel.getImg_url()).into(headImageView);
                 }
                nicknameTextView.setText(userModel.getNick_name());
                scoreTextView.setText("积分:"+userModel.getPoint());
            }

            @Override
            public void failed(String error) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.main_page4_name){
            boolean isLogin = share.getBoolean(ShareKey.ISLOGIN, false);
            if(isLogin){//已经登陆
                return;
            }
        }
        if(view.getId() == R.id.main_page4_person_info
                ||view.getId() == R.id.main_page4_my_collection
                ||view.getId() == R.id.main_page4_head){
            boolean isLogin = share.getBoolean(ShareKey.ISLOGIN, false);
            if(!isLogin){//没登陆
                Toast.makeText(getActivity(),R.string.unlogin_tip_message,Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent intent = new Intent();

        switch (view.getId()){
            case R.id.main_page4_person_info:
                intent.setClass(getActivity(), PersonInfoActivity.class);
                break;
            case R.id.main_page4_my_collection:
                //intent.setClass(getActivity(), MyCollectionActivity.class);
                break;
            case R.id.main_page4_settings:
                intent.setClass(getActivity(), SettingActivity.class);
                break;
            case R.id.main_page4_name:
                intent.setClass(getActivity(), LoginActivity.class);
                break;
            case R.id.main_page4_head:
                intent.setClass(getActivity(), PersonInfoActivity.class);
                break;
        }
        startActivity(intent);
    }


}
