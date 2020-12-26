package com.yhjx.yhservice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.activity.TaskDetailsActivity;
import com.yhjx.yhservice.adapter.RecordListAdapter;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.TaskRecordReq;
import com.yhjx.yhservice.api.domain.response.TaskRecordRes;
import com.yhjx.yhservice.base.BaseFragment;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.model.TaskOrder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 维修记录
 */
public class RecordFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lv_record)
    ListView mRecordLV;
    TextView mUserNameTV;

    RecordListAdapter mRecordListAdapter;
    WaitDialog mWaitDialog;

    LoginUserInfo mLoginUserInfo;

    List<TaskOrder> mTaskOrderList;


    private View addHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.view_record_list_header, null);
        mUserNameTV = headerView.findViewById(R.id.text_user_name);
        return headerView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        mRecordLV.addHeaderView(addHeader());
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecordListAdapter = new RecordListAdapter(mContext);
        mRecordLV.setAdapter(mRecordListAdapter);
        mRecordLV.setOnItemClickListener(mOnItemClick);
        mRecordLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //当前最顶上的item是listview的第一条时打开
                if (firstVisibleItem == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
        mWaitDialog = new WaitDialog(mContext);

        initData();

        return view;
    }

    private void initData() {
        mLoginUserInfo = RunningContext.getsLoginUserInfo();
        if (mLoginUserInfo == null) {
            return;
        }
        String userName = String.format(getString(R.string.task_fragment_show_user_name), mLoginUserInfo.userName);
        mUserNameTV.setText(userName);
        loadData();
    }


    private void loadData() {
        TaskRecordReq req = new TaskRecordReq();
        req.userNo = mLoginUserInfo.userNo;
        req.pageNo = 1;
        req.pageSize = 20;
        new ApiModel(mContext).queryRecordOrder(req, new ResultHandler<TaskRecordRes>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(TaskRecordRes data) {
                setData(data);
            }

            @Override
            public void onFinish() {
                mWaitDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void setData(TaskRecordRes data) {
        mTaskOrderList = data.list;
        mRecordListAdapter.setData(data.list);
    }


    @Override
    public void onRefresh() {
        // 上拉刷新
        loadData();
    }

    AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.setClass(mContext, TaskDetailsActivity.class);
            intent.putExtra(TaskDetailsActivity.TASK_ORDER_EXTRA_KEY, mTaskOrderList.get(position-1));
            startActivity(intent);
        }
    };
}
