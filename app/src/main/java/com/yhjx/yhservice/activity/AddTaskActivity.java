package com.yhjx.yhservice.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.GetCarInfoReq;
import com.yhjx.yhservice.api.domain.request.TaskHandlerRepairReq;
import com.yhjx.yhservice.api.domain.response.GetCarInfoRes;
import com.yhjx.yhservice.api.domain.response.ServiceUserRegisterRes;
import com.yhjx.yhservice.api.domain.response.Vehicle;
import com.yhjx.yhservice.api.domain.response.VehicleState;
import com.yhjx.yhservice.base.BaseActionBarActivity;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.model.LocationInfo;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.model.TaskOrder;
import com.yhjx.yhservice.util.StorageUtils;
import com.yhjx.yhservice.util.ToastUtils;
import com.yhjx.yhservice.util.YHUtils;
import com.yhjx.yhservice.view.TranslucentActionBar;
import com.yhjx.yhservice.view.YHButton;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加保修任务
 */
public class AddTaskActivity extends BaseActionBarActivity  {

    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;
    @BindView(R.id.edit_vin)
    EditText mVinEditText;
    @BindView(R.id.edit_customer_name)
    EditText mCustomerNameEditText;
    @BindView(R.id.edit_customer_tel)
    EditText mCustomerTelEditText;
    @BindView(R.id.edit_fault_desc)
    EditText mFaultDescEditText;
    @BindView(R.id.edit_vehicle_name)
    EditText mVehicleNameEditText;
    @BindView(R.id.edit_vehicle_address)
    EditText mVehicleAddressEditText;
    @BindView(R.id.img_vehicle_address)
    ImageView mVehicleAddressImageView;
    @BindView(R.id.btn_submit)
    YHButton mSubmitButton;

    ApiModel mApiModel;
    // 车辆信息
    GetCarInfoRes mCardInfo;
    // 车架号
    String mVin;

    LoginUserInfo mLoginUserInfo;

    WaitDialog mWaitDialog;


    @Override
    public int layoutResID() {
        return R.layout.activity_add_task;
    }

    @Override
    public String setTitle() {
        return "添加任务单";
    }

    @Override
    public void createView(Bundle savedInstanceState) {
        mWaitDialog = new WaitDialog(this);
        mApiModel = new ApiModel();
        mSubmitButton.setOnClickListener(mSubmitClicker);
        mVehicleAddressImageView.setOnClickListener(mAddressImgClick);
        mVinEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String vin = mVinEditText.getText().toString();
                    if (!TextUtils.isEmpty(vin)) {
                        vin = "YH"+vin;
                        mVin = vin;
                        queryVehicleInfo(vin.trim());
                    }
                }
            }
        });
        mLoginUserInfo = RunningContext.getsLoginUserInfo();
    }

    /**
     * 查询车辆信息
     * @param vin
     */
    private void queryVehicleInfo(String vin) {
        GetCarInfoReq req = new GetCarInfoReq();
        req.vehicleVin = vin;
        mApiModel.queryVehicleInfo(req, new ResultHandler<GetCarInfoRes>() {
            @Override
            protected void onSuccess(GetCarInfoRes data) {
                if (data == null || data.vehicle == null && data.vehicleState == null) {
                    ToastUtils.showToast(AddTaskActivity.this,"未查询到车辆，请检查车架号是否正确");
                    return;
                }
                mCardInfo = data;
                mVehicleNameEditText.setText(mCardInfo.vehicle.vehicleName);
                mVehicleAddressEditText.setText(mCardInfo.vehicleState.vehicleAddress);
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                super.onFailed(errCode, errMsg);
                ToastUtils.showToast(AddTaskActivity.this,"查询车辆异常，请检查车架号是否正确");
            }
        });
    }



    View.OnClickListener mSubmitClicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCardInfo == null) {
                ToastUtils.showToast(AddTaskActivity.this,"请确认车架号是否正确！");
                return;
            }
            TaskHandlerRepairReq req = new TaskHandlerRepairReq();
            req.customerName = mCustomerNameEditText.getText().toString();
            req.customerTel = mCustomerTelEditText.getText().toString();
            req.vehicleVin = mVin;
            req.faultDesc = mFaultDescEditText.getText().toString();

            Vehicle vehicle = mCardInfo.vehicle;
            VehicleState vehicleState = mCardInfo.vehicleState;
            if (!checkParam(req)) {
                return;
            }
            req.vehicleAddress = vehicleState.vehicleAddress;
            req.vehicleLatitude = vehicleState.vehicleLatitude;
            req.vehicleLongitude = vehicleState.vehicleLongitude;
            req.vehicleName = vehicle.vehicleName;
            req.vehicleType = vehicle.vehicleType;

            if (mLoginUserInfo == null) {
                ToastUtils.showToast(AddTaskActivity.this,"未获取到用户信息，请退出后重新进入");
                return;
            }
            req.userNo = mLoginUserInfo.userNo;
            req.userName = mLoginUserInfo.userName;

            LocationInfo locationInfo = StorageUtils.getCurrentLocation();
            if (locationInfo == null) {
                ToastUtils.showToast(AddTaskActivity.this,"请确认开启定位权限，开启后重新进入app");
                return;
            }
            req.longitude = locationInfo.longitude;
            req.latitude = locationInfo.latitude;
            req.userAddress = locationInfo.address;

            submit(req);
        }
    };

    private View.OnClickListener mAddressImgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCardInfo.vehicleState != null) {
                // 跳转到地图界面
                String coordinate = YHUtils.getFormatValue(R.string.intent_map_coordinate_str,mCardInfo.vehicleState.vehicleLatitude,mCardInfo.vehicleState.vehicleLongitude);
                Uri uri=Uri.parse(coordinate);  //打开地图定位
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                ComponentName cn = mapIntent.resolveActivity(getPackageManager());
                if(cn == null){
                    ToastUtils.showToast(AddTaskActivity.this,"请先安装第三方导航软件");
                }else{
                    startActivity(mapIntent);
                }
            }
        }
    };

    private boolean checkParam(TaskHandlerRepairReq req ) {
        if (!YHUtils.validParams(req.customerName, req.customerTel,
                req.faultDesc, req.vehicleVin)) {
            ToastUtils.showToast(AddTaskActivity.this,"必填参数缺失！");
            return false;
        }
        return true;
    }


    /**
     * 提交接口
     * @param req
     */
    private void submit(TaskHandlerRepairReq req) {
        mApiModel.repair(req, new ResultHandler<TaskOrder>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(TaskOrder data) {
                if (data != null) {
                    if (mLoginUserInfo.userNo.equals(data.serviceUserNo)) {
                        ToastUtils.showToast(AddTaskActivity.this,"提交成功！");
                        finish();
                    } else {
                        showWarningDialog(data.serviceUserName,data.serviceUserTel);
                    }
                }
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                super.onFailed(errCode, errMsg);
                ToastUtils.showToast(AddTaskActivity.this,"提交失败！"+errMsg);
            }

            @Override
            public void onFinish() {
                mWaitDialog.dismiss();
            }
        });
    }

    private void showWarningDialog(String userName,String tel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
        builder.setTitle("提示");
        builder.setMessage("当前车辆已报修，并指派 "+userName+"("+tel+"） 安排维修" );
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
