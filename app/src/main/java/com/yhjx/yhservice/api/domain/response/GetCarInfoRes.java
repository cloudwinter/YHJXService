package com.yhjx.yhservice.api.domain.response;


import java.io.Serializable;

/**
 * @Author xiayundong
 * @Date 20:47 2020/12/21
 */
public class GetCarInfoRes implements Serializable {

    /**
     * 车辆基本信息
     */
    public Vehicle vehicle;

    /**
     * 车辆状态信息
     */
    public VehicleState vehicleState;
}
