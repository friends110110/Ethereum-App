package com.hundsun.codecompete.data.ConstantValue;

/**
 * Created by Administrator on 2016/7/29.
 */
public class ConstantValue {
    //请求码
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;


    //二维码生成标识字段
    public static String QR_GENERATE_CODE_FLAG ="generate_code";



    //n=21146加密方式
    //public static String MY_V3_STR="{\"address\":\"2498e8af9e4b524ee9e73e4a6feb82879b56a507\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"0f8611a94b8e526b0c53b1012f9fb76898c9513dec4655e400c61f322e3a6427\",\"cipherparams\":{\"iv\":\"5e5a2ef45f41811d3f7477f831a1b238\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"966b292ce9c8913d6e125c788b0c43e57421762ccb0f36e8f9f5982aa668eeba\"},\"mac\":\"0bcd5a09cce9c29cca138998510e6dd8a7153aeecf41f85a8fe9a77b0c46277b\"},\"id\":\"b1f3180a-72ff-477d-aa7d-3b14caac2a3e\",\"version\":3}";
    //n=1024加密方式
//    public static String MY_V3_STR="{\"address\":\"2498e8af9e4b524ee9e73e4a6feb82879b56a507\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"5eed594c5db3d8300033d7cf490b5a65faf2fb0d55dbcaa2a5a0bf8ddb4c2d5d\",\"cipherparams\":{\"iv\":\"bf5dde65c5f88b778421bb4676a9e27d\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":1024,\"r\":8,\"p\":1,\"salt\":\"90e4dfaa5bc099bdb6b504d34ed70681280f7bd6b65cb0e466fddfa5403ce20f2412dca09db7dc72447d056bfc43517f3ef304eea3b5db0b7a48401e859b37c4\"},\"mac\":\"82a18260b43a7e7bc4b67825bbdd8d101faf5afd2e101c1ce08eb147846dc578\"},\"id\":\"5829c9c8-643e-4c38-ad19-13e64bca7012\",\"version\":3}\n";
    //n=2048
    public static String MY_V3_STR="{\"address\":\"2498e8af9e4b524ee9e73e4a6feb82879b56a507\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"ccb4ec519e98f7150a246016b7cc6967e0986c73832999e18713661017e51cac\",\"cipherparams\":{\"iv\":\"03dc9cf3710b3d5221feb8b4e9e02810\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":2048,\"r\":8,\"p\":1,\"salt\":\"727e741291590c80b5f93035d6911f868774f090ced6323ccd6c9736a2bc9b9ebdc35606259643f96903a1f83678b58477e0f19ae8850df53a31f24c3ececb0e\"},\"mac\":\"d60011f985bf8c9de3e97fa64ad6af5dae2dec6c710ef26529a573e57751e10f\"},\"id\":\"46ab06ba-8930-47c3-9663-6ceaf749cd59\",\"version\":3}";
    public static String MY_PASSWORD="wang123123";
    public static String MY_PRIVATE_KEY="1fe1f9aabee3135ecd11dfa99a56cd73f9b6b0a39e3bd61eb2e1dad000c9335b";
    public static String MY_PUBLIC_KEY="049656743af8a52d91b42058502b2909eca4ad2deaa057b4a0df7ea2757eb9fdbebb7f2d9aebecaa0ca5c53f24cd5a333423d0590817823b4aca0120ac53071459";
    public static String MY_ADDRESS="2498e8af9e4b524ee9e73e4a6feb82879b56a507";

    public static String TO_ADDRESS="7f4B46632E36356B622BF21957802c03abe0A5a8";

    /**
     * VALUE FOR DEFAULT
     */
    public static String VALUE_PERSON_INFO="个人信息";
}
