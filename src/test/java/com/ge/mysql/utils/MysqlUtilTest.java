package com.ge.mysql.utils;

import org.junit.Test;

/**
 * 开启断言 assert 关键字
 *
 * @author dengzhipeng
 * @date 2019/06/28
 */
public class MysqlUtilTest {

    @Test
    public void analyzeColumnNameTest(){
        assert (MysqlUtil.analyzeColumnName("int").equals("Integer"));
    }

    @Test
    public void analyzeColumnJavaPackageTest(){
        assert (MysqlUtil.analyzeColumnJavaPackage("int").equals(Integer.class.getName()));
    }

    public static void main(String args[]) {
        boolean isOpen = false;
        // 如果开启了断言，会将isOpen的值改为true
        assert isOpen = true;
        // 打印是否开启了断言，如果为false，则没有启用断言
        System.out.println(isOpen);
    }
}

