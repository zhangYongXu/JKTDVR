package com.geeksworld.jktdvr.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.tools.Common;
import com.geeksworld.jktdvr.tools.ShareKey;
import com.geeksworld.jktdvr.tools.Tool;
import com.geeksworld.jktdvr.tools.UploadPhoto;
import com.geeksworld.jktdvr.tools.Url;
import com.geeksworld.jktdvr.views.MyWidget;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class HeadIconActivity extends TakePhotoActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView display_headicon;
    private SharedPreferences share;
    private HeadIconActivity ins_head;
    private PopupWindow pop_select;
    private TakePhoto takePhoto;
    private ArrayList<String> imgs = new ArrayList<>();
    private RequestManager glide;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //状态栏透明
        if (Build.VERSION.SDK_INT > 20) {
            if(!Common.hasNotchInScreen(this)){
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }else {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }

        }
        setContentView(R.layout.activity_displayheadicon);
        ins_head = this;
        share = ShareKey.getShare(this);
        takePhoto = getTakePhoto();
        glide = Glide.with(this);
        activity = this;

        initView();
        setHeadIcon();
    }

    private void initView() {
        View inTitle = findViewById(R.id.inTitle);
        View inSub = findViewById(R.id.inSub);
        inTitle.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title_name = inTitle.findViewById(R.id.title_name);
        TextView sub_name = inSub.findViewById(R.id.sub_name);
        TextView title_right = inTitle.findViewById(R.id.title_right);
        title_right.setVisibility(View.VISIBLE);
        title_right.setText(R.string.choice_picture);
        title_name.setText(R.string.headIcon_title);
        sub_name.setText(R.string.submit);

        RelativeLayout topStatusViewRelativeLayout = (RelativeLayout) findViewById(R.id.navigationTopStatueBgViewLayout);
        Common.hasNotchHandleNavigationStatusBg(this,topStatusViewRelativeLayout);

        title_right.setOnClickListener(this);
        inSub.setOnClickListener(this);

        display_headicon = findViewById(R.id.display_headIcon);
        display_headicon.setOnClickListener(this);
    }

    private void setHeadIcon() {
        String head_img = getIntent().getStringExtra("head_img");
        if (!Tool.isNull(head_img)) {
            glide.load(head_img).into(display_headicon);
        } else {
            display_headicon.setImageResource(R.mipmap.mine_head_default);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pop_select != null && pop_select.isShowing()) {
            pop_select.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                getPopSelect(v);
                break;
            case R.id.display_headIcon:
                getPopSelect(v);
                break;
            case R.id.title_back:
                finish();
                break;

            case R.id.inSub:
                completeUserHeadImg();
                break;

            default:
                break;
        }
    }

    private void getPopSelect(View view) {
        pop_select = MyWidget.getPopupWindow(this, view, R.layout.pop_select, false);
        View view1 = pop_select.getContentView();
        ListView listView = view1.findViewById(R.id.listView);

        final ArrayList<String> selectList = getSelectList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_text_center, R.id.text1, selectList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        view1.findViewById(R.id.pop_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pop_select != null && pop_select.isShowing())
                    pop_select.dismiss();
            }
        });
    }

    private ArrayList<String> getSelectList() {
        ArrayList<String> list_select = new ArrayList<>();
        list_select.add("拍照");
        list_select.add("相册");
        list_select.add("取消");
        return list_select;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (pop_select != null && pop_select.isShowing())
            pop_select.dismiss();
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        switch (i) {
            case 0: //拍照
                takePhoto.onPickFromCapture(imageUri);
                break;

            case 1: //相册
                takePhoto.onPickMultiple(1);
                break;

            case 2:
                if (pop_select != null && pop_select.isShowing())
                    pop_select.dismiss();
                break;

            default:
                break;
        }
    }

    private void completeUserHeadImg() {
        if (imgs.size() == 0) {
            Tool.toast(activity,"请设置图片");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "" + share.getInt(ShareKey.UID, 0));
        UploadPhoto.postForm(Url.completeUserHeadImg, params, imgs, new UploadPhoto.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String message = obj.getString("message");
                    if (code == 1) {
                        JSONObject result1 = obj.getJSONObject("result");
                        String img_url = result1.getString("imgUrl");
                        SharedPreferences.Editor edit = share.edit();
                        edit.putString(ShareKey.IMG_URL, img_url);
                        edit.commit();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(HeadIconActivity.this, PersonInfoActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void downfailed(String error) {
                Tool.toast(activity,"头像设置失败");
            }
        });
    }

    private CropOptions getCropOptions() {
        int height = 1920;
        int width = 1080;
        CropOptions.Builder builder = new CropOptions.Builder();
//            builder.setAspectX(width).setAspectY(height);
        builder.setOutputX(width).setOutputY(height);

        builder.setWithOwnCrop(true);
        return builder.create();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result.getImages() == null)
            System.out.println("返回null");
        else {
            String originalPath = result.getImages().get(0).getOriginalPath();
            imgs.clear();
            imgs.add(originalPath);
            glide.load(new File(originalPath)).into(display_headicon);
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Tool.toast(this, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }
}
