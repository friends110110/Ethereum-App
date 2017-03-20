package com.hundsun.codecompete;


import com.hundsun.codecompete.utils.LogUtils;
import com.hundsun.jresplus.testing.engine.web.HttpHelper;
import com.hundsun.jresplus.testing.engine.web.HttpResponse;
import org.ethereum.service.BusinessException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.security.SignatureException;

//import org.testng.annotations.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)

public class TransactionUnitTest {
    @Test
    public void sendTrans() throws BusinessException, SignatureException, IOException {
//        JsonRPC.eth_sendTransaction(ConstantValue.TO_ADDRESS,"1","hello");
    }

    @Test
    public void receTrans() throws BusinessException, SignatureException, IOException {
        String txHash="0x3dbf2f9d3eef91cf3cb473ea42b6d513ab0de396d6bb367b9fb49e8a02183d3e";
        HttpResponse response = HttpHelper.target("http://testnet.etherscan.io/tx/" + txHash)
                .addHeader("HOST", "testnet.etherscan.io")
                .addHeader("Referer", "http://testnet.etherscan.io/search?" + txHash)
                .addHeader("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_2_1 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5")
                .get();
        String content = response.getContent();
        LogUtils.w(content);
    }
}