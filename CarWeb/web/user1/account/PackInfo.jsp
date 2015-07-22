<%@ page import="cn.sh.guanghua.util.tools.ParamTools,
                 cn.sh.guanghua.mediastack.common.Constants,
                 cn.sh.guanghua.util.tools.StringTools,
                 cn.sh.guanghua.util.tools.RowPage,
                 java.util.List,
                 cn.sh.guanghua.mediastack.common.Db,
                 java.util.Iterator,
                 cn.sh.guanghua.mediastack.dataunit.*,
                 cn.sh.guanghua.util.tools.PageHelper,
                 cn.sh.guanghua.cache.CacheManager"%><%@ page
 contentType="text/html;charset=gb2312" %><%
     Pack pack ;
     long lPackId;
     long mediaIcpId;
     lPackId = ParamTools.getLongParameter(request,"pack_id",-1);
     mediaIcpId =  ParamTools.getLongParameter(request,"mediaicp_id",-1);
     pack = (Pack)CacheManager.getInstance().getFromDB("Pack",lPackId);

     if(pack != null){
         List mediaList = pack.getMediaIds();


 %>

        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
                <link rel="stylesheet" href="../../css/css_view.css"/>
                <link rel="stylesheet" href="../../css/css_list.css"/>
                <title>��Ʒ��</title>
            <body>
                <div align="center">
                    <form name="FrmPackInfo" method="post" action="PackDo.jsp?WebService=true">
                        <table width="600" align="center" border="0">
                            <tr>
                                <td colspan="6" align="center">
                                        <img src="../../images/account/title.gif"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="1" align="right">��Ʒ������:</td>
                                <td width="7"/>
                                <td>
                                    <%=pack.getPackName()%>
                                </td>
                                <td colspan="1" align="right">��������:</td>
                                <td width="7"/>
                                <td>
                                    <%=StringTools.date2string(pack.getStartTime())%>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="1" align="right">�۸�:</td>
                                <td width="7"/>
                                <td>

                                    <%=pack.getPackPrice()*1.0/100%>
                                </td>
                                <td colspan="1" align="right">��������:</td>
                                <td width="7"/>
                                <td>
                                    <%=StringTools.date2string(pack.getEndTime())%>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="1" align="right">�����Żݣ�OFF %��:</td>
                                <td width="7"/>
                                <td>
                                    <%=pack.getPackOff()%>
                                </td>
                                <td colspan="1" align="right">��Ч�ڣ��죩:</td>
                                <td width="7"/>
                                <td>
                                    <%=pack.getDuration()%>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>  </td>
                                <td>
                                </td>
                            </tr>
                        </table>
                        <table align="center" border="0" bordercolordark="#ffffff" bordercolorlight="#cccccc" cellpadding="0" cellspacing="0" width="600">
                            <tr>
                                <td>��ǰ��Ʒ��������ӰƬ��</td>
                            </tr>
                        </table>
    <table align="center" border="1" bordercolordark="#ffffff" bordercolorlight="#cccccc" cellpadding="0" cellspacing="0" width="600" class="table_border" frame="box">
        <tr>
            <th align="center" width="50%">Ƭ��</th>
            <th align="center" width="10%">��Ա</th>
            <th align="center" width="10%">����</th>
            <th align="center" width="10%">����</th>
            <th align="center" width="10%">��Ŀ</th>
            <th align="center" width="10%">�㲥�۸�</th>
        </tr>
        <% if(mediaList != null){
            for (int i=0; i<mediaList.size(); i++){
                long mediaId = ((Long)mediaList.get(i)).longValue();
                Media media = (Media)CacheManager.getInstance().getFromDB("Media",mediaId);
                MediaIcp mediaIcp = (MediaIcp)CacheManager.getInstance().getFromDB("MediaIcp",mediaIcpId);
                MediaHometown mht = (MediaHometown)CacheManager.getInstance().getFromDB("MediaHometown",media.getMediaHometownid());
                MediaSubject ms = (MediaSubject)CacheManager.getInstance().getFromDB("MediaSubject",media.getMediaSubjectid());
        %>
            <tr>
            <td align="center"><%=media.getMediaName()%></th>
            <td align="center"><%=media.getMediaActors()%></th>
            <td align="center"><%=media.getMediaDirectors()%></th>
            <td align="center"><%=mht.getMediahometownName()%></th>
            <td align="center"><%=ms.getMediasubjectName()%></th>
            <td align="center"><%=mediaIcp.getMediaPrice()*1.0/100%></th>
            </tr>
        <% }
        } %>


    </table>


                        <table align="center">
                            <tr>
                                <td align="center"><a href="javascript:close();">�ر�</a></td>
                            </tr>
                        </table>
                    </form>
                </div>
            </body>
        </html>
<%
         }
%>
