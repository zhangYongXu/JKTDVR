package com.geeksworld.jktdvr.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;

/**
 * Created by xhs on 2018/4/2.
 */

public class FragMain3TabContentVideoFragment extends BaseFragment implements View.OnClickListener{




    private HomeViewModel homeViewModel;


    private EditText videoTypeEditText;


    private HomeTagModel videoHomeTagModel;



    public void setHomeViewModel(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

    private HomeTagModel homeTagModel;

    public void setHomeTagModel(HomeTagModel homeTagModel) {
        this.homeTagModel = homeTagModel;
    }

    public static FragMain3TabContentVideoFragment newInstance(HomeViewModel inhomeViewModel) {
        FragMain3TabContentVideoFragment newFragment = new FragMain3TabContentVideoFragment();
        newFragment.setHomeViewModel(inhomeViewModel);
        return newFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.frag_main3_tab_content_video1_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initView(View view){


        videoTypeEditText = (EditText)view.findViewById(R.id.picType);


        view.findViewById(R.id.videoSelectTypeLayout).setOnClickListener(this);
        view.findViewById(R.id.videoSelectPicButton).setOnClickListener(this);
        view.findViewById(R.id.videoType).setOnClickListener(this);
        view.findViewById(R.id.videoSubmitButton).setOnClickListener(this);
    }


    private void loadData() {

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.videoSelectTypeLayout:{
                showSelectListView();
            }
            break;
            case R.id.videoType:{
                showSelectListView();
            }
            break;
            case  R.id.videoSelectPicButton:{

            }
            break;
            case  R.id.videoSubmitButton:{

            }
            break;
        }
    }

    private void showSelectListView(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int size = homeViewModel.getAllTagList().size();
        String[] mItems = new String[size];
        for(int i = 0;i<size;i++){
            String value = homeViewModel.getAllTagList().get(i).getDataDicName();
            mItems[i] = value;
        }
        builder.setTitle("选择作品类型");
        int selectIndex = 0;
        for(HomeTagModel homeTagModel : homeViewModel.getAllTagList()){

            if(videoHomeTagModel != null){
                selectIndex = homeViewModel.getAllTagList().indexOf(videoHomeTagModel);
                break;
            }

        }
        builder.setSingleChoiceItems(mItems, selectIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog) dialog).getListView();
                // which表示点击的条目
                Object checkedItem = lw.getAdapter().getItem(which);
                // 既然你没有cancel或者ok按钮，所以需要在点击item后使dialog消失
                dialog.dismiss();
                // 更新你的view

                videoTypeEditText.setText((String) checkedItem);
                if (which < homeViewModel.getAllTagList().size()) {
                    videoHomeTagModel = homeViewModel.getAllTagList().get(which);
                }


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
