package com.yhjx.yhservice.view;

import java.util.Observer;

/**
 * 可校验接口
 * 
 * @author xingtongju
 * 
 */
public interface Verifiable {

	/**
	 * 校验
	 * 
	 * @return
	 */
	boolean verify();

	/**
	 * 是否为空白
	 * 
	 * @return
	 */
	boolean isBlank();

	/**
	 * 添加verify接口的观察者
	 * 
	 * @param obj
	 */
	void addObserver(Observer obj);
}
