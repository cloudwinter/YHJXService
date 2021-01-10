package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

public class TaskOrderReq implements Serializable {

    /**
     * 维修人员用户编号
     */
    public String userNo;
    public int pageSize;
    public int pageNum;
}
