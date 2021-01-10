package com.yhjx.yhservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.adapter.StationListAdapter;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.StationListReq;
import com.yhjx.yhservice.api.domain.response.ServiceStationListRes;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.model.StationModel;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 服务站选择界面
 */
public class StationSelectedActivity extends BaseActivity {

    public static final String TAG = "StationSelectedActivity";

    public static final String RESULT_DATA_KEY = "RESULT_DATA";

    @BindView(R.id.edit_search)
    protected EditText editTextSearch;
    @BindView(R.id.text_cancel)
    protected TextView textViewCancel;
    @BindView(R.id.lv)
    protected ListView listView;

    private StationListAdapter mStationListAdapter;

    private List<StationModel> modelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_select);
        ButterKnife.bind(this);

        textViewCancel.setOnClickListener(mOnClick);
        editTextSearch.addTextChangedListener(mTextWatcher);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) RunningContext.sAppContext
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(StationSelectedActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    if (editTextSearch.getText().toString().isEmpty()) {
                        ToastUtils.showToast(RunningContext.sAppContext,"搜索栏不能为空！");
                    } else {
                        //搜索
                        searchStation();
                    }
                    return true;
                }
                return false;
            }
        });

        mStationListAdapter = new StationListAdapter(this);
        listView.setAdapter(mStationListAdapter);
        listView.setOnItemClickListener(mOnItemClick);

    }

    /**
     * 搜索服务站
     */
    private void searchStation() {
        String searchVal = editTextSearch.getText().toString();
        if (TextUtils.isEmpty(searchVal)) {
            return;
        }
        StationListReq req = new StationListReq();
        req.pageNum = 1;
        req.pageSize = 10;
        req.stationName = searchVal.trim();
        new ApiModel(this).queryStationList(req, new ResultHandler<ServiceStationListRes>() {
            @Override
            protected void onSuccess(ServiceStationListRes data) {
                LogUtils.d(TAG, "StationSelectedActivity.searchStation 返回结果：" + data);
                if (data == null || data.count == 0) {
                    mStationListAdapter.setData(null);
                    return;
                }
                modelList = data.list;
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
            searchStation();
        }
    };


    private AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.putExtra(RESULT_DATA_KEY, modelList.get(position));
            setResult(RESULT_OK,intent);
            finish();
        }
    };


    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_cancel:
                    break;
            }
            finish();
        }
    };


}
