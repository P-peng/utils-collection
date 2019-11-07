package com.ge.String.utils;

/**
 * @author dengzhipeng
 * @date 2019/06/29
 */
public class StringUtil {

    /**
     * 下划线转驼峰法
     * @param args
     * @return
     */
    public static String underlineToHump(String args){
        StringBuilder result = new StringBuilder();
        if (!args.contains("_")){
            return args;
        }
        String array[] = args.split("_");
        for(String s : array){
            if(result.length() == 0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
}
