package com.ge.eth.utils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @author dengzhipeng
 * @date 2018/08/17
 */
public class EthUtil {

    private static String BASE_URL = "https://mainnet.infura.io/v3/1b87fa0b1a2d4147b0df388dafea4635";

    private static Web3j web3 = Web3j.build(new HttpService(BASE_URL));

    /** ETH 系列  **/

    /**
     * 创建ETH新地址，返回新地址和私钥
     */
    public void createAccount(String password, String filePath) throws Exception {
        Bip39Wallet wallet;
        try {
            wallet = WalletUtils.generateBip39Wallet(password, new File(filePath));
            String keyStoreKey = wallet.getFilename();
            String memorizingWords = wallet.getMnemonic();
            Credentials credentials = WalletUtils.loadBip39Credentials(password, wallet.getMnemonic());
            String publicKey = credentials.getEcKeyPair().getPublicKey().toString(16);
            String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        } catch (Exception e) {
            throw new Exception("创建以太坊钱包失败");
        }

    }

    /**
     * 根据hash查交易详情 原格式，无效的hash会返回空对象
     * @param hash
     * @return
     * @throws IOException
     */
    public static Optional<Transaction> getInfoByHashRaw(String hash) throws IOException {
        return web3.ethGetTransactionByHash(hash).send().getTransaction();
    }

    /**
     * 根据hash查交易是否收到详情 原格式，无效的hash会返回空对象，
     * 验证交易hash用此方法
     * @param hash
     * @return
     * @throws IOException
     */
    public static Optional<TransactionReceipt> getInfoReceiptByHashRaw(String hash) throws IOException {
        return web3.ethGetTransactionReceipt(hash).send().getTransactionReceipt();
    }


    /**
     * 可用
     * 检查以太坊地址是否有效
     * @param address
     * @return
     */
    public static boolean checkEthAddress(String address){
        if (address == null || !address.startsWith("0x")) {
            return false;
        }
        return isValidAddress(address);
    }


    /**
     * 可用
     * 根据地址获取地址ETh的余额
     * @param address
     * @return
     * @throws IOException
     */
    public static BigDecimal getEthBalanceByAddress(String address) throws IOException {
        return Convert.fromWei(web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance().toString(), Convert.Unit.ETHER);
    }

    /**
     * 可用
     * 根据地址获取当前nonce的值，返回的是最后一次的nonce + 1
     * @param address
     * @return
     * @throws IOException
     */
    public static BigInteger getNonceByAddress(String address) throws IOException {
        return web3.ethGetTransactionCount(address,DefaultBlockParameterName.LATEST).send().getTransactionCount();
    }


    /**
     * 发起各种币的转账
     */
    public void accountEthTransaction(){

    }

    /**
     * 各种币的授权
     */
    public void accountApprove(){

    }

    /**
     * 各种币的授权交易(归集)
     */
    public void accountApproveTransaction(){

    }


    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);
        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }
        return cleanInput.length() == 40;
    }

    /** ERC20 系列 **/

    private static final String DATA_PREFIX = "0x70a08231000000000000000000000000";


    public static BigDecimal getBalanceByAddressAndContract(String address, String contract) throws IOException {
        Function function = new Function(
                "balanceOf",
                Arrays.asList(new Address(address)),
                Arrays.asList(new TypeReference<Address>(){})
        );
        String str = FunctionEncoder.encode(function);
        String value = web3.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(address,
                        contract, str ), DefaultBlockParameterName.PENDING).send().getValue();
        String s = new BigInteger(value.substring(2), 16).toString();
        BigDecimal balance = new BigDecimal(s).divide(new BigDecimal(6), RoundingMode.HALF_DOWN);
        return balance;
    }

    public static BigDecimal getBalance(String address, String contractAddress) throws IOException {
        String value = Admin.build(new HttpService(BASE_URL))
                .ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(address,
                        contractAddress, DATA_PREFIX + address.substring(2)), DefaultBlockParameterName.PENDING).send().getValue();
        String s = new BigInteger(value.substring(2), 16).toString();
        BigDecimal balance = new BigDecimal(s).divide(new BigDecimal(6), RoundingMode.HALF_DOWN);
        return balance;
    }



}
