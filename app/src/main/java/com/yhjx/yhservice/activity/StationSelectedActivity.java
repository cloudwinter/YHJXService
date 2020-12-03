package com.yhjx.yhservice.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.adapter.StationListAdapter;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.StationListReq;
import com.yhjx.yhservice.api.domain.response.ServiceStationListRes;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.util.LogUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 服务站选择界面
 */
public class StationSelectedActivity extends BaseActivity {

    public static final String TAG = "StationSelectedActivity";

    @BindView(R.id.edit_search)
    protected EditText editTextSearch;
    @BindView(R.id.text_cancel)
    protected TextView textViewCancel;
    @BindView(R.id.lv)
    protected ListView listView;

    private StationListAdapter mStationListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_select);
        ButterKnife.bind(this);

        textViewCancel.setOnClickListener(mOnClick);
        editTextSearch.addTextChangedListener(mTextWatcher);

        mStationListAdapter = new StationListAdapter(this);
        listView.setAdapter(mStationListAdapter);
        listView.setOnItemClickListener(mOnItemClick);

    }

    /**
     * 搜索服务站
     *
     * @param searchVal
     */
    private void searchStation(String searchVal) {
        if (TextUtils.isEmpty(searchVal)) {
            return;
        }
        StationListReq req = new StationListReq();
        req.pageNo = 0;
        req.pageSize = 10;
        req.stationName = searchVal.trim();
        new ApiModel().queryStationList(req, new ResultHandler<ServiceStationListRes>() {
            @Override
            protected void onSuccess(ServiceStationListRes data) {
                LogUtils.d(TAG, "StationSelectedActivity.searchStation 返回结果：" + data);
                if (data == null || data.count == 0) {
                    mStationListAdapter.setData(null);
                    return;
                }
                mStationListAdapter.setData(data.list);
            }
        });
    }


    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i(TAG, "afterTextChanged: " + s.toString());
            searchStation(s.toString());
        }
    };


    private AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO
            finish();
        }
    };


    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


}
