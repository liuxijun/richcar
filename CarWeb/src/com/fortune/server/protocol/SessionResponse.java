package com.fortune.server.protocol;

import com.fortune.util.AppConfigurator;
import com.fortune.util.StringUtils;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 14-1-22
 * Time: ÉÏÎç9:45
 *
 */
public class SessionResponse implements Serializable {
    private int resultCode;
    private long contentLength;
    private long startTime;
    private long stopTime;
    private List<StreamData> resultData;
    private OutputStream outStream;
    private String contentType;

    public SessionResponse() {
        resultData = new ArrayList<StreamData>();
    }

    public SessionResponse(int resultCode) {
        this.resultCode = resultCode;
    }
    public SessionResponse(int resultCode,String message){
        this(resultCode,message,null);
    }
    public SessionResponse(int resultCode,String message,String charset){
        if(charset==null||"".equals(charset)){
            AppConfigurator config = AppConfigurator.getInstance();
            charset = config.getConfig("p2p.httpEncoding","GBK");
        }
        resultData = new ArrayList<StreamData>(1);
        this.resultCode = resultCode;
        if(message!=null){
            try {
                resultData.add(new StreamData(message.getBytes(charset)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            contentLength = resultData.get(0).getLength();
        }
        contentType = "text/xml";
    }
    public SessionResponse(int resultCode, long contentLength, byte[] resultData, OutputStream outStream) {
        this.resultCode = resultCode;
        this.contentLength = contentLength;
        this.resultData = new ArrayList<StreamData>(1);
        this.resultData.add(new StreamData(resultData));
        this.outStream = outStream;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public long getContentLength() {
        contentLength = 0;
        for(StreamData data:resultData){
           contentLength += data.getLength();
        }
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public List<StreamData> getResultData() {
        return resultData;
    }

    public void setResultData(List<StreamData> resultData) {
        this.resultData = resultData;
    }

    public OutputStream getOutStream() {
        return outStream;
    }

    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void addResultData(int length,byte[] data){
        resultData.add(new StreamData(length,data));
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public String toString(){
        return "{\n  code:"+resultCode+",\n  length:"+getContentLength()+",\n  count:"+resultData.size()+
                ",\n  contentType:'" + contentType+
                "',\n  startTime:'" + StringUtils.date2string(startTime)+
                "',\n  stopTime:'" + StringUtils.date2string(stopTime)+
                "',\n  duration:" +(stopTime-startTime)+
                "\n}";
    }
}
