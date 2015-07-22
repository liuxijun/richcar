package com.fortune.util.net.m3u8;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 14-1-21
 * Time: обнГ5:05
 *
 */
public class M3U8Parser {
   public static Hls parser(String m3u8Content){
       if(m3u8Content==null||"".equals(m3u8Content.trim())){
           return null;
       }
       return new Hls(m3u8Content);

   }
}
