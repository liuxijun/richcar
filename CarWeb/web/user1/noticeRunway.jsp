<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-6-15
  Time: 15:12:07
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@page
        import="com.runway.race.ipm.*,cn.sh.guanghua.mediastack.user.*,cn.sh.guanghua.util.tools.*,
        cn.sh.guanghua.mediastack.common.Constants" %><%@ page import="org.apache.log4j.Logger,
        cn.sh.guanghua.cache.CacheManager,
        cn.sh.guanghua.mediastack.dataunit.*,
        java.util.List,
        cn.sh.guanghua.mediastack.common.Db" %><%
    String errorInfo = "ipmFlag为空";
    long mediaIcpId = ParamTools.getLongParameter(request,"mediaicp_id",-1);
    long clipId = ParamTools.getLongParameter(request,"clip_id",-1);
    long mediaId = ParamTools.getLongParameter(request,"media_id",-1);
    long channelId = ParamTools.getLongParameter(request,"channel_id",-1);
    //long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
    long impId = -1;
    int resultID = -1;
    Ipm ipm = new IpmImpl();
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    //String userId = "";
    String userInfo ;
    String sessionId  ;
    String clientIp = request.getRemoteAddr();
    boolean  result = false;
    logger.debug(request.getQueryString());
    String ipmFlag = request.getParameter("ipmFlag");
    if(su==null){
        userInfo = request.getParameter("userInfo");
        sessionId = request.getParameter("userSessionId");
    }else{
        userInfo = su.getUserInfo();
        sessionId = su.getSessionId();
    }
    if (sessionId != null){
        //userId = su.getUserId();
        String fileID =request.getParameter("fileID");
        if(fileID!=null){
            fileID = java.net.URLDecoder.decode(fileID,"UTF-8");
        }else{
            logger.debug("fileID为空，开始重新计算");
            MediaIcp mediaIcp =  (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
            Media media ;
            if(mediaIcp!=null){
                media = (Media)CacheManager.getInstance().getFromDB("Media",mediaIcp.getMediaId());
            }else{
                media = (Media)CacheManager.getInstance().getFromDB("Media",mediaId);
            }
            if(media!=null){
                impId = media.getMediaImpid();
                fileID = media.getMediaName();
                MediaClip mediaClip = (MediaClip) CacheManager.getInstance().getFromDB("MediaClip", clipId);
                if(mediaClip ==null){
                    logger.error("影片片段为空，不能继续：clipId="+clipId);
                }else{
                    List clips = null;
                    try {
                        clips = Db.MediaClip().getMediaClipsByMediaid(media.getMediaId());
                    } catch (Exception e) {
                        logger.error("获取播放列表发生异常："+e.getMessage());
                    }
                    if(clips.size() == 1){
                    }else{
                        fileID = fileID + " ["+mediaClip.getMediaclipVolume()+"]";
                    }
                }
            }else{
               logger.error("媒体为空，无法获取fileID！参数为"+request.getQueryString());
            }
        }
        String spID = request.getParameter("spID");
        if(spID==null){
            spID=request.getParameter("spId");
        }
        if(spID==null){
            logger.warn("spID为空，准备重新获取:"+request.getQueryString());
            if(impId>0){
                Imp imp = null;
                try {
                    imp = (Imp) Db.Imp().getObject(impId);
                } catch (Exception e) {
                    logger.error("获取");
                }
                if(imp!=null&& "是".equals(imp.getImpEmail())){
                   spID = imp.getImpAddress(); 
                }
            }else{
                logger.error("impID无法获取："+request.getQueryString());
            }
        }
        if(spID!=null){
            fileID = fileID+"|"+spID;
        }
        if(ipmFlag==null){
            logger.warn("ipmFlag为空，准备从数据里进行计算："+request.getQueryString());
            if(impId>0){
                Imp imp = null;
                try {
                    imp = (Imp) Db.Imp().getObject(impId);//Db.Imp().getObject(impId);
                } catch (Exception e) {
                    logger.error("获取IMP时发生异常："+e.getMessage());
                }
                if(imp!=null&& "是".equals(imp.getImpEmail())){
                   ipmFlag = "0"; 
                }else{
                    ipmFlag = "1";
                }
            }else{
                logger.error("impID无法获取："+request.getQueryString());
            }
        }
        String rootCategoryID = request.getParameter("rootCategoryID");
        if(rootCategoryID==null){
            logger.warn("rootCategoryID为空，准备重新获取："+request.getQueryString());
            long rootChannelID = 0;
            Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel",channelId);
            if(channel!=null){
                if(channel.getMonthFee() == -1){//当前栏目收费--1,免费--0
                    ipmFlag = null;
                }else{
                    if(channel.getChannelStatus() == 0){
                        if(channel.getChannelParentid() != -1){//子栏目
                            Channel parentChannel = (Channel)CacheManager.getInstance().getFromDB("Channel",channel.getChannelParentid());
                            if(parentChannel.getChannelStatus() == 0){
                                if(parentChannel.getChannelParentid() != -1){
                                    Channel pChannel = (Channel)CacheManager.getInstance().getFromDB("Channel",parentChannel.getChannelParentid());
                                    if(pChannel.getChannelStatus() == 1){
                                        rootChannelID = pChannel.getChannelId();
                                    }
                                }
                            }else{
                                rootChannelID = parentChannel.getChannelId();
                            }
                        }
                    }else{
                        rootChannelID = channel.getChannelId();
                    }
                    Channel rootChannel = (Channel)CacheManager.getInstance().getFromDB("Channel", rootChannelID);
                    if(rootChannel == null){
                        rootCategoryID = "0";
                    }else{
                         rootCategoryID = rootChannel.getCategoryID();
                    }
                    if("0".equals(rootCategoryID)){//所有频道免费
                        logger.debug("频道无需触发点播事件："+channel.getChannelName());
                        ipmFlag=null;
                    }
                }
            }else{
                logger.error("频道信息为空，不能获取rootCategoryID："+request.getQueryString());
            }
        }
        if(ipmFlag!=null){
            try {
                logger.debug("准备通知润汇：rootCategoryID="+rootCategoryID+",fileID="+fileID+",ipmFlag="+ipmFlag+",clientIp="+clientIp);
                IpmResult ir = ipm.AccountProcessFree(userInfo,sessionId,
                        rootCategoryID,fileID,ipmFlag,clientIp);
                resultID = ir.getResultID();
                errorInfo = ir.getErrorInfo();
                result = true;
            } catch (Exception e) {
                logger.error("通知润汇发生异常："+e.getMessage());
                result = false;
                errorInfo = e.getMessage();
                resultID = 501;
            }
            if(errorInfo!=null){
                errorInfo = errorInfo.replace('\'','"');
                errorInfo = errorInfo.replaceAll("\r","\\\r");
                errorInfo = errorInfo.replaceAll("\n","\\\n");
            }else{
                errorInfo = "没有任何返回值";
            }
        }else{
            logger.debug("ipmFlag为空，不用发送消息："+request.getQueryString());
        }
    }else{
        //访客无权观看
        logger.debug("用户没登陆，不用发送消息："+request.getQueryString());
        errorInfo = "session timeout!";
        resultID = 500;
    }
    StringBuffer resultBuf = new StringBuffer();
    resultBuf.append("{\r\nsuccess:").append(result).append(",\r\nresult:")
            .append(resultID).append(",\r\nerrorInfo:'").append(errorInfo).append("',\r\n");
    resultBuf.append("userId:'").append(userInfo).append("',sessionId:'").append(sessionId).append("'}\r\n");
    logger.debug("返回给客户端消息：\r\n"+resultBuf.toString());
%><%=resultBuf.toString()%><%!
   Logger logger = Logger.getLogger("com.fortune.rms.noticeRunway.jsp");
%>