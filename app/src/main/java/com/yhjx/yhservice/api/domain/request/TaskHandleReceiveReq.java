package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

/**
 * 接单
 * @Author xiayundong
 * @Date 17:52 2020/10/10
 */
public class TaskHandleReceiveReq implements Serializable {

    /**
     * 任务单号
     */
    public String taskNo;
    /**
     * 维修人员用户编号
     */
    public String userNo;

    /**
     * 接单经纬度
     */
    public String longitude;

    /**
     * 接单经纬度
     */
    public String latitude;

    /**
     * 接单经纬度
     */
    public String userAddress;

}
