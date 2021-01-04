package com.yhjx.yhservice.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdatePasswordReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateStagnationReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateStationReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateTelReq;
import com.yhjx.yhservice.api.domain.response.ServiceUserUpdateStagnationRes;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.model.StationModel;
import com.yhjx.yhservice.util.ToastUtils;
import com.yhjx.yhservice.view.TranslucentActionBar;
import com.yhjx.yhservice.view.YHButton;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户信息编写
 */
public class EditUserInfoActivity extends BaseActivity implements View.OnClickListener, TranslucentActionBar.ActionBarClickListener {

    public static final int STATION_REQUEST_STATION_CODE = 103;
    public static final int STATION_REQUEST_STAGNATION_CODE = 104;

    public static final String EXTRA_FROM_KEY = "EXTRA_FROM";

    public static final String FROM_VALUE_MODIFY_PHONE = "MODIFY_PHONE";
    public static final String FROM_VALUE_MODIFY_PASSWORD = "MODIFY_PASSWORD";
    public static final String FROM_VALUE_MODIFY_STATION = "MODIFY_STATION";
    public static final String FROM_VALUE_MODIFY_STAGNATION = "MODIFY_STAGNATION";


    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;

    @BindView(R.id.ll_user_tel)
    LinearLayout newUserTelLL;
    @BindView(R.id.edit_new_user_tel)
    EditText mNewUserTel;

    @BindView(R.id.ll_user_password)
    LinearLayout newUserPasswordLL;
    @BindView(R.id.edit_new_password)
    EditText mNewUserPassword;

    @BindView(R.id.ll_user_station)
    LinearLayout newUserStationLL;
    @BindView(R.id.edit_new_station)
    EditText mNewUserStation;

    @BindView(R.id.ll_new_stagnation_station)
    LinearLayout newUserStagnationStationLL;
    @BindView(R.id.edit_new_stagnation_station)
    EditText mNewUserStagnationStation;

    @BindView(R.id.btn_save)
    YHButton mSaveButton;

    LoginUserInfo mLoginUserInfo;

    String mFromValue = "";

    ApiModel mApiModel;

    WaitDialog mWaitDialog;

