package com.ge.eth.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
public class EthAccount {
    /**
     * 助记词
     */
    private String mnemonic;

    /**
     * 地址
     */
    private String address;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 公钥
     */
    private String publicKey;
}
