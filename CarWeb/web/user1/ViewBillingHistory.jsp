<%@ page contentType="text/html; charset=gb2312" %><%@page
import="java.util.*,

        cn.sh.guanghua.util.tools.PageHelper,
        cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.util.tools.StringTools,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.user.BillingManager,
        cn.sh.guanghua.util.tools.SearchHelper,
        cn.sh.guanghua.mediastack.user.SessionUser"%><%

            SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
            if (su == null){
                 response.sendRedirect("Login.jsp?msg=session_unvalid");
                 return;
            }

            String userId = su.getUserId();

            List billList ;
            Date startTime = ParamTools.getDatetimeParameter(request,"start_time","1970-01-01");
            Date endTime =  ParamTools.getDatetimeParameter(request,"end_time","2050-01-01");

            PageHelper pageHelper=new PageHelper(request,session);
            int goodsType = ParamTools.getIntParameter(request,"goods_type",-1);
            long billCount = BillingManager.getCount(userId,startTime,endTime,goodsType);

            String orderBy = ParamTools.getParameter(request,"order_by","b.CreateTime desc");
            String orderBy0=null ;
            if(orderBy != null){
                orderBy0 = SearchHelper.translateToSQL(orderBy);
            }
            pageHelper.setAllRowCount(billCount);
            billList = BillingManager.getBilling(userId,startTime,endTime,goodsType,orderBy0,
                    (int)pageHelper.getStartRow(),(int)pageHelper.getRowCountPerPage());
            long goodsCost = BillingManager.getGoodsCostSum(userId,startTime,endTime,goodsType);
            StringBuffer sbData = new StringBuffer();
            sbData.append(pageHelper.getDataHeader("10-10-80-0065BillHistory"));

            sbData.append(pageHelper.addElement("goods-cost",String.valueOf(goodsCost*1.0/100)));
            sbData.append(pageHelper.addElement("user-id",userId));

            sbData.append(pageHelper.addElement("start-time",StringTools.date2string(startTime)));
            sbData.append(pageHelper.addElement("end-time",StringTools.date2string(endTime)));
            sbData.append(pageHelper.addElement("goods-type",""+goodsType));
            sbData.append(PageHelper.addElement("order-by",orderBy));
            sbData.append(pageHelper.getList("bills",billList));
            sbData.append(pageHelper.getPageBlack());
            sbData.append(pageHelper.getDataTailer("no any error"));
            out.println(pageHelper.getOutHeader("3.2"));
            //out.println(pageHelper.getManagerInfoBlock());
            out.println(pageHelper.getSessionBlock());
            out.println(sbData.toString());
            out.println(pageHelper.getOutTailer());
%>