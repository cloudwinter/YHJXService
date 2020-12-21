package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

/**
 * description:更新经纬度
 * author: ncs
 * create on: 2020/11/24
 */
public class UpdateLocationReq implements Serializable {
    /**
     * 维修人员用户编号
     */
    public String userNo;
    /**
     * 经纬度
     */
    public String longitude;

    /**
     * 经纬度
     */
    public String latitude;

    /**
     * 经纬度
     */
    public String userAddress;

}
