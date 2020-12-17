package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

public class ServiceUserUpdateStagnationReq implements Serializable {

    /**
     * 用户id
     */
    public Integer id;

    /**
     * 维修人员用户编号
     */
    public String userNo;

    /**
     * 维修人员驻点服务站唯一id
     */
    public Integer stagnationStationId;

    /**
     * 驻点服务站名称
     */
    public String stagnationStationName;

}
