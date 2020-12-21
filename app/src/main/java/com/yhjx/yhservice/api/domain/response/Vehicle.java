package com.yhjx.yhservice.api.domain.response;

import java.io.Serializable;
import java.util.Date;

/**
 * description:
 * author: ncs
 * create on: 2020/10/23
 */

/**
 * 农机车辆表
 */
public class Vehicle implements Serializable {
    /**
     * 车辆ID
     */
    public Integer vehicleId;

    /**
     * 车架号
     */
    public String vehicleVin;

    /**
     * 车辆4G卡mac地址
     */
    public String vehicleMac;

    /**
     * 发动机号
     */
    public String vehicleEngineNum;

    /**
     * 车辆规格型号
     */
    public String vehicleType;

    /**
     * 车辆名称
     */
    public String vehicleName;

    /**
     * 销售时间
     */
    public Date saleTime;

    /**
     * 车辆保修状态（0正常在保 1过保）
     */
    public String serviceStatus;

    /**
     * 经销商
     */
    public String dealer;

    /**
     * 经销商电话
     */
    public String dealerTel;

    /**
     * 联系人
     */
    public String dealerContacts;

    /**
     * 经销商地址
     */
    public String dealerAddress;

    public String vehicleAddress;

    public String vehicleLongitude;

    public String vehicleLatitude;

    public Date coordinateUpdateTime;

    /* 保修状态
     * (1正常状态，2报修状态,3维修状态)
     */
    public String vehicleIssueStatus;
    /**
     * 车辆状态（0停止 1工作 2故障）
     */
    public String vehicleStatus;
}