    /**
     * 选中的服务站id和name
     */
    private Integer stationId;
    private String stationName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_edit_user);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mFromValue = intent.getStringExtra(EXTRA_FROM_KEY);
        String title = "";
        if (FROM_VALUE_MODIFY_PHONE.equals(mFromValue)) {
            newUserTelLL.setVisibility(View.VISIBLE);
            title = "修改手机号";
        } else if (FROM_VALUE_MODIFY_PASSWORD.equals(mFromValue)) {
            newUserPasswordLL.setVisibility(View.VISIBLE);
            title = "修改密码";
        } else if (FROM_VALUE_MODIFY_STATION.equals(mFromValue)) {
            newUserStationLL.setVisibility(View.VISIBLE);
            title = "修改服务站";
        } else if (FROM_VALUE_MODIFY_STAGNATION.equals(mFromValue)) {
            newUserStagnationStationLL.setVisibility(View.VISIBLE);
            title = "修改驻点";
        }
        actionBar.setData(title, R.mipmap.ic_back, null, 0, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());

        mNewUserStation.setOnClickListener(this);
        mNewUserStagnationStation.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);


        mLoginUserInfo = RunningContext.getsLoginUserInfo();
        mApiModel = new ApiModel(this);
        mWaitDialog = new WaitDialog(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.edit_new_station:
                // 选择服务站
                intent.setClass(EditUserInfoActivity.this, StationSelectedActivity.class);
                startActivityForResult(intent,STATION_REQUEST_STATION_CODE);
                break;
            case R.id.edit_new_stagnation_station:
                // 选择服务站
                intent.setClass(EditUserInfoActivity.this, StationSelectedActivity.class);
                startActivityForResult(intent,STATION_REQUEST_STAGNATION_CODE);
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }


    private void save() {
        if (FROM_VALUE_MODIFY_PHONE.equals(mFromValue)) {
            String newTel = mNewUserTel.getText().toString() == null ? null:mNewUserTel.getText().toString().trim();
            if (TextUtils.isEmpty(newTel)) {
                ToastUtils.showToast(this,"新手机号不能为空");
                return;
            }
            modifyUseTel(newTel);
        } else if (FROM_VALUE_MODIFY_PASSWORD.equals(mFromValue)) {
            String newPassword = mNewUserPassword.getText().toString() == null ? null:mNewUserPassword.getText().toString().trim();
            if (TextUtils.isEmpty(newPassword)) {
                ToastUtils.showToast(this,"新密码不能为空");
                return;
            }
            modifyUsePassword(newPassword);
        } else if (FROM_VALUE_MODIFY_STATION.equals(mFromValue)) {
            String newStation = mNewUserStation.getText().toString() == null ? null:mNewUserStation.getText().toString().trim();
            if (TextUtils.isEmpty(newStation) || TextUtils.isEmpty(stationName)) {
                ToastUtils.showToast(this,"新服务站不能为空");
                return;
            }
            modifyStation();
        } else if (FROM_VALUE_MODIFY_STAGNATION.equals(mFromValue)) {
            String newStagnation = mNewUserStagnationStation.getText().toString() == null ? null:mNewUserStagnationStation.getText().toString().trim();
            if (TextUtils.isEmpty(newStagnation) || TextUtils.isEmpty(stationName)) {
                ToastUtils.showToast(this,"新驻点不能为空");
                return;
            }
            modifyStationStation();
        }
    }


    private void modifyUseTel(final String newTel) {
        // 修改手机号
        ServiceUserUpdateTelReq req = new ServiceUserUpdateTelReq();
        req.id = mLoginUserInfo.id;
        req.userNo = mLoginUserInfo.userNo;
        req.userTel = newTel;
        req.userPassword = mLoginUserInfo.userPassword;
        mApiModel.updateTel(req, new ResultHandler<Void>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(Void data) {
                ToastUtils.showToast(EditUserInfoActivity.this,"修改成功");
                mLoginUserInfo.userTel = newTel;
                RunningContext.setLoginUserInfo(mLoginUserInfo);
                finish();
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

    private void modifyUsePassword(final String userPassword) {
        // 修改密码
        ServiceUserUpdatePasswordReq req = new ServiceUserUpdatePasswordReq();
        req.id = mLoginUserInfo.id;
        req.userNo = mLoginUserInfo.userNo;
        req.userPassword = mLoginUserInfo.userPassword;
        req.userNewPassword = userPassword;
        mApiModel.updatePassword(req, new ResultHandler<Void>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(Void data) {
                ToastUtils.showToast(EditUserInfoActivity.this,"修改成功");
                mLoginUserInfo.userPassword = userPassword;
                RunningContext.setLoginUserInfo(mLoginUserInfo);
                finish();
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


    private void modifyStation() {
        // 修改服务站
        ServiceUserUpdateStationReq req = new ServiceUserUpdateStationReq();
        req.id = mLoginUserInfo.id;
        req.userNo = mLoginUserInfo.userNo;
        req.stationId = stationId;
        req.stationName = stationName;
        req.userPassword = mLoginUserInfo.userPassword;
        mApiModel.updateStation(req, new ResultHandler<Void>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(Void data) {
                ToastUtils.showToast(EditUserInfoActivity.this,"修改成功");
                mLoginUserInfo.stationId = stationId;
                mLoginUserInfo.stationName = stationName;
                RunningContext.setLoginUserInfo(mLoginUserInfo);
                finish();
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

    private void modifyStationStation() {
        // 修改驻点
        ServiceUserUpdateStagnationReq req = new ServiceUserUpdateStagnationReq();
        req.id = mLoginUserInfo.id;
        req.userNo = mLoginUserInfo.userNo;
        req.stagnationStationId = stationId;
        req.stagnationStationName = stationName;
        req.userPassword = mLoginUserInfo.userPassword;
        mApiModel.updateStagnation(req, new ResultHandler<ServiceUserUpdateStagnationRes>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(ServiceUserUpdateStagnationRes data) {
                ToastUtils.showToast(EditUserInfoActivity.this,"修改成功");
                mLoginUserInfo.stagnationStationId = stationId;
                mLoginUserInfo.stagnationStationName = stationName;
                mLoginUserInfo.stagnationStationAddress = data.stagnationStationAddress;
                mLoginUserInfo.stagnationStationLatitude = data.stagnationStationLatitude;
                mLoginUserInfo.stagnationStationLongitude = data.stagnationStationLongitude;
                RunningContext.setLoginUserInfo(mLoginUserInfo);
                finish();
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STATION_REQUEST_STATION_CODE || requestCode == STATION_REQUEST_STAGNATION_CODE) {
            if (RESULT_OK == resultCode) {
                StationModel stationModel = (StationModel) data.getSerializableExtra(StationSelectedActivity.RESULT_DATA_KEY);
                if (stationModel != null) {
                    stationId = stationModel.id;
                    stationName = stationModel.stationName;
                }
                if (FROM_VALUE_MODIFY_STATION.equals(mFromValue)) {
                    mNewUserStation.setText(stationName);
                } else if (FROM_VALUE_MODIFY_STAGNATION.equals(mFromValue)) {
                    mNewUserStagnationStation.setText(stationName);
                }
            }
        }
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() { }
}
