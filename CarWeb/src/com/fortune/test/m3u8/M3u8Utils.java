package com.fortune.test.m3u8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-12-10
 * Time: 8:54:17
 * 直播结束后生成播放列表
 */
public class M3u8Utils {
    public void createPlayList(int startNo,int stopNo,String dirName,String fileName){
        File file = new File(fileName);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("#EXTM3U\r\n" +
                    "#EXT-X-TARGETDURATION:10\r\n" +
                    "#EXT-X-MEDIA-SEQUENCE:0\r\n");
            for(int i=startNo;i<=stopNo;i++){
                String fileNo = ""+i;
                while(fileNo.length()<4){
                    fileNo = "0"+fileNo;
                }
                fw.write("#EXTINF:10,\r\n" +
                        dirName+"/"+fileNo+".ts\r\n");
            }
            fw.write("#EXT-X-ENDLIST");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args){
        M3u8Utils utils = new M3u8Utils();
        utils.createPlayList(42,1863,"shijiazhuang20111220","c:/iphone/sjz20111221.m3u8");
    }
}
