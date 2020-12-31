package com.yhjx.yhservice.api;

import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.BaseResult;
import com.yhjx.networker.http.Body;
import com.yhjx.networker.http.GET;
import com.yhjx.networker.http.Multipart;
import com.yhjx.networker.http.POST;
import com.yhjx.networker.http.Part;
import com.yhjx.networker.http.Streaming;
import com.yhjx.networker.http.Url;
import com.yhjx.networker.retrofit.Call;
import com.yhjx.yhservice.api.domain.request.GetCarInfoReq;
import com.yhjx.yhservice.api.domain.request.GetFaultCategoryListReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserLoginReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserRegisterReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdatePasswordReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateStagnationReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateStationReq;
import com.yhjx.yhservice.api.domain.request.ServiceUserUpdateTelReq;
import com.yhjx.yhservice.api.domain.request.StationListReq;
import com.yhjx.yhservice.api.domain.request.TaskHandleCancelReq;
import com.yhjx.yhservice.api.domain.request.TaskHandleCheckReq;
import com.yhjx.yhservice.api.domain.request.TaskHandleFinishReq;
import com.yhjx.yhservice.api.domain.request.TaskHandleReceiveReq;
import com.yhjx.yhservice.api.domain.request.TaskHandleStartReq;
import com.yhjx.yhservice.api.domain.request.TaskHandlerRepairReq;
import com.yhjx.yhservice.api.domain.request.TaskOrderDetailReq;
import com.yhjx.yhservice.api.domain.request.TaskOrderReq;
import com.yhjx.yhservice.api.domain.request.TaskRecordReq;
import com.yhjx.yhservice.api.domain.request.UpdateLocationReq;
import com.yhjx.yhservice.api.domain.request.VersionUpdateReq;
import com.yhjx.yhservice.api.domain.response.GetCarInfoRes;
import com.yhjx.yhservice.api.domain.response.GetFaultCategoryListRes;
import com.yhjx.yhservice.api.domain.response.ServiceStationListRes;
import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.api.domain.response.ServiceUserRegisterRes;
import com.yhjx.yhservice.api.domain.response.ServiceUserUpdateStagnationRes;
import com.yhjx.yhservice.api.domain.response.TaskOrderRes;
import com.yhjx.yhservice.api.domain.response.TaskRecordRes;
import com.yhjx.yhservice.api.domain.response.UploadImgRes;
import com.yhjx.yhservice.api.domain.response.Vehicle;
import com.yhjx.yhservice.api.domain.response.VersionUpdateRes;
import com.yhjx.yhservice.model.TaskOrder;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;


public interface ApiService {


    /**
     * 版本更新app
     * @param req
     * @return
     */
    @POST("app/service/common/updateVersion")
    SSCall<BaseResult<VersionUpdateRes>> versionUpdate(@Body VersionUpdateReq req);

    // 图片上传接口
    @Multipart
    @POST("common/upload")
    SSCall<BaseResult<UploadImgRes>> uploadImg(@Part MultipartBody.Part file);

    // 查询故障类型列表
    @POST("app/service/common/getFaultCategoryList")
    SSCall<BaseResult<GetFaultCategoryListRes>> queryFaultCategoryList(@Body GetFaultCategoryListReq req);


    // 查询服务站列表
    @POST("app/service/common/stationList")
    SSCall<BaseResult<ServiceStationListRes>> queryStationList(@Body StationListReq req);

    // 查询车辆信息
    @POST("app/service/common/getVehicleInfo")
    SSCall<BaseResult<GetCarInfoRes>> queryVehicleInfo(@Body GetCarInfoReq req);

    // 上报坐标信息
    @POST("app/service/common/updateLocation")
    SSCall<BaseResult<Void>> updateLocation(@Body UpdateLocationReq req);


    // 注册接口
    @POST("app/service/user/register")
    SSCall<BaseResult<ServiceUserRegisterRes>> register(@Body ServiceUserRegisterReq req);


    // 登录接口
    @POST("app/service/user/login")
    SSCall<BaseResult<ServiceUser>> login(@Body ServiceUserLoginReq req);


    // 修改手机号
    @POST("app/service/user/updateTel")
    SSCall<BaseResult<Void>> updateTel(@Body ServiceUserUpdateTelReq req);

    // 修改手机号
    @POST("app/service/user/updatePassword")
    SSCall<BaseResult<Void>> updatePassword(@Body ServiceUserUpdatePasswordReq req);

    // 修改服务站
    @POST("app/service/user/updateStation")
    SSCall<BaseResult<Void>> updateStation(@Body ServiceUserUpdateStationReq req);

    // 修改驻点
    @POST("app/service/user/updateStagnation")
    SSCall<BaseResult<ServiceUserUpdateStagnationRes>> updateStagnation(@Body ServiceUserUpdateStagnationReq req);







    // 查询任务单
    @POST("app/service/task/getPendingTaskList")
    SSCall<BaseResult<TaskOrderRes>> queryTaskList(@Body TaskOrderReq req);

    // 查询维修单
    @POST("app/service/task/getHandlerTaskList")
    SSCall<BaseResult<TaskRecordRes>> queryRecordList(@Body TaskRecordReq req);

    // 查询单个任务单详情
    @POST("app/service/task/getTaskDetail")
    SSCall<BaseResult<TaskOrder>> queryTaskDetail(@Body TaskOrderDetailReq req);




    // 新增保修单
    @POST("app/service/task/repair")
    SSCall<BaseResult<TaskOrder>> repair(@Body TaskHandlerRepairReq repairReq);

    // 接单
    @POST("app/service/task/receive")
    SSCall<BaseResult<Void>> receive(@Body TaskHandleReceiveReq repairReq);

    // 取消
    @POST("app/service/task/stop")
    SSCall<BaseResult<Void>> cancel(@Body TaskHandleCancelReq repairReq);

    // 开工和完工的precheck接口
    @POST("app/service/task/check")
    SSCall<BaseResult<Boolean>> check(@Body TaskHandleCheckReq checkReq);


    // 开工接口
    @POST("app/service/task/start")
    SSCall<BaseResult<Void>> start(@Body TaskHandleStartReq startReq);

    // 完工接口
    @POST("app/service/task/finish")
    SSCall<BaseResult<Void>> finish(@Body TaskHandleFinishReq finishReq);



}
