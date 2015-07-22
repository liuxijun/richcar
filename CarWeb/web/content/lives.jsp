<%@ page import="java.util.ArrayList" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.server.message.TranscoderMessager" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="org.dom4j.Element" %><%@ page
        import="com.fortune.util.XmlUtils" %><%@ page
        import="org.dom4j.Node" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="java.net.URLDecoder" %><%@ page
        import="org.apache.commons.io.FileUtils" %><%@ page
        import="java.io.File" %><%@ page
        import="java.io.IOException" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-17
  Time: 下午5:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    List<String[]> lives = new ArrayList<String[]>();
/*
    lives.add(new String[]{"安徽卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3139397C317C383030307CE5AE89E5BEBDE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"北京卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3230307C317C383030307CE58C97E4BAACE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"兵团卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3137347C317C383030307CE585B5E59BA2E58DABE8A786207C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"东方卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3139377C317C383030307CE4B89CE696B9E58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"东南卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3132337C317C383030307C646E77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"甘肃卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3130377C317C383030307C677377737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"广东卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3132377C317C383030307C676477737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"广西卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3132307C317C383030307C677877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"贵州卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3133317C317C383030307C677A77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"河北卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131357C317C383030307C68656277737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"河南卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3132327C317C383030307C68656E77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"黑龙江卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3132347C317C383030307C686C6A77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"湖北卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131377C317C383030307C68756277737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"吉林卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3133307C317C383030307C6A6C77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"江苏卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3139387C317C383030307CE6B19FE88B8FE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"江西卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131347C317C383030307C6A7877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"辽宁卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3132397C317C383030307C6C6E77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"旅游卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3230327C317C383030307CE69785E6B8B8E58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"内蒙古卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131327C317C383030307C6E6D6777737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"宁夏卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131317C317C383030307C6E7877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"青海卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3130387C317C383030307C716877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"山东卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3133337C317C383030307C736477737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"山西卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131337C317C383030307C73616E7877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"陕西卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3139347C317C383030307CE99995E8A5BFE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"深圳卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3230317C317C383030307CE6B7B1E59CB3E58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"四川卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131387C317C383030307C736377737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"天津卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3132387C317C383030307C746A77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"西藏卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3130397C317C383030307C787A77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"新疆卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131307C317C383030307C786A77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"云南卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3131397C317C383030307C796E77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"浙江少儿","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3136377C317C383030307CE6B599E6B19FE5B091E584BF7C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"浙江卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3139357C317C383030307CE6B599E6B19FE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"中国教育电视台","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030347C3230337C317C383030307CE4B8ADE59BBDE69599E882B2E794B5E8A786E58FB07C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"重庆卫视","http://tvzhibo.wasu.tv/5B63686E5D575330303030303030337C3133347C317C383030307C637177737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
*/
    lives.add(new String[]{"安徽卫视","http://60.12.204.22/ahws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3139397C317C383030307CE5AE89E5BEBDE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"北京卫视","http://60.12.204.22/bjws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3230307C317C383030307CE58C97E4BAACE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"兵团卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3137347C317C383030307CE585B5E59BA2E58DABE8A786207C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"东方卫视","http://60.12.204.22/dfws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3534317C317C383030307CE4B89CE696B9E58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"东南卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3132337C317C383030307C646E77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"甘肃卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3130377C317C383030307C677377737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"广东卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3132377C317C383030307C676477737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"广西卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3132307C317C383030307C677877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"贵州卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3133317C317C383030307C677A77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"河北卫视","http://60.12.204.22/hebws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3131357C317C383030307C68656277737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"河南卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3132327C317C383030307C68656E77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"黑龙江卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3132347C317C383030307C686C6A77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"湖北卫视","http://60.12.204.22/hbws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3131377C317C383030307C68756277737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"吉林卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3133307C317C383030307C6A6C77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"江苏卫视","http://60.12.204.22/jsws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3139387C317C383030307CE6B19FE88B8FE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"江西卫视","http://60.12.204.22/jxws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3131347C317C383030307C6A7877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"辽宁卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3132397C317C383030307C6C6E77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"旅游卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3230327C317C383030307CE69785E6B8B8E58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"内蒙古卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3131327C317C383030307C6E6D6777737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"宁夏卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3131317C317C383030307C6E7877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"青海卫视","http://yitianstr.dj17kan.com5B63686E5D575330303030303030347C3533387C317C383030307CE99D92E6B5B7E58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"山东卫视","http://60.12.204.22/sdws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3133337C317C383030307C736477737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"山西卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3131337C317C383030307C73616E7877737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"陕西卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3532367C317C383030307CE99995E8A5BFE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"深圳卫视","http://60.12.204.22/szws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3230317C317C383030307CE6B7B1E59CB3E58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"四川卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3131387C317C383030307C736377737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"天津卫视","http://60.12.204.22/tjws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3132387C317C383030307C746A77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"西藏卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3533397C317C383030307CE8A5BFE8978FE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"新疆卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3534307C317C383030307CE696B0E79686E58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"云南卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3131397C317C383030307C796E77737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"浙江少儿","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3533307C317C383030307CE6B599E6B19FE5B091E584BF7C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"浙江卫视","http://60.12.204.22/zjws/z.m3u8","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3532347C317C383030307CE6B599E6B19FE58DABE8A7867C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"中国教育电视台","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030347C3230337C317C383030307CE4B8ADE59BBDE69599E882B2E794B5E8A786E58FB07C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});
    lives.add(new String[]{"重庆卫视","http://yitianstr.dj17kan.com/5B63686E5D575330303030303030337C3133347C317C383030307C637177737C687474707C74735B2F63686E5D5B74735D307C687474705B2F74735DVSDNSOOONERCOM00"});

    TranscoderMessager messager = new TranscoderMessager();
    String[] selectedIds = request.getParameterValues("selectedIds");
    String command = request.getParameter("command");
    String ip = request.getParameter("transcoderIp");
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.lives.jsp");
    if(ip==null){
        ip = "192.168.10.101";
    }
    String hlsIp = request.getParameter("hlsIp");
    if(hlsIp==null){
        hlsIp = "127.0.0.1";
    }
    int hlsPort = StringUtils.string2int(request.getParameter("hlsPort"),80);
    String result = "";
    int port = StringUtils.string2int(request.getParameter("transcoderPort"),2030);
    if("createXml".equals(command)){
        String xmlPath = request.getParameter("resultPath");
        if(xmlPath == null){
            xmlPath = application.getRealPath("/transcoderCfg");
        }
        String normalDir= "tv";//"普通单播";
        String multiDir = "multi";//"组播方式";
        String multiOnStreamingServerSide = "onRtspGw";//"组播流媒体服务器端";
        String bindInterface = "@192.168.10.107";
        String liveServer = request.getParameter("liveServer");
        int liveServerPort =StringUtils.string2int(request.getParameter("liveServerPort"),80);

        if(liveServer==null)liveServer="192.168.10.65";
       for(int i=1,l=lives.size();i<=l;i++){
           String[] live =lives.get(i-1);
           String name=live[0];
           String url = live[1];
           String numberStr = ""+i;
           if(i<10){
               numberStr="0"+numberStr;
           }
           if(i<100){
               numberStr="0"+numberStr;
           }
///*
           int phoneChannelId = (1000+i);
           int padChannelId = (1100+i);
           int flvChannelId = (2000+i);
           int rtspChannelId = (3000+i);
           int audioSampleRateHz = 48000;
           String fromPCDir = "fromPC";
           String extraParameters = "&amp;maxcashsize=512&amp;reconn=30";
           String sourceType = "l=ts_over_http&amp;i=";
           if(url.toLowerCase().contains("m3u8")){
               sourceType = "l=hls&amp;i=";
               extraParameters = "";
           }
           createXml(xmlPath+"/" +normalDir+
                   "/"+i+".hls.pad.xml",name,sourceType+url+extraParameters,
                   "l=ts_over_http&amp;i="+liveServer+":"+liveServerPort+"/"+padChannelId,
                   "h264",
                   "high_profile","31",720,405,1000,25,"aac",96,audioSampleRateHz,padChannelId,i+".pad.sdp");
           createXml(xmlPath+"/" +normalDir+
                   "/"+i+".hls.phone.xml",name,sourceType+url+extraParameters,
                   "l=ts_over_http&amp;i="+liveServer+":"+liveServerPort+"/"+phoneChannelId,
                   "h264","baseline","21",640,360,400,20,"aac",64,audioSampleRateHz,phoneChannelId,i+".phone.sdp");
           createXml(xmlPath+"/" +normalDir+
                   "/"+i+".flv.xml",name,sourceType+url+extraParameters,
                   "l=flv_over_http&amp;i="+liveServer+":"+liveServerPort+"/"+flvChannelId,
                   "h264","baseline","30",640,360,250,20,"aac",64,audioSampleRateHz,flvChannelId,i+".flv.sdp");
//*/
           createXml(xmlPath+"/" +normalDir+
                   "/"+i+".rtsp.xml",name,sourceType+url+extraParameters,
                   "l=rtp_over_http&amp;i="+liveServer+":554/"+rtspChannelId,
                   "h264","baseline","21",320,180,250,15,"aac",64,audioSampleRateHz,rtspChannelId,/*i+".rtsp.sdp"*/""+rtspChannelId);

           createXml(xmlPath+"/" +fromPCDir+
                   "/"+i+".hls.pad.xml",name,sourceType+url+extraParameters,
                   "l=ts_over_http&amp;i="+liveServer+":"+liveServerPort+"/"+padChannelId,
                   "transparent",
                   "","31",720,405,1000,25,"transparent",96,audioSampleRateHz,padChannelId,i+".pad.sdp");
           createXml(xmlPath+"/" +fromPCDir+
                   "/"+i+".hls.phone.xml",name,sourceType+url+extraParameters,
                   "l=ts_over_http&amp;i="+liveServer+":"+liveServerPort+"/"+phoneChannelId,
                   "transparent","","21",640,360,400,20,"transparent",64,audioSampleRateHz,phoneChannelId,i+".phone.sdp");
           createXml(xmlPath+"/" +fromPCDir+
                   "/"+i+".flv.xml",name,sourceType+url+extraParameters,
                   "l=flv_over_http&amp;i="+liveServer+":"+liveServerPort+"/"+flvChannelId,
                   "h264","baseline","30",640,360,250,20,"aac",64,audioSampleRateHz,flvChannelId,i+".flv.sdp");
//*/
           createXml(xmlPath+"/" +fromPCDir+
                   "/"+i+".rtsp.xml",name,sourceType+url+extraParameters,
                   "l=rtp_over_http&amp;i="+liveServer+":554/"+rtspChannelId,
                   "h264","baseline","21",320,180,250,15,"aac",64,audioSampleRateHz,rtspChannelId,/*i+".rtsp.sdp"*/""+rtspChannelId);

           //trans目录下是放到拉取华数源进行转码的转码服务器上
           createXml(xmlPath+"/" +multiDir+
                   "/"+i+".hls.pad.xml",name,sourceType+url+extraParameters,
                   "l=rtp&amp;i=225.1.1."+i+":5"+numberStr+bindInterface,
                   "h264","high_profile","31",720,405,1000,25,"aac",96,audioSampleRateHz,padChannelId,""+padChannelId);
           createXml(xmlPath+"/" +multiDir+
                   "/"+i+".hls.phone.xml",name,sourceType+url+extraParameters,
                   "l=rtp&amp;i=225.1.1."+i+":6"+numberStr+bindInterface,
                   "h264","main_profile","30",640,360,400,20,"aac",64,audioSampleRateHz,phoneChannelId,""+phoneChannelId);
///*
//rtsp暂时不支持组播方式，但是可以通过一个转码就行转发
           createXml(xmlPath+"/" +multiDir+
                   "/"+i+".rtsp.xml",name,sourceType+url+extraParameters,
                   "l=rtp&amp;i=225.1.1."+i+":7"+numberStr+bindInterface,
                   "h264","baseline","21",320,180,250,15,"aac",64,audioSampleRateHz,rtspChannelId,""+rtspChannelId);
//  */
           createXml(xmlPath+"/" +multiDir+
                   "/"+i+".flv.xml",name,sourceType+url+extraParameters,
                   "l=rtp&amp;i=225.1.1."+i+":8"+numberStr+bindInterface,
                   "h264","baseline","21",640,360,300,20,"aac",64,audioSampleRateHz,flvChannelId,""+flvChannelId);
           //tv目录下是放在每台rtsp服务器(流媒体服务器)上读取组播信息然后推送到每个rtsp_gw的转码，其实就是转播
           createXml(xmlPath+"/" +multiOnStreamingServerSide+
                   "/"+i+".hls.pad.xml",name,"l=rtp&amp;i=225.1.1."+i+":5"+numberStr,"l=ts_over_http&amp;i=127.0.0.1:80/0",
                   "h264",null,null,-1,-1,25,-1,"aac",-1,audioSampleRateHz,padChannelId,null);
           createXml(xmlPath+"/" +multiOnStreamingServerSide+
                   "/"+i+".hls.phone.xml",name,"l=rtp&amp;i=225.1.1."+i+":6"+numberStr,"l=ts_over_http&amp;i=127.0.0.1:80/0",
                   "h264",null,null,-1,-1,20,-1,"aac",-1,audioSampleRateHz,phoneChannelId,null);
           createXml(xmlPath+"/" +multiOnStreamingServerSide+
                   "/"+i+".hls.rtsp.xml",name,"l=rtp&amp;i=225.1.1."+i+":7"+numberStr,"l=rtp_over_http&amp;i=127.0.0.1:80/0",
                   "h264",null,null,-1,-1,20,-1,"aac",-1,audioSampleRateHz,rtspChannelId,null);
           createXml(xmlPath+"/" +multiOnStreamingServerSide+
                   "/"+i+".hls.flv.xml",name,"l=rtp&amp;i=225.1.1."+i+":8"+numberStr,"l=flv_over_http&amp;i=127.0.0.1:80/0",
                   "h264",null,null,-1,-1,20,-1,"aac",-1,audioSampleRateHz,flvChannelId,null);

       }
    }else if("createSql".equals(command)){
        for(int i=1,l=lives.size();i<=l;i++){
            String[] live =lives.get(i-1);
            String name=live[0];
            String url = live[1];
            out.println("insert into tr_source(id,name,source,type,status,description)values(" +i+
                    ",'"+name+"','l=ts&i="+url+"&amp;maxcashsize=512&amp;reconn=30',2,1,'"+name+"TS格式源'"+
                    ");");
        }
        return;
    }
    String allTransferInfo = messager.listTranscoderTask(ip, port);
//    Map<String,Map<String,Object>> result = new HashMap<String,Map<String,Object>>();
    if (allTransferInfo != null&&!"".equals(allTransferInfo.trim())) {
        result +="<xml>"+allTransferInfo+"</xml>";
        Element root = XmlUtils.getRootFromXmlStr(allTransferInfo);
        if (root != null) {
            List sessions = root.selectNodes("list/item");
            if(sessions!=null){
                for(Object o:sessions){
                    Node sessionNode = (Node) o;
                    Map<String,String> params = getParameters(sessionNode,"param");
                    String sessionId = params.get("TTID");
                    String source = URLDecoder.decode(params.get("src"),"UTF-8");
                    for(String[] live:lives){
                        String url = live[1];
                        if(source.contains(url)){
                            live[2]=sessionId;
                            break;
                        }
                    }
                }
            }
        } else {
            logger.error("getTaskCount 时 xml解析过程出现问题：" + allTransferInfo);
        }
    } else {
        logger.debug(ip + ":" + port + " 数据返回为空，无法继续！");
    }
    if(selectedIds!=null){
        messager.setParameter("","");
        messager.setExtraParameter("\t<param n=\"recover\" v=\"1\" />\r\n"+
                "\r\n"+
                "\t<param n=\"waiting\" v=\"0\" />\r\n"+
                "\r\n"+
                "\t<param n=\"offline\" v=\"0\" />\r\n"+
                "\r\n"+
                "\r\n"+
                "\t<character version=\"1.0\">\r\n"+
                "\r\n"+
                "\t\t<video_codec>\r\n"+
                "\r\n"+
                "\t\t\t<__param n=\"name\"      v=\"transparent\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"name\"      v=\"h264\"         />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"profile\"   v=\"baseline\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"fastmode\"  v=\"2\"   />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"width\"     v=\"640\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"height\"    v=\"480\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"bitrate\"   v=\"300\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"bitratectrl\"     v=\"0\"  />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"framerate\"       v=\"-1\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"keyframerate\"    v=\"2\"  />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"referenceframes\" v=\"3\"  />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"bframes\"         v=\"3\"  />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"multithreads\"    v=\"1\"  />\r\n"+
                "\r\n"+
                "\t\t</video_codec>\r\n"+
                "\r\n"+
                "\t\t<audio_codec>\r\n"+
                "\r\n"+
                "\t\t\t<__param n=\"name\"      v=\"transparent\" />\r\n"+
                "\r\n"+
                "\t\t\t<__param n=\"name\"      v=\"mp2a\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"name\"        v=\"aac\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"subtype\"     v=\"\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"samplerate\"  v=\"-1\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"channels\"    v=\"1\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"bitrate\"     v=\"32\" />\r\n"+
                "\r\n"+
                "\t\t\t<param n=\"batchnumb\" v=\"3\" />\r\n"+
                "\r\n"+
                "\t\t\t<_param n=\"volume\"    v=\"32\" />\r\n"+
                "\r\n"+
                "\t\t</audio_codec>\r\n"+
                "\r\n"+
                "\t\t<miscellaneous>\r\n"+
                "\r\n"+
                "\t\t\t<__param n=\"affinity_profile\" v=\"1\" />\r\n"+
                "\r\n"+
                "\t\t\t<__param n=\"syncqueue_deep\"   v=\"0\" />\r\n"+
                "\r\n"+
                "\t\t\t<__param n=\"independent\"      v=\"1\" />\r\n"+
                "\r\n"+
                "\t\t\t<__param n=\"ts_filterloss\"    v=\"0\" />\r\n"+
                "\r\n"+
                "\t\t</miscellaneous>\r\n"+
                "\r\n"+
                "\t</character>\r\n"+
                "\n");
        for(String idStr:selectedIds){
            logger.debug("准备处理索引："+idStr);
            int id = StringUtils.string2int(idStr,-1);
            if(id>=0&&id<lives.size()){
                String[] live = lives.get(id);
                result+="准备处理"+idStr+":" + live[0]+
                        "...";
                if("start".equals(command)){

                    String tempStr = "启动："+(messager.startTranscoder(ip,port,"l=ts_over_http&amp;i="+live[1],"l=ts_over_http&amp;i="+hlsIp+":" +
                            hlsPort+"/100"+id));
                    result+=tempStr+"<br/>";
                    logger.debug("处理"+live[0]+"结果："+tempStr);
                }else if("stop".equals(command)){
                    String ttid = live[2];
                    String tempResult;
                    if(ttid==null||"".equals(ttid)){
                        tempResult = "直播："+live[0]+"尚未启动！";
                    }else{
                        String stopResult = messager.stopTransferSession(ip,port,ttid);
                        if("success".equals(stopResult)){
                            live[2]="";
                        }
                        tempResult =live[0]+"处理结果："+ stopResult;
                    }
                    logger.debug(tempResult);
                    result+="<br/>"+tempResult;
                }
            }
        }
    }else{
        result = result+"没有输入任何参数<br/>";
    }
    if(command==null){
        command= "";
    }
%>
<html>
<head>
    <title>直播启动与停止</title>
    <script type="text/javascript">
        function sendToServer(command){
            liveForm.command.value = command;
            liveForm.submit();
        }
        function stopSelected(){
           sendToServer("stop");
        }
        function startSelected(){
           sendToServer("start");
        }
    </script>
</head>
<body>
<p><%=result%></p>
<form action="?" method="post" name="liveForm" id="liveForm">
    <table border="1" cellspacing="0">
        <tr bgcolor="#E0E0E0">
            <td width="120">直播节目</td>
            <td width="120">当前状态</td>
            <td width="660">连接</td>
        </tr>
        <%
            for(int i=0,l=lives.size();i<l;i++){
                String[] live = lives.get(i);
                String ttid = "";
                if(live.length>2){
                    ttid = live[2];
                }
%>     <tr>
        <td>
            <input type="checkbox" name="selectedIds" value="<%=i%>"><%=live[0]%>
        </td>
        <td>
            <%=(ttid==null||"".equals(ttid.trim()))?"尚未启动":"已经启动"%>
        </td>
        <td>
            <%=live[1]%>
        </td>
</tr>
        <%
            }
        %>
        <tr>
            <td colspan="3">
                <label for="transcoderIp">转码服务器IP：</label><input style="width:400px" id="transcoderIp" name="transcoderIp" value="<%=ip%>"/>
                <label for="transcoderPort">转码服务器端口：</label><input style="width:100px" id="transcoderPort" name="transcoderPort" value="<%=port%>"/>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <label for="hlsIp">流化服务器IP：</label><input style="width:400px" id="hlsIp" name="hlsIp" value="<%=hlsIp%>"/>
                <label for="hlsPort">流化服务器端口：</label><input style="width:100px" id="hlsPort" name="hlsPort" value="<%=hlsPort%>"/>
            </td>
        </tr>
        <tr>
            <td colspan="3" align="center"><input type="button" value="停止选中的直播" onclick="stopSelected()">&nbsp;&nbsp;
            <input type="button" value="启动选中的直播" onclick="startSelected()"></td>
        </tr>
    </table>
    <input type="hidden" name="command" value="<%=command%>">
</form>
</body>
</html><%!
    public Map<String,String> getParameters(Node node,String listName){
        List parameters = node.selectNodes(listName);
        Map<String,String> result = new HashMap<String,String>();
        if(parameters!=null){
            for(Object o:parameters){
                Node parameter = (Node) o;
                String name = XmlUtils.getValue(parameter, "@n", null);
                if(name!=null){
                    String value = XmlUtils.getValue(parameter, "@v", null);
                    result.put(name,value);
                }
            }
        }
        return result;
    }

    public String getXml(String name ,String url,String dst,
                         String videoCodec,
                         String profile,String level,int width,int height,int bandwidth,int frameRate,
            String audioCodec,int aBandwidth,int sampleRateHz,
            int liveChannel,String sdpFileName){
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n").append(
                "<req version=\"1.0\">\r\n").append(
                "<task type=\"transc\" target=\"start\">\r\n").append(
                "\r\n").append(
                "\t<param n=\"src\" v=\"").append(url)
                .append("\" />\r\n").append(
                "\t<param n=\"dst\" v=\"").append(dst).append("\" />\r\n").append(
                "\t\r\n").append(
                "\t<param n=\"static\"  v=\"1\" />\r\n").append(
                "\t<param n=\"recover\" v=\"0\" />\r\n").append(
                "\t<param n=\"waiting\" v=\"0\" />\r\n").append(
                "\t<param n=\"offline\" v=\"0\" />\r\n").append(
                "\r\n").append(
                "\t<character version=\"1.0\">\r\n");
        if(profile==null||"".equals(profile.trim())){
            xml.append(
                    "\t\t<video_codec>\r\n")
                    .append("\t\t\t<param n=\"name\"      v=\"transparent\" />\r\n").append(
                    "\t\t</video_codec>\r\n").append(
                    "\t\t<audio_codec>\r\n").append(
                    "\t\t\t<param n=\"name\"      v=\"transparent\" />\r\n").append(
                    "\t\t\t<param n=\"batchnumb\"   v=\"6\" />\r\n").append(
                    "\t\t</audio_codec>\r\n");
        }else{
                xml.append(
                        "\t\t<video_codec>\r\n")
                        .append("\t\t\t<param n=\"name\"      v=\"" +videoCodec+
                                "\"         />\r\n").append(
                "\t\t\t<param n=\"profile\"   v=\"" ).append(profile).append("\" />\r\n").append(
                "\t\t\t<param n=\"level\"   v=\"" ).append(level).append("\" />\r\n").append(
                "\t\t\t<param n=\"fastmode\"  v=\"2\"   />\r\n").append(
                "\t\t\t<param n=\"width\"     v=\"" ).append(width).append(
                "\" />\r\n").append(
                "\t\t\t<param n=\"height\"    v=\"" ).append(height).append(
                "\" />\r\n").append(
                "\t\t\t<param n=\"bitrate\"   v=\"" ).append(bandwidth).append(
                "\" />\r\n").append(
                "\t\t\t<param n=\"bitratectrl\"     v=\"1.0\"  />\r\n").append(
                "\t\t\t<param n=\"framerate\"       v=\"").append(frameRate)
                        .append("\" />\r\n").append(
                "\t\t\t<param n=\"keyframerate\"    v=\"3\"  />\r\n").append(
                "\t\t\t<param n=\"referenceframes\" v=\"3\"  />\r\n").append(
                "\t\t\t<param n=\"bframes\"         v=\"3\"  />\r\n").append(
                "\t\t\t<param n=\"multithreads\"    v=\"4\"  />\r\n").append(
                "\t\t</video_codec>\r\n").append(
                "\t\t<audio_codec>\r\n").append(
                "\t\t\t<__param n=\"name\"      v=\"transparent\" />\r\n").append(
                "\t\t\t<param n=\"name\"        v=\"" +audioCodec+
                        "\" />\r\n").append(
                "\t\t\t<param n=\"samplerate\"  v=\"").append(sampleRateHz).append("\" />\r\n").append(
                "\t\t\t<param n=\"channels\"    v=\"1\" />\r\n").append(
                "\t\t\t<param n=\"bitrate\"     v=\"").append(aBandwidth).append(
                "\" />\r\n").append(
                "\t\t\t<param n=\"batchnumb\"   v=\"6\" />\r\n").append(
                "\t\t</audio_codec>\r\n");
        }
        xml.append(
                "\t\t<miscellaneous>\r\n").append(
                "\t\t\t<__param n=\"syncqueue_deep\"   v=\"0\" />\r\n").append(
                "\t\t\t<__param n=\"independent\"      v=\"1\" />\r\n").append(
                "\t\t\t<__param n=\"ts_filterloss\"    v=\"0\" />\r\n").append(
                "\t\t\t<param n=\"hls_group\"          v=\"" ).append(liveChannel)
                .append("\" />\r\n").append(
                "\t\t\t<param n=\"hls_duration\"       v=\"6\" />\r\n");
        if(sdpFileName!=null&&!"".equals(sdpFileName)){
            xml.append("\t\t\t<param n=\"sdp_file\" v=\"").append(sdpFileName).append("\" />\r\n");
        }
        xml.append(
                "\t\t</miscellaneous>\r\n").append(
                "\t</character>\r\n").append(
                "</task>\r\n").append(
                "</req>\n").append("<!--电视台：").append(name)
                .append("-->\r\n");
        return xml.toString();
    }
    public boolean createXml(String xmlFile,String name ,String url,String dst,
                             String videoCodec,
                             String profile,String level,int width,int height,int bandwidth,int frameRate,
                             String audioCodec,int aBandwidth,int sampleRateHz,int liveChannel,String sdpFileName){
        try {
            FileUtils.writeStringToFile(new File(xmlFile),getXml(name,url,dst,videoCodec,profile, level, width,
                    height,bandwidth,frameRate,audioCodec,aBandwidth,sampleRateHz,liveChannel,sdpFileName),"UTF-8");
        } catch (IOException e) {
            return false;
        }
        return true;
    }
%>