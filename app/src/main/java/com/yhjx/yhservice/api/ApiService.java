package com.yhjx.yhservice.api;

import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.BaseResult;
import com.yhjx.networker.http.Body;
import com.yhjx.networker.http.POST;
import com.yhjx.yhservice.api.domain.request.ServiceUserLoginReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserRegisterReq;
import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.api.domain.response.ServiceUserRegisterRes;

public interface ApiService {

    // 注册接口
    @POST("/app/service/user/register")
    SSCall<BaseResult<ServiceUserRegisterRes>> register(@Body ServiceUserRegisterReq req);


    // 登录接口
    @POST("/app/service/user/login")
    SSCall<BaseResult<ServiceUser>> login(@Body ServiceUserLoginReq req);

}
