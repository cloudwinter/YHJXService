package com.yhjx.yhservice.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {
    /**
     * 手机号正则
     */
    private static String PHONE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

    /**
     * 是否是正确的手机号格式
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(phone.trim())) {
            return false;
        }
        if (phone.length() != 11) {
            return false;
        }
        Pattern p = Pattern.compile(PHONE_REGEX);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
