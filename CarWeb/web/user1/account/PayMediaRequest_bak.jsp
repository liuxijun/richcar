<%@ page
contentType="text/html; charset=GBK" %><%@ page
import="cn.sh.guanghua.mediastack.dataunit.*,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.mediastack.common.Db,
        java.util.Date,java.util.Calendar,
        java.util.List,
        java.sql.Connection,
        cn.sh.guanghua.util.pooldb.DbTool,
        java.sql.PreparedStatement,
        cn.sh.guanghua.mediastack.user.*,
        cn.sh.guanghua.cache.CacheManager,
        java.net.InetAddress,
        com.runway.race.ipm.IpmResult,
        com.runway.race.ipm.Ipm,
        com.runway.race.ipm.IpmImpl"%><%@ page import="org.apache.log4j.Logger" %><%
    //��ȡӰƬ����
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.PayMediaRequest_bak");
    long mediaIcpId = ParamTools.getLongParameter(request,"mediaicp_id",-1);
    long clipId = ParamTools.getLongParameter(request,"clip_id",-1);
    long mediaId = ParamTools.getLongParameter(request,"media_id",-1);
    long channelId = ParamTools.getLongParameter(request,"channel_id",-1);//ý��������Ŀ��id����root��Ŀ
    long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
    String mediaUrl = ParamTools.getParameter(request,"mu","");
    long serviceType = ParamTools.getLongParameter(request,"service_type",0);
    long rootChannelID = ParamTools.getLongParameter(request,"rootchannelid",0);
///*
    String paramString = "WebService=true&mediaicp_id="+ mediaIcpId +"&clip_id=" +
            clipId + "&media_id="+ mediaId +"&channel_id=" + channelId + "&icp_id="+icpId+
            "&mu=" + mediaUrl +"&service_type=" + serviceType;
