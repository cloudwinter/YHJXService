package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

public class StationListReq implements Serializable {
    /**
     * 根据名称模糊查询
     */
    public String stationName;
    public int pageSize;
    public int pageNum;
}
