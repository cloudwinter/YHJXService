package com.yhjx.yhservice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.adapter.FaultTypeListAdapter;
import com.yhjx.yhservice.api.domain.response.FaultCategory;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * 选择故障类型对话框
 */
public class SelectFaultTypeDialog extends Dialog {

    private Context mContext;

    private ListView mListView;

    private FaultTypeListAdapter mListAdapter;

    private List<FaultCategory> mDataList;

    private FaultCategory mSelectedItem = null;

    public SelectFaultTypeDialog(@NonNull Context context) {
        super(context, R.style.selectFaultTypeDialog);
        mContext = context;
        initView();
    }

    public SelectFaultTypeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    /**
     * 设置数据
     * @param dataList
     */
    public void setDataList(List<FaultCategory> dataList) {
        mDataList = dataList;
        mListAdapter.setData(mDataList);
    }

    public FaultCategory getSelected() {
        return mSelectedItem;
    }


    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_fault_type, null);
        mListView = view.findViewById(R.id.list);
        mListAdapter = new FaultTypeListAdapter(mContext);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(mOnItemClick);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.selectFaultTypeDialogAnimation);
    }


    private AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSelectedItem = mDataList.get(position);
            dismiss();
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        // 清空选中
        mSelectedItem = null;
    }

    public interface OnDialogItemClickListener {

    }
}
