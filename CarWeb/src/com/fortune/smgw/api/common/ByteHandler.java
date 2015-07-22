package com.fortune.smgw.api.common;

import java.nio.ByteBuffer;

public class ByteHandler
{
    public static byte[] getNullByteArray1()
    {
        return getNullByteArray(1);
    }

    public static byte[] getNullByteArray5() {
        return getNullByteArray(5);
    }

    public static byte[] getNullByteArray6() {
        return getNullByteArray(6);
    }

    public static byte[] getNullByteArray8() {
        return getNullByteArray(8);
    }

    public static byte[] getNullByteArray10() {
        return getNullByteArray(10);
    }

    public static byte[] getNullByteArray16() {
        return getNullByteArray(16);
    }

    public static byte[] getNullByteArray21() {
        return getNullByteArray(21);
    }

    private static byte[] getNullByteArray(int size) {
        byte[] tmp = new byte[size];
        return tmp;
    }

    public static String readString(ByteBuffer buffer, int size)
    {
        byte[] tmp = getNullByteArray(size);
        buffer.get(tmp, 0, tmp.length);
        return TypeConvert.byte2String(tmp);
    }

    public static int readUIntByOneByte(ByteBuffer buffer)
    {
        byte tmp = buffer.get();
        return tmp & 0xFF;
    }
}