package com.yhjx.yhservice.api.domain.request;


import java.io.Serializable;

/**
 * @Author xiayundong
 * @Date 17:52 2020/10/10
 */
public class ServiceUserRegisterReq implements Serializable {

    /**
     * 用户名称
     */
    public String userName;

    /**
     * 用户密码
     */
    public String userPassword;

    /**
     * 用户手机号
     */
    public String userTel;

    /**
     * 归属服务站id
     */
    public Integer stationId;

    /**
     * 驻点服务站id
     */
    public Integer stagnationStationId;

}
