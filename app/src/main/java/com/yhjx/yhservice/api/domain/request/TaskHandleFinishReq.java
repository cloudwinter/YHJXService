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
    private String taskNo;

    /**
     * 维修人员用户编号
     */
    private String userNo;

    /**
     * 完工图片，多张图片逗号分割
     */
    private String endImgPath;
    /**
     * 完工经纬度
     */
    private String longitude;

    /**
     * 完工经纬度
     */
    private String latitude;
    /**
     * 地址
     */
    private String userAddress;
    /**
     * 故障类别id
     */
    private Integer faultCategoryId;
    /**
     * 故障类别名称
     */
    private String faultCategoryName;
    /**
     * 故障原因
     */
    private String faultCause;
    /**
     * 故障配件
     */
    private String faultAccessories;

}
