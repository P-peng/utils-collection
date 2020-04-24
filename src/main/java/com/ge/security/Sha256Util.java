package com.ge.security;

import org.junit.Test;

import java.security.MessageDigest;

/**
 * Sha256散列值
 * @author dengzhipeng
 * @date 2020/04/24
 */
public class Sha256Util {

    @Test
    public void test(){
        String str = "Hello, world!4251";
        System.out.println(sha256(str));
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }


}
