package com.hundsun.codecompete;

import android.app.Application;
import android.test.ApplicationTestCase;
import com.hundsun.codecompete.login.LoginModel;
import org.junit.Test;

import java.security.GeneralSecurityException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    LoginModel model;
    @Test
    public void testRestoreWallet() {
        String content="{\"address\":\"2498e8af9e4b524ee9e73e4a6feb82879b56a507\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"0f8611a94b8e526b0c53b1012f9fb76898c9513dec4655e400c61f322e3a6427\",\"cipherparams\":{\"iv\":\"5e5a2ef45f41811d3f7477f831a1b238\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"966b292ce9c8913d6e125c788b0c43e57421762ccb0f36e8f9f5982aa668eeba\"},\"mac\":\"0bcd5a09cce9c29cca138998510e6dd8a7153aeecf41f85a8fe9a77b0c46277b\"},\"id\":\"b1f3180a-72ff-477d-aa7d-3b14caac2a3e\",\"version\":3}";
        String passwd="wang123123";
        try {
            model.restoreWallet(content,passwd);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}