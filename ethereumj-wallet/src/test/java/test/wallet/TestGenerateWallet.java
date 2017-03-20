package test.wallet;

import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.spongycastle.util.encoders.Hex;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

public class TestGenerateWallet {
    private static Logger log = Logger.getLogger(TestGenerateWallet.class);
    @Test
    public void testSimpleNewWallet() {
	// 1. Create Instance
	Wallet wallet = CommonWallet.generate();
	Assert.assertNotNull(wallet);
	log.warn("Private Key:" + wallet.getPrivateKeyString());
	log.warn("Public Key:" + wallet.getPublicKeyString());
	log.warn("Address:" + wallet.getAddressString());
    }

    @Test
    public void testGenerateFromPrivateKey() {
	// 1. Create Instance
	Wallet wallet = CommonWallet.fromPrivateKey(Hex.decode("4d40981315085349d11a7432ae716098c4ba7def0c0180b0c12d8e5f65a9659d"));
	Assert.assertNotNull(wallet);
	log.warn("Private Key:" + wallet.getPrivateKeyString());

	// 2. Get Public Key and Address and compare
	String publicKey = wallet.getPublicKeyString();
	String address = wallet.getAddressString();
	log.warn("Public Key:" + publicKey);
	log.warn("Address:" + address);
	Assert.assertEquals(
	        publicKey,
	        "043cf549095081daee94f7d4de4c7e71092f8b2c25f327ff67a6a41a380cc4c752580104906ec0e3154f8cb2cbb013474f43a0bd40e138b765b8bf674fddddc12c");
	Assert.assertEquals(address, "15a495e3d7f62857bf53e7c9684348414f44b3ea");
    }

    @Test
    public void testGenerateFromPublicKey() {
	// 1. Create Instance
	Wallet wallet = CommonWallet
	        .fromPublicKey(Hex
	                .decode("04dc5dc04b955fc053a6cddf973a4e9c611737cfea2760e063768fa79ef3ce992caac09c64e52e521b3a1adf5c3cdecef8d44440bc7877d36af3a5d83373843e78"));
	Assert.assertNotNull(wallet);

	// 2. Get Public Key and Address and compare
	String publicKey = wallet.getPublicKeyString();
	String address = wallet.getAddressString();
	log.warn("Public Key:" + publicKey);
	log.warn("Address:" + address);
	Assert.assertEquals(
	        publicKey,
	        "04dc5dc04b955fc053a6cddf973a4e9c611737cfea2760e063768fa79ef3ce992caac09c64e52e521b3a1adf5c3cdecef8d44440bc7877d36af3a5d83373843e78");
	Assert.assertEquals(address, "e29958bd8ccc91186821f328b14584c7c9986bec");

	// 3. Wallet from public only not allowed to get private key
	String privateKey = wallet.getPrivateKeyString();
	log.warn("Private Key: " + privateKey);
	Assert.assertNull(privateKey);
    }
}
