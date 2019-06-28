package com.ge.properties.utils;

import org.junit.Test;

import java.io.IOException;

/**
 * 用于读取properties文件的属性
 * @author dengzhipeng
 * @date 2019/06/28
 */
public class ReadPropertiesUtilTest {


    @Test
    public void readByName() throws IOException {
        assert (ReadPropertiesUtil.readByName("./src/main/resources/properties/demo.properties", "author").equals("dengzhipeng"));
    }
}
