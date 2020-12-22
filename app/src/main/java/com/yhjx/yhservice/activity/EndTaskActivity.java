package com.yhjx.yhservice.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.TaskOrderDetailReq;
import com.yhjx.yhservice.api.domain.response.FaultCategory;
import com.yhjx.yhservice.api.domain.response.GetFaultCategoryListRes;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.dialog.SelectCameraDialog;
import com.yhjx.yhservice.dialog.SelectFaultTypeDialog;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.model.TaskOrder;
import com.yhjx.yhservice.util.DateUtil;
import com.yhjx.yhservice.util.ImageUtil;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.StorageUtils;
import com.yhjx.yhservice.util.ToastUtils;
import com.yhjx.yhservice.view.AddImgView;
import com.yhjx.yhservice.view.TranslucentActionBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 完工
 */
public class EndTaskActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener {
    
    public static final String TAG = "EndTaskActivity";

    public static String TASK_NO_EXTRA_KEY = "task_no";
    private static String ADDVIEW_TYPE_LOCALE = "LOCALE";
    private static String ADDVIEW_TYPE_PARTS = "PARTS";

    @BindView(R.id.action_bar)
    TranslucentActionBar mActionBar;
    @BindView(R.id.tv_task_no)
    TextView mTaskNoTV;
    @BindView(R.id.tv_vin)
    TextView mVinTV;
    @BindView(R.id.tv_customer_name)
    TextView mCustomerNameTV;
    @BindView(R.id.tv_customer_tel)
    TextView mCustomerTelTV;
    @BindView(R.id.text_fault_desc)
    TextView mFaultDescTV;

    @BindView(R.id.ll_fault_type)
    LinearLayout mFaultTypeLL;
    @BindView(R.id.tv_fault_type)
    TextView mFaultTypeTV;
    @BindView(R.id.edit_fault_reason)
    EditText mFaultReasonEV;
    @BindView(R.id.edit_parts)
    EditText mPartsEV;
    @BindView(R.id.addimg_locale)
    AddImgView mLocalAddImg;
    @BindView(R.id.addimg_parts)
    AddImgView mPartsAddImg;


    @BindView(R.id.btn_submit)
    Button mSubmitBtn;

    /**
     * 任务单号
     */
    String taskNo;
    LoginUserInfo mLoginUserInfo;

