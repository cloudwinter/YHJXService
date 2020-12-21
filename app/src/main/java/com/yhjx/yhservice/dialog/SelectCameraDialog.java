package com.yhjx.yhservice.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.yhjx.yhservice.R;
import com.yhjx.yhservice.util.YHUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

/**
 * 相册选择对话框
 */
public class SelectCameraDialog extends Dialog implements View.OnClickListener {

    public static int OPEN_CAMERA_REQUEST_CODE = 301;
    public static int SELECT_PHONE_REQUEST_CODE = 302;
    public static int PERMISSIONS_REQUEST_CODE = 300;

    private Context mContext;

    private RelativeLayout taskPicture;
    private RelativeLayout photo;
    private TextView cancel;
    private String cameraUrl;

    public SelectCameraDialog(@NonNull Context context) {
        super(context, R.style.selectCameraDialogStyle);
        mContext = context;
        initView(context);
    }

    public SelectCameraDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_camera, null);
        taskPicture = view.findViewById(R.id.rl_take_picture);
        taskPicture.setOnClickListener(this);
        photo = view.findViewById(R.id.rl_photo);
        photo.setOnClickListener(this);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        setContentView(view);

        setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.selectFaultTypeDialogAnimation);
    }

    @Override
    public void show() {
        super.show();
    }

    public void show(String cameraUrl) {
        this.cameraUrl = cameraUrl;
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.rl_take_picture:
                openCamera();
                dismiss();
                break;
            case R.id.rl_photo:
                selectPhoto();
                dismiss();
                break;

        }
    }


    private void openCamera() {
        if (isPermission()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File cameraPhoto = new File(cameraUrl);
            Uri photoUri = FileProvider.getUriForFile(
                    mContext,
                    mContext.getPackageName() + ".fileprovider",
                    cameraPhoto);
            //指定照片保存路径（SD卡）传的是uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            ((Activity)mContext).startActivityForResult(intent, OPEN_CAMERA_REQUEST_CODE);
        }
    }


    private void selectPhoto() {
        if (isPermission()) {
            // 激活系统图库，选择一张图片
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            ((Activity)mContext).startActivityForResult(intent, SELECT_PHONE_REQUEST_CODE);
        }
    }




    private boolean isPermission() {
        boolean granted = true;
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET};
        List<String> newApplyPermissions = new ArrayList<>();
        for (String permission:permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mContext, permission)){
                granted = false;
                newApplyPermissions.add(permission);
            }
        }
        if (!granted) {
            ActivityCompat.requestPermissions((Activity) mContext, YHUtils.listConvertToArray(newApplyPermissions),PERMISSIONS_REQUEST_CODE);
        }
        return granted;
    }

}
