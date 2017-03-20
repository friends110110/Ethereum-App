package com.hundsun.codecompete.data;

import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import org.ethereum.wallet.Wallet;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/7/28.
 * 为了保护以太坊中的私钥，所以建立以password密码和v3格式文件组合来产生私钥
 */
public class Session implements Serializable{

    //钱包信息，由于wallet的操作比较耗时，所以暂存wallet中的数据。根据钱包信息可获取 私钥、公钥、地址等等
    private Wallet wallet;
    //账户名 也可算是昵称
    private String accountName;
    //邮箱
    private String eMailAddress;
    //手机号码
    private String mobilePhone;
    //v3文件
    private String v3Str;
    //密码    与v3文件共同可以算出出私钥
    private String password;
    // 本人 以太坊地址
    private String address;
    // 私钥，  根据 v3Str 与password 得出primarykey
    private String privateKey;
    //公钥
    private String publicKey;
    //以太币
    private String etheCoin;
    //代币,恒生币
    private String hsCoin;

    private String portraitUrl;


    public Wallet getWallet() {
        return wallet;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
        setAddress(wallet.getAddressString());
        setPrivateKey(wallet.getPrivateKeyString());
        setPublicKey(wallet.getPublicKeyString());

        /*存在密码不存在v3文件，那么就生成它
        * 不可能存在  存在v3文件但是没有密码的情况
        * */
        if (null==getV3Str()&&null!=getPassword()){//
            try {
                setV3Str(wallet.toV3(password));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取以太币积分币等
         */
//        refreshOthers();
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }



    public String getAccountName() {
        return accountName==null? ConstantValue.VALUE_PERSON_INFO:accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public String getHsCoin() {
        return hsCoin;
    }

    public void setHsCoin(String hsCoin) {
        this.hsCoin = hsCoin;
    }

    public String getEtheCoin() {
        return etheCoin;
    }

    public void setEtheCoin(String etheCoin) {
        this.etheCoin = etheCoin;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getV3Str() {
        return v3Str;
    }

    public void setV3Str(String v3Str) {
        this.v3Str = v3Str;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
