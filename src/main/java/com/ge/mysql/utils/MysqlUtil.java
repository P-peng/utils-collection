package com.ge.mysql.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于mysql的字段类型向java对应转换工具类。
 * 示例：调用 analyzeColumnName("int") return Integer   调用 analyzeColumnJavaPackage("int") return java.lang.Integer
 *
 * @author dengzhipeng
 * @date 2019/06/28
 */
public class MysqlUtil {
    private static final Map<String, JavaColumn> TYPE_MAP = new HashMap<String, JavaColumn>(){{
        put("int", new JavaColumn("Integer", Integer.class.getName()));
        put("char", new JavaColumn("String", String.class.getName()));
        put("varchar", new JavaColumn("String", String.class.getName()));
        put("bit", new JavaColumn("Byte", Byte.class.getName()));
        put("bigint", new JavaColumn("BigInteger", BigInteger.class.getName()));
        put("float", new JavaColumn("Float", Float.class.getName()));
        put("double",  new JavaColumn("Double", Double.class.getName()));
        put("decimal", new JavaColumn("BigDecimal", BigDecimal.class.getName()));
        put("datetime", new JavaColumn("Date", Date.class.getName()));
        put("date",  new JavaColumn("Date", Date.class.getName()));
    }};

    /**
     * 传入sql字段类型，返回java字段名字
     * @param sqlType
     * @return
     */
    public static String analyzeColumnName(String sqlType){
        return TYPE_MAP.get(sqlType).getName();
    }

    /**
     * 传入sql字段类型，返回java字段所在的包
     * @param sqlType
     * @return
     */
    public static String analyzeColumnJavaPackage(String sqlType){
        return TYPE_MAP.get(sqlType).getJavaPackage();
    }

}


class JavaColumn{
    private String name;
    private String javaPackage;

    public JavaColumn(){}

    public JavaColumn(String name, String javaPackage) {
        this.name = name;
        this.javaPackage = javaPackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }
}