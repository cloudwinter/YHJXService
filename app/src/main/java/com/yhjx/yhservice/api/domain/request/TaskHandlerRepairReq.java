package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

/**
 * 报修
 * @Author xiayundong
 * @Date 17:52 2020/10/10
 */
public class TaskHandlerRepairReq implements Serializable {
    /**
     * 客户名称
     */
    public String customerName;

    /**
     * 客户手机号
     */
    public String customerTel;

    /**
     * 车架号
     */
    public String vehicleVin;

    /**
     * 故障描述
     */
    public String faultDesc;

    /**
     * 车辆名称
     */
    public String vehicleName;

    /**
     * 车辆地址
     */
    public String vehicleAddress;

    /**
     * 车辆经纬度
     */
    public String vehicleLongitude;

    /**
     * 车辆经纬度
     */
    public String vehicleLatitude;

    /**
     * 维修人员用户编号
     */
    public String userNo;
    /**
     * 维修人员用户姓名
     */
    public String userName;

    /**
     * 车辆规格型号
     */
    public String vehicleType;

    /**
     * 备注
     */
    public String remark;

}
