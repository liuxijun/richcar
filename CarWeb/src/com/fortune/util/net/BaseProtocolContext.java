package com.fortune.util.net;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-1-31
 * Time: 12:39:20
 */
public class BaseProtocolContext {
    Logger logger = Logger.getLogger("cdn.gslb"); 
    private String command;
    private String requestUrl;
    private String queryString;
    private String protocol;
    private String version;
    private String remoteIp;
    
    protected Map<String, String> headers = new HashMap<String, String>();
    private boolean headerProcessed = false;
    private boolean responseWritten= false;
    private InputStream is;
    private OutputStream os;
    private int returnCode;
    private long contentLength;
    private String returnMessage;
    private Map<String,String> responseHeaders = new HashMap<String,String>();
    public BaseProtocolContext() {
         responseWritten = false;
    }

    public String getParameter(String line, String splitor, int index) {
        if (line != null) {
            String[] params = line.split(splitor);
            if (params != null) {
                if (index < params.length) {
                    return params[index];
                }
            }
        }
        return null;
    }

    public void processHeaders() {
        String line;
        try {
            BufferedReader sockIn = new BufferedReader(new InputStreamReader(is));
            String firstLine = sockIn.readLine();
            int i = 0;
            if (firstLine != null) {
                command = getParameter(firstLine, " ", 0);
                queryString = getParameter(firstLine, " ", 1);
                String protocolVersion = getParameter(firstLine, " ", 2);
                if (protocolVersion != null) {
                    protocol = getParameter(protocolVersion, "/", 0);
                    version = getParameter(protocolVersion, "/", 1);
                }
            }
            while (((line = sockIn.readLine()) != null)) {
                if ("".equals(line)) {
                    break;
                }
                int pos = line.indexOf(":");
                if (pos > 0) {
                    String header = line.substring(0, pos);
                    String value = line.substring(pos + 1);
                    headers.put(header.toUpperCase(), value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        headerProcessed = true;
    }

    public InputStream getRequestBody() {
        if (!headerProcessed) {
            processHeaders();
        }
        return is;
    }

    public OutputStream getResponseBody()throws Exception {
        if(!responseWritten){
            throw new Exception("尚未设置输出信息！");
        }
        return os;
    }

    public void onConnected(InputStream inputStream, OutputStream outputStream) {
        is = inputStream;
        os = outputStream;
        processHeaders();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String headerName){
        return headers.get(headerName);
    }

    public void setHeader(String headerName,String headerValue){
        headers.put(headerName,headerValue);
    }

    public String getResponseHeader(String headerName){
        return responseHeaders.get(headerName);
    }

    public boolean setResponseHeader(String headerName,String headerValue){
        if(responseWritten)return false;
        responseHeaders.put(headerName,headerValue);
        return true;
    }

    public boolean setResponseHeaders(int returnCode,long contentLength){
        if(responseWritten){
            return false;
        }

        this.contentLength = contentLength;
        if(contentLength>0){
            setResponseHeader("Content-length",""+contentLength);
        }
        this.returnCode = returnCode;
        if(returnMessage==null){
            switch(returnCode){
                case 200:
                    returnMessage = "OK";
                    break;
                case 302:
                    returnMessage = "Temp Removed";
                    break;
                case 404:
                    returnMessage = "File Not Found";
                    break;
                case 500:
                    returnMessage = "Internal Error";
                    break;
                case 505:
                    returnMessage = "Forbidden";
                    break;
                default:
                    returnMessage = "Unknown";
                    break;
            }
        }
        
        String headerMessage = getResponseHeadersStr();
        try {
/*
            logger.debug("headers:\r\n"+headerMessage);
            logger.debug("message.string.length="+headerMessage.length());
            logger.debug("message.bytes.length="+headerMessage.getBytes().length);
*/
            os.write(headerMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        responseWritten = true;
        return true;
    }

    public String getResponseHeadersStr(){
        StringBuffer result = new StringBuffer();
        result.append(protocol).append("/").append(version).append(" ").append(returnCode).append(" ").append(returnMessage).append("\r\n");
        result.append(getHeadersStr(responseHeaders));
        return result.toString();
    }

    public String getRequestHeadersStr(){
        StringBuffer result = new StringBuffer();
        result.append(protocol).append("/").append(version).append(" ").append(returnCode).append(" ").append(returnMessage).append("\r\n");
        result.append(getHeadersStr(headers));
        return result.toString();
    }
    public String getHeadersStr(Map<String,String> headerSet) {
        StringBuffer result = new StringBuffer();
/*
        if(returnCode==302){
            String key="Location";
            result.append(key).append(": ").append(responseHeaders.get(key)).append("\r\n");
        }else{
            for (String key : responseHeaders.keySet()) {
                result.append(key).append(": ").append(responseHeaders.get(key)).append("\r\n");
            }
        }
*/
        for (String key : headerSet.keySet()) {
            result.append(key).append(": ").append(headerSet.get(key)).append("\r\n");
        }
        result.append("\r\n");
        return result.toString();
    }

    public boolean isResponseWritten() {
        return responseWritten;
    }

    public void setResponseWritten(boolean responseWritten) {
        this.responseWritten = responseWritten;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }
}
