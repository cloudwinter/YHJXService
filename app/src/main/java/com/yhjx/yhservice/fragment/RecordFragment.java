package com.yhjx.yhservice.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * 维修记录
 */
public class RecordFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
