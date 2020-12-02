package com.yhjx.yhservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener,TranslucentActionBar.ActionBarClickListener {

    @BindView(R.id.action_bar)
    private TranslucentActionBar actionBar;

    @BindView(R.id.edit_name)
    private EditText editUserName;
    @BindView(R.id.edit_tel)
    private EditText editUserTel;
    @BindView(R.id.edit_password)
    private EditText editUserPassword;
    @BindView(R.id.edit_service_station)
    private EditText editServiceStation;
    @BindView(R.id.edit_stagnation_service_station)
    private EditText editStagnationServiceStation;

    @BindView(R.id.btn_register)
    private Button buttonRegister;
    @BindView(R.id.text_login)
    private TextView textViewLogin;


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
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        actionBar.setData(null, R.mipmap.ic_back, null, 0, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_service_station:
                // 选择服务站
                break;
            case R.id.edit_stagnation_service_station:
                // 选择归属服务站
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.text_login:
                Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
                break;
            default:break;

        }
    }


    private void register() {
        // TODO
    }

}
