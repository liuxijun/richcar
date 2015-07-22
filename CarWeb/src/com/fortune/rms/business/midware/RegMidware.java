package com.fortune.rms.business.midware;

import com.fortune.util.AppConfigurator;
import com.fortune.util.MD5Utils;
import com.fortune.util.StringUtils;
import com.fortune.util.TcpUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-5-31
 * Time: 17:21:55
 * ��֤�м��
 */
public class RegMidware {
        private Logger logger = Logger.getLogger(this.getClass());
	    public  String regUrl(String mediaURL,String MiddleServerAddress, int MiddleServerPort,boolean isMiddleware){
	        if(isMiddleware){
	           // return SecureLogin.registUrl(url);
	        	String result;
	            try {
	                //��ȡ������Ϣ

	                java.net.Socket socketRequest = new java.net.Socket(MiddleServerAddress, MiddleServerPort);
	                //׼����ȡ����
	                BufferedReader bufferedReader = null;
	                PrintStream printStream = null;
	                try {
	                    //��ʼ����������豸
	                    bufferedReader = new BufferedReader(new InputStreamReader(socketRequest.getInputStream()));
	                    printStream = new PrintStream(socketRequest.getOutputStream());
	                    //���ָ�ע��һ��SID
	                    printStream.println("regsid " + mediaURL);
	                    //��ȡ������Ϣ
	                    result = bufferedReader.readLine();
	                } catch (Exception e) {   //�������������ֱ�ӷ���ԭ����mms����
	                    result = mediaURL + "&key=0&dat=" + (System.currentTimeMillis() / 1000);
	                }
	                try {
	                    if (bufferedReader != null) {
	                        bufferedReader.close();
	                        //bufferedReader = null;
	                    }
	                    if (printStream != null) {
	                        printStream.close();
	                        //printStream = null;
	                    }
	                    //�ر����ӣ�ԭ��������������Զ��ر�
	                    socketRequest.close();
	                } catch (Exception e) {
                        logger.error(e);
	                }
	            } catch (Exception e) {
	                //�������������ֱ�ӷ���ԭ����mms����
	                result = mediaURL + "&key=0&dat=" + (System.currentTimeMillis() / 1000);
	                //��ӡ������Ϣ
	                System.out.println(new java.util.Date() + "Error when regsid:" + e);
	                //��ӡ�����ջ
	                e.printStackTrace();
                    logger.error(e);
	            }
	            return result;
	        }else{
	            return mediaURL;
	        }
	    }

    public String regUrl(String url,String clientIp){
        String result = url;
        String tokenPwd = AppConfigurator.getInstance().getConfig("cdn.tokenPassword","fortuneRMS");
        String clearUrl = StringUtils.getClearURL(result);
        while(clearUrl.startsWith("/")&&clearUrl.length()>1){
           clearUrl = clearUrl.substring(1);
        }
        String checkURL = clearUrl+"&clientip="+clientIp+tokenPwd;
        try {
            String checkToken = MD5Utils.getMD5String(checkURL);
            result = result+"&encrypt="+checkToken;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("JDK���ܴ���û��MD5���㷨��");
        }
        return result;
    }

    @SuppressWarnings("unused")
    public  String regUrlV1(String url, String clientIp){
        String midwareIp = AppConfigurator.getInstance().getConfig("midware.ip","");
        int midwarePort = AppConfigurator.getInstance().getIntConfig("midware.port",0);
        return getGslbUrl(regUrl(url,midwareIp,midwarePort,true),clientIp);
    }

    public String getGslbUrl(String requestUrl,String remoteAddr){
        String result = null;
        try {
            if(requestUrl!=null){

                //requestUrl =java.net.URLDecoder.decode(requestUrl,"UTF-8");
                //requestUrl =java.net.URLEncoder.encode(requestUrl,"gbk");
                //System.out.println("requestUrl="+requestUrl);
                String srcIp = TcpUtils.getHostFromUrl(requestUrl);

                TcpUtils client = new TcpUtils();
                if(client.open(srcIp,554)){
                    if(requestUrl.indexOf("?")>0){
                        requestUrl +="&";
                    }else{
                        requestUrl+="?";
                    }
                    requestUrl+="clientIpForCdn="+remoteAddr;
                    String sendMsg = "DESCRIBE " +requestUrl+
                            " RTSP/1.0\n" +
                            "User-Agent: WMPlayer/9.0.0.2991 guid/3300AD50-2C39-46C0-AE0A-5F8407B9BFEA\n" +
                            "Accept: application/sdp\n" +
                            "Accept-Charset: UTF-8, *;q=0.1\n" +
                            "X-Accept-Authentication: Negotiate, NTLM, Digest, Basic\n" +
                            "Accept-Language: zh-CN, *;q=0.1\n" +
                            "CSeq: 1\n" +
                            "Supported: com.microsoft.wm.srvppair, com.microsoft.wm.sswitch, com.microsoft.wm.eosmsg, com.microsoft.wm.predstrm\n" +
                            "\n";
                    client.write(sendMsg);
                    String clientResult = client.readAll();
                    int l = clientResult.indexOf("Location:");
                    if(l>0){
                        l+=9;
                        result = "";
                        char ch = clientResult.charAt(l);
                        int len = clientResult.length();
                        while(ch!='\n'){
                            result+=ch;
                            l++;
                            if(l>=len)break;
                            ch = clientResult.charAt(l);
                        }
                        result = result.trim();
                        //System.out.println("result="+result);
                    }
                }

                if (!"".equals(result)){
                    String dstIp = TcpUtils.getHostFromUrl(result);
                    //dstIp = "61.55.145.45";
                    result = requestUrl.replaceFirst(srcIp, dstIp);
                    //result = requestUrl;
                }
            }

        } catch (Exception e) {
            System.err.println(e);
        }

        return result;
    }


    public static void main(String args[]){
        RegMidware rm = new RegMidware();
        System.out.println(rm.getGslbUrl("rtsp://hbcdn.openhe.net/hecommedia/jidong/000000��Ӱ/����1944/����1944-1.wmv?aip=61.55.144.106&apt=8200&dat=1314693804&key=92510" ,"202.106.15.57"));

        //System.out.println(regUrl("rtsp://hbcdn.openhe.net/idcmedia/bjwangshang/����/�����ڱ���[30��]/alangzaibeijin01.wmv?mid=-1&icp=-1&cid=-1&imp=-1&uid=-1&rip=61.55.144.213&svr=-1&sid=-1&fee=0"));

    }
}
