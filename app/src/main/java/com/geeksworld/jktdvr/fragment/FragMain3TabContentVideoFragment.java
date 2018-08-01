package com.geeksworld.jktdvr.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.aBase.BaseViewModel;
import com.geeksworld.jktdvr.activity.LoginActivity;
import com.geeksworld.jktdvr.activity.MainActivity;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.model.HomeTagModel;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.tools.UploadPhoto;
import com.geeksworld.jktdvr.tools.UploadVideo;
import com.geeksworld.jktdvr.tools.UriUtils;
import com.geeksworld.jktdvr.tools.Url;
import com.geeksworld.jktdvr.viewModel.HomeViewModel;
import com.geeksworld.jktdvr.views.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by xhs on 2018/4/2.
 */

public class FragMain3TabContentVideoFragment extends BaseFragment implements View.OnClickListener{


    private static final int RESULT_LOAD_IMAGE = 0003;
    private static final int RESULT_LOAD_VIDEO = 0004;

    private interface OnUploadComplete{
        void uploadComplete(String url);
    };

    private HomeViewModel homeViewModel;

    private EditText videoTypeEditText;
    private EditText videoNameEditText;
    private EditText videoIntroductionEditText;

    private String picOriginalPath;

    private String videoOriginalPath;


    private HomeTagModel videoHomeTagModel;

    private RequestManager glide;

    private HomeItemModel homeItemModel;

    private SharedPreferences share;

    private ImageView showImageView;

    //private VideoView videoView;
    private LinearLayout videoViewLayout;

    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;

    private ProgressDialog progressDialog;

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

