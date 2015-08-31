package com.fortune.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-12-2
 * Time: 14:51:23
 * MD5 算法的Java Bean
 */
public class MD5Utils {

    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 计算文件的MD5
     *
     * @param fileName 文件的绝对路径
     * @return fileMd5
     * @throws IOException fileError
     * @throws NoSuchAlgorithmException
     *                             NoSomething
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
     * @throws NoSuchAlgorithmException
     *                             NoSomething
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

    public static List<String> getFileMD5Strings(File file,int blockSize) throws IOException, NoSuchAlgorithmException {
        FileInputStream in = new FileInputStream(file);
        List<String> result = new ArrayList<String>();
        byte[] buffer = new byte[blockSize];
        int len;
        MessageDigest allMd5 = MessageDigest.getInstance("MD5");
        while ((len = in.read(buffer)) > 0) {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(buffer, 0, len);
            allMd5.update(buffer,0,len);
            result.add(bufferToHex(messageDigest.digest()));
        }
        result.add(bufferToHex(allMd5.digest()));
        in.close();
        return result;
    }


    public static String getMD5String(byte[] inBuffer) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(inBuffer, 0, inBuffer.length);
        return bufferToHex(messageDigest.digest());
    }

    public static byte[] getMD5ByteArrayFromString(String inputStr) throws NoSuchAlgorithmException {
        return getMD5(inputStr.getBytes());
    }

    public static byte[] getMD5(byte[] inBuffer) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(inBuffer, 0, inBuffer.length);
        return messageDigest.digest();
    }

    public static String getMD5String(String inBuffer) throws NoSuchAlgorithmException {
        return getMD5String(inBuffer.getBytes());
    }

    public static String bufferToHex(byte bytes[]) {
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
    public static void main(String[] args){

        File sourceFile = new File("E:\\AD_DATA_NEW\\林荫大道\\林荫大道video-3(2分48).avi");
        try {
            long startTime = System.currentTimeMillis();
            String md5s = MD5Utils.getFileMD5String(sourceFile);
            System.out.println("'"+sourceFile.getAbsolutePath()+"' MD5=\n"+md5s);
            long endTime = System.currentTimeMillis();
            System.out.println("Total times:"+(endTime-startTime)+"ms");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}