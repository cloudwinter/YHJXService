package com.yhjx.yhservice.api;

import com.yhjx.networker.NetworkerClient;
import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.BaseResult;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.domain.request.ServiceUserLoginReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserRegisterReq;
import com.yhjx.yhservice.api.domain.request.StationListReq;
import com.yhjx.yhservice.api.domain.response.ServiceStationListRes;
import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.api.domain.response.ServiceUserRegisterRes;
import com.yhjx.yhservice.util.ToastUtils;

public class ApiModel {


    /**
     * 登录接口
     * @param req
     * @param handler
     */
    public void login(ServiceUserLoginReq req, ResultHandler<ServiceUser> handler) {
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
        ApiService apiService = buildApiService();
        SSCall<BaseResult<ServiceUserRegisterRes>> call = apiService.register(req);
        call.enqueue(handler);
    }


    /**
     * 注册接口
     * @param req
     * @param handler
     */
    public void queryStationList(StationListReq req, ResultHandler<ServiceStationListRes> handler) {
        ApiService apiService = buildApiService();
        SSCall<BaseResult<ServiceStationListRes>> call = apiService.queryStationList(req);
        call.enqueue(handler);
    }





    /**
     * 构建apiService
     * @return
     */
    private ApiService buildApiService() {
        if (!RunningContext.checkNetWork()) {
            ToastUtils.showNotNetwork(RunningContext.sAppContext);
        }
        return NetworkerClient.create(RunningContext.BASEURL, ApiService.class);
    }
}
