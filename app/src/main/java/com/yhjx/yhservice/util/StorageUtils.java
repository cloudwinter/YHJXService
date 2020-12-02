package com.yhjx.yhservice.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yhjx.yhservice.model.LoginUserInfo;

public class StorageUtils {

    static class KEY {
        public static String LOGIN_USER_KEY = "login_user";
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
}
