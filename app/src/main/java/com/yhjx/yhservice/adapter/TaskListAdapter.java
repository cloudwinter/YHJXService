package com.yhjx.yhservice.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.base.BaseListAdapter;
import com.yhjx.yhservice.model.StationModel;
import com.yhjx.yhservice.model.TaskOrder;
import com.yhjx.yhservice.util.ToastUtils;
import com.yhjx.yhservice.view.YHButton;


/**
 * 任务单列表
 */
public class TaskListAdapter extends BaseListAdapter<TaskOrder> {


    public ButtonClickListener mButtonClickListener;

    public TaskListAdapter(Context context,ButtonClickListener buttonClickListener) {
        super(context);
        mButtonClickListener = buttonClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent,false);
        }

        final TaskOrder order = getItem(position);
        if (order == null) {
            return convertView;
        }
        // 任务单号
        TextView orderNum = getChildView(convertView,R.id.text_task_num);
        orderNum.setText(getFormatValue(R.string.task_item_order_no,order.taskNo));

        // 车架号
        TextView vin = getChildView(convertView,R.id.text_vin);
        vin.setText(getFormatValue(R.string.task_item_vin,order.vehicleVin));

        // 车型
        TextView vehicleName = getChildView(convertView,R.id.text_vehicle_name);
        vehicleName.setText(getFormatValue(R.string.task_item_vehicle_name,order.vehicleName));

        // 客户姓名
        TextView customerName = getChildView(convertView,R.id.text_customer_name);
        customerName.setText(getFormatValue(R.string.task_item_customer_name,order.customerName));

        // 客户电话
        TextView customerTel = getChildView(convertView,R.id.text_customer_tel);
        customerTel.setText(getFormatValue(R.string.task_item_customer_tel,order.customerTel));

        // 客户地址
        TextView customerAddress = getChildView(convertView,R.id.text_customer_address);
        customerAddress.setText(getFormatValue(R.string.task_item_customer_address,order.vehicleAddress));

        // 故障描述
        TextView faultDesc = getChildView(convertView,R.id.text_fault_desc);
        faultDesc.setText(getFormatValue(R.string.task_item_fault_desc,order.faultDesc));


        // 拨打电话
        LinearLayout llCustomerTel = getChildView(convertView,R.id.lv_customer_tel);
        llCustomerTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到拨打电话页面
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + order.customerTel));
                mContext.startActivity(dialIntent);
            }
        });

        LinearLayout llCustomerAddress = getChildView(convertView,R.id.lv_customer_address);
        llCustomerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到地图界面
                String coordinate = getFormatValue(R.string.intent_map_coordinate_str,order.vehicleLatitude,order.vehicleLongitude);
                Uri uri=Uri.parse(coordinate);  //打开地图定位
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                ComponentName cn = mapIntent.resolveActivity(mContext.getPackageManager());
                if(cn == null){
                    ToastUtils.showToast(mContext,"请先安装第三方导航软件");
                }else{
                    mContext.startActivity(mapIntent);
                }
            }
        });


        // 操作按钮
        YHButton receiveBtn = getChildView(convertView,R.id.but_task_receive);
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonClickListener.receiveClick(order);
            }
        });
        YHButton startBtn = getChildView(convertView,R.id.but_task_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonClickListener.startClick(order);
            }
        });
        YHButton endBtn = getChildView(convertView,R.id.but_task_end);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonClickListener.endClick(order);
            }
        });
        YHButton cancelBtn = getChildView(convertView,R.id.but_task_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonClickListener.cancelClick(order);
            }
        });

        // 订单状态
        TextView textOrderStatus = getChildView(convertView,R.id.text_task_status);
        String orderStatus = order.taskStatus;
        // 设置订单状态
        if (orderStatus.equals("1")) {
            // 已指派，待接单
            textOrderStatus.setText("待接单");
            receiveBtn.setEnabled(true);
            startBtn.setEnabled(false);
            endBtn.setEnabled(false);
            cancelBtn.setEnabled(true);
        } else if (orderStatus.equals("2")) {
            // 已接单，待开工
            textOrderStatus.setText("待开工");
            receiveBtn.setEnabled(false);
            startBtn.setEnabled(true);
            endBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
        } else if (orderStatus.equals("3") || orderStatus.equals("4")) {
            // 已开工（正常开工或者异常开工）,待完工
            textOrderStatus.setText("待完工");
            receiveBtn.setEnabled(false);
            startBtn.setEnabled(false);
            endBtn.setEnabled(true);
            cancelBtn.setEnabled(false);
        } else {
            // 非操作状态
            textOrderStatus.setText("");
            // 其他状态都不可以操作
            receiveBtn.setEnabled(false);
            startBtn.setEnabled(false);
            endBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
        }
        return convertView;
    }


    private String getFormatValue(int resId,String... params) {
        return String.format(getString(resId),params);
    }


    public interface ButtonClickListener {
        // 接单
        void receiveClick(TaskOrder order);
        // 开工
        void startClick(TaskOrder order);
        // 完工
        void endClick(TaskOrder order);
        // 取消
        void cancelClick(TaskOrder order);

    }
}
