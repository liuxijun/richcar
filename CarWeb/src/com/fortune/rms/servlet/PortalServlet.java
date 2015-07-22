package com.fortune.rms.servlet;

import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.template.logic.logicInterface.ChannelTemplateLogicInterface;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-11-7
 * Time: 9:55:54
 * ��ȡhtml�ļ���������
 */
public class PortalServlet extends HttpServlet {
    Logger logger = Logger.getLogger(this.getClass());
    public final String nameAndModifyDateSplitor = "__m__";
    private Map<String,String> channelTemplate;

    @SuppressWarnings("unchecked")
    public String getTemplate(long channelId,int type){
        Template template = null;
        channelTemplate = (Map<String,String>) CacheUtils.get("allDefaultTemplate","allDefaultTemplate",
                new DataInitWorker(){
                    public Object init(Object key0,String cacheName){
                        Map<String,String> result=new HashMap<String,String>();
                        try{
                            //channelPropFileName = PortalRedirector.class.getResource(channelPropFileName).getFile();
                            Properties props = new Properties();
                            String channelPropFileName = "/channelTemplates.properties";
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
                                    result.put(key,Property);
                                }
                            }
                        }catch(Exception e){
                            logger.error("��ȡchannelId��ģ���ӦĬ�������ļ������з����쳣��"+e.getMessage());
                            e.printStackTrace();
                        }
                        return result;
                    }
                });
        if(channelId>0){
            template =(Template) CacheUtils.get(channelId+"_"+type,"channelTemplatesCache",
                    new DataInitWorker(){
                        public Object init(Object channelIdAndType,String cacheName){
                            ChannelTemplateLogicInterface channelTemplateLogicInterface=
                                    (ChannelTemplateLogicInterface)SpringUtils.getBeanForApp("channelTemplateLogicInterface");
                            String[] data =channelIdAndType.toString().split("_");
                            Long channelId = StringUtils.string2long(data[0], -1);
                            int templateType = StringUtils.string2int( data[1],-1);
                            if(templateType== ChannelTemplateLogicInterface.TEMPLATE_TYPE_INDEX){
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
                        CspLogicInterface cspLogicInterface = (CspLogicInterface) SpringUtils.getBean("cspLogicInterface");
                        return cspLogicInterface.get((Long)cspId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
            if(csp!=null){
                result = AppConfigurator.getInstance().getConfig("portalDefaultRootDir","/page")+"/"+ csp.getAlias()+"/"+template.getFileName();
                while(result.contains("//")){
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
    public String getFileContentFromCache(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            fileName +=nameAndModifyDateSplitor+file.lastModified();
        }
        Object result = CacheUtils.get(fileName,"htmlFileCache",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                logger.debug("�ļ��и��»�δ��ʼ�������ڴ�:"+key);
                String fileName = key.toString();
                //long fileDate=0;
                String[] fileData = key.toString().split(nameAndModifyDateSplitor);
                if(fileData.length>1){
                    fileName = fileData[0];
                    //fileDate= StringUtils.string2long(fileData[1],fileDate);
                }
                File srcFile = new File(fileName);
                if(!srcFile.exists()){
                    logger.warn("�ļ���"+srcFile.getAbsolutePath()+"  �����ڣ�����index.html");
                    srcFile = new File(fileName+"/index.html");
                }
                if(!srcFile.exists()){
                    logger.warn("�ļ���"+srcFile.getAbsolutePath()+"  �����ڣ����أ�");
                    return null;
                }
                String result = "";
                BufferedInputStream is =null;
                try {
                    is = new BufferedInputStream(
                            new FileInputStream(srcFile));
                    byte[] buffer = new byte[128000];
                    int readLen;
                    while((readLen=is.read(buffer))>0){
                       result += new String(buffer,0,readLen); 
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    if(is!=null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return result;
            }
        });
        if(result == null){
            return null;
        }
        return result.toString();
    }
    public void sendHtmlContent(HttpServletRequest request, HttpServletResponse response){
        String url = request.getServletPath();
        String fileUrl = url;
        //�ļ����Ĺ����ǣ������vm����ͷ��.htm����׺�ģ�����Ϊ��ģ��
        if(url!=null){
            int p=url.indexOf("/vm_");//��3���»������ָ��ļ������
            if(p>0){
                fileUrl = url.substring(p+4);
                p = fileUrl.indexOf(".htm");
                if(p>0){
                    fileUrl = fileUrl.substring(0,p);
                }
                String[] ids = fileUrl.split("_");
                String cspId = null;
                long channelId= -1;
                long contentId = -1;
                if(ids.length>0){
                    cspId=ids[0];
                    if(ids.length>1){
                        channelId = StringUtils.string2long(ids[1],-1);
                    }
                    if(ids.length>2){
                        contentId = StringUtils.string2long(ids[2],-1);
                    }
                }
                if(channelId<0){
                    //channelIdû�����룬������Ҫ�²�һ����
                    ContentLogicInterface contentLogic = (ContentLogicInterface) SpringUtils.getBeanForApp("contentLogicInterface");
                    Channel channel  = contentLogic.getContentBindChannel(0L,contentId);
                    if(channel!=null){
                        channelId = channel.getId();
                    }
                }
                int tempalteType = ChannelTemplateLogicInterface.TEMPLATE_TYPE_INDEX;
                if(channelId>0){
                    if(contentId<=0){
                        tempalteType = ChannelTemplateLogicInterface.TEMPLATE_TYPE_LIST;
                    }else{
                        tempalteType = ChannelTemplateLogicInterface.TEMPLATE_TYPE_DETAIL;
                    }
                }
                String templateUrl = getTemplate(channelId,tempalteType);
                //������Ŀ¼���͵�ǰҪ���ʵĲ���һ����Ŀ¼�£�������ȥ
                String filePath = FileUtils.extractFilePath(templateUrl,"/");
                String urlPath = FileUtils.extractFilePath(url,"/");
                if(!filePath.equals(urlPath)){
                    url = filePath+"/vm_"+cspId;
                    if(channelId>0){
                        url +="_"+channelId;
                        if(contentId>0){
                            url+="_"+contentId;
                        }
                    }
                    url+=".htm";
                    try {
                        response.sendRedirect(url);
                        return;
                    } catch (IOException e) {
                        logger.error("�ض���" +url+
                                "ʱ�����쳣��"+e.getMessage());
                    }
                }
                fileUrl = templateUrl;
            }
        }
        String fileContent =getFileContentFromCache(request.getSession().getServletContext().getRealPath(fileUrl));
        if(fileContent==null){
            fileContent = "<html><header><title>404</title></header><body>File not found!</body></html>";
        }
        PrintWriter pw = null;
        try {
            response.setCharacterEncoding("GBK");
            pw = response.getWriter();
            pw.print(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pw!=null){
                pw.close();
            }
        }

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendHtmlContent(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendHtmlContent(request,response);
    }
}
