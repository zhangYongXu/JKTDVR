package com.geeksworld.jktdvr.tools;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;

/**
 * Created by xhs on 2017/11/7.
 */

public class MyTime extends CountDownTimer {
    private TextView textView;
    private Context context;

    public MyTime(Context context, TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.context = context;
    }

    @Override
    public void onTick(long l) {
        textView.setText(context.getResources().getString(R.string.hint_codeRe) + l / 1000);
    }

    @Override
    public void onFinish() {
        textView.setText(R.string.hint_code);
        textView.setClickable(true);
    }

}