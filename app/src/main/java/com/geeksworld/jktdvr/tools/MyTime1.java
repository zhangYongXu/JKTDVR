package com.geeksworld.jktdvr.tools;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.viewModel.UserViewModel;

import java.util.Random;

/**
 * Created by xhs on 2017/11/7.
 */

public class MyTime1 extends CountDownTimer {
    private TextView textView;
    private Context context;
    private  UserViewModel userViewModel;

    public MyTime1(Context context, TextView textView, long millisInFuture, long countDownInterval, UserViewModel userViewModel) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.context = context;
        this.userViewModel = userViewModel;
    }

    @Override
    public void onTick(long l) {
        int min=3;
        int max=59;
        Random random = new Random();
        int num = random.nextInt(max)%(max-min+1) + min;
        if(num<l / 1000){
            textView.setText("验证码:"+userViewModel.getCurrentValidateCode());
            this.cancel();
        }else {
            textView.setText(context.getResources().getString(R.string.hint_codeRe) + l / 1000);
        }
    }

    @Override
    public void onFinish() {
        textView.setText(R.string.hint_code);
        textView.setClickable(true);
    }

}