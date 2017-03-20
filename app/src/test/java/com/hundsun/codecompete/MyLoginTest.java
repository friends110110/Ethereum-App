package com.hundsun.codecompete;


import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.data.Session;
import com.hundsun.codecompete.login.LoginModel;
import com.hundsun.codecompete.utils.LogUtils;
import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)

public class MyLoginTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void restoreWallettest() throws Exception {
        LoginModel loginModel=new LoginModel();
        loginModel.restoreWallet(ConstantValue.MY_V3_STR,"123");
    }
    @Test
    public void createWallettest() throws Exception {
        LoginModel loginModel=new LoginModel();
        Session session=loginModel.createWallet("wang123");
    }
    @Test
    public void createnewAlgorithmWallettest() throws Exception {
        Wallet wallet=CommonWallet.fromPrivateKey(Hex.decode(ConstantValue.MY_PRIVATE_KEY));
        String v3Str=wallet.toV3("wang123123");
        LogUtils.i(v3Str);
    }

}