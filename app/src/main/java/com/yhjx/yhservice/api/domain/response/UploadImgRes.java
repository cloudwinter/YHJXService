package com.yhjx.yhservice.api.domain.response;

import java.io.Serializable;

/**
 * 图片上传返回结果
 */
public class UploadImgRes implements Serializable {

    public String fileName;

    public String url;


    @Override
    public String toString() {
        return "UploadImgRes{" +
                "fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
