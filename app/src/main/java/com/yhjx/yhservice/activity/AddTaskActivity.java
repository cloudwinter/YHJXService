package com.yhjx.yhservice.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加保修任务
 */
public class AddTaskActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener {

    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;
    @BindView(R.id.edit_vin)
    EditText mVinEditText;
    @BindView(R.id.edit_customer_name)
    EditText mCustomerNameEditText;
    @BindView(R.id.edit_fault_desc)
    EditText mFaultDescEditText;
    @BindView(R.id.edit_vehicle_name)
    EditText mVehicleNameEditText;
    @BindView(R.id.edit_vehicle_address)
    EditText mVehicleAddressEditText;
    @BindView(R.id.btn_submit)
    Button mSubmitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        actionBar.setData("添加任务单", R.mipmap.ic_back, null, 0, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());

        mSubmitButton.setOnClickListener(mSubmitClicker);
        mVinEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                   // TODO 焦点移开事件
                }
            }
        });

    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }


    View.OnClickListener mSubmitClicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO 提交
        }
    };
}
