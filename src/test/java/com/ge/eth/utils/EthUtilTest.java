package com.ge.eth.utils;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * @author dengzhipeng
 * @date 2019/11/07
 */
public class EthUtilTest {

    String hash = "0xc31860547cbedb56471ff482037b70cc3a90a616b0531b8f3b24e71b3c68d97d2";
    String address = "0xb8dc927983fb012eec27f7bfcf171872f9ece4c9";
    String contract = "0x4df9500a02d64dcff99562ad128432832bac29a4";

    @Test
    public void getInfoByHashRawTest(){
        try {
            EthUtil.getInfoByHashRaw(hash);
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


    /** ERC20系列 **/
    @Test
    public void getBalanceByAddressAndContractTest(){
        try {
            System.out.println(EthUtil.getBalance(address, contract));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
