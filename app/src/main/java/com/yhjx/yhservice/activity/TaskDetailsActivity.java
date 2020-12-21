package com.yhjx.yhservice.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.model.TaskOrder;
import com.yhjx.yhservice.util.YHUtils;
import com.yhjx.yhservice.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 任务详情界面
 */
public class TaskDetailsActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener {

    public static String TASK_ORDER_EXTRA_KEY = "task_order";

    @BindView(R.id.action_bar)
    TranslucentActionBar mActionBar;
    @BindView(R.id.tv_task_no)
    TextView mTaskNoTV;
    @BindView(R.id.tv_vin)
    TextView mVinTV;
    @BindView(R.id.tv_customer_name)
    TextView mCustomerNameTV;
    @BindView(R.id.tv_customer_tel)
    TextView mCustomerTelTV;
    @BindView(R.id.text_fault_desc)
    TextView mFaultDescTV;
    @BindView(R.id.tv_fault_type)
    TextView mFaultTypeTV;
    @BindView(R.id.tv_fault_reason)
    TextView mFaultReasonTV;
    @BindView(R.id.tv_part)
    TextView mPartTV;
    @BindView(R.id.start_task_img1)
    ImageView mStartImg1;
    @BindView(R.id.start_task_img2)
    ImageView mStartImg2;
    @BindView(R.id.end_task_img1)
    ImageView mEndImg1;
    @BindView(R.id.end_task_img2)
    ImageView mEndImg2;

    private TaskOrder mTaskOrder;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_detail_task);
        ButterKnife.bind(this);
        mActionBar.setData("订单详情", R.mipmap.ic_back, null, 0, null, this);
        mActionBar.setStatusBarHeight(getStatusBarHeight());
        mTaskOrder = (TaskOrder) getIntent().getSerializableExtra(TASK_ORDER_EXTRA_KEY);

        mImageLoader = ImageLoader.getInstance();
        initData();
    }

    private void initData() {
        if (mTaskOrder == null) {
            return;
        }
        mTaskNoTV.setText(YHUtils.getFormatValue(R.string.task_item_order_no, mTaskOrder.taskNo));
        mVinTV.setText(YHUtils.getFormatValue(R.string.task_item_vin, mTaskOrder.vehicleVin));
        mCustomerNameTV.setText(YHUtils.getFormatValue(R.string.task_item_customer_name, mTaskOrder.customerName));
        mCustomerTelTV.setText(YHUtils.getFormatValue(R.string.task_item_customer_tel, mTaskOrder.customerTel));
        mFaultDescTV.setText(YHUtils.getFormatValue(R.string.task_item_fault_desc, mTaskOrder.faultDesc));

        mFaultTypeTV.setText(mTaskOrder.faultCategoryName);
        mFaultReasonTV.setText(mTaskOrder.faultCause);
        mPartTV.setText(mTaskOrder.faultAccessories);
        if (!TextUtils.isEmpty(mTaskOrder.startImgPath)) {
            String[] startImgPathArray = mTaskOrder.startImgPath.split(",");
            mImageLoader.displayImage(startImgPathArray[0],mStartImg1);
            if (startImgPathArray.length > 1) {
                mImageLoader.displayImage(startImgPathArray[1],mStartImg2);
            }
        }
        if (!TextUtils.isEmpty(mTaskOrder.endImgPath)) {
            String[] endImgPathArray = mTaskOrder.endImgPath.split(",");
            mImageLoader.displayImage(endImgPathArray[0],mEndImg1);
            if (endImgPathArray.length > 1) {
                mImageLoader.displayImage(endImgPathArray[1],mEndImg2);
            }
        }
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
