package com.yhjx.yhservice.model;

import java.io.Serializable;

public class LoginUserInfo implements Serializable {

    /**
     * 自增id
     */
    public Integer id;

    /**
     * 维修人员用户编号
     */
    public String userNo;

    /**
     * 维修人员名称
     */
    public String userName;

    /**
     * 维修人员手机号
     */
    public String userTel;

    /**
     * 维修人员密码
     */
    public String userPassword;

    /**
     * 维修人员所属服务站唯一id
     */
    public Integer stationId;

    /**
     * 服务站名称
     */
    public String stationName;

    /**
     * 维修人员驻点服务站唯一id
     */
    public Integer stagnationStationId;

    /**
     * 驻点服务站名称
     */
    public String stagnationStationName;

    /**
     * 驻点服务站位置
     */
    public String stagnationStationAddress;

    /**
     * 驻点服务站坐标（经度）
     */
    public String stagnationStationLongitude;

    /**
     * 驻点服务站坐标（纬度）
     */
    public String stagnationStationLatitude;

    /**
     * 维修人员状态（0待审核，1正常，2离职 3审核不通过）
     */
    public String userFlag;

}
