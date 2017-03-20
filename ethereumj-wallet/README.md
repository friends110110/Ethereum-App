以太坊钱包
====
> 钱包是用来存储和维护密钥对的工具

主要接口:`org.etheCoin.wallet.Wallet`
实现类: 普通钱包实现类`org.etheCoin.wallet.CommonWallet

## 创建钱包
### 创建钱包

  1. **生成一个新钱包**
  `CommonWallet.generate();`  
  输入: 无  
  输出: Wallet 实例  
  　
  2. **从私钥恢复钱包**  
  从已经拥有的私钥恢复钱包实例
  `CommonWallet.fromPrivateKey(byte[] privKeyBytes)`  
  输入: 私钥  
  输出: Wallet 实例  
  　
  3. **从公钥恢复钱包**  
  从获取的公钥恢复钱包实例，注意：从公钥恢复的钱包只能用来做验签，不能用于签名
  `CommonWallet.fromPublicKey(byte[] publicKeyBytes)`  
  输入：公钥  
  输出:  Wallet 实例  
  　  
  4. **从密钥存储文件(keyfile)恢复钱包**  
  从V3格式的密钥存储文件恢复钱包
  `fromV3(String json, String password)`  
  输入:  V3格式的JSON字符串和保护密码  
  输出:  Wallet 实例

## 签名
  对消息进行签名。**注意**：由于JDK不支持SHA3, 签名数据需要之前计算HASH
    `sign(byte[] messageHash) throws SignatureException`
    输入: 需要签名的HASH  
    输出: ECDSA签名对象
    
## 验签
  对收到的报文进行签名验证。使用前需要从交易中获取的公钥来创建钱包
  `verify(byte[] messageHash, ECDSASignature signature) throws SignatureException`  
  输入: 报文的SHA3 HASH 和交易中解析的签名对象  
  输出: boolean，是否验证通过
