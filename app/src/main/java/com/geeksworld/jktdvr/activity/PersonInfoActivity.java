package com.geeksworld.jktdvr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseActivity;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.model.UserModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.viewModel.UserViewModel;

/**
 * Created by xhs on 2018/4/9.
 */

public class PersonInfoActivity extends BaseActivity {
    static final int SexTypeMale = 1;
    static final int SexTypeFeMale = 2;

    private int REQUESTCODE_REFRESH = 1;

    private TextView nicknameTextView;
    private ImageView headImageView;

    private SharedPreferences share;

    private UserViewModel userViewModel;

    private int u_id;

    private EditText phoneEditText;
    private EditText nicknameEditText;
    private EditText emailEditText;
    private EditText workPositonEditText;
    private RadioGroup sexRadioGroup;
    private TextView subimtTextView;
    private int sexType;
    private TextView scoreTextView;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
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
        titleView.setText("个人信息");
    }
    @Override
    protected void initData() {
        super.initData();
        share = ShareKey.getShare(this);
        userViewModel = new UserViewModel(this,this);
        u_id = share.getInt(ShareKey.UID, 0);

        phoneNumber = share.getString(ShareKey.PHONE,"");
    }
    @Override
    protected void initView() {
        super.initView();
        nicknameTextView = (TextView) findViewById(R.id.main_page4_name);
        headImageView = (ImageView) findViewById(R.id.main_page4_head);
        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        phoneEditText = (EditText) findViewById(R.id.phoneNumber);
        phoneEditText.setText(phoneNumber);
        nicknameEditText = (EditText) findViewById(R.id.nickName);
        emailEditText = (EditText) findViewById(R.id.email);
        workPositonEditText = (EditText) findViewById(R.id.position);
        sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);
        sexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                if(id == R.id.sexRadioButtonMale){
                    sexType = SexTypeMale;
                }else if(id == R.id.sexRadioButtonFemale){
                    sexType = SexTypeFeMale;
                }
            }
        });

        subimtTextView = (TextView) findViewById(R.id.submit);
        subimtTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        scoreTextView = (TextView)findViewById(R.id.scoreTextView);
    }


    private void loadData(){
        String u_id_str = u_id+"";
        userViewModel.postRequestUserInfo(u_id_str, new BaseViewModel.OnRequestDataComplete<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                if (!Tool.isNull(userModel.getImg_url()) && PersonInfoActivity.this != null)
                    Glide.with(PersonInfoActivity.this).load(userModel.getImg_url()).into(headImageView);
                nicknameEditText.setText(userModel.getNick_name());
                nicknameTextView.setText(userModel.getNick_name());
                scoreTextView.setText("积分:"+userModel.getPoint());
                emailEditText.setText(userModel.getEmail());
                workPositonEditText.setText(userModel.getCompany());
                if(userModel.getSex() == 1){
                    sexRadioGroup.check(R.id.sexRadioButtonMale);
                }else {
                    sexRadioGroup.check(R.id.sexRadioButtonFemale);
                }
            }

            @Override
            public void failed(String error) {

            }
        });
    }

    private void updateData(){
        String nickStr = nicknameEditText.getText().toString().trim();
        String emailStr = emailEditText.getText().toString().trim();
        String workStr = workPositonEditText.getText().toString().trim();
        int sexStr = sexType;
        if (Tool.isNull(nickStr)) {
            Tool.toast(PersonInfoActivity.this, "请填写昵称");
            return;
        }
        if (Tool.isNull(emailStr)) {
            Tool.toast(PersonInfoActivity.this, "请填写邮箱");
            return;
        } if (Tool.isNull(workStr)) {
            Tool.toast(PersonInfoActivity.this, "请填写单位");
            return;
        }
        if (!(sexType == SexTypeMale || sexType == SexTypeFeMale)) {
            Tool.toast(PersonInfoActivity.this, "请选择性别");
            return;
        }

        UserModel userModel = new UserModel();
        userModel.setU_id(u_id);
        userModel.setNick_name(nickStr);
        userModel.setEmail(emailStr);
        userModel.setCompany(workStr);
        userModel.setSex(sexType);
        userViewModel.postRequestUpdateUserInfo(userModel, new BaseViewModel.OnRequestDataComplete<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                Tool.toast(PersonInfoActivity.this, "个人信息修改完成");
                loadData();
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
