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
 * @author dengzhipeng
 * @date 2019/11/07
 */
public class EthUtilTest {

    String hash = "0xd17a26c905adda2d4f3b1388778c0ac4930113b25df9fa92400cf889b18eca08";
    String address = "0x107B7c28E991760bD7E647195A931e174202825a";
    String contract = "0x4df9500a02d64dcff99562ad128432832bac29a4";

    long chainId = ChainIdLong.MAINNET;

    @Test
    public void createAccountTest() throws Exception {

        System.out.println(EthUtil.createAccount());
    }

    @Test
    public void getInfoByHashRawTest(){
        try {
            System.out.println(EthUtil.getInfoByHashRaw(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    @Test
    public void getNonceByAddressTest(){
        try {
            System.out.println(EthUtil.getNonceByAddress("0x236161041b291e4f24C6c159864dcc998657D92b"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkEthAddressTest(){
        System.out.println(EthUtil.checkEthAddress("0xfC33984A16FeC91Bece89f73B65f60841F08059B"));
    }

    @Test
    public void tokenTransactionTest() throws IOException {
        String fromAddress = "xx";
        String privateKey = "xx";

        String toAddress = "xx";
        Double amount = new Double(0.0310);
        /**
         * 手续费倍率，2最低，4慢，6中，8快
         * 估算，4 gasPrice 约等于 0.01usd,
         * 估算， 每加 2 gasPrice 相当于 +0.01usd
         */
        BigDecimal gasPrice = new BigDecimal(6);
        BigInteger nonce = EthUtil.getNonceByAddress(fromAddress);

        try {
            System.out.println(EthUtil.ethTransactionRaw(privateKey, toAddress, amount, gasPrice, nonce));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** ERC20系列 **/
    @Test
    public void getBalanceByAddressAndContractTest(){
        try {
//            System.out.println(EthUtil.getBalanceByAddressAndContract(address, contract));
            System.out.println(EthUtil.getBalanceByAddressAndContractRaw(address, contract));
            System.out.println(EthUtil.getBalanceByAddressAndContract(address, contract));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void contractTransactionTest(){
        String privateKey = "xx";
        String toAddress = "0x06193DD85759b278063e0F7da45Ed84BF0C7b765";
        Double amount = new Double(0.000001);
        BigDecimal gasPrice = new BigDecimal(2);
        BigInteger nonce = new BigInteger("6");
        int decimals = 18;
        try {
            System.out.println(EthUtil.contractTransaction(privateKey, contract, toAddress, amount, decimals, gasPrice, nonce, chainId));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
