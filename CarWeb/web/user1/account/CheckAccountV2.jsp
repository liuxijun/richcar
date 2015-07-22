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
    //��ȡӰƬ����
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
    //session���
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
        //�ÿ���Ȩ�ۿ�
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
        logger.error("ӰƬƬ��Ϊ�գ����ܼ�����clipId="+clipId);
	    out.println("ӰƬƬ��Ϊ��");
	    return;
    }
    String fileID;
    List clips = Db.MediaClip().getMediaClipsByMediaid(media.getMediaId());
    if(clips.size() == 1){
        fileID = media.getMediaName();
    }else{
        fileID = media.getMediaName()+ " ["+mediaClip.getMediaclipVolume()+"]";
    }
    if(channel.getMonthFee() == -1){//��ǰ��Ŀ�շ�--1,���--0
        logger.debug("��ǰ��Ŀֱ�Ӳ��ţ�"+channel.getChannelName()+"(id="+channelId+"),param="+paramString);
        response.sendRedirect("PlayerV2.jsp?"+paramString);//ֱ�Ӳ���
        return;
    }
    //��ȡ�Ʒ�Ƶ��
    if(channel.getChannelStatus() == 0){
        if(channel.getChannelParentid() != -1){//����Ŀ
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
    if("0".equals(rootCategoryID)){//����Ƶ�����
        if(rootChannel!=null){
            logger.debug("����Ƶ����ѣ�ֱ�Ӳ��ţ�"+rootChannel.getChannelName()+"(id="+rootChannelID+"),param="+paramString);
        }else{
            logger.debug("����Ƶ����ѣ�ֱ�Ӳ���,rootChannelΪ��(id="+rootChannelID+"),param="+paramString);
        }
        response.sendRedirect("PlayerV2.jsp?"+paramString);
        return;
    }

    Imp imp = (Imp)Db.Imp().getObject(media.getMediaImpid());
    logger.debug("��ʼ���Ƶ����" +rootCategoryID+"�����¶������");
    IpmResult query = ipm.QuerySubscribe(userInfo, sessionId, rootCategoryID, "0", clientIp);
    logger.debug("���¶�����" +rootCategoryID+"�����������,����ֵ��query.getErrorInfo="+query.getErrorInfo()+"(1Ϊ�Ѿ�����)");
    paramString += "&fileID="+java.net.URLEncoder.encode(java.net.URLEncoder.encode(fileID,"UTF-8"),"UTF-8")+"&rootCategoryID="+rootCategoryID;
    if("1".equals(query.getErrorInfo())){//�Ѿ�����
        session.setAttribute(Constants.USER_MEDIA_SESSION_HEAD + clipId + "_" + channelId,"true");
        //todo zbxue �ӵ㲥�ֳ�
        if (imp!=null && "��".equals(imp.getImpEmail())){
            String spID = imp.getImpAddress();
            paramString += "&spID="+spID;
            if(!ipmNoticeInPlayer){
                //ipm.AccountProcessFree(su.getUserInfo(),su.getSessionId(),rootCategoryID,fileID+"|"+spID,"0",clientIp);
            }else{
                ipmFlag="&ipmFlag=0";
            }
//            System.out.println("buymedia ���£�"+fileID+"|"+spID);
            logger.debug("����²��ţ�param="+paramString);
            response.sendRedirect("PlayerV2.jsp?"+paramString+ipmFlag);
            return;

        }else{
            if(!ipmNoticeInPlayer){
                IpmResult ap = new IpmResult();//ipm.AccountProcessFree(userInfo, sessionId, rootCategoryID, fileID, "1",  clientIp);
                if(ap.getResultID() == 0){
                    logger.debug("��ͨ��ʱ֪��㣬׼�����ţ�param="+paramString);
                    response.sendRedirect("PlayerV2.jsp?"+paramString);
                    return;
                }else{
                    logger.error("��۽ӿڷ����쳣��resultID="+ap.getResultID());
                    response.sendRedirect("error.jsp?msg="+ ap.getResultID());
                }
            }else{
                ipmFlag="&ipmFlag=1";
                paramString +=ipmFlag;
                logger.debug("׼�����ţ�"+paramString);
                response.sendRedirect("PlayerV2.jsp?"+paramString);
                return;
            }
        }
    }else{
        //û���Ϲ����£���ת���Ϲ�ҳ��
        String spID = null;
        if (imp!=null && "��".equals(imp.getImpEmail())){
            spID = imp.getImpAddress();
        }
        String fileIDspID = fileID;
        if(spID!=null){
            fileIDspID=fileID+"|"+spID;
        }
        logger.debug("��ѯAAProcess������fileID="+fileIDspID);
        IpmResult result = ipm.AAProcess(userInfo, sessionId, rootCategoryID, fileIDspID, "0", "0", clientIp);//�����Ȩ�ӿ�
        if (imp!=null && "��".equals(imp.getImpEmail())){
            ipmFlag = "&spID="+spID+"&ipmFlag=0";
            logger.debug("�����imp��"+imp.getImpName()+"���ĵ㲥��"+fileID+"|"+spID);
        }else{
            if(imp!=null){
                logger.debug("���Ǵ����imp������֪ͨ��㣺"+imp.getImpName());
            }else{
                logger.debug("���Ǵ����imp������֪ͨ��㣬impΪnull");
            }
        }
        if(("314".equals(result.getErrorInfo())) || (result.getResultID() == 314)){
            //todo zbxue �㲥�ֳ�
            logger.debug("�����ţ����벥��ҳ�棺����="+paramString+ipmFlag);
            response.sendRedirect("PlayerV2.jsp?"+paramString+ipmFlag);
            return;
        }else{
            logger.debug("�û�û�й��򣬽��빺��ҳ�棺����="+paramString+ipmFlag);
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

                //icp������Ƶ��
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