package com.yhjx.yhservice.base;


import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * actionBar 基类
 */
public abstract class BaseActionBarActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener {

    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;

    public abstract int layoutResID();

    public abstract String setTitle();

    public abstract void createView(Bundle savedInstanceState);

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
        setContentView(layoutResID());
        ButterKnife.bind(this);
        actionBar.setData(setTitle(), R.mipmap.ic_back, null, 0, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());
    }


}
