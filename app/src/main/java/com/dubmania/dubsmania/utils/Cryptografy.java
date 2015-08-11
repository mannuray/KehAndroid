package com.dubmania.dubsmania.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hardik.parekh on 8/3/2015.
 */
public class Cryptografy {
    private static final String MASTERKEY = "DUBMANIA";
    private SecretKeySpec key;
    private Cipher cipher;

    public Cryptografy() {
        byte[] bKey = {
                (byte) 0xAC, (byte) 0x59, (byte) 0xC0, (byte) 0x2E,
                (byte) 0xAD, (byte) 0xFE, (byte) 0xDE, (byte) 0xBE
        };
        key = new SecretKeySpec(bKey, MASTERKEY);
        try {
            cipher = Cipher.getInstance(MASTERKEY);
        } catch (Exception exception) {

        }
    }

    public String getEncryptData(String password) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptData = cipher.doFinal(password.getBytes());
            return new String(encryptData);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public String getDecryptData(String data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptData = cipher.doFinal(data.getBytes());
            return new String(decryptData);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
