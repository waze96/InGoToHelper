package com.example.ausias.ingotohelper;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by waze9 on 02/04/2017.
 */

class MyCipher {
    private String cipherPass = "IngosToHelper2017";
    private IvParameterSpec iv = null;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    final private static byte[] IV_PARAM = { 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
    private Cipher cipher;
    private Key sKey;

    MyCipher(){
        iv = new IvParameterSpec(IV_PARAM);
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(cipherPass.getBytes("UTF-8"));
            hash = Arrays.copyOf(hash, 256/8);
            sKey = new SecretKeySpec(hash, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKey, iv);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public String cipher(String pass){
        byte[] data = null;
        byte[] decryptedData = null;
        try {
            data = pass.getBytes("UTF-8");
            decryptedData = cipher.doFinal(data);
            return bytesToHex(decryptedData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
