<%@ page import="com.fortune.rms.business.module.model.Property" %><%@ page import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page import="java.util.List" %><%@ page import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page import="com.fortune.rms.business.system.model.Device" %><%@ page import="com.fortune.rms.business.encoder.model.EncoderTemplate" %><%@ page import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface" %><%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page import="com.fortune.rms.business.content.model.Content" %><%@ page import="java.util.Date" %><%@ page import="org.apache.log4j.Logger" %><%@ page import="java.io.File" %><%@ page import="com.fortune.util.*" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-10
  Time: 下午4:16
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    PropertyLogicInterface propertyLogicInterface = (PropertyLogicInterface) SpringUtils.getBean("propertyLogicInterface",session.getServletContext());
    String propertyCode = "Media_Url_Source";
    Property property = propertyLogicInterface.getByCode(propertyCode);
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.scanSourceFile.jsp");
    int errorPlaybackCount = 0;
    int errorFileNotExistsCount=0;
    int allCount = 0;
    Date startTime = new Date();
    int startIndex = StringUtils.getIntParameter(request.getQueryString(),"startIndex",0);
    String result = "--"+StringUtils.date2string(startTime)+",从" +startIndex+"开始扫描\r\n";
    String scanResultFileName = "scanResult_"+StringUtils.date2string(startTime,"yyyyMMddHHmmss")+".txt";
    String scanFilePath = "/import/scanResult";
    boolean debugSuccess = "true".equals(request.getParameter("debugSuccess"));
    if(property!=null){
        ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());

        ContentProperty cp = new ContentProperty();
        cp.setPropertyId(property.getId());
        //cp.setExtraInt(ContentPropertyLogicInterface.EXTRA_INT_NOT_ENCODED);
        List<ContentProperty> sourceClips = contentPropertyLogicInterface.search(cp);

        if(sourceClips!=null&&sourceClips.size()>0){
            DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
            Device encoder = new Device();
            encoder.setType(DeviceLogicInterface.DEVICE_TYPE_ENCODER);

            ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
            EncoderTemplateLogicInterface encoderTemplateLogicInterface = (EncoderTemplateLogicInterface) SpringUtils.getBean("encoderTemplateLogicInterface",session.getServletContext());

            //List<EncoderTemplate> templates = encoderTemplateLogicInterface.getAll();
            allCount = sourceClips.size();
            int i=0;
            for(i = startIndex;i<allCount;i++){
                ContentProperty contentProperty = sourceClips.get(i);
                Content content = contentLogicInterface.get(contentProperty.getContentId());
                Device contentDevice = deviceLogicInterface.get(content.getDeviceId());
                String sourceFileName = contentProperty.getStringValue();
                //sourceFileName = StringUtils.getClearURL(contentDevice.getUrl()+"/"+sourceFileName).substring(4);
                if(i%50==0){
                    logger.debug("扫描第"+ i +"个文件，"+(100* i /allCount)+"%");
                    //FileUtils.writeContinue(application.getRealPath(scanFilePath),scanResultFileName,result);
                    //result = "";
                }
                while(sourceFileName.contains("//")){
                    sourceFileName = sourceFileName.replaceAll("//","/");
                }
                String wholeFilePath = contentDevice.getLocalPath()+"/"+sourceFileName;
                File sourceFile = new File(wholeFilePath);
                if(sourceFile.exists()&&(!sourceFile.isDirectory())){
                    SimpleFileInfo fileInfo = new SimpleFileInfo(sourceFileName,-1,
                            new Date(sourceFile.lastModified()),sourceFile.isDirectory(), FileType.video);
                    boolean playSuccessed = false;
                    String thumbPic =contentProperty.getThumbPic();
                    if(thumbPic==null){
                        thumbPic = "";
                    }
                    thumbPic = thumbPic.trim();
                    if(StringUtils.getIntParameter(thumbPic,"width",-1)>0){
                        //已经处理过，是可以播放的
                        if(debugSuccess){
                            String tempStr = getContentInfo(content, contentProperty)+"已经处理过，处理结果："+thumbPic;
                            logger.debug(tempStr);
                            result +="\r\n"+content.getCspId()+"\t"+content.getId()+"\t"+contentProperty.getId()+"\t"+tempStr;
                        }
                    }else{
                        try {
                            logger.debug("正在扫描"+getContentInfo(content, contentProperty)+","+sourceFile.getAbsolutePath());
                            if(sourceFile.length()>2*1024){//如果文件小于2KB，则忽略这个文件
                                if(FileUtils.setFileMediaInfo(sourceFile.getAbsolutePath(),fileInfo)){
                                    if(fileInfo.getLength()>0){
                                        playSuccessed = true;

                                    }
                                }else{
                                }
                            }else{
                                String tempStr = "文件小于2KB，可能有问题！放弃播放扫描这个文件："+getContentInfo(content, contentProperty)+","+
                                        sourceFile.getAbsolutePath();
                                result+= "\r\n"+content.getCspId()+"\t"+content.getId()+"\t"+contentProperty.getId()+"\t"+tempStr;
                                logger.error(tempStr);

                            }
                        } catch (Exception e) {
                            logger.error("处理"+getContentInfo(content, contentProperty)+"时发生问题："+e.getMessage());
                        }
                        if(playSuccessed){
                            contentProperty.setExtraInt(4L);
                            contentProperty.setLength((long)fileInfo.getLength());
                            thumbPic+="?width="+fileInfo.getWidth()+"&height="+fileInfo.getHeight()+"&duration="+fileInfo.getLength();
                            contentProperty.setThumbPic(thumbPic);
                            //contentProperty.setExtraData(fileInfo.getWidth()+"x"+fileInfo.getHeight()+","+fileInfo.getLength());
                            //contentProperty.set
                            if(debugSuccess){
                                String tempStr =""+getContentInfo(content, contentProperty)+"处理结果："+thumbPic;
                                logger.debug(tempStr);
                                result +="\r\n"+content.getCspId()+"\t"+content.getId()+"\t"+contentProperty.getId()+"\t"+tempStr;
                            }
                            contentPropertyLogicInterface.save(contentProperty);
                        }else{
                            String tempStr = getContentInfo(content,contentProperty)+
                                    "无法获取文件详细信息，可能这个文件无法播放："+sourceFileName
                                    +","+sourceFile.getAbsolutePath();
                            result += "\r\n" + tempStr;
                            logger.error(tempStr);
                            errorPlaybackCount++;
                        }
                    }
                }else{
                    String tempStr= getContentInfo(content, contentProperty);
                    tempStr  += "源文件丢失："+sourceFileName+","+sourceFile.getAbsolutePath();
                    result+="\r\n"+content.getCspId()+"\t"+content.getId()+"\t"+contentProperty.getId()+"\t"+tempStr;
                    logger.error(tempStr);
                    errorFileNotExistsCount++;
                }
            }
        }else{
            logger.warn("没有找到任何的资源需要编码！");
        }
    }else{
        logger.warn("属性没找到："+propertyCode);
    }
    String tempStr = "累计扫描文件："+allCount+"个，其中缺失文件："+errorFileNotExistsCount+
            "个，可能无法播放文件："+ errorPlaybackCount +"个";
    logger.debug(tempStr);
    result+="\r\n0\t0\t0\t"+tempStr;
    result+="\r\n--"+StringUtils.date2string(new Date())+"结束扫描";
    FileUtils.writeContinue(application.getRealPath(scanFilePath),scanResultFileName,result);
%><html>
<head>
    <title>扫描所有文件</title>
</head>
<body>
   <p>累计扫描文件<%=errorPlaybackCount%>个！<a href="<%=scanFilePath+"/"+scanResultFileName%>">查看</a></p>
</body>
</html><%!
    public String getContentInfo(Content content,ContentProperty contentProperty){
        String result = "资源《"+content.getName()+"》";
        Long intValue = contentProperty.getIntValue();
        if(intValue!=null&&intValue>0){
             result +="第"+intValue+"集";
        }
        return result;
    }
    //V201304150940
%>