package com.yhjx.yhservice.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;


import com.yhjx.yhservice.util.ListUtil;

import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Observer;

/**
 * 自定义按钮组件
 * 
 * @author xiayundong
 * 
 */
public class YHButton extends Button implements Observer {

	private static final long PERFORM_DELAY_TIME = 0;
	private LinkedHashSet<Verifiable> mVerifiers = new LinkedHashSet<Verifiable>();
	/**
	 * 外部设置的点击监听器
	 */
	private OnClickListener mOuterClickListener = null;
	/**
	 * button是否处于休眠状态，用于防止按钮响应多次点击
	 */
	private boolean mIsSleep = false;

	/**
	 * 自动下一步
	 */
	private boolean mAutoPerformClick = false;


	private CountDownTimer mClickTimer = new CountDownTimer(500, 500) {

		@Override
		public void onTick(long millisUntilFinished) {

		}

		@Override
		public void onFinish() {
			mIsSleep = false;
		}
	};

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// 防止timer异常，不能使mIsSleep恢复初始值
		mIsSleep = false;
	};

	/**
	 * 内部点击监听器
	 */
	private OnClickListener mInternalClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 非冻结状态下，才响应点击事件
			if (!mIsSleep) {
				if (isVerify() && mOuterClickListener != null) {
					mOuterClickListener.onClick(v);
				}
			}
			// 每一次点击都冻结按钮，直至停下咸猪手，500毫秒后，才响应点击事件
			mIsSleep = true;
			mClickTimer.cancel();
			mClickTimer.start();
		}
	};

	@Override
	public void setOnClickListener(OnClickListener outClickListener) {
		super.setOnClickListener(mInternalClick);
		mOuterClickListener = outClickListener;
	}

	public YHButton(Context context) {
		super(context);
	}

	public YHButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public YHButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 添加观察者
	 * 
	 * @author wyqiuchunlong
	 * @param verifier
	 */
	public void observer(Verifiable verifier) {
		if (this.isEnabled()) {
			this.setEnabled(false);
		}

		// 校验：如果当前verifier属于view 但是 不可显示则不做监听
		if ((verifier instanceof View)
				&& ((View) verifier).getVisibility() != View.VISIBLE) {
			update(null, null);
			return;
		}
		if (verifier != null && !mVerifiers.contains(verifier)) {
			mVerifiers.add(verifier);
			verifier.addObserver(this);
		}

		update(null, null);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}

	/**
	 * 解除观察
	 * 
	 * @author wyqiuchunlong
	 * @param verifier
	 */
	public void removeObserver(Verifiable verifier) {

		if (verifier != null) {
			mVerifiers.remove(verifier);
			if (ListUtil.isEmpty(mVerifiers)) {
				mAutoPerformClick = false;
			}
			this.update(null, null);
		}
	}

	/**
	 * 清空观察者
	 */
	public void clearObserver() {
		if (!ListUtil.isEmpty(mVerifiers)) {
			mVerifiers.clear();
			mAutoPerformClick = false;
			this.update(null, null);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (mAutoPerformClick) {
			if (!ListUtil.isEmpty(mVerifiers)) {
				if (isVerify()) {
					this.postDelayed(new Runnable() {

						@Override
						public void run() {
							performClick();
						}
					}, PERFORM_DELAY_TIME);
				}
			}
		} else {
			for (Verifiable verifier : mVerifiers) {
				if (verifier.isBlank()) {
					YHButton.this.setEnabled(false);
					return;
				}
			}
			YHButton.this.setEnabled(true);
		}
	}

	/**
	 * 是否已通过验证
	 * 
	 * @return
	 */
	private boolean isVerify() {
		for (Verifiable verifier : mVerifiers) {
			if (!verifier.verify()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置自动执行下一步按钮
	 * 
	 * @param autoPerformClick
	 */
	public void setAutoPerformClick(boolean autoPerformClick) {
		mAutoPerformClick = autoPerformClick;
	}

	/**
	 * 获得是否自动执行下一步
	 * 
	 * @return mAutoPerformClick
	 */
	public boolean isAutoPerformClick() {
		return mAutoPerformClick;
	}

	public int getVerifiersSize() {
		if (ListUtil.isEmpty(mVerifiers)) {
			return 0;
		}
		return mVerifiers.size();
	}

}
