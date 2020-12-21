package com.yhjx.yhservice.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.model.TaskOrder;
import com.yhjx.yhservice.util.ToastUtils;
import com.yhjx.yhservice.util.YHUtils;
import com.yhjx.yhservice.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加保修任务
 */
public class AddTaskActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener {

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
    @BindView(R.id.btn_submit)
    Button mSubmitButton;

    ApiModel mApiModel;
    // 车辆信息
    GetCarInfoRes mCardInfo;
    // 车架号
    String mVin;

    LoginUserInfo mLoginUserInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        actionBar.setData("添加任务单", R.mipmap.ic_back, null, 0, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());

        mApiModel = new ApiModel();
        mSubmitButton.setOnClickListener(mSubmitClicker);
        mVinEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                   // TODO 焦点移开事件
                    String vin = mVinEditText.getText().toString();
                    if (!TextUtils.isEmpty(vin)) {
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
                if (data == null && data.vehicle != null && data.vehicleState != null) {
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



    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }


    View.OnClickListener mSubmitClicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TaskHandlerRepairReq req = new TaskHandlerRepairReq();
            req.customerName = mCustomerNameEditText.getText().toString();
            req.customerTel = mCustomerTelEditText.getText().toString();
            req.vehicleVin = mVin;
            req.faultDesc = mFaultDescEditText.getText().toString();

            Vehicle vehicle = mCardInfo.vehicle;
            VehicleState vehicleState = mCardInfo.vehicleState;
            if (checkParam(req)) {
                return;
            }
            req.vehicleAddress = vehicleState.vehicleAddress;
            req.vehicleLatitude = vehicleState.vehicleLatitude;
            req.vehicleLongitude = vehicleState.vehicleLongitude;
            req.vehicleName = vehicleState.vehicleName;
            req.vehicleType = vehicle.vehicleType;

            req.userNo = mLoginUserInfo.userNo;
            req.userName = mLoginUserInfo.userName;
            submit(req);
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
            protected void onSuccess(TaskOrder data) {

            }
        });
    }
}
