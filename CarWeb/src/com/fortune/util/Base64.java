package com.fortune.util;

/**
 * Created by IntelliJ IDEA.
 * User: yiyang
 * Date: 2011-5-4
 * Time: 10:21:06
 * To change this template use File | Settings | File Templates.
 */
public class Base64 {
    static public byte[] encode(byte[] data){
        byte[] out = new byte[((data.length + 2) / 3) * 4];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4){
            boolean quad = false;
            boolean trip = false;
            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length){
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length){
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return out;
    }

    static private byte[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".getBytes();
    static private byte[] codes = new byte[256];
    static {
        for (int i = 0; i < 256; i++)
        codes[i] = -1;
        for (int i = 'A'; i <= 'Z'; i++)
        codes[i] = (byte) (i - 'A');
        for (int i = 'a'; i <= 'z'; i++)
        codes[i] = (byte) (26 + i - 'a');
        for (int i = '0'; i <= '9'; i++)
        codes[i] = (byte) (52 + i - '0');
        codes['+'] = 62;
        codes['/'] = 63;
    }

    
}
