package com.fortune.smgw.api.common;

public class TypeConvert
{
    public static String byte2String(byte[] buf)
    {
        int offset = 0;
        int pos = offset;

        for (; offset < buf.length; offset++) {
            if (buf[offset] == 0) {
                break;
            }
        }
        if (offset > pos)
            offset--;
        else if (offset == pos) {
            return "";
        }
        int len = offset - pos + 1;
        byte[] bb = new byte[len];
        System.arraycopy(buf, pos, bb, 0, len);
        String str = new String(bb);
        return str;
    }

    public static int byte2int(byte[] b) {
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

    public static int byte2int(byte[] b, int offset) {
        return b[(offset + 3)] & 0xFF | (b[(offset + 2)] & 0xFF) << 8 | (b[(offset + 1)] & 0xFF) << 16 | (b[offset] & 0xFF) << 24;
    }

    public static long byte2long(byte[] b, int offset) {
        return b[(offset + 7)] & 0xFF | (b[(offset + 6)] & 0xFF) << 8 | (b[(offset + 5)] & 0xFF) << 16 |
                (b[(offset + 4)] & 0xFF) << 24 | (b[(offset + 3)] & 0xFF) << 32 | (b[(offset + 2)] & 0xFF) << 40 |
                (b[(offset + 1)] & 0xFF) << 48 | b[offset] << 56;
    }

    public static long byte2long(byte[] b) {
        return b[7] & 0xFF | (b[6] & 0xFF) << 8 | (b[5] & 0xFF) << 16 | (b[4] & 0xFF) << 24 | (b[3] & 0xFF) << 32 |
                (b[2] & 0xFF) << 40 | (b[1] & 0xFF) << 48 | b[0] << 56;
    }

    public static short byte2short(byte[] b, int offset) {
        return (short)(b[(offset + 1)] & 0xFF | (b[offset] & 0xFF) << 8);
    }

    public static short byte2short(byte[] b) {
        return (short)(b[1] & 0xFF | (b[0] & 0xFF) << 8);
    }

    public static void int2byte(int n, byte[] buf, int offset) {
        buf[offset] = ((byte)(n >> 24));
        buf[(offset + 1)] = ((byte)(n >> 16));
        buf[(offset + 2)] = ((byte)(n >> 8));
        buf[(offset + 3)] = ((byte)n);
    }

    public static byte[] int2byte(int n) {
        byte[] b = new byte[4];
        b[0] = ((byte)(n >> 24));
        b[1] = ((byte)(n >> 16));
        b[2] = ((byte)(n >> 8));
        b[3] = ((byte)n);
        return b;
    }

    public static void long2byte(long n, byte[] buf, int offset) {
        buf[offset] = ((byte)(int)(n >> 56));
        buf[(offset + 1)] = ((byte)(int)(n >> 48));
        buf[(offset + 2)] = ((byte)(int)(n >> 40));
        buf[(offset + 3)] = ((byte)(int)(n >> 32));
        buf[(offset + 4)] = ((byte)(int)(n >> 24));
        buf[(offset + 5)] = ((byte)(int)(n >> 16));
        buf[(offset + 6)] = ((byte)(int)(n >> 8));
        buf[(offset + 7)] = ((byte)(int)n);
    }

    public static byte[] long2byte(long n) {
        byte[] b = new byte[8];

        b[0] = ((byte)(int)(n >> 56));
        b[1] = ((byte)(int)(n >> 48));
        b[2] = ((byte)(int)(n >> 40));
        b[3] = ((byte)(int)(n >> 32));
        b[4] = ((byte)(int)(n >> 24));
        b[5] = ((byte)(int)(n >> 16));
        b[6] = ((byte)(int)(n >> 8));
        b[7] = ((byte)(int)n);
        return b;
    }

    public static void short2byte(int n, byte[] buf, int offset) {
        buf[offset] = ((byte)(n >> 8));
        buf[(offset + 1)] = ((byte)n);
    }

    public static byte[] short2byte(int n) {
        byte[] b = new byte[2];
        b[0] = ((byte)(n >> 8));
        b[1] = ((byte)n);
        return b;
    }

    public static byte[] string2byte(String n, int index) {
        byte[] temp = new byte[index];
        for (int i = 0; i < index; i++) {
            temp[i] = 0;
        }
        if ((n != null) && (!n.equals(""))) {
            System.arraycopy(n.getBytes(), 0, temp, 0, n.getBytes().length);
        }
        return temp;
    }

    public static String byte2HexString(byte[] sour) {
        String str = new String();
        str = "0x" + str;
        String temp = null;
        for (int i = 0; i < sour.length; i++) {
            temp = Integer.toHexString(sour[i]);
            if (temp.length() < 2) {
                temp = "0" + temp;
            }

            if (sour[i] < 0) {
                temp = temp.substring(6, 8);
            }
            str = str + temp;
        }
        return str.trim();
    }
}