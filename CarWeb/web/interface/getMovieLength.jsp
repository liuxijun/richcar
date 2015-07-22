<%@ page import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="java.io.File" %><%@ page import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page import="com.fortune.util.*" %><%@ page
        import="java.util.List" %><%@ page import="java.util.ArrayList" %><%@ page
        import="org.apache.log4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-6-18
  Time: 上午10:30
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    long deviceId = StringUtils.string2long(request.getParameter("deviceId"),-1);
    int resultCode = 0;
    int resultLength = 0;
    String allResultMsg = "";
    List<Map<String,Object>> results = new ArrayList<Map<String, Object>>();
    if(deviceId>0){
        String urls = request.getParameter("urls");
        if(urls!=null){
            DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
            Device device = deviceLogicInterface.get(deviceId);
            String[] allUrl = urls.split(";");
            for(String url:allUrl){
                while(url.contains("%")){
                    url = java.net.URLDecoder.decode(url, "UTF-8");
                }
                String fileName = device.getLocalPath()+"/"+url;

                File file = new File(fileName);
                String resultMsg;
                int length = 0;
                Map<String,Object> fileResult = new HashMap<String, Object>();
                if(file.exists()){
                    if(!file.isDirectory()){
                        SimpleFileInfo fileInfo = new SimpleFileInfo(file);
                        if(FileUtils.setFileMediaInfo(file.getAbsolutePath(),fileInfo)){
                            if(fileInfo.getLength()>=0){
                                length= (int) fileInfo.getLength();
                                resultLength += length;
                                resultCode = 200;
                                resultMsg = "文件时长："+length;
                            }else{
                                resultCode = 501;
                                resultMsg="文件无法获取信息，可能不是一个视频文件："+file.getAbsolutePath();
                            }
                        }else{
                            resultCode = 501;
                            resultMsg="文件无法获取信息，可能不是一个视频文件："+file.getAbsolutePath();
                        }
                    }else{
                        resultCode = 500;
                        resultMsg = "这是个目录，不是文件："+file.getAbsolutePath();
                    }
                }else{
                    resultCode = 404;
                    resultMsg="文件不存在："+file.getAbsolutePath();
                }
                fileResult.put("file",url);
                fileResult.put("fullFileName",file.getAbsolutePath());
                fileResult.put("code",resultCode);
                fileResult.put("msg",resultMsg);
                fileResult.put("length",length);
                results.add(fileResult);
            }
        }else{
            resultCode = 405;
            allResultMsg = "缺少参数:urls";
        }
    }else{
        resultCode = 405;
        allResultMsg = "缺少参数：deviceId";
    }
    Map<String,Object> result = new HashMap<String, Object>();
    result.put("resultMsg",allResultMsg);
    result.put("resultCode",resultCode);
    result.put("resultLength",resultLength) ;
    result.put("files",results);
    String resultStr = JsonUtils.getJsonString(result);
    logger.debug(resultStr);
    out.print(resultStr);
%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.getMovieLength.jsp");

%>