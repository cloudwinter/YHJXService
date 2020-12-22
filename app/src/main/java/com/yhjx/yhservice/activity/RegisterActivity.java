package com.yhjx.yhservice.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.ServiceUserRegisterReq;
import com.yhjx.yhservice.api.domain.response.ServiceUserRegisterRes;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.model.StationModel;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.ToastUtils;
import com.yhjx.yhservice.util.YHUtils;
import com.yhjx.yhservice.view.TranslucentActionBar;
import com.yhjx.yhservice.view.YHButton;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener,TranslucentActionBar.ActionBarClickListener {

    public static final String TAG = "RegisterActivity";

    public static final int STATION_REQUEST_CODE = 103;
    public static final int STAGNATION_REQUEST_CODE = 104;

    @BindView(R.id.action_bar)
    protected TranslucentActionBar actionBar;

    @BindView(R.id.edit_name)
    protected EditText editUserName;
    @BindView(R.id.edit_tel)
    protected EditText editUserTel;
    @BindView(R.id.edit_password)
    protected EditText editUserPassword;
    @BindView(R.id.edit_service_station)
    protected EditText editServiceStation;
    @BindView(R.id.edit_stagnation_service_station)
    protected EditText editStagnationServiceStation;

    @BindView(R.id.btn_register)
    protected YHButton buttonRegister;
    @BindView(R.id.text_login)
    protected TextView textViewLogin;

    protected StationModel mSelectedStationModel;
    protected StationModel mSelectedStagnationStationModel;

    protected WaitDialog mWaitDialog;


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
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        actionBar.setData("注册", R.mipmap.ic_back, null, 0, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());
        editServiceStation.setOnClickListener(this);
        editStagnationServiceStation.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        mWaitDialog = new WaitDialog(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.edit_service_station:
                // 选择服务站
                intent.setClass(RegisterActivity.this, StationSelectedActivity.class);
                startActivityForResult(intent,STATION_REQUEST_CODE);
                break;
            case R.id.edit_stagnation_service_station:
                // 选择驻点
                intent.setClass(RegisterActivity.this, StationSelectedActivity.class);
                startActivityForResult(intent,STAGNATION_REQUEST_CODE);
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.text_login:
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:break;

        }
    }

    private ServiceUserRegisterReq buildReq() {
        ServiceUserRegisterReq req = new ServiceUserRegisterReq();
        req.userName = YHUtils.trim(editUserName.getText().toString());
        req.userPassword = YHUtils.trim(editUserPassword.getText().toString());
        req.userTel = YHUtils.trim(editUserTel.getText().toString());

        if (YHUtils.validParams(req.userName, req.userPassword, req.userTel)) {
            ToastUtils.showToast(this,"必填参数缺失！");
            return null;
        }
        req.stationId = mSelectedStationModel.id;
        req.stagnationStationId = mSelectedStagnationStationModel.id;
        return req;
    }


    private void register() {
        ServiceUserRegisterReq req = buildReq();
        if (req == null) {
            return;
        }
        new ApiModel().register(req, new ResultHandler<ServiceUserRegisterRes>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(ServiceUserRegisterRes data) {
                ToastUtils.showToast(RegisterActivity.this,"注册成功，等待审核");
                finish();
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                super.onFailed(errCode, errMsg);
                ToastUtils.showToast(RegisterActivity.this,"注册失败");
            }

            @Override
            public void onFinish() {
                mWaitDialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == STATION_REQUEST_CODE) {
                mSelectedStationModel = (StationModel) data.getSerializableExtra(StationSelectedActivity.RESULT_DATA_KEY);
                if (mSelectedStationModel != null) {
                    editServiceStation.setText(mSelectedStationModel.stationName);
                }
            } else if(requestCode == STAGNATION_REQUEST_CODE) {
                mSelectedStagnationStationModel = (StationModel) data.getSerializableExtra(StationSelectedActivity.RESULT_DATA_KEY);
                if (mSelectedStagnationStationModel != null) {
                    editStagnationServiceStation.setText(mSelectedStagnationStationModel.stationName);
                }
            }
        }
    }
}
