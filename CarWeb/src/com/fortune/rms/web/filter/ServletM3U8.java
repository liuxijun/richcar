package com.fortune.rms.web.filter;

import com.fortune.rms.business.system.logic.logicImpl.SecurityUtils;
import com.fortune.server.protocol.M3U8;
import com.fortune.server.protocol.M3U8Stream;
import com.fortune.util.AppConfigurator;
import com.fortune.util.FileUtils;
import com.fortune.util.MD5Utils;
import com.fortune.util.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xjliu on 14-11-12.
 * 过滤m3u8请求，处理防盗链和绝对路径等问题
 */
public class ServletM3U8 extends HttpServlet {
    private Logger logger = Logger.getLogger(this.getClass());
    private Map<String,String> pathMap = null;

    public String getLocalFileName(String url){
        AppConfigurator configurator = AppConfigurator.getInstance();
        if(pathMap==null){
            pathMap = new HashMap<String, String>();
            for(int i=0;i<32;i++){
                String path = configurator.getConfig("system.virtualPath.path"+i+"",null);
                if(path!=null){
                    while(path.startsWith("/")&&path.length()>1){
                        path = path.substring(1);
                    }
                    while(path.endsWith("/")){
                        path = path.substring(0,path.length()-2);
                    }
                    if("".equals(path)){
                        continue;
                    }
                    String to = configurator.getConfig("system.virtualPath.path"+i+"To",null);
                    if(to!=null){
                        logger.debug("增加映射："+path+"->"+to);
                        pathMap.put(path,to);
                    }
                }
            }
        }
        while(url.startsWith("/")&&url.length()>1){
            url = url.substring(1);
        }
        int p=url.indexOf("/");
        String mapPath;
        String result;
        if(p>0){
            mapPath = url.substring(0,p);
            //url = url.substring(p);
            result = pathMap.get(mapPath);
            if(result!=null){
                url = url.substring(p);
            }
        }else{
            result = null;
        }
        if(result==null){
            result = configurator.getConfig("system.virtualPath.root","/home/fortune/movie");
            logger.warn("未能在虚拟目录映射中找到对应数据，从配置中读取根目录："+result);
        }
        return result+"/"+url;
    }
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getServletPath().replace(".M3U8",".m3u8");
        String version = request.getParameter("version");
        if(version==null){
            version="3";
        }
        String localFileName = getLocalFileName(fileName);
        File m3u8File = new File(localFileName);
        String queryString =request.getQueryString();
        if(queryString==null){
            queryString = "";
        }
        String encrypt = StringUtils.getParameter(queryString,"encrypt");
        if(encrypt==null){
            SecurityUtils su = SecurityUtils.getInstance();
            String url = su.regUrl(fileName+"?"+queryString,request.getRemoteAddr(),
                    su.getTokenPwd(null,null));
            int p =url.indexOf("?");
            if(p>0){
                queryString = url.substring(p);
                while (queryString.length()>1&&queryString.charAt(0)=='?'){
                    queryString = queryString.substring(1);
                }
            }
        }
        logger.debug("准备处理请求："+fileName+","+m3u8File.getAbsolutePath());
        if(m3u8File.exists()){
            M3U8 m3u8 = new M3U8();
            String m3u8Content = FileUtils.readFileInfo(localFileName);
            M3U8Stream stream = new M3U8Stream(0,0,fileName+"?"+queryString,m3u8Content);
            m3u8.addStream(stream);
            m3u8.setVersion(version);
            //默认设置为绝对路径。
            m3u8.setRelateUrlType(AppConfigurator.getInstance().getBoolConfig("system.m3u8.relateUrlType",false));
            response.getWriter().print(m3u8);

            logger.debug("处理请求结束：" + fileName + "," + m3u8File.getAbsolutePath());
        }else{
            logger.error("文件不存在："+m3u8File.getAbsolutePath());
            response.setStatus(404);
            response.getWriter().println("<html><head><title>FileError</title></head><body>File Not Exists:" +fileName+
                    "</body></html>");
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }
}
