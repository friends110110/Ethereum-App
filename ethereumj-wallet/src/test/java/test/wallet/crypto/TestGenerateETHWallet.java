package test.wallet.crypto;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.security.spec.X509EncodedKeySpec;

import org.ethereum.crypto.ECKey.ECDSASignature;
import org.ethereum.crypto.jce.SpongyCastleProvider;
import org.ethereum.util.ByteUtil;
import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 测试生成以太坊密钥对
 * 
 * @author tongsh
 *
 */
public class TestGenerateETHWallet extends SCBasedCase {
    private static SecureRandom secureRandom = new SecureRandom();

    @Test(description = "使用SC生成钱包")
    public void generateBySC() throws Exception {
	// 1. 注册SC Provider
	registerSCProvider();

	// 2. 生成密钥对
	KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, "SC");
	keyGen.initialize(SECP256K1_CURVE, secureRandom);
	KeyPair keyPair = keyGen.generateKeyPair();

	//3. 获取ETH私钥
	ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
	byte[] priv = ByteUtil.bigIntegerToBytes(privateKey.getS(), 32);
	System.out.println("Private Key:\t" + Hex.toHexString(priv));

	//4. 获取ETH公钥
	BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();
	byte[] pub = publicKey.getQ().getEncoded(false);
	System.out.println("Public Key:\t" + Hex.toHexString(pub));

	// 5. 与钱包对比校验
	Wallet wallet = CommonWallet.fromPrivateKey(priv); // 从私钥恢复钱包
	String pub2 = wallet.getPublicKeyString(); // 期望生成的公钥相同
	System.out.println("Public Key Extracted:\t" + pub2);
	Assert.assertEquals(pub2, Hex.toHexString(pub), "Public Keys are different!");

    }

    @Test(description = "使用Sun EC 生成钱包")
    public void generateBySunEC() throws Exception {

	// 1. 生成密钥对
	KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, "SunEC");
	keyGen.initialize(SECP256K1_CURVE, secureRandom);
	KeyPair keyPair = keyGen.generateKeyPair();

	//2. 获取ETH私钥
	ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
	byte[] priv = ByteUtil.bigIntegerToBytes(privateKey.getS(), 32);
	System.out.println("Private Key:\t" + Hex.toHexString(priv));

	//3. 转换公钥
	ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
	ECPoint point = publicKey.getW();
	byte[] pub = ByteUtil.merge(new byte[] {4}, BigIntegers.asUnsignedByteArray(point.getAffineX()),BigIntegers.asUnsignedByteArray(point.getAffineY()));
	System.out.println("Public Key:\t" + Hex.toHexString(pub));
	
	// 4. 校验
	Wallet wallet = CommonWallet.fromPrivateKey(priv);
	String pub2 = wallet.getPublicKeyString();
	System.out.println("Public Key Extracted:\t" + pub2);
	Assert.assertEquals(pub2, Hex.toHexString(pub), "Public Keys are different!");
    }

    /**
     * 生成一个测试数据
     * 
     * @return
     */
    private byte[] getData() {
	byte[] data = new byte[32];
	new SecureRandom().nextBytes(data);
	return data;
    }

    /**
     * 生成签名
     * 
     * @param data
     * @param key
     * @param provider
     * @return 数字前面没
     * @throws Exception
     */
    private byte[] sign(byte[] data, PrivateKey key, String provider) throws Exception {
	Signature signature = Signature.getInstance("NONEWithECDSA", provider);
	signature.initSign(key);
	signature.update(data);
	byte[] sig = signature.sign();
	return sig;
    }

    /**
     * 验证签名 假设验签放收到了发送过来的数据，签名，以及一个编码过的公钥
     * 
     * @return 签名是否通过验证
     */
    private boolean verify(byte[] data, byte[] sig, PublicKey pubKey, String provider) throws Exception {
	// // 1 解析公钥
	// X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pub);
	// KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, provider);
	// PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
	// System.out.println("Pub Key Extracted:\t" +
	// Hex.toHexString(pubKey.getEncoded()));

	// 6.2 验证签名
	Signature signature = Signature.getInstance("NONEWithECDSA", provider);
	signature.initVerify(pubKey);

	signature.update(data);
	boolean is_verify = signature.verify(sig);
	System.out.println("Verify:\t" + is_verify);
	return is_verify;
    }
}