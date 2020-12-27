package com.yhjx.yhservice.api;

import android.content.Context;

import com.yhjx.networker.NetworkerClient;
import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.BaseResult;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.networker.callback.ResultNotifier;
import com.yhjx.networker.retrofit.Call;
import com.yhjx.networker.retrofit.Callback;
import com.yhjx.networker.retrofit.Retrofit;
import com.yhjx.yhservice.RunningContext;
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
import com.yhjx.yhservice.util.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ApiModel {

    private Context mContext;

    public ApiModel() {

    }

    public ApiModel(Context context) {
        mContext = context;
    }


    // ##################  通用接口 #############


    /**
     * 版本更新
     * @param handler
     */
    public void updateVersion(ResultHandler<VersionUpdateRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        VersionUpdateReq req = new VersionUpdateReq();
        req.productCode = "SHWX_ANDROID_APP";
        req.currentVersionCode = Integer.valueOf(RunningContext.getVersionCode()+"");
        ApiService apiService = buildApiService();
        SSCall<BaseResult<VersionUpdateRes>> call = apiService.versionUpdate(req);
        call.enqueue(handler);
    }



    /**
     * 上传图片
     * @param filePath
     * @param handler
     */
    public void uploadImg(String filePath, ResultHandler<UploadImgRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filePath, requestFile);

        SSCall<BaseResult<UploadImgRes>> call = apiService.uploadImg(body);
        call.enqueue(handler);
    }


    /**
     * 查询故障类别信息
     * @param req
     * @param handler
     */
    public void queryFaultCategoryList(String userNo, ResultHandler<GetFaultCategoryListRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        GetFaultCategoryListReq req = new GetFaultCategoryListReq();
        req.userNo = userNo;
        ApiService apiService = buildApiService();
        SSCall<BaseResult<GetFaultCategoryListRes>> call = apiService.queryFaultCategoryList(req);
        call.enqueue(handler);
    }


    /**
     * 查询车辆信息
     * @param req
     * @param handler
     */
    public void queryVehicleInfo(GetCarInfoReq req, ResultHandler<GetCarInfoRes> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<GetCarInfoRes>> call = apiService.queryVehicleInfo(req);
        call.enqueue(handler);
    }


    /**
     * 上传服务人员坐标位置
     * @param req
     * @param handler
     */
    public void updateLocation(UpdateLocationReq req, ResultHandler<Void> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Void>> call = apiService.updateLocation(req);
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


    // ########## 用户接口 ####################

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
     * 修改服务站
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
     * 修改驻点
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


    // ######################任务接口

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
     * 查询维修记录详情接口
     * @param req
     * @param handler
     */
    public void queryTaskDetail(TaskOrderDetailReq req, ResultHandler<TaskOrder> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<TaskOrder>> call = apiService.queryTaskDetail(req);
        call.enqueue(handler);
    }


    // ################## 操作接口

    /**
     * 新增保修单接口
     * @param req
     * @param handler
     */
    public void repair(TaskHandlerRepairReq req, ResultHandler<TaskOrder> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<TaskOrder>> call = apiService.repair(req);
        call.enqueue(handler);
    }


    /**
     * 接单接口
     * @param req
     * @param handler
     */
    public void receive(TaskHandleReceiveReq req, ResultHandler<Void> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Void>> call = apiService.receive(req);
        call.enqueue(handler);
    }


    /**
     * 取消接口
     * @param req
     * @param handler
     */
    public void cancel(TaskHandleCancelReq req, ResultHandler<Void> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Void>> call = apiService.cancel(req);
        call.enqueue(handler);
    }



    /**
     * 取消接口
     * @param req
     * @param handler
     */
    public void check(TaskHandleCheckReq req, ResultHandler<Boolean> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Boolean>> call = apiService.check(req);
        call.enqueue(handler);
    }


    /**
     * 取消接口
     * @param req
     * @param handler
     */
    public void start(TaskHandleStartReq req, ResultHandler<Void> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Void>> call = apiService.start(req);
        call.enqueue(handler);
    }


    /**
     * 取消接口
     * @param req
     * @param handler
     */
    public void end(TaskHandleFinishReq req, ResultHandler<Void> handler) {
        if (!preCheck(handler)) {
            return;
        }
        ApiService apiService = buildApiService();
        SSCall<BaseResult<Void>> call = apiService.finish(req);
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
