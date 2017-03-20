package test.wallet.crypto;

import java.security.Security;
import java.security.spec.ECGenParameterSpec;

import org.spongycastle.jce.provider.BouncyCastleProvider;

abstract class SCBasedCase {
    public static final String ALGORITHM = "EC";
    public static final String CURVE_NAME = "secp256k1";
    public static final ECGenParameterSpec SECP256K1_CURVE = new ECGenParameterSpec(CURVE_NAME);

    public void registerSCProvider() {
	// 注册新的Provider
	BouncyCastleProvider provider = new BouncyCastleProvider();
	Security.addProvider(provider);
    }

}
