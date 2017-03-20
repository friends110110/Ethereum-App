package com.hundsun.codecompete.persondetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.view.View;
import com.hundsun.codecompete.QR.GenerateQRActivity;
import com.hundsun.codecompete.R;
import com.hundsun.codecompete.base.BaseActivity;
import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.data.source.local.Repository;
import com.hundsun.codecompete.databinding.ActivityPersonDetailBinding;
import com.hundsun.codecompete.utils.ForwardUtils;
import com.hundsun.codecompete.utils.SweetDialogUtils;
import com.hundsun.codecompete.utils.ThreadPoolUtils;

public class PersonDetailActivity extends BaseActivity {

    private ActivityPersonDetailBinding mPeopleDetailActivityBinding;
    private PersonDetailViewModel personDetailViewModel;

    @Override
    protected void initView() {
        mPeopleDetailActivityBinding =DataBindingUtil.setContentView(this, R.layout.activity_person_detail);
        mPeopleDetailActivityBinding.loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetDialogUtils.showConfirmDialog(PersonDetailActivity.this, true, new SweetDialogUtils.ConfirmDialog() {
                    @Override
                    public void confirmCallback() {
                        personDetailViewModel.loginOut();
                        finish();
                    }
                });
            }
        });
        mPeopleDetailActivityBinding.imageQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra(ConstantValue.QR_GENERATE_CODE_FLAG, Repository.getInstance().getSession().getV3Str());
                ForwardUtils.forward(PersonDetailActivity.this,GenerateQRActivity.class,intent);
            }
        });

        personDetailViewModel=new PersonDetailViewModel();
        mPeopleDetailActivityBinding.setDetailViewModel(personDetailViewModel);
    }


    /**
     * 头部操作，子类可覆写
     */
    protected void displayHomeAsUpEnabled() {
        setSupportActionBar(mPeopleDetailActivityBinding.toolbar);
        mPeopleDetailActivityBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        personDetailViewModel.start();
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                personDetailViewModel.refreshData();
            }
        });
    }

}
