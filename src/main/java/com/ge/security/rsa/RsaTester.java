package com.ge.security.rsa;


import java.util.Map;

public class RsaTester {

    static String publicKey;
    static String privateKey;


    public static void main(String[] args) throws Exception {
        //获得公钥和私钥
        try {
            Map<String, Object> keyMap = RsaUtil.genKeyPair();
            publicKey = RsaUtil.getPublicKey(keyMap);
            privateKey = RsaUtil.getPrivateKey(keyMap);
            System.err.println("公钥: \n" + publicKey);
            System.err.println("私钥： \n" + privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //yfx：RSA加解密效率低，并且好像是新线程执行的，导致下面的日志输出位置，不是按照代码顺序
        test();
//        testSign();
//        testHttpSign();

    }

    static void test() throws Exception {
        System.out.println("==========================================");
        System.err.println("公钥加密——私钥解密");
        String source = "{\"address\":\"0xe02a3ef9c05c0195ea36abe61d7adc6f3f5fefdb\",\"privateKey\":\"95c03435401eec4157e03c49c807435943ffd24a51d15421cda5029235670050\",\"publicKey\":\"156ae15970ab299a6d67379f26c37078753a32a0559db04a434e981956f00d9f109d30d2fcc4d837c4a0ed7bfa6160cd19f3d856baccd003aa30b31d11de6471\",\"mnemonic\":\"[grab unusual iron silk love peace divide holiday good equal ginger oil]\"}}";
        System.out.println("\r加密前文字：\r\n" + source);
        String encodedData = RsaUtil.encryptByPublicKey(source, publicKey);
        System.out.println("加密后文字：\r\n" + encodedData);
        String decodedData = RsaUtil.decryptByPrivateKey(encodedData, privateKey);
        System.out.println("解密后文字: \r\n" + decodedData);
    }

    static void testSign() throws Exception {
        System.out.println("==========================================22");
        System.err.println("私钥加密——公钥解密");
        String source = "132129342156311256315612136512136511231251365121356121311621361213112653";
        System.out.println("原文字：\r\n" + source);
        String encodedData = RsaUtil.encryptByPrivateKey(source, privateKey);
        System.out.println("加密后：\r\n" + encodedData);
        String decodedData = RsaUtil.decryptByPublicKey(encodedData, publicKey);
        System.out.println("解密后: \r\n" + decodedData);
        System.out.println("==========================================333");
        System.err.println("私钥签名——公钥验证签名");
        String sign = RsaUtil.sign(encodedData, privateKey);
        System.err.println("签名:\r" + sign);
        boolean status = RsaUtil.verify(encodedData, publicKey, sign);
        System.err.println("验证结果:\r" + status);
    }

    static void testHttpSign() throws Exception {
        System.out.println("==========================================4444");
        String param = "id=1&name=张三";
        String encodedData = RsaUtil.encryptByPrivateKey(param, privateKey);
        System.out.println("加密后：" + encodedData);

        String decodedData = RsaUtil.decryptByPublicKey(encodedData, publicKey);
        System.out.println("解密后：" + new String(decodedData));

        String sign = RsaUtil.sign(encodedData, privateKey);
        System.err.println("签名：" + sign);

        boolean status = RsaUtil.verify(encodedData, publicKey, sign);
        System.err.println("签名验证结果：" + status);
    }


}

