package com.yhjx.yhservice.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.TaskHandleCheckReq;
import com.yhjx.yhservice.api.domain.request.TaskHandleStartReq;
import com.yhjx.yhservice.api.domain.request.TaskOrderDetailReq;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.dialog.SelectCameraDialog;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.model.LocationInfo;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.model.TaskOrder;
import com.yhjx.yhservice.util.DateUtil;
import com.yhjx.yhservice.util.ImageUtil;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.StepTask;
import com.yhjx.yhservice.util.StorageUtils;
import com.yhjx.yhservice.util.ToastUtils;
import com.yhjx.yhservice.view.AddImgView;
import com.yhjx.yhservice.view.TranslucentActionBar;
import com.yhjx.yhservice.view.YHButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 开工
 */
public class StartTaskActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener {

    public static final String TAG = "StartTaskActivity";

    public static String TASK_NO_EXTRA_KEY = "task_no";
    private static String ADDVIEW_TYPE_LOCALE = "LOCALE";
    private static String ADDVIEW_TYPE_FAULT = "FAULT";

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
    @BindView(R.id.addimg_locale)
    AddImgView mLocaleAddImgView;
    @BindView(R.id.addimg_fault)
    AddImgView mFaultAddImgView;
    @BindView(R.id.btn_submit)
    YHButton mSubmitBtn;
    // 网络请求
    ApiModel apiModel;
    // 对话框
    WaitDialog mWaitDialog;
    SelectCameraDialog selectCameraDialog;

