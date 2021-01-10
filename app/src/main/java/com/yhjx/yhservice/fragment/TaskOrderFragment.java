package com.yhjx.yhservice.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.activity.EndTaskActivity;
import com.yhjx.yhservice.activity.HomeActivity;
import com.yhjx.yhservice.activity.StartTaskActivity;
import com.yhjx.yhservice.adapter.TaskListAdapter;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.TaskHandleCancelReq;
import com.yhjx.yhservice.api.domain.request.TaskHandleReceiveReq;
import com.yhjx.yhservice.api.domain.request.TaskOrderReq;
import com.yhjx.yhservice.api.domain.response.TaskOrderRes;
import com.yhjx.yhservice.base.BaseFragment;
import com.yhjx.yhservice.dialog.WaitDialog;
import com.yhjx.yhservice.model.LocationInfo;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.model.TaskOrder;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.StorageUtils;
import com.yhjx.yhservice.util.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 任务订单
 */
public class TaskOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TaskListAdapter.ButtonClickListener {

    public static final String TAG = "TaskOrderFragment";
    public static final int REQUEST_CODE = 104;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lv)
    ListView mTaskLV;
    TextView mUserNameTV;
    TextView mTvTaskNumTV;

    TaskListAdapter mTaskListAdapter;
    WaitDialog mWaitDialog;

    LoginUserInfo mLoginUserInfo;

    ApiModel mApiModel;


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG,"onResume");
        // 开启一次定位
        RunningContext.sAMapLocationClient.startLocation();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);
        mTaskLV.addHeaderView(addHeader());
        swipeRefreshLayout.setOnRefreshListener(this);
        mTaskListAdapter = new TaskListAdapter(mContext, this);
        mTaskLV.setAdapter(mTaskListAdapter);
        mTaskLV.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        mApiModel = new ApiModel();
        mWaitDialog = new WaitDialog(mContext);
        initData();
        return view;
    }

    private View addHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.view_task_list_header, null);
        mUserNameTV = headerView.findViewById(R.id.text_user_name);
        mTvTaskNumTV = headerView.findViewById(R.id.text_task_num);
        return headerView;
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
        // 加载数据前开启一次定位
        RunningContext.sAMapLocationClient.startLocation();
        TaskOrderReq req = new TaskOrderReq();
        req.userNo = mLoginUserInfo.userNo;
        req.pageNum = 1;
        req.pageSize = 20;
        mApiModel.queryTaskOrder(req, new ResultHandler<TaskOrderRes>() {

            @Override
            public void onStart() {
                mWaitDialog.show();
            }

            @Override
            protected void onSuccess(TaskOrderRes data) {
                setData(data);
            }

            @Override
            public void onFinish() {
                mWaitDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void setData(TaskOrderRes data) {
        if (data == null || data.count == 0) {
            mTvTaskNumTV.setText(data.count + "个");
            return;
        }
        mTvTaskNumTV.setText(data.count + "个");
        mTaskListAdapter.setData(data.list);
    }


    @Override
    public void onRefresh() {
        // 下拉刷新
        loadData();
    }

    @Override
    public void receiveClick(TaskOrder order) {
        // 接单
        // 1、获取当前的经纬度坐标
        LocationInfo locationInfo =  StorageUtils.getCurrentLocation();
        // 2、请求接单接口
        TaskHandleReceiveReq req = new TaskHandleReceiveReq();
        req.taskNo = order.taskNo;
        req.userNo = mLoginUserInfo.userNo;
        req.latitude = locationInfo.latitude;
        req.longitude = locationInfo.longitude;
        req.userAddress = locationInfo.address;
        mApiModel.receive(req, new ResultHandler<Void>() {


            @Override
            protected void onSuccess(Void data) {
                ToastUtils.showToast(mContext, "接单成功");
                loadData();
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                super.onFailed(errCode, errMsg);
                ToastUtils.showToast(mContext, "接单失败:"+errMsg);
            }

        });

    }

    @Override
    public void startClick(TaskOrder order) {
        Intent intent = new Intent();
        intent.putExtra(StartTaskActivity.TASK_NO_EXTRA_KEY, order.taskNo);
        intent.setClass(mContext, StartTaskActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void endClick(TaskOrder order) {
        Intent intent = new Intent();
        intent.putExtra(StartTaskActivity.TASK_NO_EXTRA_KEY, order.taskNo);
        intent.setClass(mContext, EndTaskActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void cancelClick(TaskOrder order) {
        // 取消
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("退出");
        builder.setMessage("您确定要取消当前任务");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelTask();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        // 去除title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    /**
     * 取消任务
     */
    private void cancelTask() {
        // 1、获取当前的经纬度坐标
        LocationInfo locationInfo =  StorageUtils.getCurrentLocation();

        TaskHandleCancelReq req = new TaskHandleCancelReq();
        req.userNo = mLoginUserInfo.userNo;
        req.userName = mLoginUserInfo.userName;

        req.latitude = locationInfo.latitude;
        req.longitude = locationInfo.longitude;
        req.userAddress = locationInfo.address;
        req.stopReason = "个人原因";

        mApiModel.cancel(req, new ResultHandler<Void>() {
            @Override
            protected void onSuccess(Void data) {
                ToastUtils.showToast(mContext,"取消成功");
                loadData();
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                super.onFailed(errCode, errMsg);
                ToastUtils.showToast(mContext,"取消失败！");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == 104 || REQUEST_CODE == HomeActivity.ADD_REQUEST_CODE ) {
            // 刷新界面
            loadData();
        }
    }
}
