package com.fortune.rms.servlet;

import com.fortune.rms.business.template.logic.logicInterface.ChannelTemplateLogicInterface;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-11-7
 * Time: 9:55:54
 * 读取html文件，并返回
 */
public class UrlRewriteServlet extends HttpServlet {
    Logger logger = Logger.getLogger(this.getClass());
    public final String nameAndModifyDateSplitor = "__m__";

    public String getFileContentFromCache(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            fileName +=nameAndModifyDateSplitor+file.lastModified();
        }
        Object result = CacheUtils.get(fileName,"htmlFileCache",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                logger.debug("文件有更新或未初始化进入内存:"+key);
                String fileName = key.toString();
                //long fileDate=0;
                String[] fileData = key.toString().split(nameAndModifyDateSplitor);
                if(fileData.length>1){
                    fileName = fileData[0];
                    //fileDate= StringUtils.string2long(fileData[1],fileDate);
                }
                File srcFile = new File(fileName);
                if(!srcFile.exists()){
                    logger.warn("文件："+srcFile.getAbsolutePath()+"  不存在！尝试index.html");
                    srcFile = new File(fileName+"/index.html");
                }
                if(!srcFile.exists()){
                    logger.warn("文件："+srcFile.getAbsolutePath()+"  不存在！返回！");
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
        if(url!=null){
            int p=url.indexOf("___");//用3个下划线来分割文件与参数
            if(p>0){
                fileUrl = url.substring(0,p).replace("_dot_",".");

            }
        }
        String fileContent =getFileContentFromCache(request.getSession().getServletContext().getRealPath(fileUrl));
        if(fileContent==null){
            fileContent = "<html><header><title>404</title></header><body>File not found!</body></html>";
        }
        ServletOutputStream out = null;
        PrintWriter pw = null;
        try {
            response.setCharacterEncoding("GBK");
            pw = response.getWriter();
            pw.print(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out!=null){
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(pw!=null){
                pw.close();
                pw = null;
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
