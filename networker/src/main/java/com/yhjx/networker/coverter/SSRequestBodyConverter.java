package com.yhjx.networker.coverter;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.yhjx.networker.retrofit.Converter;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.ByteString;

/**
 * 请求正文的解析
 *
 * Created by czhang on 16/8/3.
 */
final class SSRequestBodyConverter<T> implements Converter<T, RequestBody> {
	private static final MediaType MEDIA_TYPE = MediaType
			.parse("application/json; charset=UTF-8");
	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private final Gson gson;
	private final TypeAdapter<T> adapter;

	SSRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
		this.gson = gson;
		this.adapter = adapter;
	}

	@Override
	public RequestBody convert(T value) throws IOException {
		// 如果实现了encryptable接口，则对参数进行加密
		// 创建对应的body并打印日志
//		Buffer buffer = new Buffer();
//		Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
//		JsonWriter jsonWriter = new JsonWriter(writer);
//		// null值不转化
//		jsonWriter.setSerializeNulls(false);
//		// JsonWriter jsonWriter = gson.newJsonWriter(writer);
//		adapter.write(jsonWriter, value);
//		jsonWriter.close();

		String jsonString = gson.toJson(value);
		Log.e("SSRequestBodyConverter","请求参数为：" + jsonString);
//		ByteString byteString = buffer.readByteString();
//		byte[] requestBytes = jsonString.getBytes();
		return RequestBody.create(MEDIA_TYPE, jsonString);
	}
}
