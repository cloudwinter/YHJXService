package com.yhjx.yhservice.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener,TranslucentActionBar.ActionBarClickListener {

    public static final String TAG = "RegisterActivity";

    public static final int STATION_REQUEST_CODE = 103;

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
    protected Button buttonRegister;
    @BindView(R.id.text_login)
    protected TextView textViewLogin;


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
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.edit_service_station:
                // 选择服务站
                intent.setClass(RegisterActivity.this, StationSelectedActivity.class);
                startActivityForResult(intent,STATION_REQUEST_CODE);
            case R.id.edit_stagnation_service_station:
                // 选择归属服务站
                intent.setClass(RegisterActivity.this, StationSelectedActivity.class);
                startActivityForResult(intent,STATION_REQUEST_CODE);
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


    private void register() {
        // TODO
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STATION_REQUEST_CODE) {
            LogUtils.d(TAG,"onActivityResult:-->");
        }
    }
}
