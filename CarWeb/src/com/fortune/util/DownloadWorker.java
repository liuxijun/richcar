package com.fortune.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-5
 * Time: 13:31:26
 * ���������߳�
 */
public class DownloadWorker extends Thread {
    protected Log log = LogFactory.getLog(this.getClass());
    private String sURL;
    private Map<String, String> parameters;
    private int number = 0;

    public DownloadWorker(String sURL, Map<String, String> parameters) {
        this.sURL = sURL;
        this.parameters = parameters;
    }

    public void run() {
        //final String fUrl = sURL;
        //final Map<String, String> fParams = parameters;
        HttpURLConnection httpURLConnection;

        if (parameters != null) {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key : parameters.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(parameters.get(key));
                i++;
            }

            sURL += param;

        }
        boolean downloadFailed = true;
        while (downloadFailed) {
            log.debug("���Ի�ȡ���ݣ�"+sURL);
            httpURLConnection = HttpUtil.getHttpURLConnectionInstance(sURL);
            try {
                httpURLConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            int responseCode = 0;
            String responseMessage;
            try {
                responseCode = httpURLConnection.getResponseCode();
                responseMessage = httpURLConnection.getResponseMessage();
                if("".equals(responseMessage)){
                    
                }
            } catch (IOException e) {
                log.error("���ع����з����쳣�����ӣ�"+sURL+",����"+e);
            }

            if (responseCode != 200) {
                downloadFailed = true;
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                number++;
                if (number <= 5) {
                } else {
                    downloadFailed = false;
                    log.error("���Դ����������׼���˳���"+number);
                    if(!downloadFailed){//ֻ��Ϊ��ȥ������
                    }
                    break;
                }
            } else {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 64);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    byte[] buf = new byte[1024 * 64];
                    int n;
                    while ((n = inputStream.read(buf)) > 0) {
                        baos.write(buf, 0, n);
                    }
                    inputStream.close();
                    //byte[] responseContent = baos.toByteArray();
                    httpURLConnection.disconnect();
                    downloadFailed = false;
                } catch (IOException e) {
                    log.error("�������ݷ�������"+sURL+",����"+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
