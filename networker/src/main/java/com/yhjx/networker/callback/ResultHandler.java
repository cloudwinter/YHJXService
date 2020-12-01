package com.yhjx.networker.callback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by xiayundong on 2019/5/22.
 */

public abstract class ResultHandler<T> implements ResultNotifier<BaseResult<T>> {

    protected abstract void onSuccess(T date);


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
                T data = (T) msg.obj;
                onSuccess(data);
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
            message.obj = data.resultData;
            handler.sendMessage(message);
        }
    }


    @Override
	public void notifyFailure(int errCode, String errMsg) {

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
