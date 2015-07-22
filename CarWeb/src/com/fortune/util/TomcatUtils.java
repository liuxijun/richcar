package com.fortune.util;



import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12-10-29
 * Time: ÏÂÎç1:37

 */
public class TomcatUtils {
    public static Logger logger  = Logger.getLogger("tomcatUtils");

    public void startUp(){

    }

    public void shutDown(){

    }

    public static void reBoot(){
    String cmd = "cmd /c start D:/fortune/rms/ROOT/reBoot.bat";
    logger.log(Level.INFO,"cmd bat:"+cmd.toString());
    try {
        Process ps = Runtime.getRuntime().exec(cmd);

    } catch(IOException ioe) {
        ioe.printStackTrace();
    }
}

    public static void main(String args[]){
        reBoot();
    }
}
