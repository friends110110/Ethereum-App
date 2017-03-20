package com.hundsun.codecompete;

import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteStreams;
import com.hundsun.jresplus.testing.base.AbstractBaseTestCase;
import com.hundsun.jresplus.testing.engine.web.HttpHelper;
import com.hundsun.jresplus.testing.engine.web.HttpResponse;
import org.ethereum.contract.Contract;
import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey.MissingPrivateKeyException;
import org.ethereum.service.BusinessException;
import org.ethereum.service.RpcHelper;
import org.ethereum.util.Utils;
import org.ethereum.wallet.CommonWallet;
import org.ethereum.wallet.Wallet;
import org.spongycastle.util.encoders.Hex;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 测试通过RPC方式调用积分代币合约的兑换功能
 * 
 * @author tongsh
 *
 */
public class MyContractTest extends AbstractBaseTestCase {
    private static String contract_address = "81867a79f4939E77418696Bf363089F220a0368B";
    private static AtomicLong tx_ind = null;

    /**
     * 买入积分
     * <p/>
     * 发送一定数量的以太币到积分代币合约兑换积分
     * 
     * @throws IOException
     * @throws BusinessException
     * @throws SignatureException 
     * @throws MissingPrivateKeyException 
     */
    @Test
    public void testSellScore() throws IOException, BusinessException, MissingPrivateKeyException, SignatureException {

	// 1. 创建钱包，后续签名和获取交易地址要用
	Wallet wallet = CommonWallet.fromPrivateKey(Hex.decode("590bf470f0236aabc70dad3788dc98fcd3dbcf8923ffe87d7070958fbee8d43c"));

	// 2. 创建合约类

	// 2.1 加载合约ABI
	InputStream in = getClass().getResourceAsStream("org/ethereum/service/ScoreToken.abi");
	String abi = new String(ByteStreams.toByteArray(in));
	in.close();

	// 2.2 创建合约实例
	Contract contract = Contract.fromABI(abi);

	// 2.3. 获取函数编码：对于卖出交易，需要提供一个参数 - 卖出积分
	BigInteger sell_value = new BigInteger("100"); // 卖出100分
	byte[] payload = contract.function("sell").getData(sell_value);

	// 3. 准备交易数据

	// 3.1 获取当前交易计数，这个Nonce 需要从链上获取当前地址的交易计数做为计数器
	// 这里用了一个静态计数器保存，下次无需再访问
	if (tx_ind == null) {
	    JSONObject result = RpcHelper.call("eth_getTransactionCount", wallet.getAddressString(), "latest");
	    BigInteger ind = Utils.toBigNumber(result.getString("result"));
	    tx_ind = new AtomicLong(ind.longValue());
	}
	BigInteger nonce = BigInteger.valueOf(tx_ind.getAndIncrement());

	// 3.2 获取当前油价
	JSONObject result = RpcHelper.call("eth_gasPrice");
	BigInteger gasPrice = Utils.toBigNumber(result.getString("result"));

	// 3.3 油量限额, 假设30000，可以使用eth_estimateGas去估算
	BigInteger gasLimit = new BigInteger("30000");

	// 3.4 创建交易
	// 对于卖出代币，无需发送以太币过去
	Transaction tx = Transaction.create(contract_address, BigInteger.ZERO, nonce, gasPrice, gasLimit, payload);

	// 4. 对交易进行签名
	tx.sign(wallet);
	byte[] encoded = tx.getEncoded();

	// 5. 发送交易
	result = RpcHelper.call("eth_sendRawTransaction", Hex.toHexString(encoded));
	log.warn(result);

	assertNotNull(result, "发送交易失败");
	String txHash = result.getString("result");
	log.warn("Tx Hash: " + txHash);
	
	//6. 等待30秒，查询测试站交易，应该可以查到数据
	try {
	    Thread.currentThread().sleep(30000);
        } catch (InterruptedException e) {
        }
	
	HttpResponse response = HttpHelper.target("http://testnet.etherscan.io/tx/" + txHash)
		.addHeader("HOST", "testnet.etherscan.io") 
		.addHeader("Referer", "http://testnet.etherscan.io/search?" + txHash) 
		.addHeader("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_2_1 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5")
		.get();
	String content = response.getContent();
	log.warn(content);
	assertEquals(response.getStatus(), 200);
	assertTrue(content.indexOf(txHash)>0, "没找到指定的交易");


        String []a=new String[]{};
        String []b=new String[]{};


    }

}
