package com.yhjx.yhservice.model;

import java.io.Serializable;

/**
 * 站点信息
 */
public class StationModel implements Serializable {

    /**
     * 自增id
     */
    public Integer id;

    /**
     * 服务站名称
     */
    public String stationName;

    /**
     * 服务站位置中文名称
     */
    public String stationAddress;

    /**
     * 服务站坐标（经度）
     */
    public String stationLongitude;

    /**
     * 服务站坐标（纬度）
     */
    public String stationLatitude;

    /**
     * 服务站-省
     */
    public String stationProvince;

    /**
     * 服务站-市
     */
    public String stationCity;

    /**
     * 服务站-联系人
     */
    public String stationContacts;

    /**
     * 服务站-联系电话
     */
    public String stationTel;

    /**
     * 服务站类型（0公司，1特约服务站，2客户服务站）
     */
    public String stationType;

    /**
     * 服务站状态（0正常，1异常）
     */
    public String stationStatus;
}
