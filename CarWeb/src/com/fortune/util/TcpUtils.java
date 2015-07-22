package com.fortune.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-12-3
 * Time: 15:15:56
 * TCP/IP的一些工具
 */
public class TcpUtils {
    private Socket sockRequest = null;//new java.net.Socket( MiddleServerAddress, MiddleServerPort );
    private BufferedReader sockIn = null;
    private InputStream socketInStream;
    private PrintStream sockOut = null;
    protected Logger logger = Logger.getLogger(getClass());
    public Socket getSockRequest() {
        return sockRequest;
    }

    public void setSockRequest(Socket sockRequest) {
        this.sockRequest = sockRequest;
    }

    public void init(Socket socket){
        try {
            socketInStream = socket.getInputStream();
            sockIn = new BufferedReader(new InputStreamReader(socketInStream));
            sockOut = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.error("无法初始化Socket!"+e.getMessage());
        }
    }
    public boolean open(String serverAddr, int serverPort) {
        try {
            sockRequest = new Socket(serverAddr, serverPort);
            init(sockRequest);
            //初始化输入输出设备
        } catch (Exception e) {
            logger.error("无法初始化"+serverAddr+":"+serverPort+","+e.getMessage());
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            //关闭连接，原来的输入输出会自动关闭
            sockRequest.close();
            return true;
        } catch (Exception e) {
            logger.error("无法关闭Socket："+e.getMessage());
            return false;
        }
    }

    public int write(String msg) {
        if (sockOut != null) {
            sockOut.print(msg);
            return msg.length();
        } else {
            return 0;
        }
    }

    public int writeln(String msg) {
        return write(msg + "\r\n");
    }

    public String read() {
        if (sockIn != null) {
            try {
                return sockIn.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
        return null;
    }

    public String readAll(String endStr) {
        StringBuffer inputStr = new StringBuffer();
        String temp;
        try {
            while ((temp = sockIn.readLine()) != null) {
                if (temp.equals(endStr)) {
                    break;
                }
                inputStr.append(temp).append("\n");
            }
            inputStr.append("\n");
        } catch (Exception e) {
            logger.error("无法读取数据："+e.getMessage());
        }
        return inputStr.toString();
    }

    public String readAll() {
        StringBuffer result = new StringBuffer();
        char[] chs = new char[128];
        int dataLength = 0;
        try {
            //boolean hasMoreData = true;
            while (true) {
                dataLength = sockIn.read(chs);
                if (dataLength > 0) {
                    result.append(chs, 0, dataLength);
                }
                if (dataLength < chs.length) {
                    //hasMoreData=false;
                    break;
                }
                chs[0] = '\0';
            }
        } catch (Exception e) {
            logger.error("无法读取数据："+e.getMessage());
        }
        return result.toString();
    }

    public int read(byte[] dataBuffer){
        try {
            return socketInStream.read(dataBuffer);
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        return 0;
    }

    public void write(byte[] dataBuffer){
        try {
            sockOut.write(dataBuffer);
        } catch (IOException e) {
            logger.error("无法写数据："+e.getMessage());
        }
    }
    public static String getHostFromUrl(String url) {
        String result = url;
        if (url == null) return null;
        url = url.toLowerCase();
        int i = url.indexOf("://");
        if (i > 0) {
            url = url.substring(i + 3);
        }
        i = url.indexOf("/");
        while (i == 0) {
            url = url.substring(i + 1);
            i = url.indexOf("/");
        }
        int k = url.indexOf(":");
        if (k > 0&&i>k) {
            result = url.substring(0, k);
        } else {
            i = url.indexOf("/");
            if (i > 0) {
                result = url.substring(0, i);
            } else {
                result = url;
            }
        }
        return result;
    }

    public static int getPortFromUrl(String url) {
        int protocolDefaultPort = 80;
        if (url == null) return 0;
        url = url.toLowerCase();
        int i = url.indexOf("://");
        if (i > 0) {
            String protocolHeader = url.substring(0, i);
            url = url.substring(i + 3);
            if (protocolHeader.startsWith("http")) {
                protocolDefaultPort = 80;
            } else if (protocolHeader.startsWith("rtsp")) {
                protocolDefaultPort = 554;
            } else if (protocolHeader.startsWith("mms")) {
                protocolDefaultPort = 1755;
            } else if (protocolHeader.startsWith("telnet")) {
                protocolDefaultPort = 23;
            } else if (protocolHeader.startsWith("ftp")) {
                protocolDefaultPort = 21;
            }
        }
        i = url.indexOf("/");
        while (i == 0) {
            url = url.substring(i + 1);
            i = url.indexOf("/");
        }
        i = url.indexOf(":");
        if (i > 0) {
            url = url.substring(i + 1);
            i = url.indexOf("/");
            String portStr = null;
            if (i > 0) {
                portStr = url.substring(0, i);
            }
            if (portStr != null) {
                return StringUtils.string2int(portStr, protocolDefaultPort);
            }
        }
        return protocolDefaultPort;
    }
    
}
