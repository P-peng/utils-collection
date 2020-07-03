package com.ge.eth.utils;

import com.google.common.collect.ImmutableList;
import com.sun.istack.internal.logging.Logger;
import org.bitcoinj.crypto.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;
import sun.security.provider.SecureRandom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不依赖网络ETH工具类
 *
 * @author
 * @date 2019/11/07
 */
public class EthLocalUtil {

    private static Logger logger = Logger.getLogger(EthLocalUtil.class) ;

    static final String OX = "0x";

    /**
     * 助记词类型 ETH
     */
    private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

    /** ETH 系列  **/

    /**
     * 创建ETH新地址,不依赖于链上
     *
     * @return mnemonic -> 助记词， privateKey -> 私钥， publicKey -> 公钥， address -> 地址
     * @throws MnemonicException.MnemonicLengthException
     */
    public static Map<String, String> createAccount() throws MnemonicException.MnemonicLengthException {
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
        HashMap<String, String> map = new HashMap<>(8);
        map.put("mnemonic", mnemonic);
        map.put("privateKey", privateKey);
        map.put("publicKey", publicKey);
        map.put("address", address);
        return map;
    }

    /**
     * 可用
     * 检查以太坊地址是否有效, 不依赖于链上
     * @param address
     * @return
     */
    public static boolean checkEthAddress(String address){
        if (address == null || !address.startsWith(OX)) {
            return false;
        }
        return isValidAddress(address);
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


}
