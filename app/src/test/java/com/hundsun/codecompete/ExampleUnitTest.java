package com.hundsun.codecompete;


import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.utils.LogUtils;
import com.hundsun.codecompete.utils.RefreshDataUtils;
import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.assertEquals;

//import org.testng.annotations.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void createnewAlgorithmWallettest() throws Exception {
        Wallet wallet= CommonWallet.fromPrivateKey(Hex.decode(ConstantValue.MY_PRIVATE_KEY));
        String v3Str=wallet.toV3("wang123123");
        LogUtils.i(v3Str);

    }

    @Test
    public void refreshData() throws Exception {
        RefreshDataUtils.refreshSession();
    }


}