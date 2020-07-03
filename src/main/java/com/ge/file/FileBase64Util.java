package com.ge.file;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 对文件base64编码/解码
 * 依赖包: commons-io:commons-io:2.6  commons-codec:commons-codec:1.13
 *
 * @author dengzhipeng
 * @date 2019/11/08
 */
public class FileBase64Util {

    /**
     * 对文件进行 base64编码
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String encodeFile(File file) throws IOException {
        byte[] readFileToByteArray = FileUtils.readFileToByteArray(file);
        return Base64.encodeBase64String(readFileToByteArray);
    }

    /**
     * 对文件路径进行 base64编码
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String encodeFile(String filePath) throws IOException {
        return encodeFile(new File(filePath));
    }

    /**
     * 将 base64编码值 写成文件
     *
     * @param codes
     * @param file
     * @throws IOException
     */
    public static void decodeFile(String codes, File file) throws IOException {
        byte[] decodeBase64 = Base64.decodeBase64(codes);
        FileUtils.writeByteArrayToFile(file, decodeBase64);
    }

    /**
     * 将 base64编码值 以路径方式写入文件
     *
     * @param codes
     * @param filePath
     * @throws IOException
     */
    public static void decodeFile(String codes, String filePath) throws IOException {
        decodeFile(codes, new File(filePath));
    }


    /**
     * 测试用例
     *
     * @param args
     */
    public static void main(String[] args) {
        File file = new File("D:/images/view.png");
        try {
            String encode = FileBase64Util.encodeFile(file);
            System.out.println(encode);

            File file2 = new File("D:/images/vieweDecode.png");
            FileBase64Util.decodeFile(encode, file2);

            String encode2 = encodeFile("D:/images/view.png");
            System.out.println(encode2);

            FileBase64Util.decodeFile(encode2, "D:/images/vieweDecode2.png");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
