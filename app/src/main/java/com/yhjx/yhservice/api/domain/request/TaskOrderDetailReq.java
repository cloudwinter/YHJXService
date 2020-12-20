package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

public class TaskOrderDetailReq implements Serializable {

    /**
     * 维修人员用户编号
     */
    public String userNo;

    /**
     * 任务单号
     */
    public String taskNo;
}
