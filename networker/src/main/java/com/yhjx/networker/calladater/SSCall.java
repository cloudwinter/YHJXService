package com.yhjx.networker.calladater;

import android.util.Log;

import com.google.gson.Gson;
import com.yhjx.networker.callback.ResultNotifier;
import com.yhjx.networker.retrofit.Call;
import com.yhjx.networker.retrofit.Callback;
import com.yhjx.networker.retrofit.Response;

import java.io.IOException;

/**
 * Created by xiayundong on 2019/5/21.
 */

public class SSCall<T> {

    public static final String TAG = "SSCall";

    private Call<T> delegate;

    public SSCall(Call<T> delegate) {
        this.delegate = delegate;
    }

    public Response<T> execute() throws IOException {
        return delegate.execute();
    }

    public void enqueue(final ResultNotifier<T> notifier) {
        notifier.notifyStart();
        delegate.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
				try {
					Log.i(TAG, "onResponse: " + new Gson().toJson(response));
				} catch (Exception e) {
					Log.e(TAG, "onResponse: 结果解析异常：" + e.getMessage());
				}
                callbackResult(notifier, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (t != null) {
                    notifier.notifyFailure("-1", t.getMessage());
					Log.e(TAG, "onResponse: 结果返回异常：" + t.getMessage());
                }
                notifier.notifyFinish();
            }
        });
    }

    /**
     * 对调结果
     *
     * @param notifier 回调
     * @param response 网络请求的结果
     */
    private void callbackResult(ResultNotifier notifier, Response<T> response) {
        if (response == null) {
            // response为空，认为network错误
            notifier.notifyFailure("-1", "网络错误");
            notifier.notifyFinish();
            return;
        }
        int code = response.code();
        if (code >= 200 && code < 300) {
            // 成功
            notifier.notifySuccess(response.body());
            notifier.notifyFinish();
        } else {
            // 其它错误，统一取错误码为-8
            // TODO czhang 是否需要根据不同的错误类型给出提示
            notifier.notifyFailure("-8", "异常错误");
            notifier.notifyFinish();
        }
    }

}
