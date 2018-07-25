package com.geeksworld.jktdvr.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.widget.TextView;

import com.geeksworld.jktdvr.R;


/**
 * 确认对话框
 *
 * @author: xiaoluo
 * @date: 2016-12-21 18:39
 */
public class ProgressDialog extends Dialog {


    private ContentLoadingProgressBar contentLoadingProgressBar;
    private TextView progressTextView;

    public ProgressDialog(Context context) {
        super(context, R.style.AppUpdateConfirmDialog);
        init();
        setCancelable(false);
    }

    private void init() {
        setContentView(R.layout.upload_dialog_progress);
        contentLoadingProgressBar = (ContentLoadingProgressBar)findViewById(R.id.contentLoadingProgressBar);
        progressTextView = (TextView)findViewById(R.id.tv_progressTextView);
    }


    public ProgressDialog setProgress(int progress){
        contentLoadingProgressBar.setProgress(progress);
        progressTextView.setText(progress+"%");
        return this;
    }

}
