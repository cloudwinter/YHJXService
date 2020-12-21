package com.yhjx.yhservice.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.ServiceUserLoginReq;
import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.util.StorageUtils;
import com.yhjx.yhservice.util.YHUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.ll_content)
    LinearLayout mContentLL;

    LoginUserInfo mLoginUserInfo = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
        requestPermissions();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mLoginUserInfo = RunningContext.getsLoginUserInfo();
        if (mLoginUserInfo != null && !StorageUtils.isLoginUserExpire()) {
            // 超过缓存有效期，重新登录
            asyncRequestLogin(mLoginUserInfo);
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
        //启动Fill保持
        animation.setFillEnabled(true);
        //设置动画的最后一帧是保持在View上面
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContentLL.setAnimation(animation);
    }

    /**
     * 动画结束处理
     */
    private void animationEnd() {
        Intent intent = new Intent();
        if (mLoginUserInfo == null) {
            intent.setClass(SplashActivity.this, EndTaskActivity.class);
//            intent.setClass(SplashActivity.this, LoginActivity.class);
        } else {
            intent.setClass(SplashActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 异步同步用户信息
     * @param loginUserInfo
     */
    private void asyncRequestLogin(final LoginUserInfo loginUserInfo) {
        // 已经登录过了，上次登录过成功超过一天，重新登录一次
        RunningContext.threadPool().execute(new Runnable() {
            @Override
            public void run() {
                ServiceUserLoginReq req = new ServiceUserLoginReq();
                req.userTel = loginUserInfo.userTel;
                req.userPassword = loginUserInfo.userPassword;
                new ApiModel(RunningContext.sAppContext).login(req, new ResultHandler<ServiceUser>() {
                    @Override
                    protected void onSuccess(ServiceUser data) {
                        if (data != null) {
                            LoginUserInfo userInfo = YHUtils.copyByServiceUser(data);
                            // 更新用户信息
                            RunningContext.setLoginUserInfo(userInfo);
                        }
                    }
                });
            }
        });
    }

    /**
     * 动态申请权限
     */
    private void requestPermissions() {
        // TODO
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //在欢迎界面屏蔽BACK和HOME键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
