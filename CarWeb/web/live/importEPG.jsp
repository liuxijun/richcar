<%@ page import="com.fortune.rms.business.live.logic.logicInterface.EPGLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.rms.business.live.model.EPG" %><%@ page
        import="com.fortune.util.FileUtils" %><%@ page
        import="com.fortune.util.PageBean" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page import="java.io.File" %><%@ page import="java.util.*" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/6/20
  Time: 12:30
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",request.getServletContext());
    EPGLogicInterface epgLogicInterface = (EPGLogicInterface) SpringUtils.getBean("epgLogicInterface",request.getServletContext());
    String filePath = request.getParameter("filePath");
    String fileContent = request.getParameter("fileContent");
    String logs = null;
    if(filePath!=null&&!filePath.isEmpty()){
        filePath = application.getRealPath(filePath);
        File file=new File(filePath);
        if(file.exists()){
            fileContent = FileUtils.readFileInfo(file.getAbsolutePath());
            logs=process(fileContent, epgLogicInterface, contentLogicInterface);
        }else{
            logs="文件不存在："+file.getAbsolutePath();
        }
    }else{
        filePath = "";
        if(fileContent!=null&&!fileContent.isEmpty()){
            logs = process(fileContent,epgLogicInterface,contentLogicInterface);
        }
    }
    if(fileContent==null){
        fileContent = "";
    }
    if("text".equals(request.getParameter("format"))){
        out.println(logs);
        return;
    }
%><html>
<head>
    <title>节目单导入</title>
    <style type="text/css">
        .inputBox{
            width:600px;
        }
    </style>
</head>
<body>
<form action="?doImport=true" method="post">
    <table>
        <tr>
            <td colspan="2">可以输入文件名，或直接黏贴节目单文本到“节目单”</td>
        </tr>
        <tr>
            <td><label for="filePath">文件名：</label></td>
            <td><input id="filePath" type="text" class="inputBox" name="filePath" value="<%=filePath%>"></td>
        </tr>
        <%
            if(logs!=null){
%>
        <tr>
            <td>导入结果</td>
            <td><pre><%=logs%></pre></td>
        </tr>
        <%
            }
        %>
        <tr>
            <td><label for="fileContent">节目单：</label></td>
            <td><textarea id="fileContent" rows="80" cols="80" class="inputBox" style="height: 200px;" name="fileContent"><%=fileContent%></textarea></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="启动导入">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
