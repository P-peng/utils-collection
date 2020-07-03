package com.ge.security.rsa;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RS 签名/验证
 *
 * @author dengzhipeng
 * @date 2019/05/07
 */
public class RsaSignUtil {

    private final static String SIGN_TYPE_RSA = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA签名, 有编码类型针对的是字符串的编码，用于存在中文
     *
     * @param content    待签名数据
     * @param privateKey 私钥
     * @param encode     字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
        PrivateKey priKey = keyFactory.generatePrivate(priPKCS8);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(priKey);
        // 字符集
        signature.update(content.getBytes(encode));
        byte[] signed = signature.sign();
        return Base64.encode(signed);
    }

    /**
     * RSA签名, 有编码类型针对的是字符串的编码
     *
     * @param content       待签名数据
     * @param privateKey    私钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String sign(String content, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
        PrivateKey priKey = keyFactory.generatePrivate(priPKCS8);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(priKey);
        // 字符集
        signature.update(content.getBytes());
        byte[] signed = signature.sign();
        return Base64.encode(signed);
    }

    /**
     * RSA验签名验证
     *
     * @param content   原数据( 一般是：公钥加密的数据 ）
     * @param sign      签名值
     * @param publicKey 公钥
     * @param encode    字符集编码
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String publicKey, String encode) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
        byte[] encodedKey = Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

        Signature signature = Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes(encode));

        return signature.verify(Base64.decode(sign));
    }

    /**
     * RSA验签名验证
     *
     * @param content   原数据
     * @param sign      签名值
     * @param publicKey 公钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verify(String content, String sign, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
        byte[] encodedKey = Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

        Signature signature = Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes());

        return signature.verify(Base64.decode(sign));
    }


    private static String publicKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDfvZ/zSiOVw0/scDTr255rTQ4h\n" +
                    "bcrqJLP+4ZNSPwJHB9Lktv0uiXcMFiKqEC0vaRse5iwUJhLCHeeXShpJ3s/k2s+C\n" +
                    "2pgHO6ZOUNUmwIXkb40KAPBReZuWJHg+UulV/m+dBg8Z0KkWOyOafimMfP1/44d1\n" +
                    "Q9KLFblgNS90V29QxQIDAQAB\n";

    private static String privateKey =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN+9n/NKI5XDT+xw\n" +
                    "NOvbnmtNDiFtyuoks/7hk1I/AkcH0uS2/S6JdwwWIqoQLS9pGx7mLBQmEsId55dK\n" +
                    "Gknez+Taz4LamAc7pk5Q1SbAheRvjQoA8FF5m5YkeD5S6VX+b50GDxnQqRY7I5p+\n" +
                    "KYx8/X/jh3VD0osVuWA1L3RXb1DFAgMBAAECgYAP6ZeXoP046IQlp7FL2PnCXd+U\n" +
                    "Zw+YaXXXuclVfx+1V4wbMhx9k89Ar85VVAMyrQH9x5b48+A/qArRiral7E/Sr9x3\n" +
                    "eM6XBskNuBBnVpBQBy+WYHLwB3lQ2nZ1M1KXsCaqeHzExqLATtN+O4tQoQKoPv8P\n" +
                    "5vYCBCuxqcTpk8HcgQJBAPr7WO/5Ratx2F6sZo1exqECt2Pog1J8Bbsz9uZGeHAr\n" +
                    "jhaY520nRCNLlvBjSfvSXQWxKA2FxX2NDZkibaSHXaECQQDkNtf0gMbey+m4CwPQ\n" +
                    "FuGjSA28pjv6V2dXH/AbhmoQorZ/5fPO6MBEH5OP4uum8LzyaqgOC7A4IJP/mJ+f\n" +
                    "/PilAkEA2pZKEUayrOzMGzhfYMSojdaFzlfU9+PYQgCyCkBZ6KemanvlIyXNrEfL\n" +
                    "P7XZ256NMcXnMk/Nftl4fGSFxYO3wQJAYF7UhRiYYl8jvCdSnd37vLLhsidUrpuY\n" +
                    "NQ90mnsBcTL09D+L3HUnM91Nt/YwYxfIAmqNuxiuepUvSkEOL0ZpTQJBAMJrhKNX\n" +
                    "jjdQgs/aN/NylcT1bwuxrrbghejw12NmpXQNmIyXeC9NKgtGyW2rPqhmd0N4arik\n" +
                    "wdtr5rvV7+XGhJQ=\n";


    /**
     * 测试用例
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            String sign = RsaSignUtil.sign("123456", privateKey);
            System.out.println("sign = " + sign);
            boolean bool = RsaSignUtil.verify("123456", sign, publicKey);
            System.out.println(bool);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

