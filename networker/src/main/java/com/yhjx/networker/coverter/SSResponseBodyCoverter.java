package com.yhjx.networker.coverter;

import com.google.gson.TypeAdapter;
import com.yhjx.networker.retrofit.Converter;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by xiayundong on 2019/5/21.
 */

public class SSResponseBodyCoverter<T> implements Converter<ResponseBody, T> {

	private TypeAdapter<T> typeAdapter;

	public SSResponseBodyCoverter(TypeAdapter<T> typeAdapter) {
		this.typeAdapter = typeAdapter;
	}

	@Override
	public T convert(ResponseBody value) throws IOException {
		String rawBody = new String(value.bytes());
		T response = typeAdapter.fromJson(rawBody);
		return response;
	}
}
