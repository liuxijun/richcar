package com.fortune.util;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-7-10
 * Time: 下午5:22
 * HTTPS访问的助手类
 */

import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.*;


public class HttpsUtils {
    public static Logger logger = Logger.getLogger("com.fortune.util.HttpsUtils");
    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    public static String get(String host,int port,String url,String content) throws Exception {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                new java.security.SecureRandom());
        SSLSocketFactory factory = sc.getSocketFactory();
        Socket socket = factory.createSocket(host, port);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        String outMsg;
        if(content==null||"".equals(content.trim())){
            outMsg = "GET /interface/queryUser.jsp HTTP/1.1\n" +
                    "Accept:image/gif,image/x-xbitmap,image/jpeg,application/x-shockwave-flash,application/vnd.ms-excel,application/vnd.ms-powerpoint,application/msword,*/*\n" +
                    "Accept-Language:zh-cn\n" +
                    "Accept-Encoding:gzip,deflate\n" +
                    "User-Agent:Mozilla/4.0(compatible;MSIE6.0;Windows NT 5.0)\n" +
                    "Host:192.168.1.28\n" +
                    "Connection:Keep-Alive\n" +
                    "\n";
        }else{
/*
            StringBuilder outSb = new StringBuilder();
            outSb.append("GET ").append(url).append(" HTTP/1.1\n");
            outSb.append(content);
            outSb.append("\n");
            outMsg = outSb.toString();
*/
            outMsg = content;
        }
        logger.debug("即将发送的消息：\n"+outMsg);
        out.write(outMsg);
        out.flush();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            logger.debug("收到消息："+line);
            sb.append(line).append("\n");
        }
        out.close();
        in.close();
        return sb.toString();
    }
    public static String get(String host,int port,String url,Map<String,String> headers) throws Exception {
        StringBuilder outSb = new StringBuilder();
        outSb.append("GET ").append(url).append(" HTTP/1.1\n");
        if(headers==null){
            headers = new HashMap<String,String>();
        }
        for(String header:headers.keySet()){
            outSb.append(header).append(":").append(headers.get(header)).append("\n");
        }
        outSb.append("\n\n");
        String outMsg = outSb.toString();
        if(outMsg!=null){
            outMsg = "GET /interface/queryUser.jsp HTTP/1.1\n" +
                    "Accept:image/gif,image/x-xbitmap,image/jpeg,application/x-shockwave-flash,application/vnd.ms-excel,application/vnd.ms-powerpoint,application/msword,*/*\n" +
                    "Accept-Language:zh-cn\n" +
                    "Accept-Encoding:gzip,deflate\n" +
                    "User-Agent:Mozilla/4.0(compatible;MSIE6.0;Windows NT 5.0)\n" +
                    "Host:192.168.1.28\n" +
                    "Connection:Keep-Alive\n" +
                    "\n";
        }
        return get(host,port,url,outMsg);
    }
    public static String post(String url,String data,Map<String,String> headers) throws Exception {
        InputStream in = null;
        OutputStream out = null;
/*
        byte[] buffer = new byte[4096];
*/
        String str_return = "";
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());
            URL console = new URL(url);

            HttpsURLConnection conn = (HttpsURLConnection) console
                    .openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setRequestMethod("GET");

            Map<String,List<String>> headerFields = conn.getHeaderFields();
            for(String key:headerFields.keySet()){
                List<String> header = headerFields.get(key);
                for(String h:header){
                    logger.debug(key+"="+h);
                }
            }
            for(String key:headers.keySet()){
                //conn.setRequestProperty(key,headers.get(key));
            }
            conn.connect();
            InputStream is = conn.getInputStream();
            if(data!=null&&!"".equals(data.trim())){
                out = conn.getOutputStream();
                out.write(data.getBytes());
            }
            DataInputStream indata = new DataInputStream(is);
            String ret = "";

            while (ret != null) {
                ret = indata.readLine();
                if (ret != null && !ret.trim().equals("")) {
                    str_return = str_return
                            + new String(ret.getBytes("ISO-8859-1"), "GBK")+"\r\n";
                }
            }
            conn.disconnect();
        } catch (ConnectException e) {
            logger.error("ConnectException:" + e.getMessage());
            throw e;

        } catch (IOException e) {
            logger.error("IOException:"+e.getMessage());
            throw e;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                logger.error("无法关闭输入流："+e.getMessage());
            }
            try {
                out.close();
            } catch (Exception e) {
                logger.error("无法关闭输出流："+e.getMessage());
            }
        }
        return str_return;
    }
    public static void main(String[] args){
        Map<String,String> headers = new HashMap<String,String>();
/*
        headers.put("Authorization","appKey=\"88329731e0ed75daa03ede1e30427cddfa4ch3ci\",token=\"keisda73nc6hn234k5kj6j78k1j34n5n4n6n7l9f\",authCode=\"wdrfgrfd34td75daa03ede1e30427eujbt6qsxtg\"");
        headers.put("Content-Type","application/json; charset=\"utf-8\"");
        headers.put("Accecp","application/json;");
//*/
        //headers.put("Host","192.168.1.28");
        //headers.put("Connection","Keep_Alive");
        //headers.put("Accecp","application/json,*/*");
        //headers.put("User-Agent","Mozilla/4.0(compatible;MSIE6.0)");
        try {
            System.out.println("返回结果：" + get("192.168.1.28", 443, "/interface/queryUser.jsp", ""));
            //System.out.println("返回结果：" + post("https://192.168.1.28/interface/queryUser.jsp",null, headers));
        } catch (Exception e) {
            logger.error("发生异常："+e.getMessage());
            e.printStackTrace();
        }
    }
}
