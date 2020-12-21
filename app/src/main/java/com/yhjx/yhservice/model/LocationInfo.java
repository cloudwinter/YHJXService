package com.yhjx.yhservice.model;

import java.io.Serializable;

public class LocationInfo implements Serializable {

    /**
     * 服务站位置中文名称
     */
    public String address;

    /**
     * 服务站坐标（经度）
     */
    public String longitude;

    /**
     * 服务站坐标（纬度）
     */
    public String latitude;

    /**
     * 城市
     */
    public String city;
}
