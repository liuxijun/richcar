<%@ page import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="java.util.Date" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-6-30
  Time: 上午8:06
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String action = request.getParameter("action");
    AppConfigurator configurator = AppConfigurator.getInstance();
    if("finished".equals(action)){
        String desert = request.getParameter("desert");
        String fileName = StringUtils.getParameter(desert,"save2file");
        long channelId = StringUtils.string2long(request.getParameter("channelId"), 15884423L);
        if(desert!=null){
            if(fileName==null){
                fileName = request.getParameter("save2file");
            }
            if(fileName!=null){
                Content content = new Content();
                content.setName(request.getParameter("contentName")+StringUtils.date2string(new Date()));
                content.setCspId(StringUtils.string2long(request.getParameter("contentCspId"),1L));
                long deviceId = StringUtils.string2long(request.getParameter("deviceId"), configurator.getLongConfig(
                        "system.default.autoRecordServerId", 0L
                ));
                content.setDeviceId(deviceId);
                content.setCreateTime(new Date());
                content.setIntro("自动录制节目，录制完成时间："+StringUtils.date2string(new Date()));
                content.setActors(request.getParameter("actors"));
                content.setDirectors(request.getParameter("directors"));
                content.setValidEndTime(new Date(System.currentTimeMillis()+10*365*24*3600*1000L));
                content.setValidStartTime(new Date());
                ServletContext context = session.getServletContext();
                ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",context);
                ContentCspLogicInterface contentCspLogicInterface=(ContentCspLogicInterface)SpringUtils.getBean("contentCspLogicInterface",context);
                ContentPropertyLogicInterface contentPropertyLogicInterface=(ContentPropertyLogicInterface)SpringUtils.getBean("contentPropertyLogicInterface",context);
                //ContentChannelLogicInterface contentChannelLogicInterface=(ContentChannelLogicInterface)SpringUtils.getBean("contentChannelLogicInterface",context);
                Device device = contentLogicInterface.getDevice(deviceId);
                if(device!=null){
                    //文件名是全名，这里要做截断
                    fileName = fileName.substring(device.getLocalPath().length());
                }
                content.setProperty1(channelId);
                content = contentLogicInterface.save(content);
                ContentProperty cp =new ContentProperty();
                cp.setStringValue(fileName);
                cp.setContentId(content.getId());
                Property property = contentLogicInterface.getPropertyByCode("Media_Url_Source");
                if(property==null){
                    property = contentLogicInterface.getPropertyByCode("Media_Url_768K");
                }
                cp.setPropertyId(property.getId());
                cp.setName(content.getName() + "录制节目");
                cp.setIntValue(0L);
                cp = contentPropertyLogicInterface.save(cp);
                contentCspLogicInterface.publishContent(content.getId(),content.getCspId(),channelId);
            }
        }
    }
%>{success:true}