<%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.importEPG.jsp");
    public String processFile(String fileName,EPGLogicInterface epgLogicInterface,ContentLogicInterface contentLogicInterface){
        return process(FileUtils.readFileInfo(fileName),epgLogicInterface,contentLogicInterface);
    }
    public String process(String epgs,EPGLogicInterface epgLogicInterface,ContentLogicInterface contentLogicInterface){
        String logs = "";
        List<String[]> names = new ArrayList<String[]>();
        names.add(new String[]{"中央","CCTV"});
        names.add(new String[]{"十一套","-11"});
        names.add(new String[]{"十二套","-12"});
        names.add(new String[]{"十三套","-13"});
        names.add(new String[]{"十四套","-14"});
        names.add(new String[]{"十五套","-15"});
        names.add(new String[]{"十六套","-16"});
        names.add(new String[]{"十七套","-17"});
        names.add(new String[]{"十八套","-18"});
        names.add(new String[]{"一套","-1"});
        names.add(new String[]{"二套","-2"});
        names.add(new String[]{"三套","-3"});
        names.add(new String[]{"四套","-4"});
        names.add(new String[]{"五套","-5"});
        names.add(new String[]{"六套","-6"});
        names.add(new String[]{"七套","-7"});
        names.add(new String[]{"八套","-8"});
        names.add(new String[]{"九套","-9"});
        names.add(new String[]{"十套","-10"});
        names.add(new String[]{"高清","HD"});
        if(epgs!=null&&!epgs.isEmpty()){
            String[] lines = epgs.split("\n");
            String channelHeader = "Channel:";
            String channelName=null;
            String dateHeader = "Date:";
            String date="";
            String dateFormat = "yyyyMMddHHmm";
            Content live = null;
            String liveHeader = "live:";
            String liveTail = ":live";
            String[] hdTails = new String[]{"（高清）","(高清)","HD","高清"};
            Date now = new Date();
            PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,null,null);
            List<Content> liveList = contentLogicInterface.list(-1L,-1L,15884423L,null,pageBean);
            logger.debug("一共有"+liveList.size()+"直播内容");
            Map<String,Content> lives = new HashMap<String,Content>();
            for(Content content:liveList){
                if(content!=null){
                    String contentName = content.getName();
                    lives.put(contentName,content);
                    if(contentName.toLowerCase().startsWith("cctv")){
                        contentName = contentName.substring(4);
                        if(!contentName.startsWith("-")){
                            contentName ="-"+contentName;
                        }
                        contentName = "CCTV"+contentName;
                        lives.put(contentName,content);
                    }
                    for(String hdTail:hdTails){
                        if(contentName.endsWith(hdTail)){
                            String tempName = contentName.substring(0,contentName.length()-hdTail.length());
                            logger.debug("发现高清节目，进行修正："+contentName+"->"+tempName);
                            lives.put( tempName,content);
                            for(String t:hdTails){
                                lives.put( tempName+t,content);
                            }
                        }
                    }
                }
            }
            int epgImported=0;
            int channelCount=0;
            int skipChannelCount=0;
            int epgChannelImported=0;
            String importedChannels="";
            int todayImported = 0;
            for(String line:lines){
                line = line.trim();
                if(line.startsWith(channelHeader)){
                    if(channelName != null){
                        if(live!=null){
                            logs+=channelName+"导入"+date+"的节目单"+epgChannelImported+"条！\n";
                        }else{
                            logs+=channelName+"没有在我们的平台上添加！不导入节目单！\n";
                        }
                    }
                    if(date!=null&&!date.isEmpty()){
                        logger.debug("上一个日期是："+date+",导入了"+todayImported+"条节目单（也可能是其他频道的内容）");
                    }
                    date = null;
                    todayImported = 0;
                    channelName = line.substring(channelHeader.length());
                    for(String[] name:names){
                        channelName = channelName.replace(name[0],name[1]);
                    }
                    live = lives.get(channelName);
                    if(live==null){
                        for(String tail:hdTails){
                            if(channelName.endsWith(tail)){
                                String tempName = channelName.substring(0,channelName.length()-tail.length());
                                logger.debug("发现是高清节目，修正一下再找找看："+channelName+"->"+tempName);
                                live = lives.get(tempName);
                                if(live!=null){
                                    logger.debug("修正后找到了："+channelName+"->"+tempName+",对应媒体："+live.getName());
                                }else{
                                    logger.debug("修正后也没找到："+channelName+"->"+tempName+",没有对应的媒体！");
                                }
                                break;
                            }
                        }
                    }
                    epgChannelImported=0;
                    if(live==null){
                        skipChannelCount++;
                        String log = "该频道尚未在我平台完整建立："+channelName;
                        logs+=log+"\n";
                        logger.debug(log);
                    }else{
                        importedChannels += channelName+",";
                        channelCount++;
                        logger.debug("发现频道："+channelName+",准备开启导入！");
                    }
                }else if(line.startsWith(dateHeader)){
                    if(date!=null&&!date.isEmpty()){
                        logger.debug("上一个日期是："+date+",导入了"+todayImported+"条节目单（也可能是其他频道的内容）");
                    }
                    date = line.substring(dateHeader.length());
                    logger.debug("发现日期："+date);
                    todayImported = 0;
                }else {
                    if(live!=null){
                        String[] timeAndProgram = line.split("\\|");
                        if(timeAndProgram.length>=2){
                            String[] time = timeAndProgram[0].split("-");
                            String program = timeAndProgram[1];
                            if(time.length>=2){
                                Date startTime = StringUtils.string2date(date+ time[0].replace(":",""),dateFormat);
                                Date stopTime =  StringUtils.string2date(date+time[1].replace(":",""),dateFormat);
                                if(startTime.after(stopTime)){
                                    //如果结束时间在起始时间之后，那可能是隔天的节目，23:55-01:15 昨天晚上11点55分到今天凌晨1点15分
                                    //  也可能是今天晚上11点55分到明天凌晨1点15分
                                    if(todayImported==0){
                                        //logs+="发现一个隔天节目，起始日期是昨天："+line+"\n";
                                        logger.debug("发现一个隔天节目，起始日期是昨天："+line);
                                        startTime.setTime(startTime.getTime()-24*3600L*1000L);
                                    }else{
                                        //logs+="发现一个隔天节目，结束日期是明天："+line+"\n";
                                        logger.debug("发现一个隔天节目，结束日期是明天："+line);
                                        stopTime.setTime(stopTime.getTime()+24*3600L*1000L);
                                    }
                                }
                                Long status= EPGLogicInterface.STATUS_WAITING;
                                if(startTime.before(now)){
                                    if(stopTime.before(now)){
                                        status = EPGLogicInterface.STATUS_FINISHED;
                                    }else{
                                        status = EPGLogicInterface.STATUS_WORKING;
                                    }
                                }
                                String liveIdStr = live.getContentId();
                                Long liveId = -1L;
                                if(liveIdStr!=null&&liveIdStr.startsWith(liveHeader)){
                                    liveIdStr = liveIdStr.substring(liveHeader.length());
                                    if(liveIdStr.endsWith(liveTail)){
                                        liveIdStr = liveIdStr.substring(0,liveIdStr.length()-liveTail.length());
                                        liveId = StringUtils.string2long(liveIdStr,-1);
                                    }
                                }
                                EPG epg  = new EPG(-1,program,null,startTime,stopTime,liveId,live.getId(),status);
                                EPG savedEpg = epgLogicInterface.insertEPG(epg);
                                if(savedEpg!=null){
                                    epgChannelImported++;
                                    epgImported++;
                                    todayImported++;
                                    logger.debug("日志："+savedEpg.getExtraObj());
                                }else{
                                    logger.warn("未能插入节目单："+epg.getName()+","+StringUtils.date2string(epg.getBeginTime())
                                            +"->"+StringUtils.date2string(epg.getEndTime()));
                                }
                            }
                        }
                    }
                }
            }
            String tempLog ="累计导入"+channelCount+"个频道" +importedChannels+
                    "，放弃了" +skipChannelCount+
                    "个频道节目单，共" +epgImported+
                    "条节目单";
            logs+=tempLog+"\n";
            logger.debug(tempLog);
        }else{
            logs+="节目单内容为空！\n";
        }
        return logs;
    }
%>