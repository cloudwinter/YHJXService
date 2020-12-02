package com.yhjx.yhservice;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yhjx.yhservice.model.LoginUserInfo;
import com.yhjx.yhservice.util.StorageUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class RunningContext {

    /**
     *
     */
    public static String BASEURL = "";

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


    public static void init(Application app) {
        sAppContext = app.getApplicationContext();
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
     * 校验网络连接
     * @return
     */
    public static boolean checkNetWork() {
        return isNetworkAvailable(sAppContext);
    }


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

}
