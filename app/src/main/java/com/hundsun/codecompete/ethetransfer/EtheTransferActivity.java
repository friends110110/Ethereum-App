package com.hundsun.codecompete.ethetransfer;

import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import com.hundsun.codecompete.R;
import com.hundsun.codecompete.base.BaseActivity;
import com.hundsun.codecompete.common.view.EnterPasswdDialogFragment;
import com.hundsun.codecompete.data.source.local.Repository;
import com.hundsun.codecompete.databinding.ActivityEtheTransferBinding;
import com.hundsun.codecompete.utils.ThreadPoolUtils;

public class EtheTransferActivity extends BaseActivity implements EnterPasswdDialogFragment.Validate{

    private ActivityEtheTransferBinding activityEtheTransferBinding;
    EtheTransferViewModel etheTransferViewModel;

    @Override
    protected void initView() {
        activityEtheTransferBinding=DataBindingUtil.setContentView(this, R.layout.activity_ethe_transfer);
        activityEtheTransferBinding.etheTransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterTransfer();
            }
        });

        etheTransferViewModel=new EtheTransferViewModel();
        activityEtheTransferBinding.setTransferViewModel(etheTransferViewModel);
    }


    @Override
    protected void onResume() {
        super.onResume();
        etheTransferViewModel.start();
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                etheTransferViewModel.refreshData();
            }
        });

    }

    private void enterTransfer() {
        String typeInetheCoin=activityEtheTransferBinding.etTypeInEtheCoin.getText().toString();
        if (TextUtils.isEmpty(typeInetheCoin)||!isValid(typeInetheCoin)){
            activityEtheTransferBinding.etTypeInEtheCoin.setError("请输入有效以太币");
            activityEtheTransferBinding.etTypeInEtheCoin.requestFocus();
            return;
        }
        EnterPasswdDialogFragment enterPasswdDialogFragment= (EnterPasswdDialogFragment) getFragmentManager().findFragmentByTag("dialog");
        if(enterPasswdDialogFragment==null){
            enterPasswdDialogFragment=new EnterPasswdDialogFragment();
        }
        enterPasswdDialogFragment.show(getFragmentManager(),"dialog");
    }

    private boolean isValid(String typeInetheCoin) {
        Double value;
        try{
            value=Double.valueOf(typeInetheCoin);
        }catch (NumberFormatException e){
            return false;
        }
        if (value>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean validatePasswd(String passwd) {
        return Repository.getInstance().getSession().getPassword().equals(passwd);
    }

    @Override
    public void nextStep() {
        final String fromAddress=activityEtheTransferBinding.tvFromAddress.getText().toString();
        final String toAddress=activityEtheTransferBinding.etToAddress.getText().toString();
        final String ethereumCoin=activityEtheTransferBinding.etTypeInEtheCoin.getText().toString();
//        String voteCoint=this.voteCoin;
        final String data=activityEtheTransferBinding.etData.getText().toString();
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    etheTransferViewModel.transferEthem(fromAddress,toAddress,ethereumCoin,data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(EtheTransferActivity.this.getWindow().getDecorView(), getResources().getString(R.string.send_transaction_tips), Snackbar.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(EtheTransferActivity.this.getWindow().getDecorView(), getResources().getString(R.string.fail_to_send_transaction_tips), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}
