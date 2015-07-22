package com.fortune.util;

import java.security.MessageDigest;

/**
 * Created by IntelliJ IDEA.
 * User: yiyang
 * Date: 2011-4-29
 * Time: 16:13:37
 * To change this template use File | Settings | File Templates.
 */
public class SHA1Util {
    public static String HashBase64(String str)
    {
      String ret="";
      try {
        //HashÀ„∑®
       MessageDigest sha = MessageDigest.getInstance("SHA-1");
       sha.update(str.getBytes());
       ret=new sun.misc.BASE64Encoder().encode(sha.digest());
     }
     catch (Exception e) {
       System.out.print(e.getMessage());
     }
      return ret;
    }
}
