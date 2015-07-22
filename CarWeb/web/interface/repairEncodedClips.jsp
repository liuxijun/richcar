<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ page import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface" %><%@ page import="com.fortune.rms.business.encoder.model.EncoderTask" %><%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.rms.business.content.model.ContentProperty" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %>
<%@ page import="com.fortune.rms.business.encoder.model.EncoderTemplate" %>
<%@ page import="com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface" %>
<%@ page import="com.fortune.util.CacheUtils" %>
<%@ page import="com.fortune.util.DataInitWorker" %>
<%@ page import="org.apache.log4j.Logger" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/1/28
  Time: 8:13
  To change this template use File | Settings | File Templates.
--%><%
  EncoderTaskLogicInterface encoderTaskLogicInterface = (EncoderTaskLogicInterface) SpringUtils.getBean("encoderTaskLogicInterface",session.getServletContext());
  EncoderTask task = new EncoderTask();
  task.setStatus(EncoderTaskLogicInterface.STATUS_FINISHED);
  List<EncoderTask> tasks = encoderTaskLogicInterface.search(task);
  final ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());
  final EncoderTemplateLogicInterface encoderTemplateLogicInterface = (EncoderTemplateLogicInterface)SpringUtils.getBean("encoderTemplateLogicInterface",session.getServletContext());
  String sql = "";
  String logs = "";
  for(EncoderTask t:tasks){
    ContentProperty clip = (ContentProperty) CacheUtils.get(t.getClipId(),"",new DataInitWorker(){
      public Object init(Object key,String cacheName){
        try {
          return contentPropertyLogicInterface.get((Long) key);
        } catch (Exception e) {
          return null;
        }
      }
    });
    if(clip!=null){
      EncoderTemplate template = (EncoderTemplate) CacheUtils.get(t.getTemplateId(),"encoderTemplate",new DataInitWorker(){
        public Object init(Object key,String cacheName){
          try {
            return encoderTemplateLogicInterface.get((Long)key);
          } catch (Exception e) {
            return null;
          }
        }
      });
      if(template!=null){
        ContentProperty searchBean = new ContentProperty();
        searchBean.setContentId(clip.getContentId());
        searchBean.setIntValue(clip.getIntValue());
        searchBean.setPropertyId(template.getPropertyId());
        List<ContentProperty> encodedClips = contentPropertyLogicInterface.search(searchBean);
        if(encodedClips!=null&&encodedClips.size()>0){
          if(encodedClips.size()==1){

          }else{
            String log = "转码超过一个结果连接出现："+t.getName();
            logs+=log+"\r\n";
            logger.warn(log);
          }
          ContentProperty encodedClip = encodedClips.get(0);
          if(encodedClip.getStringValue().equals(t.getDesertFileName())){
            //logger.debug("任务连接正确，无需修正："+t.getName()+","+encodedClip.getName());
          }else{
            logger.debug("任务连接错误，必须修正："+t.getName()+","+encodedClip.getName());
            sql+="update content_property set string_value='"+t.getDesertFileName()+"' where id="+encodedClip.getId()+";\r\n";
          }
        }else{
          String log = "没有找到编码后的数据："+t.getName()+","+template.getTemplateName();
          logs+=log+"\r\n";
          logger.error(log);
        }
      }else{
        String log = "没有找到转码配置："+t.getName()+",templateId="+t.getTemplateId();
        logs+=log+"\r\n";
        logger.error(log);
      }
    }else{
      String log = "没有找到原始的连接："+t.getName();
      logs+=log+"\r\n";
      logger.error(log);
    }
  }
%>
<html>
<head>
    <title></title>
</head>
<body>
<pre><%="/*"+logs+"*/\r\n"+sql%></pre>
</body>
</html><%!
  Logger logger = Logger.getLogger("com.fortune.rms.jsp.repairEncodedClips");
%>
