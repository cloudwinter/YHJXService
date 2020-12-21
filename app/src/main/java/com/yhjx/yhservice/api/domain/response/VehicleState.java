package com.yhjx.yhservice.api.domain.response;


import java.io.Serializable;
import java.util.Date;

public class VehicleState implements Serializable {
    public Long vehicleStateId;

    public String vehicleVin;

    public String vehicleAddress;

    public String vehicleLongitude;

    public String vehicleLatitude;

    public Date coordinateUpdateTime;

    public String vehicleName;
    /**
     * 保修状态
     * (1正常状态，2报修状态,3维修状态)
     */
    public String vehicleIssueStatus;
    /**
     * 车辆状态（0停止 1工作 2故障）
     */
    public String vehicleStatus;

    public String vehicleInstrument;

    public String delFlag;

    public String createBy;

    public Date createTime;

    public String updateBy;

    public Date updateTime;

    public String remark;

}