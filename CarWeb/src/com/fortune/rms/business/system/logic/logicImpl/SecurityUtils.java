package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.util.*;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by xjliu on 14-11-18.
 * ����������
 */
public class SecurityUtils {
    private static SecurityUtils ourInstance = new SecurityUtils();
    public static final int VERIFY_RESULT_ERROR=1000;
    public static final int VERIFY_RESULT_PASSED=0;
    public static final int VERIFY_RESULT_NO_ENCRYPT=VERIFY_RESULT_ERROR+1;
    public static final int VERIFY_RESULT_TIME_OUT=VERIFY_RESULT_ERROR+2;
    public static final int VERIFY_RESULT_ENCRYPT_ERROR=VERIFY_RESULT_ERROR+3;
    private Logger logger = Logger.getLogger(this.getClass());
    private AppConfigurator config = AppConfigurator.getInstance();
    public static SecurityUtils getInstance() {
        return ourInstance;
    }

    private SecurityUtils() {
    }
    public String regUrl(String url,String clientIp,String tokenPwd){
        if(url==null){
            return null;
        }
        String result = url;
        if(result.contains("?")){
            result+="&";
        }else{
            result+="?";
        }
        String timeStamp = "timestamp="+StringUtils.date2string(new Date(),"yyyyMMddHHmmss");
        result+=timeStamp;
        if(tokenPwd==null||"".equals(tokenPwd)){
            tokenPwd = AppConfigurator.getInstance().getConfig("cdn.tokenPassword","fortune2009");
        }
        String clearUrl = StringUtils.getClearURL(result);
        while(clearUrl.startsWith("/")&&clearUrl.length()>1){
            clearUrl = clearUrl.substring(1);
        }
        String checkURL = getMovieFileName(clearUrl)+"&clientip="+clientIp+tokenPwd;
        try {
            try {
                checkURL = URLDecoder.decode(checkURL, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //logger.debug("����tokenʹ�õ�URL="+checkURL);
            String checkToken = MD5Utils.getMD5String(checkURL);
            result = result+"&encrypt="+checkToken;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return result;
    }
    public String getTokenPwd(String url,String defaultPwd){
        if(url!=null){
            String host = TcpUtils.getHostFromUrl(url);
            String tempPwd =  config.getConfig("cdn.tokenPassword_"+host,null);
            if(tempPwd!=null){
                return tempPwd;
            }
        }
        return config.getConfig("cdn.tokenPassword",defaultPwd);
    }

    public String getStringBefore(String str,String cuter){
        int p=str.indexOf(cuter);
        if(p>0){
            return str.substring(0,p);
        }
        if(p ==0){
            return "";
        }
        return str;
    }

    public String getMovieFileName(String url){
        String result = FileUtils.extractFileName(url, "/");
        result = getStringBefore(result,"?");
        result = getStringBefore(result,".m3u8");
        String[] ids = new String[]{"timestamp","contentId","contentPropertyId"};
        for(String id:ids){
            String idValue = StringUtils.getParameter(url,id);
            if(idValue==null||"".equals(idValue)){
                idValue = StringUtils.getParameter(url,id.toLowerCase());
            }
            result +="&"+id+"="+idValue;
        }
/*
        result +="&timestamp="+StringUtils.getParameter(url,"timestamp")+"&contentId="+
                StringUtils.getParameter(url,"contentId")+
                "&cpId="+StringUtils.getParameter(url,"contentPropertyId");
*/
        return result;
    }

    public int verifyUrlToken(String url,String clientIp,String tokenPwd){
        int result = VERIFY_RESULT_ENCRYPT_ERROR;
        if(tokenPwd==null||"".equals(tokenPwd)){
            tokenPwd = config.getConfig("cdn.tokenPassword","fortune2009");
        }
        String clearUrl = StringUtils.getClearURL(url);
        while(clearUrl.startsWith("/")&&clearUrl.length()>1){
            clearUrl = clearUrl.substring(1);
        }
        int p=clearUrl.indexOf("&encrypt=");
        if(p>0){
            String token = StringUtils.getParameter(clearUrl, "encrypt");
            String timeStamp = StringUtils.getParameter(url,"timestamp");
            if(token!=null){
                //Ϊ�˼����ϵļ��㷽ʽ��������url�����м��㣬��������һ�֣�ͨ���˾Ϳ��ԡ�
                String[] checkUrls = new String[]{getMovieFileName(clearUrl),clearUrl};
                for(String checkUrl:checkUrls){
                    String calculateString = checkUrl+"&clientip="+clientIp+tokenPwd;
                    String checkToken = null;
                    try {
                        //logger.debug("У��ʹ�õ�URL:"+calculateString);
                        checkToken = MD5Utils.getMD5String(calculateString);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    if(token.equals(checkToken)){

                        if(timeStamp!=null&&!"".equals(timeStamp.trim())){
                            Date time = StringUtils.string2date(timeStamp,"yyyyMMddHHmmss");
                            Date now = new Date();
                            if(Math.abs(now.getTime()-time.getTime())>config.getIntConfig(
                                    "system.security.tokenTimeout",120)*60*1000L){
                                logger.warn("�û���֤����ʱ��" + clientIp + "," + url);
                            }else{
                                return VERIFY_RESULT_PASSED;
                            }
                        }else{
                            logger.warn("URL��û��ʱ�����clientIP=" + clientIp + ",url=" + url);
                            return VERIFY_RESULT_TIME_OUT;
                        }

                    }else{
                        logger.warn("�û�֤����֤ʧ�ܣ����������֤��=" + checkToken + ",\n����ʹ�õ�URL=" + calculateString + "\n,URL��֤��=" + token);
                    }
                }
                return VERIFY_RESULT_NO_ENCRYPT;
            }else{
                logger.warn("URL��û��֤�飺clientIp=" + clientIp + ",url=" + url);
            }
        }else{
            logger.warn("URL��û��֤�飺clientIp=" + clientIp + ",url=" + url);
            return VERIFY_RESULT_NO_ENCRYPT;
        }
        return result;
    }

}
