package com.fortune.util;




import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import javax.crypto.spec.SecretKeySpec;

/**
 * Created by IntelliJ IDEA.
 * User: yiyang
 * Date: 2011-4-29
 * Time: 16:28:50
 * To change this template use File | Settings | File Templates.
 */
public class ThreeDesUtil {
    private static final String Algorithm = "DESede/ECB/PKCS5Padding"; //��������㷨,���� DES,DESede,Blowfish
//    private static final String Algorithm = "DESede"; //��������㷨,���� DES,DESede,Blowfish
    private static final String charSet = "UTF-8";

    //keybyteΪ������Կ������Ϊ24�ֽ�
    //srcΪ�����ܵ����ݻ�������Դ��
    public static byte[] encryptMode(byte[] keybyte,byte[] src){
        try {
        //������Կ
            SecretKey deskey = new SecretKeySpec(keybyte,"DESede");
            //����
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);//�ڵ�һ����ļ��ܻ����
        } catch (java.security.NoSuchAlgorithmException e1) {
        e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
        e2.printStackTrace();
        }catch(Exception e3){
        e3.printStackTrace();
        }
        return null;
    }

    //keybyteΪ������Կ������Ϊ24�ֽ�
    //srcΪ���ܺ�Ļ�����

    public static byte[] decryptMode(byte[] keybyte,byte[] src){
        try {
            //������Կ
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
            //����
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
        e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
        e2.printStackTrace();
        }catch(Exception e3){
        e3.printStackTrace();
        }
        return null;
    }

    //ת����ʮ�������ַ���
    public static String byte2Hex(byte[] b){
        String hs="";
        String stmp="";
        for(int n=0; n<b.length; n++){
            stmp = (Integer.toHexString(b[n]& 0XFF));
            if(stmp.length()==1){
                hs = hs + "0" + stmp;
            }else{
                hs = hs + stmp;
            }
            if(n<b.length-1)hs=hs;
        }
        return hs.toUpperCase();
    }
}