        //videoView.setFocusable(false);

    }

    @Override
    protected void initData() {
        super.initData();
        glide = Glide.with(this);
        share = ShareKey.getShare(getActivity());
        homeItemModel = new HomeItemModel();
        player=new MediaPlayer();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgress(0);
    }

    @Override
    protected void initView(View view){


        videoTypeEditText = (EditText)view.findViewById(R.id.videoType);

        videoNameEditText = (EditText)view.findViewById(R.id.videoname);
        videoIntroductionEditText = (EditText)view.findViewById(R.id.videointroduction);
        showImageView = (ImageView)view.findViewById(R.id.showImageView);

        //videoView = (VideoView) view.findViewById(R.id.videoView);
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                videoView.start();
//            }
//        });

        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);

        videoViewLayout = (LinearLayout) view.findViewById(R.id.videoViewLayout);

        videoViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(videoView.isPlaying()){
//                    videoView.pause();
//                }else {
//                    videoView.start();
//                }
            }
        });


        view.findViewById(R.id.videoSelectTypeLayout).setOnClickListener(this);
        view.findViewById(R.id.videoSelectPicButton).setOnClickListener(this);
        view.findViewById(R.id.videoType).setOnClickListener(this);
        view.findViewById(R.id.videoSubmitButton).setOnClickListener(this);
        view.findViewById(R.id.videoSelectVideoButton).setOnClickListener(this);

        initPlayer();
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
                selectPic();
            }
            break;
            case  R.id.videoSelectVideoButton:{
                selectVideo();
            }
            break;
            case  R.id.videoSubmitButton:{
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
        String name = videoNameEditText.getText().toString();
        if(Tool.isNull(name)){
            Tool.toast(getContext(),"请输入作品名称");
            return false;
        }
        if(null == videoHomeTagModel){
            Tool.toast(getContext(),"请选择作品类型");
            return false;
        }
        if(Tool.isNull(picOriginalPath)){
            Tool.toast(getContext(),"请选择视频封面图片");
            return false;
        }
        if(Tool.isNull(videoOriginalPath)){
            Tool.toast(getContext(),"请选择视频");
            return false;
        }
        String intro = videoIntroductionEditText.getText().toString();
        if(Tool.isNull(intro)){
            Tool.toast(getContext(),"请输入视频简介");
            return false;
        }
        return true;
    }
    private void postData(){
        if(!validate()){
            return;
        }
        String name = videoNameEditText.getText().toString();
        String tagId = videoHomeTagModel.getId()+"";
        String picPath = picOriginalPath;
        final String videoPath = videoOriginalPath;
        String intro = videoIntroductionEditText.getText().toString();

        int u_id = share.getInt(ShareKey.UID, 0);
        String user_name = share.getString(ShareKey.NICK, "");

        homeItemModel.setAuthorId(u_id);
        homeItemModel.setAuthorName(user_name);
        homeItemModel.setClassVal(videoHomeTagModel.getId());
        homeItemModel.setType(HomeItemModel.HomeItemModelContentTypeVideo);
        homeItemModel.setTitle(name);
        homeItemModel.setVideoTextarea(intro);
        homeItemModel.setVideoUrl("");

        uploadPic(homeItemModel, picPath, new OnUploadComplete() {
            @Override
            public void uploadComplete(String url) {
                uploadVideo(homeItemModel, videoPath, new OnUploadComplete() {
                    @Override
                    public void uploadComplete(String url) {
                        saveData(homeItemModel);
                    }
                });
            }
        });
    }
    //上传图片
    private void uploadPic(final HomeItemModel homeItemModel, String picPath, final OnUploadComplete complete){
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
                        complete.uploadComplete(img_url);
                    }else {
                        Tool.toast(getContext(),"上传视频封面图片失败,检查网络，请重试");
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    Tool.toast(getContext(),"上传视频封面图片失败,检查网络，请重试");
                }
            }
            @Override
            public void downfailed(String error) {
                Tool.toast(getContext(),"上传视频封面图片失败,检查网络，请重试");
            }
        });
    }
    //上传视频
    private void uploadVideo(final HomeItemModel homeItemModel, String videoPath , final OnUploadComplete complete){
        progressDialog.show();
        UploadVideo.postVideoForm2(Url.postfileUpload, null, videoPath, new UploadVideo.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        JSONObject result1 = obj.getJSONObject("data");
                        String img_url = result1.getString("imgUrl");
                        homeItemModel.setVideoUrl(img_url);
                        complete.uploadComplete(img_url);
                    }else {
                        Tool.toast(getContext(),"上传视频失败,检查网络，请重试");
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    Tool.toast(getContext(),"上传视频失败,检查网络，请重试");
                }
            }
            @Override
            public void uploadProgress(long currentBytes, long contentLength, boolean isUploadComplete) {
                int progress = (int) ((currentBytes / (float) contentLength)*100);
                Log.i("uploadProgress:",progress+"");
                progressDialog.setProgress(progress);
            }
            @Override
            public void downfailed(String error) {
                progressDialog.dismiss();
                Tool.toast(getContext(),"上传视频失败,检查网络，请重试");
            }
        });
    }
    //保存数据
    private void saveData(HomeItemModel homeItemModel){
        homeViewModel.postRequestAddOrEditVRWork2(false, homeItemModel, new BaseViewModel.OnRequestDataComplete<List<HomeTagModel>>() {
            @Override
            public void success(List<HomeTagModel> homeTagModels) {
                Tool.toast(getContext(),"VR视频制作成功");
                clearUI();
            }

            @Override
            public void failed(String error) {
                Tool.toast(getContext(),"VR视频制作失败");
            }
        });
    }
    private void clearUI(){
        videoNameEditText.setText("");
        videoIntroductionEditText.setText("");
        showImageView.setImageResource(R.drawable.hd_default_image);
        picOriginalPath = null;
        videoOriginalPath = null;
        player.pause();
        videoViewLayout.setVisibility(View.GONE);
        videoTypeEditText.setText("");
        videoHomeTagModel = null;
        MainActivity.MainActivityInstance(getActivity()).selectPage(0);
    }


    private void selectPic(){
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    private void selectVideo(){

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent wrapperIntent = Intent.createChooser(intent, null);
        startActivityForResult(wrapperIntent,
               RESULT_LOAD_VIDEO);

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

    @Override
    public void onResume() {
        super.onResume();
        if(null != player) {
            player.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(null != player) {
            player.pause();
        }
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
            cursor.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//这个参数设置为true才有效，
            Bitmap bmp = BitmapFactory.decodeFile(picturePath,options);//这里的bitmap是个空
            if(bmp==null){
                Log.e("通过options获取到的bitmap为空","===");
            }

            int outWidth= options.outWidth;
            int outHeight=options.outHeight;
            if(outWidth/2 != outHeight){
                Tool.toast(getContext(),"视频封面图片必须是宽高比为2:1的图片");
                return;
            }

            picOriginalPath = picturePath;
            glide.load(new File(picOriginalPath)).into(showImageView);
        }else if(requestCode == RESULT_LOAD_VIDEO && resultCode == getActivity().RESULT_OK && null != data){
            Uri uri = data.getData();

            String videoPath = UriUtils.getPath(getContext(),uri);


            MediaMetadataRetriever retr = new MediaMetadataRetriever();
            retr.setDataSource(getContext() , uri);
            Bitmap bm = retr.getFrameAtTime(0,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

            int wVideo = bm.getWidth();
            int hVideo = bm.getHeight();
            if(wVideo/2 != hVideo){
                Tool.toast(getContext(),"视频必须是宽高比为2:1的全景视频");
                return;
            }
            if(hVideo<640){
                Tool.toast(getContext(),"视频分辨率最小为 宽1280px 高640px");
                return;
            }
            videoOriginalPath = videoPath;
            videoViewLayout.setVisibility(View.VISIBLE);
            startPlayer(uri);
        }
    }

    private void initPlayer(){

        holder=surfaceView.getHolder();
        holder.addCallback(new MyCallBack());

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 首先取得video的宽和高
                int vWidth = player.getVideoWidth();
                int vHeight = player.getVideoHeight();

                // 该LinearLayout的父容器 android:orientation="vertical" 必须
                LinearLayout linearLayout = videoViewLayout;
                int lw = linearLayout.getWidth();
                int lh = linearLayout.getHeight();

                if (vWidth > lw || vHeight > lh) {
                    // 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
                    float wRatio = (float) vWidth / (float) lw;
                    float hRatio = (float) vHeight / (float) lh;

                    // 选择大的一个进行缩放
                    float ratio = Math.max(wRatio, hRatio);
                    vWidth = (int) Math.ceil((float) vWidth / ratio);
                    vHeight = (int) Math.ceil((float) vHeight / ratio);

                    // 设置surfaceView的布局参数
                    LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(vWidth, vHeight);
                    lp.gravity = Gravity.CENTER;
                    surfaceView.setLayoutParams(lp);
                }

                player.start();
                player.setLooping(true);
            }
        });

    }
    private void startPlayer(Uri uri){

        try {
            if(player != null){
                player.stop();
                player.reset();
                player.release();
                player = null;
            }
            player = new MediaPlayer();
            initPlayer();

            player.setDataSource(getActivity(), uri);
            player.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalStateException e1){
            e1.printStackTrace();
        }
    }
    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        player = null;
    }
}
