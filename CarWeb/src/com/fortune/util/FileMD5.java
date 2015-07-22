package com.fortune.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-12-6
 * Time: 21:05:47
 * MD5
 */

/**
 * 计算文件的MD5
 */
public class FileMD5 {
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 计算文件的MD5
     *
     * @param fileName 文件的绝对路径
     * @return fileMd5
     * @throws IOException fileError
     * @throws NoSuchAlgorithmException  NoSomething
     */
    public static String getFileMD5String(String fileName) throws IOException, NoSuchAlgorithmException {
        File f = new File(fileName);
        return getFileMD5String(f);
    }

    /**
     * 计算文件的MD5，重载方法
     *
     * @param file 文件对象
     * @return fileMD5
     * @throws IOException fileNotFound
     * @throws NoSuchAlgorithmException  NoSomething
     */
    public static String getFileMD5String(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[1024 * 1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            messageDigest.update(buffer, 0, len);
        }
        in.close();
        return bufferToHex(messageDigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

}