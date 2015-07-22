package com.fortune.rms.web.file;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-8
 * Time: 18:02:34
 * 文件上传
 */
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fortune.common.Constants;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.server.message.ServerMessager;
import com.fortune.util.*;
import com.fortune.util.net.URLEncoder;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

public class FileUpload extends HttpServlet {
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result="命令不能识别或参数异常";
        Map<String,Object> jsonResult =new HashMap<String,Object>();
        boolean success = false;
        if (request.getParameter("getfile") != null && !request.getParameter("getfile").isEmpty()) {
            File file = new File(getRoot(request)+request.getParameter("getfile"));
            if (file.exists()) {
                int bytes = 0;
                ServletOutputStream op = response.getOutputStream();
                response.setContentType(getMimeType(file));
                response.setContentLength((int) file.length());
                response.setHeader( "Content-Disposition", "inline; filename=\"" + file.getName() + "\"" );
                byte[] bbuf = new byte[1024];
                DataInputStream in = new DataInputStream(new FileInputStream(file));

                while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
                    op.write(bbuf, 0, bytes);
                }
                in.close();
                op.flush();
                op.close();
            }else{
                success =false;
                result = "文件不存在："+ file.getAbsolutePath();
            }
        } else if (request.getParameter("delfile") != null && !request.getParameter("delfile").isEmpty()) {
            String url = request.getParameter("delfile");
            File file = new File(getRoot(request)+url);
            if (file.exists()) {
                if(file.delete()){// TODO:check and report success
                    String fullFileName = file.getAbsolutePath();
                    if(file.exists()){
                        result = "文件未能删除："+url;
                    }else{
                        result = "文件已经删除："+url;
                        success = true;
                    }
                }
            }else{
                result = "文件不存在，无法删除："+url;
            }
            writeSysLog("删除文件" +url+
                    "操作："+result,request);
            jsonResult.put("url",url);
        } else if (request.getParameter("getthumb") != null && !request.getParameter("getthumb").isEmpty()) {
            File file = new File(getRoot(request)+request.getParameter("getthumb"));
            if (file.exists()) {
                System.out.println(file.getAbsolutePath());
                String mimetype = getMimeType(file);
                if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")|| mimetype.endsWith("jpg") || mimetype.endsWith("gif")) {
                    BufferedImage im = ImageIO.read(file);
                    if (im != null) {
                        BufferedImage thumb = Scalr.resize(im, 75);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        if (mimetype.endsWith("png")) {
                            ImageIO.write(thumb, "PNG" , os);
                            response.setContentType("image/png");
                        } else if (mimetype.endsWith("jpeg")) {
                            ImageIO.write(thumb, "jpg" , os);
                            response.setContentType("image/jpeg");
                        } else if (mimetype.endsWith("jpg")) {
                            ImageIO.write(thumb, "jpg" , os);
                            response.setContentType("image/jpeg");
                        } else {
                            ImageIO.write(thumb, "GIF" , os);
                            response.setContentType("image/gif");
                        }
                        ServletOutputStream srvos = response.getOutputStream();
                        response.setContentLength(os.size());
                        response.setHeader( "Content-Disposition", "inline; filename=\"" + file.getName() + "\"" );
                        os.writeTo(srvos);
                        srvos.flush();
                        srvos.close();
                    }
                }
                return;
            }else{
                success = false;
                result = "文件不存在";
            }
        } else {
            PrintWriter writer = response.getWriter();
            writer.write("call POST with multipart form data");
            return;
        }
        jsonResult.put("success",success);
        jsonResult.put("message",result);
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtils.getJsonString(jsonResult));
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }
        request.setCharacterEncoding("UTF-8");
        logger.debug("new ServletFileUpload.....");
        ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
        PrintWriter writer = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        List<Map<String,Object>> json = new ArrayList<Map<String,Object>>();
        try {
            uploadHandler.setHeaderEncoding("UTF-8");
            logger.debug("uploadHandler.parseRequest(request).....");
            List<FileItem> items = uploadHandler.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String rootDir = getRoot(request);
                    String relatedDir = getRelatedPath();
                    File path = new File(rootDir+"/"+relatedDir);
                    if(!path.exists()){
                        if(!path.mkdirs()){
                            logger.error("无法创建目录："+path.getAbsolutePath());
                        }else{
                            logger.debug("目录成功创建："+path.getAbsolutePath());
                        }
                    }else{
                        logger.debug("目录存在，无需创建："+path.getAbsolutePath());
                    }
                    String fileName = getFileName(item.getName());
                    File file = new File(path.getAbsolutePath(), fileName);
                    logger.debug("尝试保存："+file.getAbsolutePath());
                    item.write(file);
                    String url = relatedDir+fileName;
                    Map<String,Object> jsono = new HashMap<String,Object>();
                    if(file.exists()){
                        logger.debug("文件保存："+file.getAbsolutePath());
                        SimpleFileInfo simpleFileInfo = new SimpleFileInfo(file);
                        if(FileUtils.setFileMediaInfo(file.getAbsolutePath(),simpleFileInfo)){
                            jsono.put("fileInfo",simpleFileInfo);
                        }
                    }
                    writeSysLog("用户上传了一个文件："+item.getName()+",大小："+item.getSize(),request);
                    jsono.put("name", URLEncoder.encode(item.getName(),"UTF-8").replaceAll("\\+", "%20"));
                    jsono.put("size", item.getSize());
                    jsono.put("url",url);
                    jsono.put("download_url", request.getServletPath()+"?getfile=" + url);
                    jsono.put("thumbnail_url", request.getServletPath()+"?getthumb=" + url);
                    jsono.put("delete_url",  request.getServletPath()+"?delfile=" + url);
                    jsono.put("delete_type", "GET");
                    jsono.put("success",true);
                    json.add(jsono);
                    logger.debug("上传返回结果："+json.toString());
                }
            }
        } catch (FileUploadException e) {
            writeSysLog("用户上传文件失败："+e.getMessage(),request);
            logger.error("用户上传文件失败："+e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            writeSysLog("用户上传文件失败:"+e.getMessage(),request);
            logger.error("用户上传文件失败："+e.getMessage());
            throw new RuntimeException(e);
        } finally {
            writer.write(JsonUtils.getJsonString(json));
            writer.close();
        }
        logger.debug("doPost finished");
    }

    private String getMimeType(File file) {
        String mimetype = "";
        if (file.exists()) {
            if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
                mimetype = "image/png";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("jpg")){
                mimetype = "image/jpg";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("jpeg")){
                mimetype = "image/jpeg";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("gif")){
                mimetype = "image/gif";
            }else {
                javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
                mimetype  = mtMap.getContentType(file);
            }
        }
        return mimetype;
    }



    private String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            suffix = filename.substring(pos + 1);
        }
        return suffix;
    }
    private String getRelatedPath(){
        return "upload/"+StringUtils.date2string(new Date(), "yyyy/MM/dd")+"/";
    }
    private String getServerMessage(String parameters,HttpServletRequest request){
        ServerMessager messager = new ServerMessager();
        return messager.postToHost(AppConfigurator.getInstance().getConfig("system.movie.fileInterfaceUrl",
                        "http://"+request.getServerName()+":"+request.getServerPort()+"/interface/files.jsp"),
                parameters,"UTF-8");

    }
    private String getRoot(HttpServletRequest request){
        String serverData = getServerMessage("command=getServerLocalPath&deviceId="+request.getParameter("deviceId")+"&cspId="+request.getParameter("cspId"),request);
        String result = null;
        if(serverData!=null&&!"".equals(serverData.trim())){
            JSONObject jsonObject = JsonUtils.getJsonObj(serverData);
            result = jsonObject.getString("rootPath");
        }
        if(result==null||"".equals(result.trim())){
            return AppConfigurator.getInstance().getConfig("system.movie.pathName","/home/fortune/movie/");
        }
        return  result;

    }
    private void writeSysLog(String logs,HttpServletRequest request){
        try {
            String result = getServerMessage("command=uploadLog&ip="+request.getRemoteAddr()+"&logs="+
                    URLEncoder.encode(logs,"UTF-8")+
                    "&adminId="+request.getParameter("adminId"),request);
            logger.debug("保存log结果："+result);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
    private String getFileName(String oldName){
        String fileName = StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_"+HzUtils.getFullSpell(oldName);
        String result = "";
        for(int i=0;i<fileName.length();i++){
            char ch = fileName.charAt(i);
            if((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9')||ch=='.'||ch=='_'||ch=='-'){
                result+=ch;
            }else{
                result+='_';
            }
        }
        return  result;
    }
}
