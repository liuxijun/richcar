package com.fortune.rms.web.filter;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-6-14
 * Time: 9:03:16
 * 探测原来的访问，将对页面的访问直接调度到新的的页面上去
 */
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Enumeration;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.net.URL;

import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.template.logic.logicInterface.ChannelTemplateLogicInterface;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.*;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class OldPortalRedirector  extends HttpServlet implements Filter {
    private Category logger ;
    private FilterConfig filterChannelsConfig;
    private Map<String,String> channelTemplate;
    private ChannelTemplateLogicInterface channelTemplateLogicInterface;


    public OldPortalRedirector(){
        channelTemplate = new HashMap<String,String>();
        logger = Logger.getLogger(getClass());
    }
    public void initChannelIds(FilterConfig filterConfig){
        this.filterChannelsConfig = filterConfig;
        logger.debug("Initing channelIds!");
        Properties props = new Properties();
        String channelPropFileName = filterConfig.getInitParameter("channelIds");
        if(channelPropFileName == null || "".equals(channelPropFileName)){
            channelPropFileName = "/channelTemplates.properties";
        }
        try {
            channelTemplateLogicInterface =(ChannelTemplateLogicInterface) SpringUtils.getBean("channelTemplateLogicInterface");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            //channelPropFileName = PortalRedirector.class.getResource(channelPropFileName).getFile();
            URL url = getClass().getResource(channelPropFileName);
            if(url != null){
                channelPropFileName = url.getFile();
                logger.debug("url:"+url.toString());
            }else{
                logger.error("getResource() return null!");
            }
            //channelPropFileName =
            logger.debug("will read config file:"+channelPropFileName);
            props.load(new BufferedInputStream(new FileInputStream(channelPropFileName)));
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String Property = props.getProperty (key);
                if(key!=null){
                    channelTemplate.put(key,Property);
                }
            }
        }catch(Exception e){
            logger.error("读取channelId和模版对应默认配置文件过程中发生异常："+e.getMessage());
            e.printStackTrace();
        }
    }
    public void init(FilterConfig filterConfig) {
        logger.debug("Initing system data!");
        filterChannelsConfig = filterConfig;
        initChannelIds(filterChannelsConfig);
    }
    public boolean isNumber(String inputStr){
        try {
            Integer.parseInt(inputStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public String getTemplate(long channelId,int type){
        Template template = null;
        if(channelId>0){
            template =(Template) CacheUtils.get(channelId+"_"+type,"channelTemplatesCache",
                    new DataInitWorker(){
                        public Object init(Object channelIdAndType,String cacheName){
                            String[] data =channelIdAndType.toString().split("_");
                            Long channelId = StringUtils.string2long(data[0],-1);
                            int templateType = StringUtils.string2int( data[1],-1);
                            if(templateType==ChannelTemplateLogicInterface.TEMPLATE_TYPE_INDEX){
                                return channelTemplateLogicInterface.getChannelIndexTemplate(channelId);
                            }else if(templateType==ChannelTemplateLogicInterface.TEMPLATE_TYPE_LIST){
                                return channelTemplateLogicInterface.getChannelListTemplate(channelId);
                            }else if(templateType==ChannelTemplateLogicInterface.TEMPLATE_TYPE_DETAIL){
                                return channelTemplateLogicInterface.getChannelDetailTemplate(channelId);
                            }
                            return null;
                        }
                    });
        }
        String result = null;
        if(template!=null){
            Csp csp = (Csp) CacheUtils.get(template.getCspId(),"cspCache",new DataInitWorker(){
                public Object init(Object cspId,String cacheName){
                    try {
                        CspLogicInterface cspLogicInterface = (CspLogicInterface)SpringUtils.getBean("cspLogicInterface");
                        return cspLogicInterface.get((Long)cspId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
            if(csp!=null){
                result = AppConfigurator.getInstance().getConfig("portalDefaultRootDir","/page")+"/"+ csp.getAlias()+"/"+template.getFileName();
                while(result.indexOf("//")>=0){
                    result = result.replace("//","/");
                }
            }
        }else{
            result = channelTemplate.get(channelId+"."+type);
        }
        if(result==null){
            result = channelTemplate.get("default"+"."+type);
        }
        return result;
    }
    public String getRedirectUrl(String url){
        int i;
        String extName,channelId,contentId;
        String redirectUrl=null;
        if(url == null){
            return null;
        }
        while(url.startsWith("/")){
            url = url.substring(1);
        }
        //获取扩展名，只有是.action才会处理
        i = url.lastIndexOf(".");
        if(i>0){
            extName = url.substring(i);
            if(extName.startsWith(".action")){
                //试图获取spId
                i = url.indexOf("_true_");
                if(i>0){
                    channelId = url.substring(0,i);
                    if(!isNumber(channelId)){
                        return null;
                    }
                    url = url.substring(i+6);

                    //检查是列表页还是详情页
                    redirectUrl = null;
                    if(url.startsWith("list")){
                        return getTemplateListUrl(channelId,url);
                    }else if(url.startsWith("index")){
                        return getTemplateIndexUrl(channelId,url);
                    }else if(url.startsWith("detail")){
                        contentId = StringUtils.getParameter(url,"mediaId");
                        if(contentId==null){
                            contentId=StringUtils.getParameter(url,"subjectId");
                        }
                        if(contentId!=null&& !"".equals(contentId)){
                            return getTemplateDetailUrl(channelId,contentId,null,url);
                        }
                    }
                }
            }else if(extName.startsWith(".jsp")&&url.indexOf("user/web/")>=0){
                channelId = StringUtils.getParameter(url,"channelId");
                contentId = StringUtils.getParameter(url,"subjectId");
                if(contentId==null){
                    contentId=StringUtils.getParameter(url,"mediaId");
                }
                if(contentId!=null&& !"".equals(contentId)){
                    return getTemplateDetailUrl(channelId,contentId,null,url);
                }else{
                    return getTemplateListUrl(channelId,url);
                }
            }else if(extName.startsWith(".html")){
               // String cspId = "";
                String[] urlData = url.substring(0,i).split("/");
                if(urlData.length>=4){
                 //   cspId = urlData[0];
                    channelId = urlData[1];
                    String type=urlData[2];
                    if("media".equals(type)){
                        contentId = urlData[3];
                        return getTemplateDetailUrl(channelId,"",contentId,url);
                    }else if("content".equals(type)){
                        return getTemplateListUrl(channelId,url);
                    }
                }else{

                }
            }
        }
        return redirectUrl;
    }
    public String getParameters(String url){
        int i = url.indexOf("?");
        if(i>0){
            if(url.length()>i+1){
                url = url.substring(i+1);
            }
        }
        return url;
    }
    public String getTemplateIndexUrl(String channelId,String url){
        return getTemplate(StringUtils.string2long(channelId,-1),ChannelTemplateLogicInterface.TEMPLATE_TYPE_INDEX)+"?channelId="+channelId+"&"+getParameters(url);
    }
    public String getTemplateListUrl(String channelId,String url){
        return getTemplate(StringUtils.string2long(channelId,-1),ChannelTemplateLogicInterface.TEMPLATE_TYPE_LIST)+"?channelId="+channelId+"&"+getParameters(url);
    }
    public String getTemplateDetailUrl(String channelId,String contentChannelId,String contentId,String url){
        String queryString ="?channelId="+channelId;
        if(contentId==null){
            queryString += "&contentChannelId="+contentChannelId;
        }else{
            queryString += "&contentId="+contentId;
        }
        queryString +="&"+getParameters(url);
        return getTemplate(StringUtils.string2long(channelId,-1),ChannelTemplateLogicInterface.TEMPLATE_TYPE_DETAIL)+queryString;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, javax.servlet.ServletException {
        String sServletPath="";
        String redirectUrl;
        HttpServletRequest hsRequest = (HttpServletRequest) request;
        sServletPath = hsRequest.getServletPath()+"?"+hsRequest.getQueryString();
        logger.debug("in request:"+sServletPath);
/*
            if(sServletPath.indexOf("09/user/search")>=0){
                Enumeration params = hsRequest.getParameterNames();
                while(params.hasMoreElements()){
                    String parameterName = params.nextElement().toString();
                    for(String value:hsRequest.getParameterValues(parameterName)){
                       logger.debug("parameter:"+parameterName+"="+value);
                    }
                }
            }
*/
        redirectUrl = getRedirectUrl(sServletPath);
        if(redirectUrl!=null){
            HttpServletResponse hr = (HttpServletResponse)response;
            hr.sendRedirect(redirectUrl);
            return;
        }
        if(channelTemplate!=null){
            redirectUrl =channelTemplate.get(sServletPath);
            if(redirectUrl != null){
                Enumeration parameters = request.getParameterNames();
                if(parameters!=null){
                    String splitor;
                    if(redirectUrl.indexOf("?")>0){
                        splitor = "&";
                    }else{
                        splitor =  "?";
                    }
                    while(parameters.hasMoreElements()){
                        String parameter = (String)parameters.nextElement();
                        redirectUrl = redirectUrl + splitor + parameter +"="+request.getParameter(parameter);
                        splitor = "&";
                    }
                }
                HttpServletResponse hr = (HttpServletResponse)response;
                hr.sendRedirect("/"+redirectUrl);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    public void destroy() {
    }


}
