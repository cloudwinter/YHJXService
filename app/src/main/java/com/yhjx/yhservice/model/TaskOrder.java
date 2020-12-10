package com.yhjx.yhservice.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TaskOrder implements Serializable {
    /**
     * 自增id
     */
    public Integer id;

    /**
     * 任务单号（关联任务单号）
     */
    public String taskNo;

    /**
     * 客户名称（最近一次报修客户名称）
     */
    public String customerName;

    /**
     * 客户手机号（最近一次报修客户手机号）
     */
    public String customerTel;

    /**
     * 车架号
     */
    public String vehicleVin;

    /**
     * 车辆名称
     */
    public String vehicleName;

    /**
     * 车辆位置 会随时更新
     */
    public String vehicleAddress;

    /**
     * 车辆坐标（经度) 会随时更新
     */
    public String vehicleLongitude;

    /**
     * 车辆坐标（纬度) 会随时更新
     */
    public String vehicleLatitude;

    /**
     * 故障描述
     */
    public String faultDesc;

    /**
     * 任务单状态（0待指派，1已指派，2已接单，3已开工，4异常开工，5已完工，6异常完工，7任务结束，8电话联系解决，9取消）
     */
    public String taskStatus;

    /**
     * 接单维修人员用户编号
     */
    public String serviceUserNo;

    /**
     * 接单维修人员名称
     */
    public String serviceUserName;

    /**
     * 接单维修人员手机号
     */
    public String serviceUserTel;

    /**
     * 服务站唯一id
     */
    public Integer serviceStationId;

    /**
     * 服务站名称
     */
    public String serviceStationName;

    /**
     * 指派时间
     */
    public Date designateTime;

    /**
     * 接单时间
     */
    public Date receivedTime;

    /**
     * 接单坐标（经,纬）(longitude,latitude)
     */
    public String receivedCoordinate;

    /**
     * 去的里程单位KM保留2位小数
     */
    public BigDecimal goKm;

    /**
     * 开工时间
     */
    public Date startTime;

    /**
     * 开工坐标（经,纬）(longitude,latitude)
     */
    public String startCoordinate;

    /**
     * 开工图片路径
     */
    public String startImgPath;

    /**
     * 完工时间
     */
    public Date endTime;

    /**
     * 完工坐标（经,纬）(longitude,latitude)
     */
    public String endCoordinate;

    /**
     * 完工图片路径1
     */
    public String endImgPath;

    /**
     * 维修工时单位小时保留2位小数
     */
    public BigDecimal workHour;

    /**
     * 故障类别唯一id
     */
    public Integer faultCategoryId;

    /**
     * 故障类别名称
     */
    public String faultCategoryName;

    /**
     * 故障配件
     */
    public String faultAccessories;
    /**
     * 故障原因
     */
    public String faultCause;

    /**
     * 结束时间（结束或者终止）
     */
    public Date finishTime;

    /**
     * 结束坐标（经,纬）(longitude,latitude)
     */
    public String finishCoordinate;

    /**
     * 回去的里程单位KM保留2位小数
     */
    public BigDecimal returnKm;


}