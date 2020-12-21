package com.yhjx.yhservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.api.domain.response.FaultCategory;
import com.yhjx.yhservice.base.BaseListAdapter;
import com.yhjx.yhservice.model.StationModel;


/**
 * 服务站列表
 */
public class FaultTypeListAdapter extends BaseListAdapter<FaultCategory> {

    public FaultTypeListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fault_type, parent,false);
        }
        // 名称
        TextView faultType = getChildView(convertView,R.id.text_fault_type);
        faultType.setText(getItem(position).faultCategoryName);
        return convertView;
    }
}
