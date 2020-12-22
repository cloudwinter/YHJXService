package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

/**
 * 开工
 * @Author xiayundong
 * @Date 17:52 2020/10/10
 */
public class TaskHandleStartReq implements Serializable {

    /**
     * 任务单号
     */
    public String taskNo;

    /**
     * 维修人员用户编号
     */
    public String userNo;

    /**
     * 开工经纬度
     */
    public String longitude;

    /**
     * 开工经纬度
     */
    public String latitude;
    /**
     * 开工图片，多张图片逗号分割
     */
    public String startImgPath;

}
