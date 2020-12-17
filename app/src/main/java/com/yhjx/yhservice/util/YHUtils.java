package com.yhjx.yhservice.util;

import com.yhjx.yhservice.api.domain.response.ServiceUser;
import com.yhjx.yhservice.model.LoginUserInfo;

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
}
