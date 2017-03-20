package com.hundsun.codecompete.persondetail;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.view.View;
import com.hundsun.codecompete.R;
import com.hundsun.codecompete.base.BaseActivity;
import com.hundsun.codecompete.databinding.ActivityMineDetailBinding;

public class MineDetailActivity extends BaseActivity {

    private ActivityMineDetailBinding mMineDetailActivityBinding;
    private MineDetailViewModel mineDetailViewModel;

    @Override
    protected void initView() {
        mMineDetailActivityBinding =DataBindingUtil.setContentView(this, R.layout.activity_mine_detail);
        mineDetailViewModel =new MineDetailViewModel();
        mMineDetailActivityBinding.setDetailViewModel(mineDetailViewModel);
    }

    /**
     * 头部操作，子类可覆写
     */
    protected void displayHomeAsUpEnabled() {
        setSupportActionBar(mMineDetailActivityBinding.toolbar);
        mMineDetailActivityBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mineDetailViewModel.start();
//        ThreadPoolUtils.execute(new Runnable() {
//            @Override
//            public void run() {
//                mineDetailViewModel.refreshData();
//            }
//        });
    }

}
