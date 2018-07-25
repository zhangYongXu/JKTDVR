package com.geeksworld.jktdvr.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.activity.LoginActivity;
import com.geeksworld.jktdvr.activity.MainActivity;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.tools.UploadPhoto;
import com.geeksworld.jktdvr.tools.Url;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by xhs on 2018/4/2.
 */

public class VRWorkEditTabContentPicFragment extends BaseFragment implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 0002;



    private HomeViewModel homeViewModel;

    private EditText picNmaeEditText;
    private EditText picTypeEditText;;

    private HomeTagModel picHomeTagModel;
    private String picOriginalPath;

    private ImageView showImageView;

    private RequestManager glide;

    private HomeItemModel homeItemModel;

    public void setHomeItemModel(HomeItemModel homeItemModel) {
        this.homeItemModel = homeItemModel;
    }

    private SharedPreferences share;
    public void setHomeViewModel(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

    private HomeTagModel homeTagModel;

    public void setHomeTagModel(HomeTagModel homeTagModel) {
        this.homeTagModel = homeTagModel;
    }

    public static VRWorkEditTabContentPicFragment newInstance(HomeViewModel inhomeViewModel,HomeItemModel homeItemModel) {
        VRWorkEditTabContentPicFragment newFragment = new VRWorkEditTabContentPicFragment();
        newFragment.setHomeViewModel(inhomeViewModel);
        newFragment.setHomeItemModel(homeItemModel);
        return newFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.frag_main3_tab_content_pic_edit_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void initData() {
        super.initData();
        glide = Glide.with(this);
        share = ShareKey.getShare(getActivity());

        for(HomeTagModel model : homeViewModel.getAllTagList()){
            if(!Tool.isNull(model.getDataDicValue())){
                int dataDicValue = Integer.parseInt(model.getDataDicValue());
                if(dataDicValue == homeItemModel.getClassVal()){
                    picHomeTagModel = model;
                    break;
                }
            }
        }
    }

    @Override
    protected void initView(View view){

        picNmaeEditText = (EditText) view.findViewById(R.id.picname);
        picTypeEditText = (EditText)view.findViewById(R.id.picType);

        showImageView = (ImageView)view.findViewById(R.id.showImageView);

        picNmaeEditText.setText(homeItemModel.getTitle());
        picTypeEditText.setText(homeItemModel.getDataDicName());
        String imgUrl = homeItemModel.getImgUrl();
        Glide.with(getContext())
                .load(imgUrl)
                .error(R.drawable.hd_default_image)
                .fallback(R.drawable.hd_default_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .crossFade()
                .thumbnail(0.6f)
                .into(showImageView);


        view.findViewById(R.id.picSelectTypeLayout).setOnClickListener(this);
        view.findViewById(R.id.picSelectPicButton).setOnClickListener(this);
        view.findViewById(R.id.picType).setOnClickListener(this);
        view.findViewById(R.id.picSubmitButton).setOnClickListener(this);
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
                postData();
            }
            break;
        }
    }
    private void tipLogin(){
        new android.app.AlertDialog.Builder(getContext()).setMessage("请先登录!")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }
    private boolean validate(){
        boolean isLogin = share.getBoolean(ShareKey.ISLOGIN, false);
        if (!isLogin) {
            tipLogin();
            return false;
        }
        String name = picNmaeEditText.getText().toString();
        if(Tool.isNull(name)){
            Tool.toast(getContext(),"请输入作品名称");
            return false;
        }
        if(null == picHomeTagModel){
            Tool.toast(getContext(),"请选择作品类型");
            return false;
        }

        return true;
    }
    private void postData(){


        if(!validate()){
            return;
        }
        String name = picNmaeEditText.getText().toString();
        String tagId = picHomeTagModel.getId()+"";
        String picPath = picOriginalPath;

        int u_id = share.getInt(ShareKey.UID, 0);
        String user_name = share.getString(ShareKey.NICK, "");

        homeItemModel.setAuthorId(u_id);
        homeItemModel.setAuthorName(user_name);
        homeItemModel.setClassVal(picHomeTagModel.getId());
        homeItemModel.setType(HomeItemModel.HomeItemModelContentTypePicture);
        homeItemModel.setTitle(name);
        homeItemModel.setVideoTextarea("");
        homeItemModel.setVideoUrl("");

        if(null == picOriginalPath){
            save(homeItemModel);
            return;
        }
        UploadPhoto.postPicForm(Url.postfileUpload, null, picPath, new UploadPhoto.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        JSONObject result1 = obj.getJSONObject("data");
                        String img_url = result1.getString("imgUrl");
                        homeItemModel.setImgUrl(img_url);

                        save(homeItemModel);

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    Tool.toast(getContext(),"上传全景图片失败,检查网络，请重试");
                }
            }

            @Override
            public void downfailed(String error) {
                Tool.toast(getContext(),"上传全景图片失败,检查网络，请重试");
            }
        });
    }
    private void save(HomeItemModel homeItemModel){
        homeViewModel.postRequestAddOrEditVRWork(true, homeItemModel, new BaseViewModel.OnRequestDataComplete<List<HomeTagModel>>() {
            @Override
            public void success(List<HomeTagModel> homeTagModels) {
                Tool.toast(getContext(),"VR全景图修改成功");
                clearUI();
            }

            @Override
            public void failed(String error) {
                Tool.toast(getContext(),"VR全景图制作失败");
            }
        });
    }
    private void clearUI(){
        picNmaeEditText.setText("");
        showImageView.setImageResource(R.drawable.hd_default_image);
        picOriginalPath = null;
        getActivity().finish();
    }
    private void selectPic(){
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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
