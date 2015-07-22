package com.fortune.vac;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: JA
 * Date: 13-1-31
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 * 读取用户信息同步的txt内容
 */
public class UserInfoSynchronize {
    private Logger logger = Logger.getLogger(getClass());

    public String readFile(String filePath) {
        String content = "";
        String fullContent = "";

        try {
            File infile = new File(filePath);
            if (infile.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(infile));
                BufferedReader br = new BufferedReader(isr);
                while ((content = br.readLine()) != null) {
                    fullContent += content;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }

        return fullContent;
    }
}
