<%@page contentType="text/html; charset=GBK" %>
<%@page import="cn.sh.guanghua.mediastack.interfaceicp.*"%><%@page
import="cn.sh.guanghua.util.tools.*"%>
<%
    String decodeInfo = request.getParameter("data");
    MediaInfoBuilder builderOut = new MediaInfoBuilder();
    if(!builderOut.parseSubmitInfo(decodeInfo)){
	  response.sendRedirect("account/error.jsp?WebService=true");
	  return;
    }else{
    	MediaInfo mediaOut = (MediaInfo)(builderOut.getMediaList().get(0));
/*
      System.out.println("media id:" + mediaOut.getMediaId());
      System.out.println("media name:" + mediaOut.getMediaName());
      System.out.println("media type:" + mediaOut.getMediaType());
      System.out.println("media url:" + mediaOut.getMediaUrl());
      System.out.println("icp id:" + builderOut.getICPId());
      System.out.println("imp id:" + builderOut.getIMPId());
      System.out.println("channel id:" + builderOut.getChannelId());
      System.out.println("icp type:" + builderOut.getICPType());
      System.out.println("play type:" + builderOut.getPlayType());
      System.out.println("account:" + builderOut.getAccount());
      System.out.println("account type:" + builderOut.getAccountType());
      System.out.println("return url:" + builderOut.getReturnUrl());
*/
	//ý��id,����mediastack�е�media id,�ǵ�����icp�е�ý��id
	long mediaId = mediaOut.getMediaId();
	//Ƶ��id,��mediastack�е�Ƶ��idһ�£�������¸�ֵΪ-icpId
	long channelId = builderOut.getChannelId();
	//icp id,��mediastack�е�icp idһ��
	long icpId = builderOut.getICPId();
	//���ŵ�url
	String mediaUrl = Base64.encode(mediaOut.getMediaUrl());
	String paramString = "?WebService=true&media_id=" +
        	mediaId + "&channel_id=" + channelId + "&icp_id="+icpId+
	        "&mu=" + mediaUrl;
	//ý�������
	session.setAttribute("media_name",mediaOut.getMediaName());
	//ý��ı����ʽ����
	session.setAttribute("media_type","" + mediaOut.getMediaType());
	//imp id,��mediastack�е�imp idһ��
	session.setAttribute("imp_id","" + builderOut.getIMPId());
	//���ص�url
	session.setAttribute("return_url",builderOut.getReturnUrl());
	//icp���������,ͨ����ֵ�����жϵ�ǰicp��mediastack����ģ����ǵ�����icp���룬�����뷽ʽ
	session.setAttribute("icp_" + icpId + "_type","" + builderOut.getICPType());
	//���ŷ�ʽ
	session.setAttribute("icp_" + icpId + "_playtype","" + builderOut.getPlayType());
	//���θ��ѵ���Ŀ����λ��
	session.setAttribute("media_account","" + builderOut.getAccount());
	//��������
	session.setAttribute("media_account_type","" + builderOut.getAccountType());
  	response.sendRedirect("Play.jsp"+paramString);
    }
%>