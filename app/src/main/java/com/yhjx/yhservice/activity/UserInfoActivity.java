package com.yhjx.yhservice.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人信息
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;

    @BindView(R.id.ll_user_name)
    LinearLayout mUserNameLL;
    @BindView(R.id.tv_user_name)
    TextView mUserNameTV;

    @BindView(R.id.ll_user_tel)
    LinearLayout mUserTelLL;
    @BindView(R.id.tv_user_tel)
    TextView mUserTelTV;

    @BindView(R.id.ll_user_password)
    LinearLayout mUserPasswordLL;

    @BindView(R.id.ll_user_station)
    LinearLayout mUserStationLL;
    @BindView(R.id.tv_user_station)
    TextView mUserStationTV;

    @BindView(R.id.ll_user_stagnation_station)
    LinearLayout mUserStagnationStationLL;
    @BindView(R.id.tv_user_stagnation_station)
    TextView mUserStagnationStationTV;

    @BindView(R.id.ll_app_version)
    LinearLayout mAppVersionLL;
    @BindView(R.id.tv_app_version)
    TextView mAppVersionTV;

    @BindView(R.id.ll_debug)
    LinearLayout mDebugLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        actionBar.setData("个人信息", 0, null, 0, null, null);
        actionBar.setStatusBarHeight(getStatusBarHeight());

        mUserTelLL.setOnClickListener(this);
        mUserPasswordLL.setOnClickListener(this);
        mUserStagnationStationLL.setOnClickListener(this);
        mUserStagnationStationTV.setOnClickListener(this);
        mAppVersionLL.setOnClickListener(this);
        mDebugLL.setOnClickListener(this);

        initData();
    }

    private void initData() {
        LoginUserInfo loginUserInfo = RunningContext.getsLoginUserInfo();
        if (loginUserInfo == null) {
            return;
        }
        mUserNameTV.setText(loginUserInfo.userName);
        mUserTelTV.setText(loginUserInfo.userTel);
        mUserStationTV.setText(loginUserInfo.stationName);
        mUserStagnationStationTV.setText(loginUserInfo.stagnationStationName);
        mAppVersionTV.setText(RunningContext.getVersionCode()+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_tel:
                // TODO
                break;
            case R.id.ll_user_password:
                // TODO
                break;
            case R.id.ll_user_station:
                // TODO
                break;
            case R.id.ll_user_stagnation_station:
                // TODO
                break;
            case R.id.ll_app_version:
                // TODO
                break;
            case R.id.ll_debug:
                // TODO
                break;
        }
    }
}
