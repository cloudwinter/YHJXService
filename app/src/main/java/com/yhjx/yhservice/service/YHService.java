package com.yhjx.yhservice.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.ApiModel;
import com.yhjx.yhservice.api.domain.request.UpdateLocationReq;
import com.yhjx.yhservice.model.LocationInfo;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.StorageUtils;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class YHService extends Service {

    public static final String TAG = "YHService";
    public static final String LOCATION_RECEIVER_ACTION = "com.yhjx.yhservice.LOCATION_RECEIVER";
    public static final String KEY_LOCATION_DATA = "LOCATION_DATA";
    public static final String CHANNEL_ID_STRING = "service_01";
    // 10分钟
    public static int LENGTH_OF_TIMING = 10 * 60 * 1000;
    public static int MSG_LOCATION_WHAT = 101;

    private final IBinder mBinder = new LocalBinder();
    private ServiceHandler mHandler = null;
    private boolean mServiceRunning;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public YHService getService() {
            return YHService.this;
        }
    }

    @Override
    public void onCreate() {
        LogUtils.d(TAG,"YHService--->onCreate()");
        super.onCreate();
        mHandler = new ServiceHandler(this);
        mServiceRunning = true;
        buildNotification();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LOCATION_RECEIVER_ACTION);
        registerReceiver(mLocationReceiver, intentFilter);
        mHandler.sendEmptyMessageDelayed(MSG_LOCATION_WHAT,LENGTH_OF_TIMING);
    }

    private void buildNotification() {
        NotificationManager notificationManager = (NotificationManager) RunningContext.sAppContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID_STRING, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        LogUtils.d(TAG,"YHService--->onDestroy()");
        super.onDestroy();
        mServiceRunning = false;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        unregisterReceiver(mLocationReceiver);
        RunningContext.stopLocation();
    }


    public boolean isRunning() {
        return mServiceRunning;
    }

    /**
     * 开启定位
     */
    private void startLocation() {
        // 开启定位
        LogUtils.i(TAG, "startLocation 开始定位");
        RunningContext.startLocation(null, false);
    }


    public class ServiceHandler extends Handler {

        private WeakReference<YHService> reference = null;

        public ServiceHandler(YHService yhService) {
            reference = new WeakReference<>(yhService);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (reference == null || reference.get() == null) {
                return;
            }
            YHService yhService = reference.get();
            if (!yhService.isRunning()) {
                LogUtils.e(TAG, "当前service非运行状态");
            }
            if (msg.what == MSG_LOCATION_WHAT) {
                // 定时任务发送定位信息
                sendEmptyMessageDelayed(MSG_LOCATION_WHAT, LENGTH_OF_TIMING);
                yhService.startLocation();
            }

        }
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
            LogUtils.i(TAG, "mLocationReceiver.mapLocation:" + mapLocation);
            if (mapLocation != null && !TextUtils.isEmpty(mapLocation.getAddress())) {
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.address = mapLocation.getAddress();
                locationInfo.city = mapLocation.getCity();
                locationInfo.latitude = coordinateToString(mapLocation.getLatitude());
                locationInfo.longitude = coordinateToString(mapLocation.getLongitude());
                StorageUtils.setCurrentLocation(locationInfo);
                uploadLocation(locationInfo);
            } else {
                // FIXME 测试
//                LocationInfo locationInfo = new LocationInfo();
//                locationInfo.longitude = "115.153443";
//                locationInfo.latitude = "38.835898";
//                locationInfo.address = "河北英虎农业机械制造有限公司";
//                StorageUtils.setCurrentLocation(locationInfo);
//                uploadLocation(locationInfo);
            }

        }
    };

    /**
     * 上传坐标位置
     */
    private void uploadLocation(LocationInfo locationInfo) {
        LoginUserInfo loginUserInfo = RunningContext.getsLoginUserInfo();
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
                LogUtils.i(TAG, "uploadLocation 坐标上传成功");
            }
        });

    }

    /**
     * 坐标转换并保留6位小数
     *
     * @param coordinate
     * @return
     */
    private String coordinateToString(double coordinate) {
        return String.format("%.6f", coordinate);
    }
}