//*/
//    String paramString = request.getQueryString();
    long userAccountId = ParamTools.getLongParameter(request,"useraccount_id",-1);
    if (userAccountId != -1){
        paramString = paramString + "&useraccount_id=" + userAccountId;
    }
    try{
        String type = ParamTools.getParameter(request, "type", "");

        long price = ParamTools.getLongParameter(request, "price", 0l);
        SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
        String userId = su.getUserId();
        String sessionId = su.getSessionId();
        String userInfo = su.getUserInfo();
        long payMoneyId = ParamTools.getLongParameter(request, "paymoney_id", 0);
        String categoryId;
        String userIP = InetAddress.getLocalHost().getHostAddress();
        userIP = userIP + " from " + request.getRemoteAddr();
        //session���
        if (userId == null && userId.equals("")){
            response.sendRedirect("error.jsp?msg=avalid_session");
            return;
        }
        //ɽ����Ŀ�����ݲ�ͬ�Ĺ������ͣ����ò�ͬ�Ľӿڣ�
        try{
            if(type.equals("media")){//���򵥸�ý�壬�������İ��μƷѽӿ�
                logger.debug("���˹��򵥸�ý���ļ�");
                MediaIcp mediaIcp =  (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
                Media media = (Media)CacheManager.getInstance().getFromDB("Media",mediaIcp.getMediaId());
                MediaClip mediaClip = (MediaClip)CacheManager.getInstance().getFromDB("MediaClip", clipId);
                //���жϸ�ý���Ƕ༯���ǵ���������ǵ���,fileID����[1]
                String fileID;
                List clips = Db.MediaClip().getMediaClipsByMediaid(media.getMediaId());
                if(clips.size() == 1){
                    fileID = media.getMediaName();
                }else{
                    fileID = media.getMediaName()+ " ["+mediaClip.getMediaclipVolume()+"]";
                }
//����Ҳ��spID����
//                /*
                try {
                    Imp imp = (Imp)Db.Imp().getObject(media.getMediaImpid());
                    if(imp!=null && "��".equals(imp.getImpEmail())){
                       fileID = fileID+"|"+imp.getImpAddress();
                        logger.debug(imp.getImpName()+" �Ǵ�����ṩ�̣�SPID="+imp.getImpAddress());
                    }else{
                        logger.debug(imp.getImpName()+" ���Ǵ�����ṩ�̣�����SPID��Ϣ��");
                    }
                } catch (Exception e) {
                    logger.error("�޷���ȡIMP��Ϣ��"+e.getMessage());
                    //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
//                */
                String clientIp = request.getRemoteAddr();
                Calendar c = Calendar.getInstance();
                Date startdate = c.getTime();
                c.add(Calendar.DATE, 1);
                Date enddate = c.getTime();

                int goodsType = Constants.GOODS_TYPE_MEDIA;
                String productDes = "����㲥����:"+media.getMediaName();

                Ipm ipm = new IpmImpl();
                Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel", rootChannelID);

                categoryId = channel.getCategoryID();
                logger.debug("��ѯ��������ӿ�AAProcess��fileID="+fileID+",categoryId="+categoryId);
                IpmResult result = ipm.AAProcess(userInfo, sessionId, categoryId, fileID, "0", "0", clientIp);
                if(result.getResultID() == 0){
                    logger.debug("��Ҫ���򣬵��ù���ӿ�AccountProcessClick��fileID="+fileID+",categoryId="+categoryId);
                    IpmResult ap = ipm.AccountProcessClick(userInfo, sessionId, categoryId, fileID, "0", "0", userIP);
                    if(ap.getResultID() == 0 || ap.getResultID() == 4 || ap.getResultID() == 5 || ap.getErrorInfo().equals("314")){//�Ʒѳɹ�,���,icp���
                        logger.debug("fileID="+fileID+",categoryId="+categoryId+"����ɹ�,����ֵ="+ap.getResultID());
                        saveBilling(userId, userIP, clipId, goodsType, price, icpId, startdate, enddate, productDes, userAccountId, payMoneyId);
                    }else{
                        logger.debug("fileID="+fileID+",categoryId="+categoryId+"����ʧ��,����ֵ="+ap.getResultID());
                        response.sendRedirect("error.jsp?msg="+ap.getResultID());
                    }
                }else if(result.getResultID() == 314){
                    logger.debug("fileID="+fileID+",categoryId="+categoryId+"�Ѿ����򣬷���="+result.getResultID());
                    logger.debug("���Բ��ţ�pramString="+paramString);
                    //paramString +="&fileID="+fileID;
                    response.sendRedirect("PlayerV2.jsp?"+paramString);
                    return;
                }else{
                    response.sendRedirect("error.jsp?msg="+result.getResultID());
                }
            }else if (type.equals("channel")){//����Ƶ�����������İ��½ӿ�
                long id = ParamTools.getLongParameter(request, "id", -1);
                Channel channel = (Channel)CacheManager.getInstance().getFromDB("Channel", id);//ý���root��Ŀ
                Calendar c = Calendar.getInstance();
                Date startdate = c.getTime();
                c.add(Calendar.DATE, (int) channel.getMonthLength());
                Date enddate = c.getTime();
                categoryId = channel.getCategoryID();
                Ipm ipm = new IpmImpl();
                IpmResult ap = ipm.OrderMonthService(userInfo, sessionId, categoryId, "0", userIP);
                if(ap.getResultID() == 0 || ap.getResultID() == 4 || ap.getResultID() == 5){//�Ʒѳɹ�,���,icp���
                     saveBilling(userId,userIP,channel.getChannelId(),Constants.GOODS_TYPE_CHANNEL,
                                price,icpId,startdate,enddate,"����Ƶ������:"+channel.getChannelName(),userAccountId,payMoneyId);
                }else{
                    response.sendRedirect("error.jsp?msg="+ ap.getResultID());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }catch(Exception e){
        session.setAttribute("ERROR", e.toString());
        e.printStackTrace();
        response.sendRedirect("error.jsp?msg=unexpect_error");
        return;
    }
%><%!


    public static boolean saveBilling(String userId,String userIP, long goodsId, long goodsType, long price, long icpId,
                                      Date startTime, Date endTime, String billingName ,long userAccountId,long payMoneyId) {
        try{
            Icp icp = (Icp)CacheManager.getInstance().getFromDB("Icp",icpId);

            String sqlStr = "insert into BILLING(BILLING_ID,USER_ID,GOODS_ID,GOODS_TYPE," +
                    "GOODS_COST,START_TIME,END_TIME,CREATE_TIME,ICP_ID,BILLING_NAME," +
                    "ICP_NAME,BILLING_VALID,USERACCOUNT_ID,USER_IP,PAYMONEY_ID) values(seq_billing_id.nextval,?,?,?,?,?,?,SYSDATE,?,?,?" +
                    ",0,?,?,?)";
            Connection conn =null;
            PreparedStatement preStmt = null;
            try{
                conn = DbTool.getInstance().getConnection();
                preStmt = conn.prepareStatement(sqlStr);
                preStmt.setString(1,userId);
                preStmt.setLong(2,goodsId);
                preStmt.setLong(3,goodsType);
                preStmt.setLong(4,price);
                preStmt.setTimestamp(5,new java.sql.Timestamp(startTime.getTime()));
                preStmt.setTimestamp(6,new java.sql.Timestamp(endTime.getTime()));
                preStmt.setLong(7,icpId);
                preStmt.setString(8,billingName);
                preStmt.setString(9,icp.getIcpName());
                preStmt.setLong(10,userAccountId);
                preStmt.setString(11,userIP);
                preStmt.setLong(12,payMoneyId);
                preStmt.executeUpdate();
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(preStmt != null){
                    preStmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>����ɹ�</title>
<link href="../../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="../../images/account/123.gif">
  <tbody>
        <tr>
          <td height="72">��</td>
        </tr>
  		<tr>
    		<td align="left" valign="top">
					<table width="100%" class="black" height="110" border="0" bordercolor="#111111" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
							  <td align="center" width="100%" valign="middle" height="90">
								  <p><b><font color="#0000FF">��ϲ��������֧���Ѿ��ɹ�!!</font></b></p>
  							  </td>
							</tr>
						</tbody>
					</table>
			</td>
		</tr>
	</tbody>
</table>
<script language="javascript">
        alert("��ϲ��������֧���Ѿ��ɹ�!!");
        location.replace("PlayerV2.jsp?<%=paramString%>");
        document.location.href = "PlayerV2.jsp?<%=paramString%>";
</script>

</body>
</html>
