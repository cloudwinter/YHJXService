package com.yhjx.yhservice.api;

import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.BaseResult;
import com.yhjx.networker.http.Body;
import com.yhjx.networker.http.POST;
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

public interface ApiService {

    // 注册接口
    @POST("app/service/user/register")
    SSCall<BaseResult<ServiceUserRegisterRes>> register(@Body ServiceUserRegisterReq req);


    // 登录接口
    @POST("app/service/user/login")
    SSCall<BaseResult<ServiceUser>> login(@Body ServiceUserLoginReq req);

    // 查询服务站列表
    @POST("app/service/common/stationList")
    SSCall<BaseResult<ServiceStationListRes>> queryStationList(@Body StationListReq req);


    // 查询任务单
    @POST("app/service/task/getPendingTaskList")
    SSCall<BaseResult<TaskOrderRes>> queryTaskList(@Body TaskOrderReq req);

    // 查询维修单
    @POST("app/service/task/getHandlerTaskList")
    SSCall<BaseResult<TaskRecordRes>> queryRecordList(@Body TaskRecordReq req);

}
