package com.ge.eth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.logging.Logger;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * * PackageName: com.ruoyi.common.coin.coinapi
 * * @author mengxiangshi
 * * @date 2019年11月21日  15:31
 * * @version 1.0
 * * Description:以太坊及以太坊代币API类
 * *
 * *......................我佛慈悲......................
 * *                      _oo0oo_
 * *                     o8888888o
 * *                     88" . "88
 * *                     (| -_- |)
 * *                     0\  =  /0
 * *                   ___/`---'\___
 * *                 .' \\|     |// '.
 * *                / \\|||  :  |||// \
 * *               / _||||| -卍-|||||- \
 * *              |   | \\\  -  /// |   |
 * *              | \_|  ''\---/''  |_/ |
 * *              \  .-\__  '-'  ___/-. /
 * *            ___'. .'  /--.--\  `. .'___
 * *         ."" '<  `.___\_<|>_/___.' >' "".
 * *        | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * *        \  \ `_.   \_ __\ /__ _/   .-` /  /
 * *    =====`-.____`.___ \_____/___.-`___.-'=====
 * *                      `=---='
 * *
 * *...............佛祖开光 ,永无BUG................
 * *        佛曰:
 * *               写字楼里写字间，写字间里程序员；
 * *               程序人员写程序，又拿程序换酒钱。
 * *               酒醒只在网上坐，酒醉还来网下眠；
 * *               酒醉酒醒日复日，网上网下年复年。
 * *               但愿老死电脑间，不愿鞠躬老板前；
 * *               奔驰宝马贵者趣，公交自行程序员。
 * *               别人笑我忒疯癫，我笑自己命太贱；
 * *               不见满街漂亮妹，哪个归得程序员？
 * *        总结：
 * *               悲哉！悲哉！与妞无缘！
 */
public class EthApi {
    private static Logger logger = Logger.getLogger(EthApi.class) ;
    private String url ;
    private static Web3j web3j ;
    private String createAddress ;
    private String privatekey ;

    public String getCreateAddress() {
        return createAddress;
    }

    public void setCreateAddress(String createAddress) {
        this.createAddress = createAddress;
    }

