package com.yhjx.yhservice.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yhjx.yhservice.model.LocationInfo;
import com.yhjx.yhservice.model.LoginUserInfo;

public class StorageUtils {

    // 登录缓存有效周期1天
    public static long LOGIN_KEEP_INTERVAL_VALID_TIME = 23 * 60 * 60 * 1000;

    static class KEY {
        public static String LOGIN_USER_KEY = "login_user";
        public static String LOGIN_USER_KEEP_TIME_KEY = "login_user_keep_time";
        public static String LOCATION_KEY = "location";
    }

    /**
     * 登录成功之后保存用户信息
     */
    public static void saveLoginInfo(LoginUserInfo loginUserInfo) {
        String val = null;
        if (loginUserInfo != null) {
            val = new Gson().toJson(loginUserInfo);
        }
        if (!TextUtils.isEmpty(val)) {
            setLoginUserExpire();
            PreferenceUtil.commitString(KEY.LOGIN_USER_KEY,val);
        }
    }

    /**
     * 获取登录信息
     * @return
     */
    public static LoginUserInfo getLoginInfo() {
        String val = PreferenceUtil.getString(KEY.LOGIN_USER_KEY, null);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return new Gson().fromJson(val, LoginUserInfo.class);
    }


    /**
     * 判断登录缓存有效期是否超期
     * @return
     */
    public static boolean isLoginUserExpire() {
        long preKeepTime = PreferenceUtil.getLong(KEY.LOGIN_USER_KEEP_TIME_KEY, System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        if (currentTime - preKeepTime > LOGIN_KEEP_INTERVAL_VALID_TIME) {
            return false;
        }
        return true;
    }

    /**
     * 设置新的缓存有效期
     * @return
     */
    public static void setLoginUserExpire() {
        PreferenceUtil.commitLong(KEY.LOGIN_USER_KEEP_TIME_KEY,System.currentTimeMillis());
    }


    public static void setCurrentLocation(LocationInfo location) {
        String val = null;
        if (location != null) {
            val = new Gson().toJson(location);
        }
        if (!TextUtils.isEmpty(val)) {
            PreferenceUtil.commitString(KEY.LOCATION_KEY, val);
        }
    }


    public static LocationInfo getCurrentLocation() {
        String val = PreferenceUtil.getString(KEY.LOCATION_KEY, null);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return new Gson().fromJson(val, LocationInfo.class);
    }


    public static void clearLogin() {
        PreferenceUtil.commitString(KEY.LOGIN_USER_KEY,null);
    }
}
