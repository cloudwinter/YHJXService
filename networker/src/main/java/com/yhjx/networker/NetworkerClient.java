package com.yhjx.networker;

import android.util.Log;

import com.yhjx.networker.calladater.SSCallAdapterFactory;
import com.yhjx.networker.coverter.SSCoverterfactory;
import com.yhjx.networker.http.Body;
import com.yhjx.networker.http.POST;
import com.yhjx.networker.retrofit.Retrofit;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by xiayundong on 2019/5/10.
 */

public class NetworkerClient {

//	/**
//	 * 网络框架需要具备的基本属性
//	 *
//	 * 支持http的各种协议
//	 *
//	 * 加快网络请求
//	 *
//	 * 减少TCP握手和分手几率
//	 *
//	 * 线程池和队列
//	 *
//	 * 多路复用
//	 *
//	 * 同步和异步
//	 *
//	 * 取消
//	 */
//
//	/**
//	 * OKHttp 3.0 调用测试
//	 *
//	 */
//	public static void okHttpClientExecuteTest() {
//		OkHttpClient httpClient = new OkHttpClient.Builder()
//				.addInterceptor(new Interceptor() {
//					@Override
//					public Response intercept(Interceptor.Chain chain)
//							throws IOException {
//						Request request = chain.request();
//						System.out.println(" request :" + request.toString());
//						return chain.proceed(request);
//					}
//				}).build();
//
//		Request request = new Request.Builder().url("http://www.baidu.com")
//				.build();
//
//		// 同步调用
//
//		try {
//			Response response = httpClient.newCall(request).execute();
//			if (response.isSuccessful()) {
//				System.out.println(" 请求成功 ");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// 异步调用
//		try {
//			httpClient.newCall(request).enqueue(new Callback() {
//				@Override
//				public void onFailure(Call call, IOException e) {
//
//				}
//
//				@Override
//				public void onResponse(Call call, Response response)
//						throws IOException {
//
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public static void retrofitExecuteTest() {
//		// Test
//		String baseUrl = "http://www.baidu.com";
//
//		Retrofit.Builder builder = generateBuilder(baseUrl);
//		builder.client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//			@Override
//			public Response intercept(Chain chain) throws IOException {
//				Request request = chain.request();
//				return chain.proceed(request);
//			}
//		}).build());
//
//		ApiService apiService = create(baseUrl, ApiService.class);
//
//		LoginRequest loginRequest = new LoginRequest();
//		com.yhjx.networker.retrofit.Call<LoginResult> call = apiService
//				.login(loginRequest);
//		call.enqueue(
//				new com.yhjx.networker.retrofit.Callback<LoginResult>() {
//					@Override
//					public void onResponse(
//							com.yhjx.networker.retrofit.Call<LoginResult> call,
//							com.yhjx.networker.retrofit.Response<LoginResult> response) {
//
//					}
//
//					@Override
//					public void onFailure(
//							com.yhjx.networker.retrofit.Call<LoginResult> call,
//							Throwable t) {
//
//					}
//				});
//
//	}

//	interface ApiService {
//
//		@POST("/login")
//		com.yhjx.networker.retrofit.Call<LoginResult> login(
//				@Body LoginRequest loginRequest);
//	}
//
//	static class LoginResult {
//
//	}
//
//	static class LoginRequest {
//
//	}

	private static Retrofit.Builder sBuilder;

	private static Retrofit.Builder generateBuilder(String baseUrl) {
		Retrofit.Builder builder = new Retrofit.Builder();
		builder.addCallAdapterFactory(new SSCallAdapterFactory())
				.addConverterFactory(new SSCoverterfactory())
				.baseUrl(baseUrl);
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
			@Override
			public void log(String message) {
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sysTime = sf.format(System.currentTimeMillis());//系统当前时间
				Log.e(System.currentTimeMillis()+"=====log======"+sysTime, message);
			}
		}).setLevel(HttpLoggingInterceptor.Level.BODY)).build();
		builder.client(client);
		return builder;
	}

	public static <T> T create(String baseUrl, Class<T> service) {
		if (sBuilder == null) {
			sBuilder = generateBuilder(baseUrl);
		}
		return sBuilder.build().create(service);
	}


}
