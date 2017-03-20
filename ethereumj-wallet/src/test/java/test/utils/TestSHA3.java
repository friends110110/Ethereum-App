package test.utils;

import org.spongycastle.crypto.digests.KeccakDigest;
import org.spongycastle.util.encoders.Hex;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestSHA3 {
//    private static SHA3Digest digest = new SHA3Digest(256);
    private static KeccakDigest digest = new KeccakDigest(256);

    @Test(dataProvider = "testData")
    public void testTwiceUpdateSHA3(String data1, String data2, String expected) {
	byte[] m1 = Hex.decode(data1);
	byte[] m2 = Hex.decode(data2);
	digest.update(m1, 0, m1.length);
	digest.update(m2, 0, m2.length);
	byte[] hash = new byte[digest.getDigestSize()];
	
	digest.doFinal(hash, 0);
	Assert.assertEquals(Hex.toHexString(hash), expected);
    }

    @DataProvider(name = "testData")
    Object[][] getData() {
	return new String[][] {

	        { "20ea693d385eacab1ebe5099a895f30ca45927ab69544ec0a0e2ae72d85c9f35",
	                "d8fd32fd4fc2051fec1164e55a97cb59006b4867240a611aac6da25c1cdb20d2",
	                "312bd5f8109c09be6347693c2faf4d7851ca4f9807e00e0b96d46af773c504da" },
	        { "ad6760a57f894a62c3e07e5c212f2488c44ca93c4afecb5268955133290ea189",
	                "3e761d9ebb81304f3a4297d6639848a9f7a4d826ec7c282a7c3126aecb425132",
	                "4eb65dc5feb5a5483aacd8c41e3d47fc43809b59391801c613155778c62f4484" } };
    }
}
