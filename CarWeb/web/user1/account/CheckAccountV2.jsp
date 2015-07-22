<%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.dataunit.*,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.common.Db,
        cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.mediastack.user.*,
        cn.sh.guanghua.cache.CacheManager,
        com.runway.race.ipm.*"%><%@ page import="org.apache.log4j.Logger" %><%
try{
    //获取影片参数
    long mediaIcpId = ParamTools.getLongParameter(request,"mediaicp_id",-1);
    long clipId = ParamTools.getLongParameter(request,"clip_id",-1);
    long mediaId = ParamTools.getLongParameter(request,"media_id",-1);
    long channelId = ParamTools.getLongParameter(request,"channel_id",-1);
    long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
    String mediaUrl = ParamTools.getParameter(request,"mu","");
    long serviceType = ParamTools.getLongParameter(request,"service_type",0);
    boolean  ipmNoticeInPlayer = !"false".equals(request.getParameter("noticeInCheckAccount"));
    String ipmFlag = "";
    Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel",channelId);
    String paramString = "WebService=true&mediaicp_id="+ mediaIcpId +"&clip_id=" +
            clipId + "&media_id="+ mediaId +"&channel_id=" + channelId + "&icp_id="+icpId+
            "&mu=" + mediaUrl +"&service_type=" + serviceType;
    long userAccountId = ParamTools.getLongParameter(request,"useraccount_id",-1);
    if (userAccountId != -1){
        paramString = paramString + "&useraccount_id=" + userAccountId;
    }
    //session检查
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    //String userId = "";
    String userInfo;
    String sessionId;
    long rootChannelID = 0;
    String clientIp = request.getRemoteAddr();

    if (su != null){
        //userId = su.getUserId();
        userInfo = su.getUserInfo();
        sessionId = su.getSessionId();
    }else{
        //访客无权观看
        response.sendRedirect("error.jsp?msg=313");
        return;
    }
    session.setAttribute(Constants.SESSION_CORPERATION_ID,""+icpId);
    session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId,"true");
    Ipm ipm = new IpmImpl();
    String rootCategoryID;
    MediaIcp mediaIcp =  (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
    Media media = (Media)CacheManager.getInstance().getFromDB("Media",mediaIcp.getMediaId());
    MediaClip mediaClip = (MediaClip)CacheManager.getInstance().getFromDB("MediaClip", clipId);
    if(mediaClip ==null){
        logger.error("影片片段为空，不能继续：clipId="+clipId);
	    out.println("影片片断为空");
	    return;
    }
    String fileID;
    List clips = Db.MediaClip().getMediaClipsByMediaid(media.getMediaId());
    if(clips.size() == 1){
        fileID = media.getMediaName();
    }else{
        fileID = media.getMediaName()+ " ["+mediaClip.getMediaclipVolume()+"]";
    }
    if(channel.getMonthFee() == -1){//当前栏目收费--1,免费--0
        logger.debug("当前栏目直接播放："+channel.getChannelName()+"(id="+channelId+"),param="+paramString);
        response.sendRedirect("PlayerV2.jsp?"+paramString);//直接播放
        return;
    }
    //获取计费频道
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
        if(rootChannel!=null){
            logger.debug("所有频道免费，直接播放："+rootChannel.getChannelName()+"(id="+rootChannelID+"),param="+paramString);
        }else{
            logger.debug("所有频道免费，直接播放,rootChannel为空(id="+rootChannelID+"),param="+paramString);
        }
        response.sendRedirect("PlayerV2.jsp?"+paramString);
        return;
    }

    Imp imp = (Imp)Db.Imp().getObject(media.getMediaImpid());
    logger.debug("开始检查频道《" +rootCategoryID+"》包月订购情况");
    IpmResult query = ipm.QuerySubscribe(userInfo, sessionId, rootCategoryID, "0", clientIp);
    logger.debug("包月订购《" +rootCategoryID+"》情况检查完毕,返回值：query.getErrorInfo="+query.getErrorInfo()+"(1为已经包月)");
    paramString += "&fileID="+java.net.URLEncoder.encode(java.net.URLEncoder.encode(fileID,"UTF-8"),"UTF-8")+"&rootCategoryID="+rootCategoryID;
    if("1".equals(query.getErrorInfo())){//已经包月
        session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId,"true");
        //todo zbxue 加点播分成
        if (imp!=null && "是".equals(imp.getImpEmail())){
            String spID = imp.getImpAddress();
            paramString += "&spID="+spID;
            if(!ipmNoticeInPlayer){
                //ipm.AccountProcessFree(su.getUserInfo(),su.getSessionId(),rootCategoryID,fileID+"|"+spID,"0",clientIp);
            }else{
                ipmFlag="&ipmFlag=0";
            }
//            System.out.println("buymedia 包月："+fileID+"|"+spID);
            logger.debug("大包月播放：param="+paramString);
            response.sendRedirect("PlayerV2.jsp?"+paramString+ipmFlag);
            return;

        }else{
            if(!ipmNoticeInPlayer){
                IpmResult ap = new IpmResult();//ipm.AccountProcessFree(userInfo, sessionId, rootCategoryID, fileID, "1",  clientIp);
                if(ap.getResultID() == 0){
                    logger.debug("不通延时知润汇，准备播放：param="+paramString);
                    response.sendRedirect("PlayerV2.jsp?"+paramString);
                    return;
                }else{
                    logger.error("润慧接口发生异常：resultID="+ap.getResultID());
                    response.sendRedirect("error.jsp?msg="+ ap.getResultID());
                }
            }else{
                ipmFlag="&ipmFlag=1";
                paramString +=ipmFlag;
                logger.debug("准备播放："+paramString);
                response.sendRedirect("PlayerV2.jsp?"+paramString);
                return;
            }
        }
    }else{
        //没有认购包月，跳转到认购页面
        String spID = null;
        if (imp!=null && "是".equals(imp.getImpEmail())){
            spID = imp.getImpAddress();
        }
        String fileIDspID = fileID;
        if(spID!=null){
            fileIDspID=fileID+"|"+spID;
        }
        logger.debug("查询AAProcess，参数fileID="+fileIDspID);
        IpmResult result = ipm.AAProcess(userInfo, sessionId, rootCategoryID, fileIDspID, "0", "0", clientIp);//润汇授权接口
        if (imp!=null && "是".equals(imp.getImpEmail())){
            ipmFlag = "&spID="+spID+"&ipmFlag=0";
            logger.debug("大包月imp‘"+imp.getImpName()+"’的点播："+fileID+"|"+spID);
        }else{
            if(imp!=null){
                logger.debug("不是大包月imp，不用通知润汇："+imp.getImpName());
            }else{
                logger.debug("不是大包月imp，不用通知润汇，imp为null");
            }
        }
        if(("314".equals(result.getErrorInfo())) || (result.getResultID() == 314)){
            //todo zbxue 点播分成
            logger.debug("允许播放，进入播放页面：参数="+paramString+ipmFlag);
            response.sendRedirect("PlayerV2.jsp?"+paramString+ipmFlag);
            return;
        }else{
            logger.debug("用户没有购买，进入购买页面：参数="+paramString+ipmFlag);
            response.sendRedirect("BuyMedia.jsp?"+paramString+ipmFlag+"&rootchannelid="+rootChannelID);
            return;
        }
    }
}catch(Exception e){
	e.printStackTrace();
	out.println("error:" + e.getMessage());
}
%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.CheckAccountV2");
/*
    public int checkPlayRight(String userID, long mediaID,long clipId, long IcpID, long channelId, long serviceType,int discount,long mediaIcpId){
        switch((int)serviceType){
            case Constants.SERVICE_TYPE_DOWN : return checkDownRight(userID,mediaID,clipId,IcpID,channelId,serviceType,discount,mediaIcpId);
            case Constants.SERVICE_TYPE_LIVING: return checkLiveRight (userID,mediaID,clipId,IcpID,channelId,serviceType,discount,mediaIcpId);
            case Constants.SERVICE_TYPE_DRM : ;
        }

        try {
           MediaIcp mediaIcp = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
           long price = mediaIcp.getMediaPrice();
           price = Math.round((price * discount * 1.0) /100);
            if (price<=0){
                return Constants.ACCESS_AUTH_SUCCESS;
            }

            String hqlStart = null;//
            int size = 0;
//             * liuxijun add from here
            String channelCondition = "";
            Channel channel = null;
            channel = (Channel)CacheManager.getInstance().getFromDB("Channel",channelId);
            if(channel != null){

                channelCondition = channelCondition + " b.Goods_Id =" + channel.getChannelId();
                while (channel.getChannelParentid() != -1){
                    channel = (Channel)CacheManager.getInstance().getFromDB("Channel",channel.getChannelParentid());
                    if (!"".equals(channelCondition)) {
                        channelCondition += " or ";
                    }
                    channelCondition = channelCondition + " b.Goods_Id =" + channel.getChannelId();
                }

                //icp的所有频道
                channel = ChannelManager.getInstance().getFirstChannel(IcpID);
                if (channel != null){
                    if (!"".equals(channelCondition)) {
                        channelCondition += " or ";
                    }
                    channelCondition = channelCondition + " b.Goods_Id =" + channel.getChannelId();
                }

                if(! "".equals(channelCondition)){
                    channelCondition = "(("+channelCondition+") and b.Goods_Type="+Constants.GOODS_TYPE_CHANNEL+")";
                }
            }

            String packCondition = "";
            List packs = mediaIcp.getPackIds();
            if(packs != null){
                for(int i=0;i<packs.size();i++){
                    long packId = ((Long)packs.get(i)).longValue();
                    if(packId > 0 ){
                        if(! "".equals(packCondition)){
                            packCondition += " or ";
                        }
                        Pack pack = (Pack)CacheManager.getInstance().getFromDB("Pack",packId);
                        packCondition += " b.Goods_Id="+pack.getPackId();
                    }
                }
            }
            if(! "".equals(packCondition)){
                packCondition = "(("+packCondition+") and b.Goods_Type="+Constants.GOODS_TYPE_PACK+")";
            }

            SqlBridge sqlBridge = new SqlBridge();
            int returnResult = Constants.ACCESS_AUTH_BUYIT;
            try {
                sqlBridge.startSession();
                do {

                    hqlStart = "select count(billing_id) from Billing b where" +
                            " b.User_Id='" + userID.replace('\'', '"') + "'" +
                            " and b.Icp_Id=" + IcpID+
                            " and b.Start_Time<SYSDATE and b.End_Time>SYSDATE and " +
                            "(( b.Goods_Id="+clipId+" and b.Goods_Type="+Constants.GOODS_TYPE_MEDIA+")";
                    if(!"".equals(channelCondition)){
                        hqlStart = hqlStart + "or"+channelCondition;
                    }
                    if(!"".equals(packCondition)){
                        hqlStart = hqlStart + " or "+packCondition;
                    }
                    hqlStart +=")";
                    ResultSet rst = sqlBridge.executeQuery(hqlStart);
                    size = 0;
                    if (rst.next()) {
                        size = rst.getInt(1);
                    }
                    rst.close();
                    if (size > 0) {
                        returnResult = Constants.ACCESS_AUTH_SUCCESS;
                    }
                } while (false);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqlBridge.endSession();
            }
            return returnResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.ACCESS_AUTH_BUYIT;
    }

    public int checkDownRight(String userID, long mediaID,long clipId, long IcpID, long channelId, long serviceType,int discount,long mediaIcpId){
        try {
            MediaIcp mediaIcp = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
            long price = mediaIcp.getMediaPrice();
            price = Math.round((price * discount * 1.0) /100);
            if (price<=0){
                return Constants.ACCESS_AUTH_SUCCESS;
            }

            String hqlStart = null;//
            int size = 0;

            SqlBridge sqlBridge = new SqlBridge();
            int returnResult = Constants.ACCESS_AUTH_BUYIT;
            try {
                sqlBridge.startSession();
                do {

                    hqlStart = "select count(billing_id) from Billing b where" +
                            " b.User_Id='" + userID.replace('\'', '"') + "'" +
                            " and b.Icp_Id=" + IcpID+
                            " and b.Start_Time<SYSDATE and b.End_Time>SYSDATE and " +
                            "( b.Goods_Id="+clipId+" and b.Goods_Type="+Constants.GOODS_TYPE_DOWNLOAD+")";

                    ResultSet rst = sqlBridge.executeQuery(hqlStart);
                    size = 0;
                    if (rst.next()) {
                        size = rst.getInt(1);
                    }
                    rst.close();
                    if (size > 0) {
                        returnResult = Constants.ACCESS_AUTH_SUCCESS;
                    }
                } while (false);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqlBridge.endSession();
            }
            return returnResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.ACCESS_AUTH_BUYIT;
    }
    public int checkLiveRight(String userID, long mediaID,long clipId, long IcpID, long channelId, long serviceType,int discount,long mediaIcpId){
        try {
            Tvchannel tvchannel = (Tvchannel)CacheManager.getInstance().getFromDB("Tvchannel",channelId);
            long price = tvchannel.getTvchannelVodprice();
            price = Math.round((price * discount * 1.0) /100);
            if (price<=0){
                return Constants.ACCESS_AUTH_SUCCESS;
            }

            String hqlStart = null;//
            int size = 0;

            SqlBridge sqlBridge = new SqlBridge();
            int returnResult = Constants.ACCESS_AUTH_BUYIT;
            try {
                sqlBridge.startSession();
                do {

                    hqlStart = "select count(billing_id) from Billing b where" +
                            " b.User_Id='" + userID.replace('\'', '"') + "'" +
                            " and b.Icp_Id=" + IcpID+
                            " and b.Start_Time<SYSDATE and b.End_Time>SYSDATE and " +
                            "( b.Goods_Id="+channelId+" and (b.Goods_Type="+Constants.GOODS_TYPE_LIVING+" or b.Goods_Type="+Constants.GOODS_TYPE_LIVECHANNEL+"))";

                    ResultSet rst = sqlBridge.executeQuery(hqlStart);
                    size = 0;
                    if (rst.next()) {
                        size = rst.getInt(1);
                    }
                    rst.close();
                    if (size > 0) {
                        returnResult = Constants.ACCESS_AUTH_SUCCESS;
                    }
                } while (false);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqlBridge.endSession();
            }
            return returnResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.ACCESS_AUTH_BUYIT;
    }

    */
%>