<%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page
        import="java.io.File" %><%@ page import="java.util.Date" %><%@ page
        import="org.apache.log4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-24
  Time: 上午8:38
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%

    String command = request.getParameter("command");
    String result = "";
    final BatchWorker worker = BatchExecutor.getInstance().getWorker("reindexer",10);
    String workerLog = "";
    int count = 0;
    int finishedCount = 0;
    if("start".equals(command)){
        if(worker!=null){
            //boolean lastWorkTerminated = worker.isTerminated();
            //ExecutorService executorService = worker.getExecutor();

            if((!worker.isTerminated())&&(worker.getCount()>0)){
                result = "上次任务还没有结束！";
            }else{
                worker.reset();
                ServletContext servletContext = session.getServletContext();
                final ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)
                        SpringUtils.getBean("contentPropertyLogicInterface",servletContext);
                PropertyLogicInterface  propertyLogicInterface = (PropertyLogicInterface)
                        SpringUtils.getBean("propertyLogicInterface",servletContext);
                final  ContentLogicInterface contentLogicInterface = (ContentLogicInterface)
                        SpringUtils.getBean("contentLogicInterface",servletContext);
                final DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface)
                        SpringUtils.getBean("deviceLogicInterface",servletContext);
                final Property clip384Property = propertyLogicInterface.getByCode("Media_Url_384k");
                ContentProperty clip = new ContentProperty();
                clip.setPropertyId(clip384Property.getId());
                clip.setContentId(StringUtils.string2long(request.getParameter("contentId"),-1));
                final List<ContentProperty> clips = contentPropertyLogicInterface.search(clip);
                result = "一共有" +clips.size()+
                        "个文件要处理！";
                logger.debug(result);
                Thread thread = new Thread(){
                    public void run(){
                        for(ContentProperty cp:clips){
                            Content content =(Content) CacheUtils.get(cp.getContentId(),"simpleContent",new DataInitWorker(){
                                public Object init(Object key,String cacheName){
                                    return contentLogicInterface.get((Long) key);
                                }
                            });
                            Device device = (Device)  CacheUtils.get(content.getDeviceId(), "device", new DataInitWorker() {
                                public Object init(Object key, String cacheName) {
                                    return deviceLogicInterface.get((Long) key);
                                }
                            });
                            BatchRunnable runnable = getRunnable(content, cp, device,contentPropertyLogicInterface);
                            worker.execute(runnable);
                        }
                        worker.shutdown();
                    }
                };
                thread.start();
            }
        }
    }else{

    }

    if(worker!=null){
        String log = worker.getLog();
        int logLength = log.length();
        if(logLength>1000){
            workerLog = log.substring(logLength-1000);
        }else{
            workerLog = log;
        }
        count = worker.getCount();
        finishedCount = worker.getFinishedCount();
    }
%><html>
<head>
    <title>启动文件重新索引</title>
</head>
<body>
  <%
      if(!"start".equals(command)){
          %><p><a href="?command=start">重新启动重建索引</a></p><%
      }else{
          %><p>处理结果：<%=result%></p><%
      }
      int totalLength =640;
      int finishedLength =0;
      if(count>0){
          finishedLength = totalLength * finishedCount/count;
      }
      int waitingLength = totalLength - finishedLength;
  %><table width="<%=totalLength%>" border="1" cellpadding="0" cellspacing="0">
      <tr>
          <td colspan="2"><%=finishedCount%>/<%=count%>&nbsp;&nbsp;&nbsp;&nbsp;<a href="?command=list&time=<%=System.currentTimeMillis()%>">刷新</a></td>
      </tr>
     <tr bgcolor="red">
         <td bgcolor="#adff2f" width="<%=finishedLength%>"></td>
         <td width="<%=waitingLength%>">&nbsp;</td>
     </tr>
  </table>
  <textarea style="width:<%=totalLength%>px;height:360px;"><%=workerLog%></textarea>
</body>
</html><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.interface.reindexMP4.jsp");
    public BatchRunnable getRunnable(final Content content,final ContentProperty clip,final Device device,
                                     final ContentPropertyLogicInterface contentPropertyLogicInterface){
         final File mediaFile = new File(device.getLocalPath()+"/"+clip.getStringValue());
         final String msgHeader = content.getName()+","+clip.getName()+" ";
         return new BatchRunnable() {
             int resultCode=-1;
             String log = StringUtils.date2string(new Date())+" - " +msgHeader+
                     "尚未启动";
             public String getLogs() {
                 return log;
             }
             public int getResultCode() {
                 return resultCode;
             }

             public void afterFinished() {
                 if(resultCode == MediaUtils.SUCCESS){
                     String thumbPic = clip.getThumbPic();
                     if(thumbPic ==null){
                         thumbPic ="";
                     }
                     String indexedParam="reindexed=true";
                     int p = thumbPic.lastIndexOf(indexedParam);
                     if(p>0){
/*
                         String afterIndexedParameter = "";
                         if(p<thumbPic.length()-indexedParam.length()){
                             afterIndexedParameter = thumbPic.substring(p+indexedParam.length());
                         }
                         thumbPic = thumbPic.substring(0,p)+afterIndexedParameter;
*/
                     }else{
                         if(thumbPic.contains("?")){
                             thumbPic+="&";
                         }else{
                             thumbPic +="?";
                         }
                         clip.setThumbPic(thumbPic+"reindexed=true");
                         contentPropertyLogicInterface.updateThumbPic(clip);
                     }
                     log = "\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+
                             "已经完成";
                 }else if(resultCode == MediaUtils.ERROR_COMMAND_LINE){
                     log+="\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+
                             "命令行错误";
                 }else if(resultCode == MediaUtils.ERROR_OUT_FILE_TIME_ERROR){
                     log+="\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+
                             "生成文件时间不匹配，可能是生成错误！";
                 }else if(resultCode == MediaUtils.ERROR_FILE_NOT_EXISTS){
                     log+="\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+
                             "文件不存在："+mediaFile.getAbsolutePath();
                 }
             }

             public void beforeStart() {
                 log ="\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+
                         "已经启动";
             }
             public void run() {
                 MediaUtils mu=new MediaUtils();
                 resultCode = mu.reindex(mediaFile.getAbsolutePath(), mediaFile.getAbsolutePath());
             }
         };
     }
%>