package com.geeksworld.jktdvr.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xhs on 2018/4/9.
 */

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{
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
    private EditText birthdayEditText;
    private RadioGroup sexRadioGroup;
    private TextView subimtTextView;
    private int sexType;
    private TextView scoreTextView;

    private String phoneNumber;

    private int selYear;
    private int selmonth;
    private int selDay;
    private String  birthtimestamp;

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
                Intent intent = new Intent();
                intent.setClass(PersonInfoActivity.this, HeadIconActivity.class);
                if (userViewModel.getCurrentUserModel() != null) {
                    intent.putExtra("head_img", userViewModel.getCurrentUserModel().getHeadImg());
                }
                startActivityForResult(intent, REQUESTCODE_REFRESH);
            }
        });

        phoneEditText = (EditText) findViewById(R.id.phoneNumber);
        phoneEditText.setText(phoneNumber);
        nicknameEditText = (EditText) findViewById(R.id.nickName);
        emailEditText = (EditText) findViewById(R.id.email);
        birthdayEditText = (EditText) findViewById(R.id.birthdayEditText);
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

        findViewById(R.id.birthdayEditText).setOnClickListener(this);
        findViewById(R.id.birthdayLayout).setOnClickListener(this);
    }


    private void loadData(){
        String u_id_str = u_id+"";
        userViewModel.postRequestUserInfo(u_id_str, new BaseViewModel.OnRequestDataComplete<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                if (!Tool.isNull(userModel.getHeadImg()) && PersonInfoActivity.this != null)
                    Glide.with(PersonInfoActivity.this).load(userModel.getHeadImg()).into(headImageView);
                nicknameEditText.setText(userModel.getNick_name());
                nicknameTextView.setText(userModel.getNick_name());

                emailEditText.setText(userModel.getEmail());
                birthdayEditText.setText(userModel.getBirth());
                if(!Tool.isNull(userModel.getBirth())){
                    birthtimestamp = getTimestamp(userModel.getBirth())+"";
                }
                if(userModel.getSex()!= null && userModel.getSex().equals("男")){
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
        String birthdayStr = birthtimestamp;
        int sex = sexType;
        if (Tool.isNull(nickStr)) {
            Tool.toast(PersonInfoActivity.this, "请填写昵称");
            return;
        }
        if (Tool.isNull(emailStr)) {
            Tool.toast(PersonInfoActivity.this, "请填写邮箱");
            return;
        } if (Tool.isNull(birthdayStr)) {
            Tool.toast(PersonInfoActivity.this, "请选择生日");
            return;
        }
        if (!(sexType == SexTypeMale || sexType == SexTypeFeMale)) {
            Tool.toast(PersonInfoActivity.this, "请选择性别");
            return;
        }

        UserModel userModel = new UserModel();
        userModel.setId(u_id);
        userModel.setNick_name(nickStr);
        userModel.setEmail(emailStr);
        userModel.setBirth(birthdayStr);
        String sexStr = sexType == SexTypeMale? "男":"女";
        userModel.setSex(sexStr);
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
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.birthdayEditText:{
                selectDate();
            }
            break;
            case R.id.birthdayLayout:{
                selectDate();
            }
            break;
        }
    }
    private void selectDate(){
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                selYear = i;
                selmonth = i1;
                selDay = i2;
                String dateStr = selYear +"-" + selmonth + "-"+selDay;

                long time = getTimestamp(dateStr);//时间戳
                birthtimestamp = time+"";
                Log.i("date:",i+","+i1+","+i2 + "t:"+time);
                birthdayEditText.setText(dateStr);
            }
        },year,month,day).show();

    }

    private long getTimestamp(String birth){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try{
            date = dateFormat.parse(birth);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
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
