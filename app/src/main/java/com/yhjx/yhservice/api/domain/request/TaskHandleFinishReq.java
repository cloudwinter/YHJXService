package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

/**
 * 完工
 * @Author xiayundong
 * @Date 17:52 2020/10/10
 */
public class TaskHandleFinishReq implements Serializable {


    /**
     * 任务单号
     */
    public String taskNo;

    /**
     * 维修人员用户编号
     */
    public String userNo;

    /**
     * 完工图片，多张图片逗号分割
     */
    public String endImgPath;
    /**
     * 完工经纬度
     */
    public String longitude;

    /**
     * 完工经纬度
     */
    public String latitude;
    /**
     * 地址
     */
    public String userAddress;
    /**
     * 故障类别id
     */
    public Integer faultCategoryId;
    /**
     * 故障类别名称
     */
    public String faultCategoryName;
    /**
     * 故障原因
     */
    public String faultCause;
    /**
     * 故障配件
     */
    public String faultAccessories;

}
