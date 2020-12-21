package com.yhjx.yhservice.util;

import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.model.LoginUserInfo;

import java.util.List;

public class YHUtils {

    public static LoginUserInfo copyByServiceUser(ServiceUser user) {
        if (user == null) {
            return null;
        }
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.id = user.id;
        loginUserInfo.userNo = user.userNo;
        loginUserInfo.userName = user.userName;
        loginUserInfo.userTel = user.userTel;
        loginUserInfo.userPassword = user.userPassword;
        loginUserInfo.stationId = user.stationId;
        loginUserInfo.stagnationStationId = user.stagnationStationId;
        loginUserInfo.stagnationStationId = user.stagnationStationId;
        loginUserInfo.stagnationStationName = user.stagnationStationName;
        loginUserInfo.stagnationStationAddress = user.stagnationStationAddress;
        loginUserInfo.stagnationStationLongitude = user.stagnationStationLongitude;
        loginUserInfo.stagnationStationLatitude = user.stagnationStationLatitude;
        loginUserInfo.userFlag = user.userFlag;
        loginUserInfo.userFlag = user.userFlag;
        return loginUserInfo;
    }

    public static String[] listConvertToArray(List<String> strList) {
        if (strList == null && strList.size() == 0) {
            return null;
        }
        String[] strArray = new String[strList.size()];
        for (int i = 0; i < strList.size(); i++) {
            strArray[i] = strList.get(i);
        }
        return strArray;
    }
}