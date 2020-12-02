package com.yhjx.networker.callback;

/**
 * Created by xiayundong on 2019/5/21.
 */

public interface ResultNotifier<T> {

	/**
	 * 开始
	 */
	void notifyStart();

	/**
	 * 成功
	 *
	 * @param data
	 *            请求body部分的数据
	 */
	void notifySuccess(T data);

	/**
	 * 失败
	 *
	 * @param errCode
	 *            本地错误码
	 * @param errMsg
	 *            本地错误描述
	 */
	void notifyFailure(String errCode, String errMsg);

	/**
	 * 结束
	 */
	void notifyFinish();

	/**
	 * 取消
	 */
	void notifyCancel();

	/**
	 * handler处理后的UI线程回调
	 *
	 * @see NJResultNotifier#notifyStart()
	 */
	void onStart();

	/**
	 * handler处理后的UI线程回调
	 *
	 * @see NJResultNotifier#onProgress(long, long)
	 */
	void onProgress(long bytes, long contentLength);

	/**
	 * handler处理后的UI线程回调
	 *
	 * @see NJResultNotifier#onCancel()
	 */
	void onCancel();

	/**
	 * handler处理后的UI线程回调
	 *
	 * @see NJResultNotifier#onFinish()
	 */
	void onFinish();
}
