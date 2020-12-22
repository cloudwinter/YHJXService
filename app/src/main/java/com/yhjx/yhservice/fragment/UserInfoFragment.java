package com.yhjx.yhservice.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.activity.EditUserInfoActivity;
import com.yhjx.yhservice.activity.LoginActivity;
import com.yhjx.yhservice.base.BaseFragment;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.view.LoggerView;
import com.yhjx.yhservice.view.YHButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人信息
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "UserInfoFragment";

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

    @BindView(R.id.btn_login_out)
    YHButton mLoginOutBT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        mUserTelLL.setOnClickListener(this);
        mUserPasswordLL.setOnClickListener(this);
        mUserStagnationStationLL.setOnClickListener(this);
        mUserStagnationStationTV.setOnClickListener(this);
        mAppVersionLL.setOnClickListener(this);
        mDebugLL.setOnClickListener(this);
        mLoginOutBT.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
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
        mAppVersionTV.setText(RunningContext.getVersionCode() + "");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_user_tel:
                intent.setClass(mContext, EditUserInfoActivity.class);
                intent.putExtra(EditUserInfoActivity.EXTRA_FROM_KEY, EditUserInfoActivity.FROM_VALUE_MODIFY_PHONE);
                startActivity(intent);
                break;
            case R.id.ll_user_password:
                intent.setClass(mContext, EditUserInfoActivity.class);
                intent.putExtra(EditUserInfoActivity.EXTRA_FROM_KEY, EditUserInfoActivity.FROM_VALUE_MODIFY_PASSWORD);
                startActivity(intent);
                break;
            case R.id.ll_user_station:
                intent.setClass(mContext, EditUserInfoActivity.class);
                intent.putExtra(EditUserInfoActivity.EXTRA_FROM_KEY, EditUserInfoActivity.FROM_VALUE_MODIFY_STATION);
                startActivity(intent);
                break;
            case R.id.ll_user_stagnation_station:
                intent.setClass(mContext, EditUserInfoActivity.class);
                intent.putExtra(EditUserInfoActivity.EXTRA_FROM_KEY, EditUserInfoActivity.FROM_VALUE_MODIFY_STAGNATION);
                startActivity(intent);
                break;
            case R.id.ll_app_version:
                updateVersion();
                break;
            case R.id.ll_debug:
                LoggerView.me.loggerSwitch();
                break;
            case R.id.btn_login_out:
                loginOut();
                break;
        }
    }

    /**
     * 更新版本
     */
    private void updateVersion() {
        // TODO
    }


    /**
     * 退出登录
     */
    private void loginOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("退出");
        builder.setMessage("您确定要退出当前账户？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        // 去除title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}
