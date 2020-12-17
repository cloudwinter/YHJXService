package com.yhjx.yhservice.api;

import android.content.Context;

import com.yhjx.networker.NetworkerClient;
import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.BaseResult;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.domain.request.ServiceUserLoginReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserRegisterReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdatePasswordReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateStagnationReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateStationReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateTelReq;
import com.yhjx.yhservice.api.domain.request.StationListReq;
import com.yhjx.yhservice.api.domain.request.TaskOrderReq;
import com.yhjx.yhservice.api.domain.request.TaskRecordReq;
import com.yhjx.yhservice.api.domain.response.ServiceStationListRes;
import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.api.domain.response.ServiceUserRegisterRes;
import com.yhjx.yhservice.api.domain.response.ServiceUserUpdateStagnationRes;
import com.yhjx.yhservice.api.domain.response.TaskOrderRes;
import com.yhjx.yhservice.api.domain.response.TaskRecordRes;
import com.yhjx.yhservice.util.ToastUtils;


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




    /**
     * 修改手机号
     * @param req
     * @param handler
     */
    public void updateTel(ServiceUserUpdateTelReq req, ResultHandler<Void> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Void>> call = apiService.updateTel(req);
        call.enqueue(handler);
    }


    /**
     * 修改密码
     * @param req
     * @param handler
     */
    public void updatePassword(ServiceUserUpdatePasswordReq req, ResultHandler<Void> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Void>> call = apiService.updatePassword(req);
        call.enqueue(handler);
    }


    /**
     * 修改密码
     * @param req
     * @param handler
     */
    public void updateStation(ServiceUserUpdateStationReq req, ResultHandler<Void> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Void>> call = apiService.updateStation(req);
        call.enqueue(handler);
    }


    /**
     * 修改密码
     * @param req
     * @param handler
     */
    public void updateStagnation(ServiceUserUpdateStagnationReq req, ResultHandler<ServiceUserUpdateStagnationRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<ServiceUserUpdateStagnationRes>> call = apiService.updateStagnation(req);
        call.enqueue(handler);
    }







    private <T> boolean preCheck(ResultHandler<T> handler) {
        if (!RunningContext.checkNetWork()) {
            ToastUtils.showNotNetwork(RunningContext.sAppContext);
            handler.notifyFinish();
            return false;
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
