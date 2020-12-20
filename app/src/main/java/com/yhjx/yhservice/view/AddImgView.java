package com.yhjx.yhservice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.response.UploadImgRes;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.util.LogUtils;

/**
 * 上传图片
 */
public class AddImgView extends RelativeLayout implements View.OnClickListener {

    public static final String TAG = "AddImgView";

    private Context mContext;

    private ImageView mCloseImg;
    private LinearLayout mAddLL;
    private TextView mImgDescTV;
    private ImageView mPictureImg;
    private WaitDialog mWaitDialog;
    /**
     * 上传成功后的图片地址
     */
    private String imageUrl;

    private AddPictureClickListener mAddPictureClickListener;

    public AddImgView(Context context) {
        super(context, null);
        mContext = context;
        initView(null);
    }

    public AddImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(attrs);
    }


    public void setAddPictureClickListener(AddPictureClickListener addPictureClickListener) {
        mAddPictureClickListener = addPictureClickListener;
    }

    private void initView(AttributeSet attrs) {
        String imgDesc = null;
        if (attrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.AddImgView);
            imgDesc = typedArray.getString(R.styleable.AddImgView_imgdesc);
        }
        LayoutInflater.from(mContext).inflate(R.layout.view_add_img, this);
        mAddLL = findViewById(R.id.ll_add);
        mAddLL.setOnClickListener(this);
        mImgDescTV = findViewById(R.id.tv_img_desc);
        mCloseImg = findViewById(R.id.img_close);
        mCloseImg.setOnClickListener(this);
        mPictureImg = findViewById(R.id.img_picture);
        mImgDescTV.setText(imgDesc);
        mWaitDialog = new WaitDialog(mContext);
    }

    /**
     * 获取上传成功的imageUrl
     * @return
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置图片
     *
     * @param url
     */
    public void setPicture(String url) {
        mPictureImg.setVisibility(VISIBLE);
        new ApiModel(mContext).uploadImg(url, new ResultHandler<UploadImgRes>() {

            @Override
            public void onStart() {
                LogUtils.i(TAG, "图片开始上传 path:" + url);
                super.onStart();
                mWaitDialog.show("上传中...");
            }

            @Override
            protected void onSuccess(UploadImgRes data) {
                LogUtils.i(TAG, "图片开始上传 成功:" + data);
                if (data != null) {
                    // 网络图片
                    imageUrl = data.url;
                    ImageLoader.getInstance().displayImage(data.url, mPictureImg, mImageLoadingListener);
                }
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                super.onFailed(errCode, errMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mWaitDialog.dismiss();
            }
        });
    }


    private ImageLoadingListener mImageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            LogUtils.d(TAG, "图片显示成功：" + imageUri);
            // 如果成功显示
            mAddLL.setVisibility(View.GONE);
            mCloseImg.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add:
                if (mAddPictureClickListener != null) {
                    mAddPictureClickListener.addPictureClick(AddImgView.this);
                }
                break;
            case R.id.img_close:
                mPictureImg.setImageDrawable(null);
                // 显示新增
                mAddLL.setVisibility(View.VISIBLE);
                break;
        }
    }


    public interface AddPictureClickListener {
        void addPictureClick(View view);
    }
}
