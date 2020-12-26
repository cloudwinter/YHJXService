package com.yhjx.yhservice.api;


import com.vector.update_app.HttpManager;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by xiayundong on 2020/12/26.
 */
public class UpdateHttpManager implements HttpManager {

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {

    }
}
