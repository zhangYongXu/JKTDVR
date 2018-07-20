package com.geeksworld.jktdvr.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.geeksworld.jktdvr.R;


/**
 * Created by xhs on 2018/5/14.
 */

public class CommonSaveImageDialog extends Dialog implements View.OnClickListener{

    private RelativeLayout saveLayout;
    private RelativeLayout cancelLayout;

    public interface OnCommonSaveImageDialogClickListener{
        void saveClicked();
        void cancelClicked();
    }



    private OnCommonSaveImageDialogClickListener clickListener;

    public void setClickListener(OnCommonSaveImageDialogClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public CommonSaveImageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_save_image_dialog_confirm);
        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initData();
        initView();
    }

    private void initData(){

    }

    private void initView(){
        saveLayout = (RelativeLayout)findViewById(R.id.SaveImageLayout);
        cancelLayout = (RelativeLayout)findViewById(R.id.CancelLayout);

        saveLayout.setOnClickListener(this);
        cancelLayout.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.SaveImageLayout:{
                dismiss();
                if(null != clickListener){
                    clickListener.saveClicked();
                }
            }
                break;
            case R.id.CancelLayout:{
                dismiss();
                if(null != clickListener){
                    clickListener.cancelClicked();
                }
            }
                break;
        }
    }
}
