package com.example.cambox.Util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    //encryption purpose SHA-1 encoded
    public static String digest(String key){
        MessageDigest strtohex = null;
        try {
            strtohex = MessageDigest.getInstance("SHA-1");
            strtohex.update(key.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encrypted =  new BigInteger(1 , strtohex.digest()).toString(16);
        return encrypted;
    }
}
