package com.ge.properties.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 用于读取properties文件的属性
 * @author dengzhipeng
 * @date 2019/06/28
 */
public class ReadPropertiesUtil {
    /**
     * 读取指定properties，指定属性
     * @param path
     * @param name
     * @return
     */
    public static String readByName(String path, String name) throws IOException {
        Properties pro = new Properties();
        FileInputStream in = new FileInputStream(path);
        pro.load(in);
        in.close();
        return pro.getProperty(name);
    }
}
