package com.yhjx.yhservice.api.domain.request;

import java.io.Serializable;

public class ServiceUserUpdateTelReq implements Serializable {

    /**
     * 用户id
     */
    public Integer id;

    /**
     * 维修人员用户编号
     */
    public String userNo;

    /**
     * 维修人员手机号
     */
    public String userTel;

    /**
     * 维修人员密码
     */
    public String userPassword;

}
