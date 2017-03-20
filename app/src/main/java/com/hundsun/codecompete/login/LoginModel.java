package com.hundsun.codecompete.login;

import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.data.Session;
import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.spongycastle.util.encoders.Hex;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/7/28.
 * 为了保护以太坊中的私钥，所以建立以password密码和v3格式文件组合来产生私钥
 */
public class LoginModel {

    public Session createWallet(final String password) throws NoSuchAlgorithmException {
        // 1. Create Instance
        Wallet wallet = CommonWallet.generate();
        Session session=new Session();
        session.setPassword(password);
        assembleSession(session,wallet);
        return session;
    }

    public Session restoreWallet(String v3Str, String password) throws GeneralSecurityException {
        Wallet wallet=CommonWallet.fromV3(v3Str, password);
        Session session=new Session();
        session.setV3Str(v3Str);
        session.setPassword(password);
        return assembleSession(session,wallet);
    }

    /**
     * 直接从私钥获取钱包，所以没有对应的 密码与v3文件
     * @return
     */
    public Session loginMyPrivateKeyWallet() {
        Wallet wallet = CommonWallet.fromPrivateKey(Hex.decode(ConstantValue.MY_PRIVATE_KEY));
        Session session=new Session();
        session.setPassword(ConstantValue.MY_PASSWORD);
        session.setV3Str(ConstantValue.MY_V3_STR);
        return assembleSession(session,wallet);
    }

    private Session assembleSession(Session session,Wallet wallet){
        session.setWallet(wallet);
        return session;
    }


}
