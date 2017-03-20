package com.hundsun.codecompete.ethetransfer;

import android.databinding.BaseObservable;
import com.hundsun.codecompete.base.BaseViewModel;
import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.data.Session;
import com.hundsun.codecompete.data.source.local.Repository;
import com.hundsun.codecompete.utils.RefreshDataUtils;
import org.ethereum.service.BusinessException;
import org.ethereum.service.JsonRPC;
import org.ethereum.wallet.Wallet;

import java.io.IOException;
import java.security.SignatureException;

/**
 * Created by Administrator on 2016/7/31.
 */
public class EtheTransferViewModel extends BaseObservable implements BaseViewModel{
    private Session session;
    /**
     * 交易携带数据
     */
    private String data;
    /**
     * 用户输入转移以太币
     */
    private String typeInEtheCoin;

    private String toAddress;

    public EtheTransferViewModel(){
    }

    public String getToAddress() {
        return toAddress;
    }
    public String getTypeInEtheCoin() {
        return typeInEtheCoin;
    }
    public Session getSession(){
        return session;
    }

    public String getData() {
        return data;
    }

    public void transferEthem(String fromAddress, String toAddress, String ethereumCoin, String data) throws BusinessException, SignatureException, IOException {
        System.out.println(fromAddress);
        Wallet wallet= Repository.getInstance().getSession().getWallet();
        JsonRPC.eth_sendTransaction(wallet,toAddress, ethereumCoin,data);
    }

    @Override
    public void start() {
        //初始化时 将用户上下文信息构造session
        session= Repository.getInstance().getSession();
        toAddress= ConstantValue.TO_ADDRESS;
    }

    public void refreshData(){
        RefreshDataUtils.refreshSession();
        notifyChange();
    }
}
