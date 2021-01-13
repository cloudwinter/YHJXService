package com.yhjx.yhservice.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhjx.yhservice.R;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.adapter.TabPagerAdapter;
import com.yhjx.yhservice.base.BaseActivity;
import com.yhjx.yhservice.base.BaseFragment;
import com.yhjx.yhservice.fragment.RecordFragment;
import com.yhjx.yhservice.fragment.TaskOrderFragment;
import com.yhjx.yhservice.fragment.UserInfoFragment;
import com.yhjx.yhservice.view.NoScrollViewPager;
import com.yhjx.yhservice.view.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, TranslucentActionBar.ActionBarClickListener {

    public static final String TAG = "HomeActivity";

    public static int ADD_REQUEST_CODE = 3;

    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;

    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;

    @BindView(R.id.tab_task)
    LinearLayout tabTask;
    @BindView(R.id.tab_task_img)
    ImageView tabTaskImg;
    @BindView(R.id.tab_task_text)
    TextView tabTaskText;
    @BindView(R.id.tab_record)
    LinearLayout tabRecord;
    @BindView(R.id.tab_record_img)
    ImageView tabRecordImg;
    @BindView(R.id.tab_record_text)
    TextView tabRecordText;
    @BindView(R.id.tab_user)
    LinearLayout tabUser;
    @BindView(R.id.tab_user_img)
    ImageView tabUserImg;
    @BindView(R.id.tab_user_text)
    TextView tabUserText;


    List<BaseFragment> fragments;
    TabPagerAdapter tabPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        actionBar.setData("任务单", 0, null, R.mipmap.ic_add_task, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());
        initView();
    }



    private void initView() {
        tabTask.setOnClickListener(this);
        tabRecord.setOnClickListener(this);
        tabUser.setOnClickListener(this);

        fragments = new ArrayList<>();
        fragments.add(new TaskOrderFragment());
        fragments.add(new RecordFragment());
        fragments.add(new UserInfoFragment());

        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(tabPagerAdapter);
        viewPager.setScroll(true);
        viewPager.setOffscreenPageLimit(3);

        setCurrentTab(1);
    }

    /**
     * 1：task
     * 2：record
     * 3：user
     */
    private void setCurrentTab(int tab) {
        viewPager.setCurrentItem(tab-1);
        switch (tab) {
            case 1:
                RunningContext.startLocation(HomeActivity.this,true);
                tabTaskImg.setSelected(true);
                tabTaskText.setSelected(true);
                tabRecordImg.setSelected(false);
                tabRecordText.setSelected(false);
                tabUserImg.setSelected(false);
                tabUserText.setSelected(false);

                actionBar.setRightVisibility(true);
                actionBar.setTitle("任务单");
                break;
            case 2:
                RunningContext.startLocation(HomeActivity.this,true);
                tabTaskImg.setSelected(false);
                tabTaskText.setSelected(false);
                tabRecordImg.setSelected(true);
                tabRecordText.setSelected(true);
                tabUserImg.setSelected(false);
                tabUserText.setSelected(false);

                actionBar.setRightVisibility(false);
                actionBar.setTitle("维修记录");
                break;
            case 3:
                RunningContext.startLocation(HomeActivity.this,true);
                tabTaskImg.setSelected(false);
                tabTaskText.setSelected(false);
                tabRecordImg.setSelected(false);
                tabRecordText.setSelected(false);
                tabUserImg.setSelected(true);
                tabUserText.setSelected(true);

                actionBar.setRightVisibility(false);
                actionBar.setTitle("个人信息");
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_task:
                setCurrentTab(1);
                break;
            case R.id.tab_record:
                setCurrentTab(2);
                break;
            case R.id.tab_user:
                setCurrentTab(3);
                break;
        }
    }

    @Override
    public void onLeftClick() {
        // do nothings
    }

    @Override
    public void onRightClick() {
        Intent intent = new Intent(HomeActivity.this, AddTaskActivity.class);
        startActivityForResult(intent,ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragments != null) {
            for (BaseFragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
