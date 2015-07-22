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
	//媒体id,不是mediastack中的media id,是第三方icp中的媒体id
	long mediaId = mediaOut.getMediaId();
	//频道id,与mediastack中的频道id一致，整体包月该值为-icpId
	long channelId = builderOut.getChannelId();
	//icp id,与mediastack中的icp id一致
	long icpId = builderOut.getICPId();
	//播放的url
	String mediaUrl = Base64.encode(mediaOut.getMediaUrl());
	String paramString = "?WebService=true&media_id=" +
        	mediaId + "&channel_id=" + channelId + "&icp_id="+icpId+
	        "&mu=" + mediaUrl;
	//媒体的名字
	session.setAttribute("media_name",mediaOut.getMediaName());
	//媒体的编码格式类型
	session.setAttribute("media_type","" + mediaOut.getMediaType());
	//imp id,与mediastack中的imp id一致
	session.setAttribute("imp_id","" + builderOut.getIMPId());
	//返回的url
	session.setAttribute("return_url",builderOut.getReturnUrl());
	//icp接入的类型,通过该值可以判断当前icp是mediastack自身的，还是第三方icp接入，及接入方式
	session.setAttribute("icp_" + icpId + "_type","" + builderOut.getICPType());
	//播放方式
	session.setAttribute("icp_" + icpId + "_playtype","" + builderOut.getPlayType());
	//按次付费的数目，单位分
	session.setAttribute("media_account","" + builderOut.getAccount());
	//付费类型
	session.setAttribute("media_account_type","" + builderOut.getAccountType());
  	response.sendRedirect("Play.jsp"+paramString);
    }
%>