package com.yhjx.yhservice.api;

import com.yhjx.networker.NetworkerClient;
import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.BaseResult;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.domain.request.ServiceUserLoginReq;
import com.yhjx.yhservice.api.domain.response.ServiceUser;

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
     * 构建apiService
     * @return
     */
    private ApiService buildApiService() {
        return NetworkerClient.create(RunningContext.BASEURL, ApiService.class);
    }
}
