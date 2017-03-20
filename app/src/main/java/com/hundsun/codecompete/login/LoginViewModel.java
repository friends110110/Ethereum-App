package com.hundsun.codecompete.login;

import com.hundsun.codecompete.base.BaseViewModel;
import com.hundsun.codecompete.data.Session;
import com.hundsun.codecompete.data.source.local.Repository;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LoginViewModel implements BaseViewModel{

    String v3Str;
    LoginModel model;
    public LoginViewModel(){
        this.model=new LoginModel();
    }

    public String getV3Str(){
        return v3Str;
    }
    /**
     * 创建钱包
     */
    public boolean createWallet(String password) {
        try {
            Session session=model.createWallet(password);
            Repository.getInstance().setSession(session);
            return true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean restoreWallet(String v3Str, String password) {
        try {
            Session session=model.restoreWallet(v3Str,password);
            Repository.getInstance().setSession(session);
            return true;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;

        }
    }

    public boolean loginMyPrivateKeyWallet(){
        Session session=model.loginMyPrivateKeyWallet();
        Repository.getInstance().setSession(session);
        return true;
    }


    @Override
    public void start() {
        Session session=Repository.getInstance().getSession();
        if (session==null){
            v3Str="none";
        }
    }
}
