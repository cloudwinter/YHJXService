package com.yhjx.yhservice.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.yhjx.networker.NetworkerClient;
import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.BaseResult;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.domain.request.ServiceUserLoginReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserRegisterReq;
import com.yhjx.yhservice.api.domain.request.StationListReq;
import com.yhjx.yhservice.api.domain.request.TaskOrderReq;
import com.yhjx.yhservice.api.domain.request.TaskRecordReq;
import com.yhjx.yhservice.api.domain.response.ServiceStationListRes;
import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.api.domain.response.ServiceUserRegisterRes;
import com.yhjx.yhservice.api.domain.response.TaskOrderRes;
import com.yhjx.yhservice.api.domain.response.TaskRecordRes;
import com.yhjx.yhservice.util.ToastUtils;

import static com.yhjx.yhservice.RunningContext.PERMISSION_REQUEST_CODE;

public class ApiModel {

    private Context mContext;

    public ApiModel(Context context) {
        mContext = context;
    }

    /**
     * 登录接口
     * @param req
     * @param handler
     */
    public void login(ServiceUserLoginReq req, ResultHandler<ServiceUser> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<ServiceUser>> call = apiService.login(req);
        call.enqueue(handler);
    }


    /**
     * 注册接口
     * @param req
     * @param handler
     */
    public void register(ServiceUserRegisterReq req, ResultHandler<ServiceUserRegisterRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<ServiceUserRegisterRes>> call = apiService.register(req);
        call.enqueue(handler);
    }


    /**
     * 查询服务站接口
     * @param req
     * @param handler
     */
    public void queryStationList(StationListReq req, ResultHandler<ServiceStationListRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<ServiceStationListRes>> call = apiService.queryStationList(req);
        call.enqueue(handler);
    }



    /**
     * 查询任务单接口
     * @param req
     * @param handler
     */
    public void queryTaskOrder(TaskOrderReq req, ResultHandler<TaskOrderRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<TaskOrderRes>> call = apiService.queryTaskList(req);
        call.enqueue(handler);
    }


    /**
     * 查询维修记录接口
     * @param req
     * @param handler
     */
    public void queryRecordOrder(TaskRecordReq req, ResultHandler<TaskRecordRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<TaskRecordRes>> call = apiService.queryRecordList(req);
        call.enqueue(handler);
    }






    private <T> boolean preCheck(ResultHandler<T> handler) {
        if (!RunningContext.checkNetWork()) {
            ToastUtils.showNotNetwork(RunningContext.sAppContext);
            handler.notifyFinish();
            return false;
        }
        if (!RunningContext.checkNetworkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION_REQUEST_CODE);
                    handler.notifyFinish();
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 构建apiService
     * @return
     */
    private ApiService buildApiService() {
        return NetworkerClient.create(RunningContext.BASEURL, ApiService.class);
    }
}
