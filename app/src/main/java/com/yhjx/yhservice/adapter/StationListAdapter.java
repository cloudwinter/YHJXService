package com.yhjx.yhservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.base.BaseListAdapter;
import com.yhjx.yhservice.model.StationModel;


/**
 * 服务站列表
 */
public class StationListAdapter extends BaseListAdapter<StationModel> {

    public StationListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_station, parent,false);
        }
        // 名称
        TextView name = getChildView(convertView,R.id.text_station_name);
        name.setText(getItem(position).stationName);
        // 地址
        TextView address = getChildView(convertView,R.id.text_station_address);
        address.setText(getItem(position).stationAddress);
        return convertView;
    }
}
