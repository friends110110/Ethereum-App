package test.utils;

import java.security.SecureRandom;

import org.ethereum.crypto.HashUtil;
import org.spongycastle.crypto.digests.KeccakDigest;
import org.spongycastle.crypto.digests.SHA3Digest;
import org.spongycastle.util.encoders.Hex;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestSHA3Simple {
    private static SHA3Digest digest = new SHA3Digest(256);
//    private static KeccakDigest digest = new KeccakDigest(256);
    @Test(dataProvider = "testData")
    public void testSimpleSHA3(String data, String expected) {
	byte[] m = Hex.decode(data);
	digest.update(m, 0, m.length);
	byte[] hash = new byte[digest.getDigestSize()];
	digest.doFinal(hash, 0);
	Assert.assertEquals(Hex.toHexString(hash), expected);
    }

    // @Test
    public void generateData() {
	byte[] m = new byte[32];
	SecureRandom random = new SecureRandom();
	random.nextBytes(m);
	digest.update(m, 0, m.length);
	byte[] hash = new byte[digest.getDigestSize()];
	digest.doFinal(hash, 0);
	System.out.println("{\"" + Hex.toHexString(hash) + "\",\"" + Hex.toHexString(m) + "\"},");
    }

    @DataProvider(name = "testData")
    Object[][] getData() {
	return new String[][] {
	        { "169bb1b7893512eeedb7b41139f83fc5f669f8b2280a4f9f3dd7da7d4a17765c",
	                "c864b4a3fc337527289d8d6ed335b26a69bcdb806006a8d04fc0a9beb250cdf7" },
	        { "ce544a27e49c412aec48e22bda7d6674a7c0775caf9b46632ab201b618c659b9",
	                "61816d5a16f1184dc8501d181b83d9b6404167f6f5267312a9a7d433813f794e" },
	        {
	                "481ba23b2ac823fd4341ad82c66315fe645334e55109897ece826569404c1f9d34ff309920f97839d94b216345552e08b20b2625a7f4f125e4cc3c7b4159e41f",
	                "d914f1fb80c1031bd0ab0d21b787e2e1690aa642a2021e24341cb6cc6d8bf384" },
	        {
	                "c91357cfdc00a43316112c1b460b77e11a942743b20f9c392d8932ad4f24c6dbdffc02cfb72b4e130c81f36668e041b0540935a7c1ac08645214e51a7330a8a6",
	                "102205385c984e44e8121327c2d7b3804eac5007d730eb60a803de1e23b1821e" } };
    }
}
