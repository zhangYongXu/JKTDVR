package com.geeksworld.jktdvr.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.activity.PageWebActivity;
import com.geeksworld.jktdvr.adapter.RecyclerMainFrag0ViewItemAdapter;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.UploadPhoto;
import com.geeksworld.jktdvr.tools.Url;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by xhs on 2018/4/2.
 */

public class FragMain3TabContentPicFragment extends BaseFragment implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 0002;



    private HomeViewModel homeViewModel;


    private EditText picTypeEditText;;

    private HomeTagModel picHomeTagModel;
    private String picOriginalPath;

    private ImageView showImageView;

    private RequestManager glide;
    public void setHomeViewModel(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

    private HomeTagModel homeTagModel;

    public void setHomeTagModel(HomeTagModel homeTagModel) {
        this.homeTagModel = homeTagModel;
    }

    public static FragMain3TabContentPicFragment newInstance(HomeViewModel inhomeViewModel) {
        FragMain3TabContentPicFragment newFragment = new FragMain3TabContentPicFragment();
        newFragment.setHomeViewModel(inhomeViewModel);
        return newFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.frag_main3_tab_content_pic_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        glide = Glide.with(this);

    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initView(View view){


        picTypeEditText = (EditText)view.findViewById(R.id.picType);

        showImageView = (ImageView)view.findViewById(R.id.showImageView);

        view.findViewById(R.id.picSelectTypeLayout).setOnClickListener(this);
        view.findViewById(R.id.picSelectPicButton).setOnClickListener(this);
        view.findViewById(R.id.picType).setOnClickListener(this);
        view.findViewById(R.id.picSubmitButton).setOnClickListener(this);
    }


    private void loadData() {

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.picSelectTypeLayout:{
                showSelectListView();
            }
            break;
            case R.id.picType:{
                showSelectListView();
            }
            break;
            case  R.id.picSelectPicButton:{
                selectPic();
            }
            break;
            case  R.id.picSubmitButton:{
                UploadPhoto.postPicForm(Url.postfileUpload, null, picOriginalPath, new UploadPhoto.OnNetWorkResponse() {
                    @Override
                    public void downsuccess(String result) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            int code = obj.getInt("code");
                            String message = obj.getString("message");
                            if (code == 1) {
                                JSONObject result1 = obj.getJSONObject("result");
                                String img_url = result1.getString("imgUrl");



                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void downfailed(String error) {

                    }
                });
            }
            break;
        }
    }

    private void selectPic(){
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
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

            if(picHomeTagModel != null){
                selectIndex = homeViewModel.getAllTagList().indexOf(picHomeTagModel);
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

                picTypeEditText.setText((String) checkedItem);
                if (which < homeViewModel.getAllTagList().size()) {
                    picHomeTagModel = homeViewModel.getAllTagList().get(which);
                }


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            picOriginalPath = picturePath;
            cursor.close();

            glide.load(new File(picOriginalPath)).into(showImageView);
        }
    }
}
