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
import com.yhjx.yhservice.model.TaskOrder;
import com.yhjx.yhservice.util.DateUtil;
import com.yhjx.yhservice.util.ToastUtils;


/**
 * 任务单列表
 */
public class RecordListAdapter extends BaseListAdapter<TaskOrder> {



    public RecordListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record, parent,false);
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

        // 开工时间
        TextView startTime = getChildView(convertView,R.id.text_start_time);
        startTime.setText(getFormatValue(R.string.task_item_start_time,order.startTime));

        // 完工时间
        TextView endTime = getChildView(convertView,R.id.text_end_time);
        endTime.setText(getFormatValue(R.string.task_item_start_time,order.endTime));

        // 订单状态
        TextView textViewOrderStatus = getChildView(convertView,R.id.text_task_status);
        String orderStatus = order.taskStatus;
        if (orderStatus.equals("5") || orderStatus.equals("6")
                || orderStatus.equals("7") || orderStatus.equals("8")) {
            textViewOrderStatus.setText("已完工");
        } else if (orderStatus.equals("9")){
            textViewOrderStatus.setText("已取消");
        }
        return convertView;
    }


    private String getFormatValue(int resId,Object... params) {
        return String.format(getString(resId),params);
    }


}
