package com.fortune.util;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {
	public static String httpRequest(String url,Map<String, String> map){
		String result = url;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			postData.add(new BasicNameValuePair(entry.getKey(),
					entry.getValue()));
		}
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					postData, HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response=null;
			try {
				response = httpClient.execute(post);
				HttpEntity httpEntity = response.getEntity();
				InputStream is = httpEntity.getContent();
				StringBuffer sb = new StringBuffer();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line;
				try {
					while((line=br.readLine())!=null){
						sb.append(line);
					}
					result = sb.toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
    public static String postToHost(String url, String postData,String strEncoding) {
        String data = "";
        try {
            URL dataUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
            if(postData==null || postData.trim().equals("")){
                con.setRequestMethod("GET");
                con.setDoOutput(false);
                con.setDoInput(true);
            }else{
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Proxy-Connection", "Keep-Alive");
                OutputStream os = con.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                byte[] dataBuffer;
                if(strEncoding!=null){
                    dataBuffer = postData.getBytes(strEncoding);
                }else{
                    dataBuffer = postData.getBytes();
                }
                dos.write(dataBuffer);
                dos.flush();
                dos.close();
            }

            InputStream is = con.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            //System.out.println("POST返回区域大小："+dis.available());
            int code = con.getResponseCode();
            byte d[] = new byte[1024*10];
            while(true){
                int i= dis.read(d);
                if(i<=0){
                    break;
                }
                data+=new String(d,0,i);
            }
            if(code== HttpURLConnection.HTTP_OK){
                //logger.debug("HTTP请求完成："+url);
            }else{
                Log.e("HttpUtils", "HTTP请求发生错误：" + code + "\n" +
                        data);
            }
            //data = new String(d);
            con.disconnect();
        } catch (Exception ex) {
        	Log.e("HttpUtils", "无法连接：" + url + "," + ex.getMessage());
        	ex.printStackTrace();
            //ex.printStackTrace();
        }
        return data;
    }
}