    WaitDialog mWaitDialog;
    SelectCameraDialog selectCameraDialog;
    SelectFaultTypeDialog selectFaultTypeDialog;
    ApiModel apiModel;
    /**
     * 图片地址
     */
    String cameraUrl;
    String addViewType; // LOCALE/PART
    // 完工图片地址 多张用逗号分隔
    String endImgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_end_task);
        ButterKnife.bind(this);
        mActionBar.setData("完工", R.mipmap.ic_back, null, 0, null, this);
        mActionBar.setStatusBarHeight(getStatusBarHeight());

        mFaultTypeLL.setOnClickListener(mSelectedFaultType);

        mLocalAddImg.setAddPictureClickListener(mAddPictureClick);
        mPartsAddImg.setAddPictureClickListener(mAddPictureClick);

        mSubmitBtn.setOnClickListener(mSubmitClick);
        apiModel = new ApiModel(this);
        mWaitDialog = new WaitDialog(this);
        selectCameraDialog = new SelectCameraDialog(this);
        selectFaultTypeDialog = new SelectFaultTypeDialog(this);
        loadData();
        asyncLoadFaultTypeData();
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() { }

    /**
     * 加载任务详情数据
     */
    private void loadData() {
        mLoginUserInfo = RunningContext.getsLoginUserInfo();
        if (mLoginUserInfo == null) {
            return;
        }
        // 初始化加载任务详情
        TaskOrderDetailReq detailReq = new TaskOrderDetailReq();
        detailReq.taskNo = taskNo;
        detailReq.userNo = mLoginUserInfo.userNo;
        apiModel.queryTaskDetail(detailReq, new ResultHandler<TaskOrder>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(TaskOrder data) {
                setData(data);
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                ToastUtils.showToast(EndTaskActivity.this,"加载失败，重新进入试试");
                finish();
            }

            @Override
            public void onFinish() {
                mWaitDialog.dismiss();
            }
        });
    }

    /**
     * 异步同步故障类型数据
     */
    private void asyncLoadFaultTypeData() {
        if (RunningContext.mock) {
            List<FaultCategory> faultCategories = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                FaultCategory faultCategory = new FaultCategory();
                faultCategory.faultCategoryName = "发动机故障";
                faultCategories.add(faultCategory);
            }
            selectFaultTypeDialog.setDataList(faultCategories);
        } else {
            RunningContext.threadPool().execute(new Runnable() {
                @Override
                public void run() {
                    apiModel.queryFaultCategoryList(mLoginUserInfo.userNo, new ResultHandler<GetFaultCategoryListRes>() {
                        @Override
                        protected void onSuccess(GetFaultCategoryListRes data) {
                            if (data != null && data.list != null) {
                                selectFaultTypeDialog.setDataList(data.list);
                                StorageUtils.setFaultCategoryList(data.list);
                            } else {
                                List<FaultCategory> categoryList =  StorageUtils.getFaultCategoryList();
                                if (categoryList != null) {
                                    selectFaultTypeDialog.setDataList(data.list);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void setData(TaskOrder order) {
        if (order == null) {
            return;
        }
        mTaskNoTV.setText(getFormatValue(R.string.task_item_order_no,order.taskNo));
        mVinTV.setText(getFormatValue(R.string.task_item_vin,order.vehicleVin));
        mCustomerNameTV.setText(getFormatValue(R.string.task_item_customer_name,order.customerName));
        mCustomerTelTV.setText(getFormatValue(R.string.task_item_customer_tel,order.customerTel));
        mFaultDescTV.setText(getFormatValue(R.string.task_item_fault_desc,order.faultDesc));
    }


    /**
     * 提交
     */
    View.OnClickListener mSubmitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 开工之前先掉precheck接口查询下距离
            // 开工提交接口
        }
    };


    /**
     * 选择故障类型
     */
    View.OnClickListener mSelectedFaultType = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectFaultTypeDialog.show();
        }
    };

    /**
     *
     */
    AddImgView.AddPictureClickListener mAddPictureClick = new AddImgView.AddPictureClickListener() {
        @Override
        public void addPictureClick(View view) {
            String strDte = DateUtil.formatDate(new Date(),"yyyyMMddHHmmss");
            cameraUrl = Environment.getExternalStorageDirectory() + "/" + strDte + ".jpg";
            selectCameraDialog.show(cameraUrl);
            switch (view.getId()) {
                case R.id.addimg_locale:
                    addViewType = ADDVIEW_TYPE_LOCALE;
                    break;
                case R.id.addimg_fault:
                    addViewType = ADDVIEW_TYPE_PARTS;
                    break;
            }
        }
    };


    private String getFormatValue(int resId,String... params) {
        return String.format(getString(resId),params);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectCameraDialog.OPEN_CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (addViewType == ADDVIEW_TYPE_LOCALE) {
                    mLocalAddImg.setPicture(cameraUrl);
                } else if (addViewType == ADDVIEW_TYPE_PARTS){
                    mPartsAddImg.setPicture(cameraUrl);
                }
            }
        } else if (requestCode == SelectCameraDialog.SELECT_PHONE_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor =getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                cameraUrl = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();
            } catch (Exception e) {
                LogUtils.d(TAG,"获取系统照片异常");
                e.printStackTrace();
            }
            if (resultCode == RESULT_OK) {
                if (addViewType == ADDVIEW_TYPE_LOCALE) {
                    mLocalAddImg.setPicture(ImageUtil.getTempPathFromPathAndCompress(cameraUrl));
                } else if (addViewType == ADDVIEW_TYPE_PARTS){
                    mPartsAddImg.setPicture(ImageUtil.getTempPathFromPathAndCompress(cameraUrl));
                }
            }
        }

    }
}
