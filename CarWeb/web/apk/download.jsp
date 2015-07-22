<%@ page import="java.io.*" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.rms.business.log.logic.logicInterface.ClientLogLogicInterface" %><%@ page
        import="java.util.Date" %><%@ page
        import="com.fortune.rms.business.log.model.ClientLog" %><%@ page
        import="com.fortune.common.Constants" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-5-13
  Time: 上午9:45
  下载apk文件
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String apkFileName = request.getParameter("fileName");
    String phone = (String) session.getAttribute(Constants.USER_PHONE_NUMBER);
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.apk.download.jsp");
    logger.debug("尝试下载，输入参数为："+request.getQueryString());
    if(phone==null){
        String token = request.getParameter("token");
        phone = request.getParameter("phone");
        if(verifyToken(token, phone)){
            logger.debug("客户端(ip=" +request.getRemoteAddr()+",phone="+phone+",user-agent="+request.getHeader("user-agent")+
                    ")尝试下载："+apkFileName);
            session.setAttribute(Constants.USER_PHONE_NUMBER,phone);
        }else{
            String updateCmd = request.getParameter("update");
            if("true".equals(updateCmd)){//低版本的升级请求中没有携带账号等信息
                logger.warn("低版本的客户端要求下载，设置默认号码13000000000，客户端信息："+request.getHeader("user-agent"));
                session.setAttribute(Constants.USER_PHONE_NUMBER,"13000000000");
            }else{
                String returnUrl = "/apk/player.jsp";
                if("woxin.apk".equals(apkFileName)||"woxuan".equals(apkFileName)){
                    returnUrl = "/page/hbMobile/download.html";
                }
                logger.warn("用户访问Referer："+request.getHeader("Referer"));
                logger.warn("用户还没有登录，让用户登录后下载:"+returnUrl);
                session.setAttribute("returnUrl",returnUrl);
                response.sendRedirect("../page/hbMobile/userLogin.jsp?returnUrl="+returnUrl);
                return;
            }
        }
    }
    String result;
    if(apkFileName != null){
        String fullFileName = application.getRealPath("/apk/"+apkFileName);
        File apkFile = new File(fullFileName);
        if(apkFile.getName().toLowerCase().endsWith(".apk")){
            if(apkFile.exists()&&!apkFile.isDirectory()&&apkFile.length()>1024*10){
                response.setContentType("application/vnd.android.package-archive");
                response.setHeader("Pragma","No-cache");
                response.setHeader("Cache-Control","no-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader( "Content-Disposition","attachment; filename=" + apkFileName);
                response.setContentLength((int)apkFile.length());
                try {

                    String userAgent = request.getHeader("user-agent");
                    String apkVersion = apkFileName.substring(0,apkFileName.length()-4);
                    InputStream in   =   new FileInputStream(apkFile);
                    byte[] dataBuffer = new byte[102400];
                    int readedLength = 1;
                    OutputStream apkOut = response.getOutputStream();
                    while(readedLength>0){
                        readedLength = in.read(dataBuffer);
                        apkOut.write(dataBuffer,0,readedLength);
                        if(readedLength<dataBuffer.length){
                            break;
                        }
                    }
                    PhoneRangeLogicInterface phoneRangeLogicInterface =(PhoneRangeLogicInterface)
                            SpringUtils.getBean("phoneRangeLogicInterface", session.getServletContext());
                    ClientLog clientLog = new ClientLog();
                    clientLog.setTime(new Date());
                    clientLog.setStatus(ClientLogLogicInterface.STATUS_DOWNLOAD);
                    clientLog.setClientVersion(apkVersion);
                    clientLog.setUserAgent(userAgent);
                    clientLog.setPhoneCode(phone);
                    if(apkVersion.contains("woxin")){
                        clientLog.setType(ClientLogLogicInterface.DOWNLOAD_TYPE_WOXIN);
                    }else if(apkVersion.contains("woxuan")){
                        clientLog.setType(ClientLogLogicInterface.DOWNLOAD_TYPE_WOXUAN);
                    }else{
                        clientLog.setType(ClientLogLogicInterface.DOWNLOAD_TYPE_INHE_PLAYER);
                    }
                    clientLog.setAreaId(phoneRangeLogicInterface.getAreaIdOfPhone(StringUtils.string2long(phone,-1)));
                    clientLog.setIp(request.getRemoteAddr());
                    clientLog.setUuid(request.getParameter("uuid"));
                    ClientLogLogicInterface clientLogLogicInterface =(ClientLogLogicInterface)
                            SpringUtils.getBean("clientLogLogicInterface", session.getServletContext());
                    clientLogLogicInterface.save(clientLog);
                    return;
                } catch (FileNotFoundException e) {
                    result = "apk文件找不到："+e.getLocalizedMessage();
                }
            }else{
                result = "文件找不到或者尺寸太小："+apkFile.getAbsoluteFile()+",大小："+apkFile.length();
            }
        }else{
            result = "文件名格式不对："+apkFileName;
        }
    }else{
        result = "没有输入参数";
    }
%><html>
<head>
    <title>无法下载</title>
</head>
<body>
无法下载文件：<%=result%><br/>
<%
    File[] files = new File(application.getRealPath("/apk")).listFiles();
    if(files!=null){
        for(File file:files){
            String fileName = file.getName();
            if(fileName.toLowerCase().endsWith(".apk")){
               out.println("<a href='?fileName=" +fileName+
                       "'>下载"+fileName+"</a><br/>");
            }
        }
    }
%>
</body>
</html>
<%@include file="/page/hbMobile/baseFunction.jsp"%>