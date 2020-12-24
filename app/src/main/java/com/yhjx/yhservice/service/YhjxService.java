package com.yhjx.yhservice.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.UpdateLocationReq;
import com.yhjx.yhservice.model.LocationInfo;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.StorageUtils;


public class YhjxService extends JobService {
    
    public static final String TAG = "YhjxService";

    public static final String LOCATION_RECEIVER_ACTION = "com.yhjx.yhservice.LOCATION_RECEIVER";
    public static final String KEY_LOCATION_DATA = "LOCATION_DATA";

    @Override
    public void onCreate() {
        LogUtils.i(TAG,"onCreate");
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LOCATION_RECEIVER_ACTION);
        registerReceiver(mLocationReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        LogUtils.i(TAG,"onDestroy");
        unregisterReceiver(mLocationReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtils.i(TAG,"onStartJob 开始执行定时任务：params="+params);
        startLocation();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtils.i(TAG,"onStartJob 暂停定时任务：params="+params);
        return false;
    }

    /**
     * 开启定位
     */
    private void startLocation() {
        // 开启定位
        RunningContext.sAMapLocationClient.startLocation();
    }

    /**
     * 上传坐标位置
     */
    private void uploadLocation(LocationInfo locationInfo) {
        LoginUserInfo loginUserInfo =  RunningContext.getsLoginUserInfo();
        if (loginUserInfo == null) {
            return;
        }
        UpdateLocationReq req = new UpdateLocationReq();
        req.userLatitude = locationInfo.latitude;
        req.userLongitude = locationInfo.longitude;
        req.userAddress = locationInfo.address;
        req.userNo = loginUserInfo.userNo;
        new ApiModel().updateLocation(req, new ResultHandler<Void>() {
            @Override
            protected void onSuccess(Void data) {
                LogUtils.i(TAG,"uploadLocation 坐标上传成功");
            }
        });


    }


    /**
     * 定位结果广播
     */
    private BroadcastReceiver mLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 定位结果
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            AMapLocation mapLocation = bundle.getParcelable(KEY_LOCATION_DATA);
            LogUtils.i(TAG,"mLocationReceiver.mapLocation:"+mapLocation);
            if (mapLocation != null && !TextUtils.isEmpty(mapLocation.getAddress())) {
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.address = mapLocation.getAddress();
                locationInfo.city = mapLocation.getCity();
                locationInfo.latitude = mapLocation.getLatitude()+"";
                locationInfo.longitude = mapLocation.getLongitude() + "";
                StorageUtils.setCurrentLocation(locationInfo);
                uploadLocation(locationInfo);
            }

        }
    };
}
