package com.ge.security.sha256;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 计算字符串的 sha256 hash值
 *
 * @author dengzhipeng
 * @date 2019/05/07
 */
public class Sha256HashUtil {

    static final String SHA_256 = "SHA-256";

    /**
     * 获取字符串的 hash 值，默认 UTF-8 编码
     *
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getHashSHA256(String value) throws NoSuchAlgorithmException {
        return getHashSHA256(value, StandardCharsets.UTF_8);
    }

    /**
     * 获取字符串的 hash 散列值
     *
     * @param value 原数据
     * @param charset 编码类型
     * @return hash 散列值
     * @throws NoSuchAlgorithmException
     */
    public static String getHashSHA256(String value, Charset charset) throws NoSuchAlgorithmException {
        byte[] bytes = value.getBytes(charset);
        return getHashSHA256(bytes);
    }


    public static String getHashSHA256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(SHA_256);
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return toHex(digest);
    }

    public static String toHex(byte[] bytes) {
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }
        return hash.toString();
    }

    /**
     * 测试用例
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            String hash = Sha256HashUtil.getHashSHA256("dsads5a4d65sa4d5as4d65as4d65as4d64as56d4a6s4d5as4d645as");
            System.out.println(hash);
            System.out.println(hash.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
