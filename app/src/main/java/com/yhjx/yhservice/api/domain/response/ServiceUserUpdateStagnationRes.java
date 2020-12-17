package com.yhjx.yhservice.api.domain.response;


import java.io.Serializable;

/**
 * @Author xiayundong
 * @Date 15:10 2020/12/17
 */
public class ServiceUserUpdateStagnationRes implements Serializable {

    /**
     * 用户id
     */
    public Integer id;

    /**
     * 维修人员用户编号
     */
    public String userNo;


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
}
