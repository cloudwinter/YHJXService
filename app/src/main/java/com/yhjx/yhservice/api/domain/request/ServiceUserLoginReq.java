package com.yhjx.yhservice.api.domain.request;


import java.io.Serializable;

/**
 * description:登录入参
 * author: ncs
 * create on: 2020/11/24
 */
public class ServiceUserLoginReq implements Serializable {

    /**
     * 维修人员手机号
     */
    public String userTel;

    /**
     * 维修人员密码
     */
    public String userPassword;


}
