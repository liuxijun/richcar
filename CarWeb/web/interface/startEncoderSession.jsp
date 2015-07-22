<%@ page import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface" %><%@ page
        import="com.fortune.rms.business.encoder.model.EncoderTask" %><%@ page
        import="com.fortune.rms.business.encoder.model.EncoderTemplate" %><%@ page
        import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="java.util.Date" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.util.CacheUtils" %><%@ page
        import="com.fortune.util.DataInitWorker" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-10
  Time: 下午4:16
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    PropertyLogicInterface propertyLogicInterface = (PropertyLogicInterface) SpringUtils.getBean("propertyLogicInterface",session.getServletContext());
    String propertyCode = "Media_Url_Source";
    Property property = propertyLogicInterface.getByCode(propertyCode);
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.startEncoderSession.jsp");
    int taskCount = 0;
    if(property!=null){
        ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());

        ContentProperty cp = new ContentProperty();
        cp.setPropertyId(property.getId());
        cp.setExtraInt(ContentPropertyLogicInterface.EXTRA_INT_NOT_ENCODED);
        List<ContentProperty> sourceClips = contentPropertyLogicInterface.search(cp);
        if(sourceClips!=null&&sourceClips.size()>0){
            EncoderTaskLogicInterface encoderTaskLogicInterface = (EncoderTaskLogicInterface) SpringUtils.getBean("encoderTaskLogicInterface",session.getServletContext());
            DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
            Device encoder = new Device();
            encoder.setType(DeviceLogicInterface.DEVICE_TYPE_ENCODER);
            List<Device> encoders = deviceLogicInterface.search(encoder);
            final ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
            EncoderTemplateLogicInterface encoderTemplateLogicInterface = (EncoderTemplateLogicInterface) SpringUtils.getBean("encoderTemplateLogicInterface",session.getServletContext());

            List<EncoderTemplate> templates = encoderTemplateLogicInterface.getAll();

            int encoderCount = encoders.size();
            for(ContentProperty contentProperty :sourceClips){
                contentProperty.setExtraInt(ContentPropertyLogicInterface.EXTRA_INT_HAS_ENCODED);
                contentProperty = contentPropertyLogicInterface.save(contentProperty);
                //随机获取一个
                int encoderIndex =(int) Math.round(Math.random()*(encoderCount-1));
                if(encoderIndex<0){
                    encoderIndex=0;
                }
                if(encoderIndex>=encoderCount){
                   encoderIndex = encoderCount-1;
                }
                encoder = encoders.get(encoderIndex);
                try {
                    Content content =(Content) CacheUtils.get(contentProperty.getContentId(),"contentCache",
                            new DataInitWorker(){
                                public Object init(Object key,String cacheName){
                                    return contentLogicInterface.get((Long) key);
                                }
                            });
                    if(content ==null){
                        continue;
                    }
                    String sourceFileName = contentProperty.getStringValue();
                    //sourceFileName = StringUtils.getClearURL(contentDevice.getUrl()+"/"+sourceFileName).substring(4);
                    while(sourceFileName.contains("//")){
                        sourceFileName = sourceFileName.replaceAll("//","/");
                    }
                    for(EncoderTemplate template :templates){
                        EncoderTask task = new EncoderTask();
                        String desertFileName = sourceFileName;
                        int i=desertFileName.lastIndexOf(".");
                        if(i>0){
                            desertFileName = desertFileName.substring(0,i)+"."+template.getTemplateCode()+"."+template.getFileFormat();
                        }else{
                            desertFileName = desertFileName+"."+template.getTemplateCode()+"."+template.getFileFormat();
                        }
                        while(desertFileName.startsWith("/")&&desertFileName.length()>1){
                            desertFileName = desertFileName.substring(1);
                        }
                        desertFileName = "/encode/"+desertFileName;
                        while(desertFileName.contains("//")){
                            desertFileName = desertFileName.replaceAll("//","/");
                        }
                        task.setClipId(contentProperty.getId());
                        task.setEncoderId(encoder.getId());
                        task.setDesertFileName(desertFileName);
                        task.setSourceFileName(sourceFileName);
                        Long length = contentProperty.getLength();
                        task.setLength(length==null?-1:length.intValue());
                        task.setProcess(0);
                        task.setStatus(EncoderTaskLogicInterface.STATUS_WAITING+100);
                        task.setEncoder(encoder);
                        task.setTemplate(template);
                        task.setTemplateId(template.getId());
                        task.setStreamServerId(content.getDeviceId());
                        task.setStartTime(new Date());

                        String name = contentProperty.getName();
                        if(name==null||"".equals(name.trim())|| StringUtils.string2int(name,-1)!=-1){
                            name = content.getName();
                        }
                        name = "《"+name+"(" +contentProperty.getIntValue()+
                                ")》转换“"+template.getTemplateName()+"”任务";
                        task.setName(name);
                        logger.debug("正在保存任务："+name);
                        encoderTaskLogicInterface.save(task);
                        taskCount++;
                    }
                } catch (Exception e) {
                    logger.error("处理过程中发生异常："+e.getMessage());
                }
/*
                if(taskCount>5){
                    break;
                }
*/
            }
        }else{
            logger.warn("没有找到任何的资源需要编码！");
        }
    }else{
        logger.warn("属性没找到："+propertyCode);
    }
    logger.debug("累计添加任务："+taskCount);
%><html>
<head>
    <title>添加编码任务</title>
</head>
<body>
   <p>累计添加<%=taskCount%>个任务！<a href="../encoder/encoderTaskListCanOrder.jsp">查看</a></p>
</body>
</html><%
//201305311703
%>