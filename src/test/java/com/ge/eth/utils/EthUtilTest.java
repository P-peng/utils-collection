package com.ge.eth.utils;

import lombok.ToString;
import org.junit.Test;
import org.web3j.tx.ChainId;
import org.web3j.tx.ChainIdLong;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * @author dengzhipeng
 * @date 2019/11/07
 */
public class EthUtilTest {

    String hash = "0xc31860547cbedb56471ff482037b70cc3a90a616b0531b8f3b24e71b3c68d97d2";
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
        try {
            BigDecimal bigDecimal = EthUtil.getEthBalanceByAddress("0x107B7c28E991760bD7E647195A931e174202825a");
            System.out.println(bigDecimal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getNonceByAddressTest(){
        try {
            System.out.println(EthUtil.getNonceByAddress("0xfC33984A16FeC91Bece89f73B65f60841F08059B"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkEthAddressTest(){
        System.out.println(EthUtil.checkEthAddress("0xfC33984A16FeC91Bece89f73B65f60841F08059B"));
    }

    @Test
    public void tokenTransactionTest(){
        String privateKey = "33969a4f087f2433c5e08ccc1011bdc0498cd19e8bb132d5fa82e687e7038dde";
        String toAddress = "0x06193DD85759b278063e0F7da45Ed84BF0C7b765";
        Double amount = new Double(0.000001);
        BigDecimal gasPrice = new BigDecimal(2);
        BigInteger nonce = new BigInteger("3");

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
        String privateKey = "33969a4f087f2433c5e08ccc1011bdc0498cd19e8bb132d5fa82e687e7038dde";
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
