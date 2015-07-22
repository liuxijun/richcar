package com.fortune.threeScreen.serverMessage;

import com.fortune.util.AppConfigurator;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yiyang
 * Date: 2011-4-20
 * Time: 10:21:06
 * To change this template use File | Settings | File Templates.
 */

public class ServerMessager {
    protected Logger logger = Logger.getLogger("com.fortune.server.message");
    protected String url = AppConfigurator.getInstance().getConfig("live.message.rtspInterface.url", "ep-get/xmlcommand");

    public String getParameterXml(String type, String target, Map<String, String> parameters) {
        return getParameterXml("1.0", type, target, parameters);
    }

    public String getParameterXml(String version, String type, String target, Map<String, String> parameters) {
        StringBuffer result = new StringBuffer();
        result.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
                .append("<req version=\"").append(version).append("\">\n")
                .append("  <task type=\"").append(type).append("\" target=\"")
                .append(target).append("\">\n");
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                if (value == null) value = "";
                result.append("    <param n=\"").append(key).append("\" v=\"")
                        .append(value).append("\"/>\n");
            }
        }
        result.append("  </task>\n");
        result.append("</req>");
        return result.toString();
    }
    
    public String getMessage(String ip, int port, String url, String parameter) {
        logger.debug("server:" + ip + ",port:" + port + ",url:" + url);
        logger.debug("data:" + parameter);
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        String result = postToHost("http://" + ip + ":" + port + url, parameter);
        logger.debug("Server returned:" + result);
        return result;
    }

    //解析带有中文信息的返回信息的post方法
    public String postToHost(String url, String postData) {
        String data = "";
        try {
            URL dataUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
            if (postData == null || postData.trim().equals("")) {
                con.setRequestMethod("GET");
                con.setDoOutput(false);
                con.setDoInput(true);
            } else {
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Proxy-Connection", "Keep-Alive");
                OutputStream os = con.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.write(postData.getBytes("UTF-8"));
                dos.flush();
                dos.close();
            }
            if (postData == null || postData.trim().equals("")) {
            } else {
            }
            InputStream is = con.getInputStream();
            int code = con.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                data += line;
            }
            br.close();
            if (code == HttpURLConnection.HTTP_OK) {
                logger.debug("HTTP Request is complete:" + url);
            } else {
                logger.error("HTTP Request Error:" + code + "\n" +
                        data);
            }
            //data = new String(d);
            con.disconnect();
        } catch (Exception ex) {
            logger.error("Can not Connect:" + url);
            ex.printStackTrace();
        }

        return data;
    }

    public String getPost(String postData) {
        String data = "";

        return data;
    }
}
