package com.yhjx.yhservice.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.ServiceUserLoginReq;
import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.PatternUtils;
import com.yhjx.yhservice.util.ToastUtils;
import com.yhjx.yhservice.util.YHUtils;
import com.yhjx.yhservice.view.YHButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static int PERMISSIONS_REQUEST_CODE = 300;

    public static final String TAG = "LoginActivity";

    @BindView(R.id.edit_tel)
    protected EditText editTextTel;
    @BindView(R.id.edit_password)
    protected EditText editTextPassword;
    @BindView(R.id.btn_login)
    protected YHButton buttonLogin;
    @BindView(R.id.text_register)
    protected TextView textViewRegister;

    private String useTel;
    private String userPassword;

    WaitDialog waitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        buttonLogin.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
        waitDialog = new WaitDialog(this);
        requestPermissions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkParam()) {
                    login();
                }
                break;
            case R.id.text_register:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private boolean checkParam() {
        String tel = editTextTel.getText().toString();
        if (TextUtils.isEmpty(tel) || TextUtils.isEmpty(tel.trim())) {
            ToastUtils.showToast(this, "手机号不能为空");
            return false;
        }
        if (!PatternUtils.checkPhone(tel)) {
            ToastUtils.showToast(this, "手机号格式错误");
            return false;
        }
        useTel = tel;
        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password.trim())) {
            ToastUtils.showToast(this, "密码不能为空");
            return false;
        }
        userPassword = password;
        return true;
    }


    /**
     * 动态申请权限
     */
    private boolean requestPermissions() {
        boolean granted = true;
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
        };
        List<String> newApplyPermissions = new ArrayList<>();
        for (String permission:permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)){
                granted = false;
                newApplyPermissions.add(permission);
            }
        }
        if (!granted) {
            ActivityCompat.requestPermissions(this, YHUtils.listConvertToArray(newApplyPermissions),PERMISSIONS_REQUEST_CODE);
        }
        return granted;
    }


    private void login() {
        if (!RunningContext.checkNetWork()) {
            ToastUtils.showNotNetwork(this);
            return;
        }
        ServiceUserLoginReq req = new ServiceUserLoginReq();
        req.userTel = useTel;
        req.userPassword = userPassword;
        LogUtils.d(TAG, "login 请求 参数：" + new Gson().toJson(req));
        new ApiModel(this).login(req, new ResultHandler<ServiceUser>() {

            @Override
            public void onStart() {
                waitDialog.show();
            }

            @Override
            protected void onSuccess(ServiceUser user) {
                LogUtils.d(TAG, "login 返回成功：" + new Gson().toJson(user));
                if (user == null) {
                    ToastUtils.showToast(LoginActivity.this,"登录失败，请重试！");
                } else {
                    if ("1".equals(user.userFlag)) {
                        ToastUtils.showToast(LoginActivity.this,"登录成功");
                        // 保存用户信息
                        LoginUserInfo loginUserInfo = copyByServiceUser(user);
                        RunningContext.setLoginUserInfo(loginUserInfo);
                        startHome();
                    } else if ("0".equals(user.userFlag)) {
                        ToastUtils.showToast(LoginActivity.this,"当前用户正在审核中，请耐心等候！");
                    } else {
                        ToastUtils.showToast(LoginActivity.this,"当前用户状态异常，请联系后台人员！");
                    }
                }
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                super.onFailed(errCode, errMsg);
                LogUtils.e(TAG, "login 返回异常：" + " errCode = " + errCode + " errMsg=" + errMsg);
                ToastUtils.showToast(LoginActivity.this,"登录失败，请重试！");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                waitDialog.dismiss();
            }
        });
    }

    private void startHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private LoginUserInfo copyByServiceUser(ServiceUser user) {
        if (user == null) {
            return null;
        }
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.id = user.id;
        loginUserInfo.userNo = user.userNo;
        loginUserInfo.userName = user.userName;
        loginUserInfo.userTel = user.userTel;
        loginUserInfo.userPassword = user.userPassword;
        loginUserInfo.stationId = user.stationId;
        loginUserInfo.stagnationStationId = user.stagnationStationId;
        loginUserInfo.stagnationStationId = user.stagnationStationId;
        loginUserInfo.stagnationStationName = user.stagnationStationName;
        loginUserInfo.stagnationStationAddress = user.stagnationStationAddress;
        loginUserInfo.stagnationStationLongitude = user.stagnationStationLongitude;
        loginUserInfo.stagnationStationLatitude = user.stagnationStationLatitude;
        loginUserInfo.userFlag = user.userFlag;
        loginUserInfo.userFlag = user.userFlag;
        return loginUserInfo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PERMISSIONS_REQUEST_CODE == requestCode) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // 授权成功
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
