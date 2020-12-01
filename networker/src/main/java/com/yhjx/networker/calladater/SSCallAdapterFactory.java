package com.yhjx.networker.calladater;

import com.yhjx.networker.retrofit.CallAdapter;
import com.yhjx.networker.retrofit.Retrofit;
import com.yhjx.networker.retrofit.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * 
 * Created by xiayundong on 2019/5/21.
 */

public class SSCallAdapterFactory extends CallAdapter.Factory {

	@Override
	public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations,
			Retrofit retrofit) {
	    Type responseType = Utils.getCallResponseType(returnType);
        return new SSCallAdapter<>(responseType);
    }
}
