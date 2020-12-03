package com.yhjx.yhservice.base;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yhjx.yhservice.util.ListUtil;

/**
 * @author xiayundong
 */
public class BaseListAdapter<T> extends BaseAdapter {
	public Context mContext;
	/**
	 * 数据mListData
	 */
	public List<T> mListData = new ArrayList<T>();

	public BaseListAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if (mListData == null || mListData.size() == 0) {
			return 0;
		}
		return mListData.size();
	}

	@Override
	public T getItem(int position) {
		if (position < 0 || position >= mListData.size()) {
			return null;
		}
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	public void setData(List<T> data) {
		if (mListData == null) {
			mListData = new ArrayList<T>();
		} else {
			mListData.clear();
		}
		if(ListUtil.isNotEmpty(data)){
			mListData.addAll(data);
		}
		this.notifyDataSetChanged();
	}

	/**
	 * add 数据
	 * 
	 * @param data
	 */
	public void addData(List<T> data) {
		if (mListData == null)
			mListData = new ArrayList<T>();
		this.mListData.addAll(data);
		this.notifyDataSetChanged();
	}

	public boolean isEmpty() {
		if (mListData != null && mListData.size() > 0) {
			return false;
		}
		return true;
	}

	public String getString(int stringId) {
		return mContext.getResources().getString(stringId);
	}

	public String getString(int stringId, String text) {
		return mContext.getResources().getString(stringId, text);
	}

	public int getColor(int colorId) {
		return mContext.getResources().getColor(colorId);
	}

	/**
	 * 获取view实例
	 * 
	 * @param view父view
	 * @param id子view
	 *            id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T extends View> T getChildView(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}
