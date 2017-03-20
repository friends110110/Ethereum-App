package org.ethereum.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ethereum.contract.Contract;
import org.ethereum.core.Transaction;
import org.ethereum.crypto.SHA3Helper;
import org.ethereum.util.Unit;
import org.ethereum.util.Utils;
import org.ethereum.wallet.Wallet;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2016/7/30.
 */
public class JsonRPC {

    private static String txHash;


    /**
     * 根据 txHash查询这笔交易是否被认可，交易是否成功
     * @param txHash
     * @return
     * @throws BusinessException
     */
    public static String eth_getTransactionByHash(String txHash) throws BusinessException {
        JSONObject result=RpcHelper.call("eth_getTransactionByHash",txHash);
        return result.getString("result");
    }
    /**
     *
     * @param address
     * @return 以太币
     * @throws Exception
     * reference by https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getbalance
     */
    public static String eth_getBalance(String address) {
        JSONObject jsonObject= null;
        String resulet=null;
        try {
            //返回json串
            jsonObject = RpcHelper.call("eth_getBalance",address,"latest");
            //获取结果，该结果为 wei为单位的币
            resulet=jsonObject.getString("result");
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        //转换成以太币单位
        String etheCoin=Unit.valueOf(Unit.ether.toString()).fromWei(resulet).toString();
        return etheCoin;
    }

    /**
     *
     * @param toAddress
     * @param value 默认传来的是以wei为单位的币，转换成以太币Unit.valueOf(Unit.ether.toString()).toWei(...)
     *              wei是最小单位，其他都是wei的10的n次
     * @param data
     * @throws BusinessException
     * @throws SignatureException
     * @throws IOException
     */
    public static void eth_sendTransaction(Wallet wallet,String toAddress ,String value,String data) throws BusinessException, SignatureException, IOException {
        // 3.1 获取当前交易计数，这个Nonce 需要从链上获取当前地址的交易计数做为计数器
        // 这里用了一个静态计数器保存，下次无需再访问
        AtomicLong tx_ind = null;
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

        /**
         * 被误导了很久  地址前面不要有0x,否则就报错
         */
        BigInteger amount=Unit.valueOf(Unit.ether.toString()).toWei(String.valueOf(value));
        Transaction tx = Transaction.create(toAddress, amount , nonce, gasPrice, gasLimit, data.getBytes("utf-8"));
        // 4. 对交易进行签名
        tx.sign(wallet);
        byte[] encoded = tx.getEncoded();
        // 5. 发送交易
        result = RpcHelper.call("eth_sendRawTransaction", Hex.toHexString(encoded));
//        result=RpcHelper.call("eth_sendTransaction","0x"+wallet.getAddressString(),toAddress,gasLimit.toString(),gasPrice.toString(),value,data);
        txHash = result.getString("result");

//        ThreadPoolUtils.execute(new Runnable() {
//            @Override
//            public void run() {
//                HttpResponse response = null;
//                try {
//                    response = HttpHelper.target("http://testnet.etherscan.io/tx/" + txHash)
//                            .addHeader("HOST", "testnet.etherscan.io")
//                            .addHeader("Referer", "http://testnet.etherscan.io/search?" + txHash)
//                            .addHeader("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_2_1 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5")
//                            .get();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                String content = response.getContent();
//                LogUtils.w(content);
//                if(content.indexOf(txHash)>0){
//                    //成功转账
//                }
//            }
//        });

    }
    static JsonRPCHelper jsonRPCHelper=new JsonRPCHelper();
    /**
     * Contract.fromABI(abi).function("set").formatSignature() 获得函数名 如  "set(uint256)"等
     * referenced by http://ethereum.stackexchange.com/questions/3514/how-to-call-a-contract-method-using-the-eth-call-json-rpc-api
     */
    /**
     * 用于函数调用 合约的函数声明要是 constant的
     * referenced by http://ethereum.stackexchange.com/questions/3514/how-to-call-a-contract-method-using-the-eth-call-json-rpc-api
     * @param fromAddress
     * @param toAddress
     * @param function  Contract.fromABI(abi).function("set").formatSignature() 获得函数名 如  "set(uint256)"等
     * @param args
     * @throws BusinessException
     */
    public static void eth_call(String fromAddress, String toAddress, Contract.Function function, String... args) throws BusinessException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("from",fromAddress);
        jsonObject.put("to",toAddress);
//        //1. 将函数名 取sha3 散列 并取前4个字节
//        String method8Bytes=jsonRPCHelper.sha3MethodFetch4Bytes(function.formatSignature());
//        StringBuilder data=new StringBuilder(method8Bytes);
//        //2.如果存在参数，那么将参数补齐到64bits ,然后 拼接到data上
//        if (args!=null){
//            for (int i=0;i<args.length;i++){
//                data.append(jsonRPCHelper.beginAppend0To64bits(args[i]));
//            }
//        }

        byte[] data=function.encode(new Object[]{args});        //这句等于以上
        //3.将拼接结果作为data数据
        jsonObject.put("data",Hex.toHexString(data));
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(jsonObject);
        jsonArray.add("latest");
        RpcHelper.callRawJson("eth_call",jsonArray);
    }

    /**
     * referenced by https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getstorageat
     * @param contractAddress
     * @param positon
     * @return
     * @throws BusinessException
     */
    public static String eth_getStorageAt(String contractAddress,int positon) throws BusinessException {
        JSONObject jsonObject=RpcHelper.call("eth_getStorageAt",contractAddress,Integer.toHexString(positon),"latest");
        return jsonObject.get("result").toString();
    }

    /**
     * 获取 位于合约中的 map类型所对应的 value值。  key 为 map里位置 key补全成64bits  拼接  map所在的位置 补全成64bits
     * referenced by https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getstorageat
     * @param contractAddress
     * @param positon
     * @param key
     * @return
     * @throws BusinessException
     */
    public static String eth_getStorageAtMap(String contractAddress,int positon,String key) throws BusinessException {
        //1.将key(address) 前补0成64bits   和  position （map第几位0开始）前补0成64bits   并且串接
        String pos=jsonRPCHelper.beginAppend0To64bits(key)+jsonRPCHelper.beginAppend0To64bits(Integer.toHexString(positon));
        //2. sha3 散列化，再 转成16进制的String 方便 call调用
        pos=Hex.toHexString(SHA3Helper.sha3(pos.getBytes()));
        JSONObject jsonObject=RpcHelper.call("eth_getStorageAt",contractAddress,pos,"latest");
        return jsonObject.get("result").toString();
    }

}
