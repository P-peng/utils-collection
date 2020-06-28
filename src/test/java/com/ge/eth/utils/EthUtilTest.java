package com.ge.eth.utils;

import lombok.ToString;
import org.junit.Test;
import org.web3j.tx.ChainId;
import org.web3j.tx.ChainIdLong;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author
 * @date 2019/11/07
 */
public class EthUtilTest {

    String hash = "0x441d44e8c1878e6a9f9175618bc07228c7f6fa8e4f1217f12e9d50df697a7b13";
    String address = "0x107B7c28E991760bD7E647195A931e174202825a";
    String contract = "0x4df9500a02d64dcff99562ad128432832bac29a4";

    long chainId = ChainIdLong.MAINNET;

    /**
     * 椭圆加密 - Secp256k1算法，获取 地址，私钥，公钥，助记词
     * @throws Exception
     * @Test T
     */
    @Test
    public void createAccountTest() throws Exception {
        for (int i = 0; i < 20; i++) {
            System.out.println(EthUtil.createAccount());
        }
    }

    /**
     * 根据交易hash获取交易信息， 能无效的交易
     * @Test T
     */
    @Test
    public void getInfoByHashRawTest(){
        try {
            System.out.println(EthUtil.getInfoByHashRaw(hash));

            System.out.println(EthUtil.getInfoReceiptByHashRaw(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ETH 查询余额
     * @Test T
     */
    @Test
    public void getBalanceByAddressTest(){
        List<String> list = new ArrayList<>();
        list.add("0xe408721d4769De32ac00B682632bd86bEaaCd706");
        list.add("0x5e224698758B3dA4021604F02771B33318Ad48Ba");
        try {
            for (String s : list) {
                BigDecimal bigDecimal = EthUtil.getEthBalanceByAddress(s);
                System.out.println(s + "  " + bigDecimal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取nonce的值，返回最新的值 + 1
     *  @Test T
     */
    @Test
    public void getNonceByAddressTest(){
        try {
            System.out.println(EthUtil.getNonceByAddress("0x236161041b291e4f24C6c159864dcc998657D92b"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  ETH检查地址
     *  @Test T
     */
    @Test
    public void checkEthAddressTest(){
        System.out.println(EthUtil.checkEthAddress("0xfC33984A16FeC91Bece89f73B65f60841F08059B"));
    }

    /**
     *  ETH 转账
     * @Test T
     * @throws IOException
     */
    @Test
    public void tokenTransactionTest() throws IOException {
        // 出金地址
        String fromAddress = "";
        // 出金地址私钥
        String privateKey = "";

        String toAddress = "";
        Double amount = new Double(0.00024);
        /**
         * 手续费倍率，2最低，4慢，6中，8快
         * 估算，4 gasPrice 约等于 0.01usd,
         * 估算， 每加 2 gasPrice 相当于 +0.01usd
         */
        BigDecimal gasPrice = new BigDecimal(3);
        BigInteger nonce = EthUtil.getNonceByAddress(fromAddress);

        try {
            System.out.println(EthUtil.ethTransactionRaw(privateKey, toAddress, amount, gasPrice, nonce));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** ERC20系列 **/

    /**
     *  ETH-ERC20 根据合约查余额
     *  @Test T
     */
    @Test
    public void getBalanceByAddressAndContractTest(){
        try {
//            System.out.println(EthUtil.getBalanceByAddressAndContract(address, contract));
            BigInteger amountInteger = EthUtil.getBalanceByAddressAndContractRaw(address, contract);
            BigDecimal money = new BigDecimal(amountInteger).divide(new BigDecimal(Math.pow(10, 18)));
            System.out.println(money);

            BigInteger amountInteger2 = EthUtil.getBalanceByAddressAndContract(address, contract);
            BigDecimal money2 = new BigDecimal(amountInteger2).divide(new BigDecimal(Math.pow(10, 18)));
            System.out.println(money2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ERC20代币转账
     * @Test Y
     */
    @Test
    public void signTokenTransactionTest(){
        // 出金地址
        String from = "";
        // 出金地址私钥
        String privateKey = "";
        // 入金地址
        String to = "";
        String amount = "0.0001";
        String contractAddres = contract;
        BigDecimal gasPrice = new BigDecimal("2");
        System.out.println(EthUtil.signTokenTransaction(from, to, amount, privateKey, contractAddres, gasPrice));
    }

    /**
     * ERC20代币转账
     * @Test Y
     */
    @Test
    public void contractTransactionTest(){
        String privateKey = "";
        String toAddress = "";
        Double amount = new Double(0.0001);
        BigDecimal gasPrice = new BigDecimal(2);
        BigInteger nonce = new BigInteger("230");
        int decimals = 18;
        try {
            System.out.println(EthUtil.contractTransaction(privateKey, contract, toAddress, amount, decimals, gasPrice, nonce, chainId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ERC20代币授权
     * @Test Y
     */
    @Test
    public void approveTest(){
        // 授权地址，例如 A -> B ， A 授权给B，则 authAddress填A地址
        String authAddress = "";
        // 授权地址私钥 , A 地址私钥
        String authAddressPrivateKey = "";
        // 被授权， B 地址
        String authTargetAddress = "";
        // 授权额度
        String amount = "300000";
        // 合约地址
        String contractAddress = contract;
        BigDecimal gasPrice = new BigDecimal("2");
        // 授权地址要高于 EHT金额 0.00024
        System.out.println(EthUtil.approve(authAddress, authTargetAddress, amount, authAddressPrivateKey, contractAddress, gasPrice));
    }

    /**
     * 从已经授权的地址提取币
     * @Test Y
     */
    @Test
    public void transferFromTest(){
        // 授权地址，例如 B <- A ， B从A地址提取，则 authAddress填A地址
        String authAddress = "";
        // 被授权 B 地址
        String authTargetAddress = "";
        // 被授权地址私钥，B 地址私钥
        String authTargetAddressPrivateKey = "";
        // 提取金额
        String amount = "0.0001";
        // 合约地址
        String contractAddress = contract;
        BigDecimal gasPrice = new BigDecimal("2");
        System.out.println(EthUtil.transferFrom(authAddress, authTargetAddress, amount, authTargetAddressPrivateKey, contractAddress, gasPrice));
    }

    /**
     *  查询B地址可以从A提取多少币
     */
    @Test
    public void allowanceTest() throws IOException {
        // 授权地址，例如 B <- A ， B从A地址可提取多少，则 authAddress填A地址
        String authAddress = "";
        // 被授权 B 地址
        String authTargetAddress = "";
        // 合约地址
        String contractAddress = contract;
        BigInteger amountInteger = EthUtil.allowance(authAddress, authTargetAddress, contractAddress);
        BigDecimal money = new BigDecimal(amountInteger).divide(new BigDecimal(Math.pow(10, 18)));
        System.out.println(money);
    }

}
