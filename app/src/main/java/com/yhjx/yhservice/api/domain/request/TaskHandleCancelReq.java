package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

/**
 * 取消
 * @Author xiayundong
 * @Date 17:52 2020/10/10
 */
public class TaskHandleCancelReq implements Serializable {


    /**
     * 任务单号
     */
    public String taskNo;

    /**
     * 维修人员用户编号
     */
    public String userNo;
    /**
     * 维修人员用户姓名
     */
    public String userName;

    /**
     * 经纬度
     */
    public String longitude;

    /**
     * 经纬度
     */
    public String latitude;
    /**
     * 地址
     */
    public String userAddress;

    /**
     * 终止原因
     */
    public String stopReason;

}
