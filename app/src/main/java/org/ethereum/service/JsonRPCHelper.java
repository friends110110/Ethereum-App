package org.ethereum.service;

import org.ethereum.crypto.SHA3Helper;
import org.spongycastle.util.encoders.Hex;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/8/4.
 */
public class JsonRPCHelper {
    /**
     * 将方法名 sha3 hash散列 并取前4个字节 8bits
     * @param method
     * @return
     */
    public String sha3MethodFetch4Bytes(String method){
//        SHA3Digest digest = new SHA3Digest(256);
//        byte[] hash = new byte[digest.getDigestSize()];
//        byte[] message = method.getBytes();
//        digest.update(method.getBytes(), 0, message.length);
//        digest.doFinal(hash, 0);
        byte[]hash=SHA3Helper.sha3(method.getBytes());
        String result = Hex.toHexString(Arrays.copyOfRange(hash, 0, 4));
        return result;
    }


    /**
     * 前面补0 成64bits
     * @param number 数字的String
     * @return
     */
    public String beginAppend0To64bits(String number){
        //0 补0   64 补成64位
        return String.format("%064d",number);
    }
}
