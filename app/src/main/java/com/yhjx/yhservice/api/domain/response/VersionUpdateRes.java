package com.yhjx.yhservice.api.domain.response;

import java.io.Serializable;

/**
 * 更新信息
 * Created by xiayundong on 2020/12/26.
 */
public class VersionUpdateRes implements Serializable {

    /**
     * 新版本名称
     */
    public String latestVersion;

    /**
     * 新版本code
     */
    public Integer latestVersionCode;

    /**
     * 新版本地址
     */
    public String latestVersionUrl;

    /**
     * 版本描述
     */
    public String versionDesc;

    /**
     * 是否强制更新
     */
    public boolean constraint;
}
