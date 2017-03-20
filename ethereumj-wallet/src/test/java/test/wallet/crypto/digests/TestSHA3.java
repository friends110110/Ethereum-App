package test.wallet.crypto.digests;

import java.util.Arrays;

import org.spongycastle.crypto.digests.SHA3Digest;
import org.spongycastle.util.encoders.Hex;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

public class TestSHA3 {
    private static Logger log = Logger.getLogger(TestSHA3.class);
    SHA3Digest digest = new SHA3Digest(256);

    @DataProvider(name = "test1")
    public static Object[][] primeNumbers() {
	return new Object[][] { { "lock(uint32)", "d3c45b8d" }, { "open(uint32,uint32)", "d1c721c5" }, { "betUp()", "ad19f442" },
	        { "betDown()", "7782090b" }, { "settlement(uint32)", "1b7f65ec" } };
    }

    // This test will run 4 times since we have 5 parameters defined
    @Test(description = "Test SHA3", dataProvider = "test1")
    public void testSHA3Digest(String value, String expected) {
	byte[] hash = new byte[digest.getDigestSize()];
	byte[] message = value.getBytes();
	digest.update(value.getBytes(), 0, message.length);
	digest.doFinal(hash, 0);
	String result = Hex.toHexString(Arrays.copyOfRange(hash, 0, 4));
	log.warn("Hash: " + result);
	Assert.assertEquals(result, expected);
    }
}
