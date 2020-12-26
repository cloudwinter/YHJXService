package com.yhjx.yhservice;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.amap.api.location.AMapLocationClient;
import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.util.StorageUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import androidx.annotation.RequiresApi;

public class RunningContext {

    public static boolean mock = false;
    public static boolean debug = false;


    /**
     * FIXME 更新接口地址
     */
//    public static String BASEURL = "http://47.116.73.239:3000/mock/27/";
    public static String BASEURL = "http://192.168.31.212:8080/";


    public static final int PERMISSION_REQUEST_CODE = 99;

    /**
     * 登录用户信息
     */
    private static LoginUserInfo sLoginUserInfo = null;

    /**
     * 全局线程池
     */
    private static ThreadPoolExecutor sThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    /**
     * 登录用户锁
     */
    private static byte[] sLoginInfoLock = new byte[0];

    /**
     * 全局静态context
     */
    public static Context sAppContext = null;

    /**
     * 全局locationClient
     */
    public static AMapLocationClient sAMapLocationClient;



    public static void init(Application app) {
        sAppContext = app.getApplicationContext();
        // 初始化地图client
        sAMapLocationClient = new AMapLocationClient(sAppContext);
        // 初始化用户信息
        getsLoginUserInfo();
    }


    /**
     * 判断是否登录
     * @return
     */
    public static boolean isLogin() {
        synchronized (sLoginInfoLock) {
            if (sLoginUserInfo != null && "1".equals(sLoginUserInfo.userFlag)){
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 获取用户信息
     * @return
     */
    public static LoginUserInfo getsLoginUserInfo() {
        synchronized (sLoginInfoLock) {
            if (sLoginUserInfo == null) {
                sLoginUserInfo = StorageUtils.getLoginInfo();
            }
            return sLoginUserInfo;
        }
    }

    /**
     * 登录成功后设置用户信息
     * @param loginUserInfo
     */
    public static void setLoginUserInfo(LoginUserInfo loginUserInfo) {
        synchronized (sLoginInfoLock) {
            StorageUtils.saveLoginInfo(loginUserInfo);
            sLoginUserInfo = loginUserInfo;
        }
    }

    /**
     * 全局线程池
     *
     * @return
     */
    public static ThreadPoolExecutor threadPool() {
        return sThreadPool;
    }



    /**
     * 校验网络连接
     * @return
     */
    public static boolean checkNetWork() {
        return isNetworkAvailable(sAppContext);
    }


    /**
     * 检查网络权限
     * @param context
     * @return
     */
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接　　　　　　
            // 则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 检查网络权限
     * @return
     */
    public static boolean checkNetworkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return sAppContext.checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 检查定位权限
     * @return
     */
    public static boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return sAppContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    /**
     * 获取版本号Name
     */
    public static String getVersionName() {
        try {
            PackageManager manager = sAppContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(sAppContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 获取版本号Code
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static long getVersionCode() {
        try {
            PackageManager manager = sAppContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(sAppContext.getPackageName(), 0);
            long version = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                version = info.getLongVersionCode();
            } else {
                version = info.versionCode;
            }
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