    public EthApi(String createAddress, String privatekey) {
        this.createAddress = createAddress;
        this.privatekey = privatekey;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public EthApi(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    /**
//     *@描述 获取ETH连接，初始化方法
//     *@参数 [url, coinDecimal]
//     *@返回值 EthApi
//     *@创建人 mengxiangshi
//     *@创建时间 2019/11/21
//     *@修改人和其它信息
//     */
//    public static EthApi getInitialization(String url){
//        try{
//            web3j = Web3j.build(new HttpService(url));
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.info("虚拟币ETH获取连接失败，错误代码：{}",new Object[]{e.getMessage()});
//        }
//        return new EthApi(url);
//    }
//    /**
//     *@描述  以太坊获取地址余额
//     *@参数 [address]
//     *@返回值 double
//     *@创建人 mengxiangshi
//     *@创建时间 2019/11/21
//     *@修改人和其它信息
//     */
//    public double getBalance(String address){
//        double balance = 0.0 ;
//        try{
//            EthGetBalance getBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
//            if(!getBalance.hasError()){
//                String value = getBalance.getBalance().toString();
//                double valueDouble = Convert.fromWei(value, Convert.Unit.ETHER).doubleValue();
//                if(valueDouble > 0){
//                    return valueDouble;
//                }else{
//                    return 0;
//                }
//            }else{
//                return 0;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.info("虚拟币ETH获取余额失败，错误代码：{}",new Object[]{e.getMessage()});
//            return balance ;
//        }
//    }
//    /**
//     *@描述  获取以太坊代币余额
//     *@参数 [fromAddress(代币地址), contractAddress(代币合约地址), decimal(小数位数)]
//     *@返回值 double
//     *@创建人 mengxiangshi
//     *@创建时间 2019/11/21
//     *@修改人和其它信息
//     */
//    public double tokenGetBalance(String fromAddress, String contractAddress, int decimal) {
//        try
//        {
//            String methodName = "balanceOf";
//            List inputParameters = new ArrayList();
//            List outputParameters = new ArrayList();
//            Address address = new Address(fromAddress);
//            inputParameters.add(address);
//            TypeReference typeReference = new TypeReference()
//            {
//            };
//            outputParameters.add(typeReference);
//            Function function = new Function(methodName, inputParameters, outputParameters);
//            String data = FunctionEncoder.encode(function);
//            org.web3j.protocol.core.methods.request.Transaction transaction = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(fromAddress, contractAddress, data);
//
//            BigInteger balanceValue = BigInteger.ZERO;
//            try {
//                EthCall ethCall = (EthCall)this.web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
//                List results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//                balanceValue = (BigInteger)((Type)results.get(0)).getValue();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return BigDecimalUtils.div(new BigDecimal(balanceValue).doubleValue(), Math.pow(10.0D, decimal), 8);
//        } catch (Exception e) {
//            logger.info("虚拟币ETH代币获取余额失败，错误代码：{}",new Object[]{e.getMessage()});
//            e.printStackTrace();
//        }
//        return decimal;
//    }
//    /**
//     *@描述  以太坊生成地址
//     *@参数 [pwd(地址密码)]
//     *@返回值 com.ruoyi.common.coin.coinapi.EthApi
//     *@创建人 mengxiangshi
//     *@创建时间 2019/11/21
//     *@修改人和其它信息
//     */
//    public EthApi createNewAddress(String pwd){
//        try{
//            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
//            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
//            WalletFile walletFile = Wallet.createStandard(pwd, ecKeyPair);
//            String keystore = objectMapper.writeValueAsString(walletFile);
//            WalletFile walletFile2 = objectMapper.readValue(keystore, WalletFile.class);
//            ECKeyPair ecKeyPair1 = Wallet.decrypt(pwd, walletFile2);
//            return new EthApi("0x"+walletFile.getAddress(),ecKeyPair1.getPrivateKey().toString(16)) ;
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.info("虚拟币ETH创建地址失败，错误代码：{}",new Object[]{e.getMessage()});
//            return null ;
//        }
//    }
//    /**
//     *@描述  ETH 转账
//     *@参数 [from(转出地址), to(转入地址), amount(转出金额), privateKey(转出地址私钥)]
//     *@返回值 java.lang.String
//     *@创建人 mengxiangshi
//     *@创建时间 2019/11/21
//     *@修改人和其它信息
//     */
//    private  String signETHTransaction(String from,String to,String amount,String privateKey){
//        try{
//            BigInteger nonce = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
//            //支付的矿工费
//            BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
//            BigInteger gasLimit = new BigInteger("210000");
//            BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
//            //签名交易
//            RawTransaction rawTransaction = RawTransaction.createTransaction (nonce, gasPrice, gasLimit, to, amountWei, "");
//            Credentials credentials = Credentials.create(privateKey);
//            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//            //广播交易0
//            String hash =  web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get().getTransactionHash();
//            logger.info("ETH转账,发送方:{},接收方:{},发送金额:{},hash:{}",new Object[]{from,to,amount,hash});
//            return hash ;
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.info("虚拟币ETH转账失败，错误代码：{}",new Object[]{e.getMessage()});
//            return null ;
//        }
//    }
//    /**
//     *@描述  eth代币转账
//     *@参数 [from(代币转出地址), to(代币转入地质), amount(转出金额), privateKey(转出地址私钥), coinAddress(代币合约地址)]
//     *@返回值 java.lang.String
//     *@创建人 mengxiangshi
//     *@创建时间 2019/11/21
//     *@修改人和其它信息
//     */
//    private String signTokenTransaction(String from,String to,String amount,String privateKey,String coinAddress){
//        try{
//            BigInteger nonce = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
//            //支付的矿工费
//            BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
//            BigInteger gasLimit = new BigInteger("210000");
//
//            Credentials credentials = Credentials.create(privateKey);
//            BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
//
//            //封装转账交易
//            Function function = new Function(
//                    "transfer",
//                    Arrays.<Type>asList(new Address(to),
//                            new org.web3j.abi.datatypes.generated.Uint256(amountWei)),
//                    Collections.<TypeReference<?>>emptyList());
//            String data = FunctionEncoder.encode(function);
//            //签名交易
//            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, coinAddress, data);
//            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//            //广播交易
//            String hash = web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get().getTransactionHash();
//            logger.info("ETH代币转账,发送方:{},接收方:{},发送金额:{},hash:{}",new Object[]{from,to,amount,hash});
//            return hash ;
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.info("虚拟币ETH代币转账失败，错误代码：{}",new Object[]{e.getMessage()});
//            return null ;
//        }
//    }
//    /**
//     *@描述 eth 区块查询
//     *@参数 [blockNum]
//     *@返回值 void
//     *@创建人 mengxiangshi
//     *@创建时间 2019/11/22
//     *@修改人和其它信息
//     */
//    private void getBlock(int blockNum){
//        try{
//            BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
//            if(blockNum == blockNumber.intValue()){
//                // 说明没有最新的区块数据
//                return ;
//            }
//            for (int i = blockNum;i<=blockNumber.intValue();i++){
//                logger.info("ETH查询当前区块：{}",new Object[]{i});
//                EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNum)), true).send().getBlock();
//                List<EthBlock.TransactionResult> transactions = block.getTransactions();
//                for (EthBlock.TransactionResult transactionResult:transactions) {
//                    if(transactionResult instanceof  EthBlock.TransactionObject){
//                        EthBlock.TransactionObject tx = (EthBlock.TransactionObject)transactionResult ;
//                        String blockHash = tx.getHash() ;
//                        String from = tx.getFrom();
//                        String to = tx.getTo();
//                        String input = tx.getInput();
//                        BigInteger gas = tx.getGas();
//                        System.out.println("from:"+from);
//                        System.out.println("to:"+to);
//                        System.out.println("gas:"+gas);
//                        System.out.println("hash:"+blockHash);
//                        System.out.println("input:"+input);
//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.info("虚拟币ETH查询区块记录错误，错误代码：{}",new Object[]{e.getMessage()});
//        }
//    }
}
