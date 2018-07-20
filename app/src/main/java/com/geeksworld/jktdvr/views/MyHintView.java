package com.geeksworld.jktdvr.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jude.rollviewpager.HintView;

/**
 * Created by xhs on 2018/4/24.
 */

public class MyHintView extends RelativeLayout implements HintView {

    public MyHintView(Context context) {
        this(context, null);
    }

    public MyHintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

      //LayoutInflater.from(context).inflate(R.layout.title_bar, this);

    }

    public MyHintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void initView(int length, int gravity) {

    }

    @Override
    public void setCurrent(int current) {

    }
}
