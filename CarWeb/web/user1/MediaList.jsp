<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * FileName：AspAdminList.jsp
 * 主要功能：按照搜索出来的结果，显示影片
 *
 * @param     pagesize:每页显示的记录数
 *            pageno:当前页面的页码，用来计算读取记录的范围
 *
 * @author：刘喜军
 * @ Date
 *    @ 开始时间： 2003-10-13
 *    @ 结束时间：
 *
 *    @ 修改人员：
 *    @ 修改日期与原因：
 */
%><%@ page contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.dataunit.*,
        cn.sh.guanghua.mediastack.datainterface.Base,
        cn.sh.guanghua.util.tools.*,
        cn.sh.guanghua.mediastack.common.Db,
        cn.sh.guanghua.mediastack.common.*,
        cn.sh.guanghua.util.hibernate.HBSession,
        cn.sh.guanghua.cache.CacheManager,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic"%><%
//    sSearchCondition = "%" + sSearchCondition + "%";
    PageHelper pageHelper = new PageHelper(request,session);
    String    sSearchCondition = ParamTools.getParameter(request,"search_condition","");
    String    sSearchType = ParamTools.getParameter(request,"search_type","media_name");
    Icp icp = new Icp();
    long icpId = ParamTools.getLongParameter(request,"icp_id",-1);
    if(icpId<=0){
        icpId = pageHelper.getCorporationId();
    }

    if(icpId < 0){
        List icps = Db.Icp().getObjects();
        if(icps != null){
            if(icps.size()>=1){
                icp = (Icp)icps.get(0);
            }
        }
        icpId = icp.getIcpId();
        pageHelper.setCorporationId(icpId);
        pageHelper.setCorporationType(icpId);
    }else{
        icp.setIcpId(icpId);
    }
            SearchHelper sh = searchMovies(icpId,sSearchCondition,(int)pageHelper.getStartRow(),
                    (int)pageHelper.getRowCountPerPage());
            pageHelper.setAllRowCount(sh.getAllRowCount());
            List medias = sh.getObjList();
            if(medias == null){
                medias = new ArrayList();
            }
    StringBuffer sbData = new StringBuffer();
    sbData.append(PageHelper.getListHeader("medias"));
    for(int i=0;i<medias.size();i++){
        IcpMediaInfo mediaInfo = (IcpMediaInfo)medias.get(i);
        Media media = mediaInfo.getMedia();
        //media.setMediaUrl(Base64.encode(media.getMediaUrl()));
        MediaIcp mi = mediaInfo.getMediaicp();
        if(mi != null && media != null){
            String mediaUrl = media.getMediaUrl()+"?";
            List clips = Db.MediaClip().getMediaClipsByMediaid(media.getMediaId());
            if(clips != null && clips.size()>0){
                MediaClip mc = (MediaClip) clips.get(0);
                if (mc != null){
                    mi = (MediaIcp) CacheManager.getInstance().getFromDB("MediaIcp",mi.getMediaicpId());
                    Server server = (Server)CacheManager.getInstance().getFromDB("Server",mi.getServerId());
                    if(server != null){
                        mediaUrl =server.getServerUrl()+"/"+ mc.getMediaclipUrl();
                        //System.out.println(mediaUrl);
                        mediaUrl = mediaUrl + "?cid=" + mi.getMediaChannelid() + "&mid="
                            + media.getMediaId() + "&icp=" + icpId + "&svr=" + server.getServerId()+
                       "&imp=" + media.getMediaId() + "&sid=" + media.getMediaSubjectid() + "&uid=testor"  + "&rip=" + request.getRemoteAddr();
                        String regMediaUrl = PlayLogic.regUrl(mediaUrl,true);
                        mediaUrl = regMediaUrl;
                    }else{
                        mediaUrl += "&sid="+mi.getServerId()+"&server=null";
                    }
                }else{
                    mediaUrl += "&mclip=null";
                }
            }else{
                mediaUrl += "&cliips=null||clips.size=0";
            }
            media.setMediaUrl(mediaUrl);
        }
        sbData.append(mediaInfo.toString());
    }
    //sbData.append(PageHelper.getList("medias",medias));
    sbData.append(PageHelper.getListTailer("medias"));
    sbData.append(PageHelper.addElement("search-condition",sSearchCondition));
    sbData.append(PageHelper.addElement("search-type",sSearchType));
    sbData.append(PageHelper.addElement("icp-id",icpId));
    sbData.append(PageHelper.addElement("client-ip",request.getRemoteAddr()));
    pageHelper.outPut(out,sbData.toString(),"10-10-80-66-MediaList",
            "no-error","List all the media","10-10-80-66-MediaList","xjiu");

%><%!
    public SearchHelper searchMovies(long icpId,String mediaName,int startRow,int rowCount){
        HBSession hb = new HBSession();
        SearchHelper sh =new SearchHelper();
        if(icpId>0){
            String hsql = "from Media as media,MediaIcp as icp"+
                    " "+
                    " where "+
                    " icp.icpId = ? and icp.mediaPublished = 1 and" +
                    " icp.mediaId = media.mediaId and media.mediaName like ? order by icp.mediaPublishdate desc";
            Object[] param = new Object[2];
            param[0] = new Long(icpId);
            param[1] = new String("%" + mediaName + "%");
            List list = null;
            try {
                int allCount  =(int) hb.getCount(hsql,param);
                list = setMedias(hb.getObjects(hsql, param, (int)startRow, (int)rowCount));
                sh.setObjList(list);
                sh.setAllRowCount(allCount);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return sh;
    }
    private List setMedias(List results){
      List list = new java.util.ArrayList();
      if (results == null)
          return new java.util.ArrayList();
      for (int i = 0; i < results.size(); i++) {
          Object[] row = (Object[]) results.get(i);
          // New a mediaInfo to sotre the other object,just like subject ,hometown and icp
          IcpMediaInfo mediaInfo= new IcpMediaInfo();
          for(int j=0;j<row.length;j++){
              Object obj = row[j];
              if(obj instanceof Media){
                  mediaInfo.setMedia((Media)obj);
              }else if(obj instanceof MediaIcp){
                  mediaInfo.setMediaicp((MediaIcp)obj);
              }else if(obj instanceof MediaSubject){
                  mediaInfo.setSubject((MediaSubject)obj);
              }else if(obj instanceof Server){
                  mediaInfo.setServer((Server)obj);
              }
          }
          list.add(mediaInfo);
      }
      return list;
    }
%>