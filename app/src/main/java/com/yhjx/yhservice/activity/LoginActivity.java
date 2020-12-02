package com.yhjx.yhservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.PatternUtils;
import com.yhjx.yhservice.util.ToastUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "LoginActivity";

    @BindView(R.id.edit_tel)
    private EditText editTextTel;
    @BindView(R.id.edit_password)
    private EditText editTextPassword;
    @BindView(R.id.btn_login)
    private Button buttonLogin;
    @BindView(R.id.text_register)
    private TextView textViewRegister;

    private String useTel;
    private String userPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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


    private void login() {
        if (!RunningContext.checkNetWork()) {
            ToastUtils.showNotNetwork(this);
            return;
        }
        ServiceUserLoginReq req = new ServiceUserLoginReq();
        req.userTel = useTel;
        req.userPassword = userPassword;
        LogUtils.d(TAG, "login 请求 参数：" + new Gson().toJson(req));
        new ApiModel().login(req, new ResultHandler<ServiceUser>() {
            @Override
            protected void onSuccess(ServiceUser user) {
                LogUtils.d(TAG, "login 返回成功：" + new Gson().toJson(user));
                if (user == null) {
                    ToastUtils.showToast(LoginActivity.this,"登录失败，请重试！");
                } else {
                    if ("2".equals(user.userFlag)) {
                        ToastUtils.showToast(LoginActivity.this,"登录成功");
                        // TODO 跳转到主页

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
        });

    }
}
