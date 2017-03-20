package test.wallet;

import com.google.common.io.ByteStreams;
import org.ethereum.crypto.HashUtil;
import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.encoders.Hex;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.security.SecureRandom;

public class TestV3Format {
    byte[] dk = Hex.decode("80d9a9b9d2b9e662903ef68b12f12a66");
    byte[] vk = Hex.decode("682bb4fb91b2633aed2da51db9a66fe2");

     @Test(description = "Test export key to V3 keyfile store format")
    public void testExportToV3() throws Exception {
	// 1. Generate a Wallet
	Wallet wallet = CommonWallet.generate();
	String address = wallet.getAddressString();
	System.out.println("Address: " + address);
	// 4. prepare cipher
	Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

	// 5. Crypto private key
	System.out.println("dk: " + Hex.toHexString(dk));
	SecretKey aesKey = new SecretKeySpec(dk, "AES");

	byte[] iv = new byte[16];
	SecureRandom random = new SecureRandom();
	random.nextBytes(iv);
	System.out.println("iv: " + Hex.toHexString(iv));
	IvParameterSpec iv_spec = new javax.crypto.spec.IvParameterSpec(iv);

	cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv_spec);

	// encrypt
	byte[] privkey = wallet.getPrivateKey();
	System.out.println("pk: " + Hex.toHexString(privkey));

	byte[] ciphertext = cipher.doFinal(privkey);
	System.out.println("Cipher: " + Hex.toHexString(ciphertext));

	// 6. Calc MAC
	byte[] mac = HashUtil.sha3(Arrays.concatenate(vk, ciphertext));
	System.out.println("MAC: " + Hex.toHexString(mac));

	// ///////////////////////////
	// Test Decrypt //
	// //////////////////////////
	// cipher.init(Cipher.DECRYPT_MODE, aesKey, iv_spec);
	// byte[] pk2 = cipher.doFinal(ciphertext);
	// System.out.println(Hex.toHexString(pk2));
	recoverFromV3(ciphertext, iv, mac, address);
    }

     @Test(dataProvider = "testData")
    public void testFromV3(String ciphertext, String iv, String mac, String address) throws Exception {
	recoverFromV3(Hex.decode(ciphertext), Hex.decode(iv), Hex.decode(mac), address);
    }

    @DataProvider(name = "testData")
    public Object[][] getTestData() {
	return new Object[][] { { "fe482f79fc8bc3e0483d0664af09eda0ff2c4f973c2accbefc44feccf3ce199c",
	        "649a4ccda1e384d3b46491fa4e8244ef", "f70aba016c28a0d1de0c5e8285406beca139d8435866bbdcd7eef5f922b65f16",
	        "2519e47e4dcaf36e345b9471fdb6b6aa254250c4" } };
    }

    public void recoverFromV3(byte[] ciphertext, byte[] iv, byte[] mac, String address) throws Exception {
	// 1. Check Mac
	
	String vmac = Hex.toHexString(HashUtil.sha3(Arrays.concatenate(vk, ciphertext)));
	System.out.println("mac: " + vmac);
	Assert.assertEquals(vmac, Hex.toHexString(mac));

	// 2. Prepare cipher
	System.out.println("Address: " + address);
	// 4. prepare cipher
	Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

	// 5. Crypto private key
	System.out.println("dk: " + Hex.toHexString(dk));
	SecretKey aesKey = new SecretKeySpec(dk, "AES");
	IvParameterSpec iv_spec = new javax.crypto.spec.IvParameterSpec(iv);
	cipher.init(Cipher.DECRYPT_MODE, aesKey, iv_spec);

	// 3. Decrypt
	byte[] pk = cipher.doFinal(ciphertext);
	System.out.println("pk:" + Hex.toHexString(pk));

	// 4. Check Address
	Wallet wallet2 = CommonWallet.fromPrivateKey(pk);
	String new_address = wallet2.getAddressString();
	System.out.println("address: " + new_address);
	Assert.assertEquals(new_address, address);
    }

    @Test
    public void testFromV3Wallet() throws Exception {
	InputStream in = getClass().getResourceAsStream("testkeyfile");
	Assert.assertNotNull(in, "打开文件失败");
	String content = new String(ByteStreams.toByteArray(in));
	in.close();

	Wallet wallet = CommonWallet.fromV3(content, "111111");
	Assert.assertEquals(wallet.getAddressString(), "2519e47e4dcaf36e345b9471fdb6b6aa254250c4");
    }
@Test
    public void testToV3Wallet() throws Exception {
	Wallet wallet = CommonWallet.generate();
	String password = "myPassowrd";
	String json = wallet.toV3(password);
	Assert.assertNotNull(json);
	System.out.println(json);
	Wallet w2 = CommonWallet.fromV3(json, password);
	Assert.assertEquals(w2.getAddressString(), wallet.getAddressString());
    }

    @Test(description = "test")
    public void testSpecifyPKToV3Wallet() throws Exception {
        Wallet wallet = CommonWallet.fromPrivateKey(Hex.decode("590bf470f0236aabc70dad3788dc98fcd3dbcf8923ffe87d7070958fbee8d43c"));
        String password = "myPassowrd";
        String json = wallet.toV3(password);
        Assert.assertNotNull(json);
        System.out.println(json);
        Wallet w2 = CommonWallet.fromV3(json, password);
        Assert.assertEquals(w2.getAddressString(), wallet.getAddressString());
    }
}