    // 任务单号
    String taskNo;
    LoginUserInfo mLoginUserInfo;
    // 图片地址
    String cameraUrl;
    // LOCALE/FAULT
    String addViewType;
    // 开工图片 用,分割
    String startImgUrl;

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_start_task);
        ButterKnife.bind(this);
        mActionBar.setData("开工", R.mipmap.ic_back, null, 0, null, this);
        mActionBar.setStatusBarHeight(getStatusBarHeight());
        mLocaleAddImgView.setAddPictureClickListener(mAddPictureClick);
        mFaultAddImgView.setAddPictureClickListener(mAddPictureClick);

        mSubmitBtn.setOnClickListener(mSubmitClick);
        // 任务单号
        taskNo = getIntent().getStringExtra(TASK_NO_EXTRA_KEY);
        apiModel = new ApiModel(this);
        mWaitDialog = new WaitDialog(this);
        selectCameraDialog = new SelectCameraDialog(StartTaskActivity.this);
        // 启动定位
        RunningContext.sAMapLocationClient.startLocation();
        loadData();
    }

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
                ToastUtils.showToast(StartTaskActivity.this, "加载失败，重新进入试试");
                finish();
            }

            @Override
            public void onFinish() {
                mWaitDialog.dismiss();
            }
        });
    }


    private void setData(TaskOrder order) {
        if (order == null) {
            return;
        }
        mTaskNoTV.setText(getFormatValue(R.string.task_item_order_no, order.taskNo));
        mVinTV.setText(getFormatValue(R.string.task_item_vin, order.vehicleVin));
        mCustomerNameTV.setText(getFormatValue(R.string.task_item_customer_name, order.customerName));
        mCustomerTelTV.setText(getFormatValue(R.string.task_item_customer_tel, order.customerTel));
        mFaultDescTV.setText(getFormatValue(R.string.task_item_fault_desc, order.faultDesc));
    }

    /**
     * 点击上传图片
     */
    AddImgView.AddPictureClickListener mAddPictureClick = new AddImgView.AddPictureClickListener() {
        @Override
        public void addPictureClick(View view) {
            switch (view.getId()) {
                case R.id.addimg_locale:
                    addViewType = ADDVIEW_TYPE_LOCALE;
                    break;
                case R.id.addimg_fault:
                    addViewType = ADDVIEW_TYPE_FAULT;
                    break;
            }
            if (!RunningContext.checkCameraPermission(StartTaskActivity.this, true)) {
                return;
            }
            addPicture();
        }
    };

    private void addPicture() {
        String strDte = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
        cameraUrl = Environment.getExternalStorageDirectory() + "/" + strDte + ".jpg";
        selectCameraDialog.show(cameraUrl);
    }

    View.OnClickListener mSubmitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 1、检查参数
            if (!checkParam()) {
                return;
            }
            // 2、提交请求
            submitStart();
        }
    };


    private boolean checkParam() {
        if (TextUtils.isEmpty(mLocaleAddImgView.getImageUrl())) {
            ToastUtils.showToast(StartTaskActivity.this, "人车现场图片未上传");
            return false;
        }
        if (TextUtils.isEmpty(mFaultAddImgView.getImageUrl())) {
            ToastUtils.showToast(StartTaskActivity.this, "故障图片未上传");
            return false;
        }
        startImgUrl = mLocaleAddImgView.getImageUrl() + "," + mFaultAddImgView.getImageUrl();
        return true;
    }


    /**
     * 1、开工之前先掉precheck接口查询下距离
     * 2、开工提交接口
     */
    private void submitStart() {
        new StepTask() {
            @Override
            public void onStep1() {
                apiModel.check(buildCheckReq(), new ResultHandler<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean data) {
                        if (data) {
                            executeNextStep();
                        } else {
                            onFinish();
                            AlertDialog.Builder builder = new AlertDialog.Builder(StartTaskActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("当前距离车辆较远确认开工？");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    executeStart();
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }

            @Override
            public void onStep2() {
                executeStart();
                onFinish();
            }
        }.execute(2);
    }

    /**
     * 执行开工接口
     */
    private void executeStart() {
        apiModel.start(buildStartReq(), new ResultHandler<Void>() {
            @Override
            protected void onSuccess(Void data) {
                ToastUtils.showToast(StartTaskActivity.this, "开工成功");
                finish();
                // TODO 返回需要刷新界面
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                ToastUtils.showToast(StartTaskActivity.this, "开工失败！");
            }
        });
    }

    /**
     * @return
     */
    private TaskHandleCheckReq buildCheckReq() {
        TaskHandleCheckReq checkReq = new TaskHandleCheckReq();
        checkReq.taskNo = taskNo;
        checkReq.userNo = mLoginUserInfo.userNo;
        LocationInfo locationInfo = StorageUtils.getCurrentLocation();
        checkReq.longitude = locationInfo.longitude;
        checkReq.latitude = locationInfo.latitude;
        checkReq.userAddress = locationInfo.address;
        return checkReq;
    }

    /**
     * @return
     */
    private TaskHandleStartReq buildStartReq() {
        TaskHandleStartReq startReq = new TaskHandleStartReq();
        LocationInfo locationInfo = StorageUtils.getCurrentLocation();
        startReq.longitude = locationInfo.longitude;
        startReq.latitude = locationInfo.latitude;
        startReq.userNo = mLoginUserInfo.userNo;
        startReq.taskNo = taskNo;
        startReq.startImgPath = startImgUrl;
        return startReq;
    }


    private String getFormatValue(int resId, Object... params) {
        return String.format(getString(resId), params);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("StartTaskActivity.onActivityResult", requestCode + "", resultCode + "");
        if (requestCode == SelectCameraDialog.OPEN_CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (addViewType == ADDVIEW_TYPE_LOCALE) {
                    mLocaleAddImgView.setPicture(cameraUrl);
                } else if (addViewType == ADDVIEW_TYPE_FAULT) {
                    mFaultAddImgView.setPicture(cameraUrl);
                }
            }
        } else if (requestCode == SelectCameraDialog.SELECT_PHONE_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                cameraUrl = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();
            } catch (Exception e) {
                LogUtils.d(TAG, "获取系统照片异常");
                e.printStackTrace();
            }
            if (resultCode == RESULT_OK) {
                if (addViewType == ADDVIEW_TYPE_LOCALE) {
                    mLocaleAddImgView.setPicture(ImageUtil.getTempPathFromPathAndCompress(cameraUrl));
                } else if (addViewType == ADDVIEW_TYPE_FAULT) {
                    mFaultAddImgView.setPicture(ImageUtil.getTempPathFromPathAndCompress(cameraUrl));
                }
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RunningContext.PERMISSIONS_REQUEST_CODE) {
            if (RunningContext.checkCameraPermission(StartTaskActivity.this, false)) {
                addPicture();
            }
        }
    }
}
