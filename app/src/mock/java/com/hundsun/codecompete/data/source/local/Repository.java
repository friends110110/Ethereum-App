package com.hundsun.codecompete.data.source.local;

import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.data.Session;
import com.hundsun.codecompete.data.source.DataSource;
import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.spongycastle.util.encoders.Hex;

/**
 * Created by Administrator on 2016/7/31.
 */
public class Repository implements DataSource {
    /**
     * 用户相关信息
     */
    Session session;
    private Repository(){
        session=new Session();
        Wallet wallet = CommonWallet.fromPrivateKey(Hex.decode(ConstantValue.MY_PRIVATE_KEY));
        session.setPassword(ConstantValue.MY_PASSWORD);
        session.setV3Str(ConstantValue.MY_V3_STR);
        session.setWallet(wallet);
    }
    private static Repository INSTANCE=new Repository();
    public static Repository getInstance(){
        return INSTANCE;
    }
    @Override
    public Session getSession() {
        return session;
    }

    public void setSession(Session session){
        this.session=session;
    }

    public void clearSession() {
        session=null;
    }
}
