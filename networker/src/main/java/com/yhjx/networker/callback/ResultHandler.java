package com.yhjx.networker.callback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by xiayundong on 2019/5/22.
 */

public abstract class ResultHandler<T> implements ResultNotifier<BaseResult<T>> {

	public static final String TAG = "ResultHandler";

    protected abstract void onSuccess(T date);

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
            if (msg.what == 200) {
				BaseResult<T> result = (BaseResult<T>) msg.obj;
				if (result == null) {
					onFailed("-8","返回结果异常");
				}
				if (result.code.equals("200")) {
					onSuccess(result.data);
				} else {
					onFailed(result.code,result.msg);
				}
            }
        }
	}

	@Override
	public void notifyStart() {

	}

    @Override
    public void notifySuccess(BaseResult<T> data) {
        if (data != null) {
            Message message = Message.obtain();
            message.what = 200;
            message.obj = data;
            handler.sendMessage(message);
        }
    }


    @Override
	public void notifyFailure(String errCode, String errMsg) {
		onFailed(errCode, errMsg);
	}

	@Override
	public void notifyFinish() {

	}

	@Override
	public void notifyCancel() {

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
