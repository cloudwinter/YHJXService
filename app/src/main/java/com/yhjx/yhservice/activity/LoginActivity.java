package com.yhjx.yhservice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.base.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.edit_tel)
    private EditText editTextTel;
    @BindView(R.id.edit_password)
    private EditText editTextPassword;
    @BindView(R.id.btn_login)
    private Button buttonLogin;
    @BindView(R.id.text_register)
    private TextView textViewRegister;

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
                break;
            case R.id.text_register:
                break;
            default:
                break;
        }
    }
}
