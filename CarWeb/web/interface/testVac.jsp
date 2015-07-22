<%@ page
        import="com.fortune.util.*" %><%@ page import="java.util.Date" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page import="com.fortune.server.message.ServerMessager" %><%@ page import="net.sf.json.JSONObject" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-24
  Time: 上午8:38
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String command = request.getParameter("command");
    String result = "";
    final BatchWorker worker = BatchExecutor.getInstance().getWorker("testVac",2);
    String workerLog = "";
    int count = 0;
    int finishedCount = 0;
    final int repeatTimes = StringUtils.string2int(request.getParameter("repeatTimes"),10);
    //默认启动100个线程，也就是重复100次
    if("start".equals(command)){
        if(worker!=null){
            //boolean lastWorkTerminated = worker.isTerminated();
            //ExecutorService executorService = worker.getExecutor();
            if((!worker.isTerminated())&&(worker.getCount()>0)){
                result = "上次任务还没有结束！";
            }else{
                worker.reset();
                final long[] contentIds = new long[]{280677959,28321145,28321156,28321123,28320791,28320773};
                final String[] userIds = new String[]{
                        "18631550823","18631704068","15512155900","18631902296","13091173369",
                        "18632267105","15633659266","18632922599","15503183409","18630303910",
                        "15531887118","13081028482","18633442586","18630267018","18633729723",
                        "15531831196","18603255965","18631710021","18632532309","15511337979",
                        "18630267018","15632729227","18631583046","15632120343","18631927019"};
//                final String[] productIds=new String[]{"","",""};
                logger.debug(result);
                Thread thread = new Thread(){
                    public void run(){
                        for(int i=0;i<repeatTimes;i++){
                            for(long contentId:contentIds){
                                for(String userId:userIds){
                                    BatchRunnable runnable = getRunnable(contentId,userId);
                                    worker.execute(runnable);
                                }
                            }
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
        if(logLength>3000){
            workerLog = log.substring(logLength-3000);
        }else{
            workerLog = log;
        }
        count = worker.getCount();
        finishedCount = worker.getFinishedCount();
    }
%><html>
<head>
    <title>VAC压力测试</title>
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
      <tr>
          <td colspan="2">
              <%
                  if(worker!=null){
%>              <table>

              <tr>
                  <td>启动时间：</td><td><%=StringUtils.date2string(worker.getStartTime())%></td>
              </tr>
              <tr>
                  <td>截止时间：</td><td><%=StringUtils.date2string(worker.getStopTime())%></td>
              </tr>
              <tr>
                  <td>最常时间：</td><td><%=worker.getMaxDuration()%></td>
              </tr>
              <tr>
                  <td>最短时间：</td><td><%=worker.getMinDuration()%></td>
              </tr>
              <tr>
                  <td>平均时间：</td><td><%=(worker.getFinishedCount()>0)?worker.getAllDuration()/worker.getFinishedCount():0%></td>
              </tr>
          </table>
              <%
                  }
              %>
          </td>
      </tr>
  </table>
  <textarea cols="80" rows="60" style="width:<%=totalLength%>px;height:360px;"><%=workerLog%></textarea>
</body>
</html><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.interface.reindexMP4.jsp");
    public BatchRunnable getRunnable(final long contentId,final String userId){
         return new BatchRunnable() {
             int resultCode=-1;

             String msgHeader = "测试媒体：" +contentId+"，用户："+userId+" ";
             String log = StringUtils.date2string(new Date())+" - "+ msgHeader+
                     "尚未启动";
             long duration = -1;
             public String getLogs() {
                 return log;
             }

             public long getDuration(){
                 return duration;
             }

             public int getResultCode() {
                 return resultCode;
             }

             public void afterFinished() {
             }

             public void beforeStart() {
                 log ="\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+
                         "已经启动";
             }

             public void run() {
                 resultCode =0;
                 String url = "/user/user!checkPlayPermissions.action";
                 ServerMessager messager = new ServerMessager();
                 Date startTime = new Date();
                 String result = messager.getMessage("127.0.0.1",80,url,"userId="+userId+"&contentId="+contentId);
                 JSONObject obj = JSONObject.fromObject(result);
                 if(obj.getBoolean("success")){
                     resultCode = 0;
                 }else{
                     resultCode = 1201;
                 }
                 Date stopTime = new Date();
                 duration = stopTime.getTime()-startTime.getTime();
                 log="\r\n"+ StringUtils.date2string(new Date())+" - " +msgHeader+"，测试结果："+resultCode+",耗时："+duration+"毫秒";
             }
         };
     }
%>