package com.yhjx.networker.calladater;


import com.yhjx.networker.retrofit.Call;
import com.yhjx.networker.retrofit.CallAdapter;

import java.lang.reflect.Type;

/**
 * Created by xiayundong on 2019/5/21.
 */

public class SSCallAdapter<T> implements CallAdapter<T,SSCall<T>> {

    private Type responseType;

    public SSCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public SSCall<T> adapt(Call<T> call) {
        return new SSCall<>(call);
    }
}
