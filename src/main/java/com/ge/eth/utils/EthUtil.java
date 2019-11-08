package com.ge.eth.utils;

import com.ge.eth.entity.EthAccount;
import com.google.common.collect.ImmutableList;
import org.bitcoinj.crypto.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import sun.security.provider.SecureRandom;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author dengzhipeng
 * @date 2019/11/07
 */
public class EthUtil {

    /**
     * 去申请key
     * @link:https://infura.io/
     */
    private static String BASE_URL = "https://mainnet.infura.io/v3/1b87fa0b1a2d4147b0df388dafea4635";

    private static Web3j web3 = Web3j.build(new HttpService(BASE_URL));


    private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

    /** ETH 系列  **/

    /**
     * 创建ETH新地址,不依赖于链上
     * @return 返回得对象携带 地址，私钥，公钥，助记词
     * @throws Exception
     */
    public static EthAccount createAccount() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
        secureRandom.engineNextBytes(entropy);
        // 生成12位助记词
        List<String> mnemonicList = MnemonicCode.INSTANCE.toMnemonic(entropy);
        // 使用助记词生成钱包种子
        byte[] seed = MnemonicCode.toSeed(mnemonicList, "");
        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
        DeterministicKey deterministicKey = deterministicHierarchy
                .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
        byte[] bytes = deterministicKey.getPrivKeyBytes();
        ECKeyPair keyPair = ECKeyPair.create(bytes);
        // 通过公钥生成钱包地址
        String address = Keys.getAddress(keyPair.getPublicKey());
        // 助记词
        String mnemonic = mnemonicList.toString().replaceAll(", ", " ");
        // 私钥 转换16进制
        String privateKey = keyPair.getPrivateKey().toString(16);
        // 公钥 转换16进制
        String publicKey = keyPair.getPublicKey().toString(16);
        EthAccount ethAccount = new EthAccount();
        ethAccount.setMnemonic(mnemonic);
        ethAccount.setPublicKey(publicKey);
        ethAccount.setPrivateKey(privateKey);
        ethAccount.setAddress(address);
        return ethAccount;
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
     * 检查以太坊地址是否有效, 不依赖于链上
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
     * 可用
     * 以太坊转账方法，手续费计算 = gasLimit * gasPrice
     * @param privateKey 私钥
     * @param toAddress 转入地址
     * @param amount    金额
     * @param gasPriceValue 加速点, 和手续费有关
     * @param nonce 第n币交易
     * @return  hash
     * @throws IOException
     */
    public static String ethTransactionRaw(String privateKey, String toAddress, double amount,BigDecimal gasPriceValue, BigInteger nonce) throws IOException {
        // gasPrice
        BigInteger gasPrice = Convert.toWei(gasPriceValue, Convert.Unit.GWEI).toBigInteger();
        // gasLimit
        BigInteger gasLimit = BigInteger.valueOf(21000);
        // 转账人私钥
        Credentials credentials = Credentials.create(privateKey);
        //创建交易
        BigInteger value = Convert.toWei(Double.toString(amount), Convert.Unit.ETHER).toBigInteger();
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, gasPrice, gasLimit, toAddress, value);
        // 签名Transaction，这里要对交易做签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        // 发送交易
        String hash = web3.ethSendRawTransaction(hexValue).send().getTransactionHash();
        return hash;
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


    private static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);
        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }
        return cleanInput.length() == 40;
    }

    /** ERC20 系列 **/

    /**
     * 可用
     * 根据合约查代币的余额, 通过原函数调用，性能低
     * @param address
     * @param contract
     * @return
     * @throws IOException
     */
    public static BigInteger getBalanceByAddressAndContractRaw(String address, String contract) throws IOException{
        // 函数类
        Function function = new Function(
                // 函数类型
                "balanceOf",
                // Solidity Types in smart contract functions
                Arrays.asList(new Address(address)),
                Arrays.asList(new TypeReference<Type>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        String value = web3.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(address, contract, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send().getValue();
        //返回16进制余额
        value = value.substring(2);
        return new BigInteger(value, 16);
    }

    /**
     * 可用
     * 根据合约查代币的余额, 拼装调用，性能高
     * @param address
     * @param contract
     * @return
     * @throws IOException
     */
    public static BigInteger getBalanceByAddressAndContract(String address, String contract) throws IOException{
        String encodedFunction = "0x70a08231000000000000000000000000" + address.substring(2);
        String value = web3.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(address, contract, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send().getValue();
        //返回16进制余额
        value = value.substring(2);
        return new BigInteger(value, 16);
    }

    /**
     * 代币转账
     */
    public static String contractTransaction(String privateKey, String contract, String toAddress, double amount,
                                              int decimals, BigDecimal gasPriceValue, BigInteger nonce, long chainId) throws IOException {
        // gasPrice
        BigInteger gasPrice = Convert.toWei(gasPriceValue, Convert.Unit.GWEI).toBigInteger();
        // gasLimit
        BigInteger gasLimit = BigInteger.valueOf(200000);
        BigInteger value = BigInteger.ZERO;
        // token转账参数
        String methodName = "transfer";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address tAddress = new Address(toAddress);
        Uint256 tokenValue = new Uint256(BigDecimal.valueOf(amount).multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
        inputParameters.add(tAddress);
        inputParameters.add(tokenValue);
        TypeReference<Bool> typeReference = new TypeReference<Bool>() {};
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        String signedData = signTransaction(nonce, gasPrice, gasLimit, contract, value, data, chainId, privateKey);
        String hash = web3.ethSendRawTransaction(signedData).send().getTransactionHash();
        return hash;
    }

    /**
     *
     */
    private static String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                         BigInteger value, String data, long chainId, String privateKey) {
        byte[] signedMessage;
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);
        signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }
}
