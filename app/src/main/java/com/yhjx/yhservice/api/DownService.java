package com.yhjx.yhservice.api;

import com.yhjx.networker.http.GET;
import com.yhjx.networker.http.Streaming;
import com.yhjx.networker.http.Url;
import com.yhjx.networker.retrofit.Call;

import okhttp3.ResponseBody;

/**
 * Created by xiayundong on 2020/12/27.
 */
public interface DownService {


    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
