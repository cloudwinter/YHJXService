package com.yhjx.networker.callback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiayundong on 2019/5/22.
 */

public abstract class ResultHandler<T> implements ResultNotifier<BaseResult<T>> {

	public static final String TAG = "ResultHandler";

	private static int MSG_WHAT_SUC = 200;
	private static int MSG_WHAT_FAILED = 201;
	private static int MSG_WHAT_FINISH = 202;
	private static String FAILED_ERROR_CODE_KEY = "errCode";
	private static String FAILED_ERROR_MSG_KEY = "errMsg";

	// 主线程回调
    protected abstract void onSuccess(T data);

    // 主线程回调
	protected void onFailed(String errCode, String errMsg) {
		Log.d(TAG,"ResultHandler.onFailed 返回结果：errCode = " + errCode + " | errMsg = " + errMsg);
	}


	private MainHandler handler;

	public ResultHandler() {
	    // 设置为主线程
		handler = new MainHandler(Looper.getMainLooper());
	}

	private class MainHandler extends Handler {

		public MainHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
            if (msg.what == MSG_WHAT_SUC) {
				BaseResult<T> result = (BaseResult<T>) msg.obj;
				if (result == null) {
					onFailed("-8","返回结果异常");
				}
				if (result.code.equals("200")) {
					onSuccess(result.data);
				} else {
					onFailed(result.code,result.msg);
				}
            } else if (msg.what == MSG_WHAT_FAILED){
				Map<String, String> errorMap = (Map<String, String>) msg.obj;
				if (errorMap == null) {
					onFailed("-8","返回结果异常");
				} else {
					onFailed(errorMap.get(FAILED_ERROR_CODE_KEY),errorMap.get(FAILED_ERROR_MSG_KEY));
				}
			} else if (msg.what == MSG_WHAT_FINISH) {
            	onFinish();
			}
        }
	}

	@Override
	public void notifyStart() {
		onStart();
	}

    @Override
    public void notifySuccess(BaseResult<T> data) {
        if (data != null) {
            Message message = Message.obtain();
            message.what = MSG_WHAT_SUC;
            message.obj = data;
            handler.sendMessage(message);
        }
    }


    @Override
	public void notifyFailure(String errCode, String errMsg) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(FAILED_ERROR_CODE_KEY, errCode);
		errorMap.put(FAILED_ERROR_MSG_KEY, errMsg);
		Message message = Message.obtain();
		message.what = MSG_WHAT_FAILED;
		message.obj = errorMap;
		handler.sendMessage(message);
	}

	@Override
	public void notifyFinish() {
		Message message = Message.obtain();
		message.what = MSG_WHAT_FINISH;
		handler.sendMessage(message);
	}

	@Override
	public void notifyCancel() {
		onCancel();
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onProgress(long bytes, long contentLength) {

	}

	@Override
	public void onCancel() {

	}

	@Override
	public void onFinish() {

	}

}
