<%@ page import="com.fortune.util.StringUtils" %><%@ page import="com.fortune.util.FileUtils" %><%@ page import="java.util.*" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-5-8
  Time: 上午10:09
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@include file="../inc/checkHeader.jsp"%><%
    String startDate = request.getParameter("startDate");
    Date startTime;
    if(startDate == null){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //星期日是1，星期一是2，星期六是7。按照我朝习惯，星期一是第一天，所以要转换一下
        dayOfWeek=dayOfWeek-1;
        if(dayOfWeek==0){
            dayOfWeek = 7;
        }
        startTime = new Date(System.currentTimeMillis()-(7+dayOfWeek-1)*24*3600*1000L);
        startDate = StringUtils.date2string(startTime,"yyyy-MM-dd")+" 00:00:00";
    }else{
        startTime = StringUtils.string2date(startDate);
    }

    String stopDate = request.getParameter("stopDate");
    if(stopDate==null){
        stopDate = StringUtils.date2string(new Date(startTime.getTime()+7*24*3600*1000L),"yyyy-MM-dd")+" 00:00:00";
    }
    String sql = "select a.name,a.id,b.phone from area a,(select distinct(tel) phone,area_id " +
            "from usr_user_login where login_time > to_date('"+startDate+"','YYYY-MM-DD HH24:MI:SS')" +
            "  and login_time<=to_date('"+stopDate+"','YYYY-MM-DD HH24:MI:SS')) b" +
            " where a.id = b.area_id ";
    long areaId = StringUtils.string2long(request.getParameter("areaId"),-1);
    List results =null;
    if(areaId>0){
        sql += " and b.area_id="+areaId;
        results = executeSql(sql,jdbcDriver,jdbcUrl,jdbcUser,jdbcPwd);
    }else{
        if("true".equals(request.getParameter("createAll"))){
            Map<String,String> all = new HashMap<String,String>();
            sql +=" order by phone";
            results = executeSql(sql,jdbcDriver,jdbcUrl,jdbcUser,jdbcPwd);
            if(results!=null){
                for(Object r:results){
                    if(r instanceof Object[]){
                        Object[] dataResult = (Object[])r;
                        List<Map<String,String>> dataRows = (List<Map<String,String>>) dataResult[1];
                        int size = dataRows.size();
                        if(size>0){
                            for(Map<String,String> row:dataRows){
                                String areaName = row.get("NAME");
                                String phones = all.get(areaName);
                                if(phones!=null&&!"".equals(phones)){
                                    phones +="\r\n";
                                }else{
                                }
                                if(phones==null){
                                    phones = "";
                                }
                                phones+=row.get("PHONE");
                                all.put(areaName, phones);
                            }
                            for(String key:all.keySet()){
                                String phones = all.get(key);
                                FileUtils.writeNew(application.getRealPath("/log/"),"p"+key+".txt",
                                        phones);
                            }
                        }
                    }
                }
            }
        }
    }
    List areaLoginStatics=executeSql("select b.id,b.name,a.loginCount from (select count(distinct(tel)) loginCount," +
            "area_id from usr_user_login where login_time > to_date('" +startDate+
            "','YYYY-MM-DD HH24:MI:SS') and login_time <=to_date('" +stopDate+
            "','YYYY-MM-DD HH24:MI:SS') group by area_id )a\n" +
            "    ,area b where a.AREA_ID = b.id order by loginCount",jdbcDriver,jdbcUrl,jdbcUser,jdbcPwd);
    List areas = executeSql("select id,name from area where id>300 and id< 1000",jdbcDriver,jdbcUrl,jdbcUser,jdbcPwd);
%><html>
<head>
    <title>登过燕赵视界平台手机号码列表</title>
</head>
<body>
   <form action="phoneList.jsp" method="post">
       <table width="100%">
           <tr><td>从<%=startDate%>到<%=stopDate%>登录情况统计：</td></tr>
           <tr>
               <td>
                   <table border="0" cellpadding="0" cellspacing="0" style="background: #E0E6E8">
<%
    for (Object r : areaLoginStatics){
        if (r instanceof Object[]){
            Object[] dataResult = (Object[]) r;
            List<Map<String,String>> dataRows = (List<Map<String,String>>) dataResult[1];
            int i=0;
            for(Map<String,String> row:dataRows){
                i++;
                long id = StringUtils.string2int(row.get("ID"),-1);
                String name = row.get("NAME");
                int count =StringUtils.string2int(row.get("LOGINCOUNT"),-1);
                out.print("<tr><td align='right' style=\"background: #F0F0F0;width:100px\">" +name+
                        "</td><td style='width:400px'>" +count+
                        "</td><td>");
/*
                if(i%4==0){
                    out.println("</tr><tr>");
                }
*/
            }
        }
    }
%>

                       </tr>
                   </table>
               </td>
           </tr>
           <tr>
               <td><label for="areaId">选择地区：</label><select id="areaId" name="areaId"><%--<option value="-1">全部</option>--%>
               <%
                   for (Object r : areas){
                       if (r instanceof Object[]){
                           Object[] dataResult = (Object[]) r;
                           List<Map<String,String>> dataRows = (List<Map<String,String>>) dataResult[1];
                           for(Map<String,String> row:dataRows){
                               long id = StringUtils.string2int(row.get("ID"),-1);
                               if(id>0){
                                   String selected = "";
                                   if(id==areaId){
                                       selected = " selected";
                                   }
               %><option value="<%=id%>"<%=selected%>><%=row.get("NAME")%></option><%
                               }
                           }
                       }
                   }
               %></select>
                   <label for="startDate">起始日期：</label><input id="startDate" type="text" name="startDate" value="<%=startDate%>">
                   <label for="stopDate">截至日期：</label><input id="stopDate" type="text" name="stopDate" value="<%=stopDate%>">
                   <input type="submit" value="搜索"></td>
           </tr>
           <%
               if(results!=null){
                   for(Object r:results){
                       if(r instanceof Object[]){
                           Object[] dataResult = (Object[])r;
                           List<Map<String,String>> dataRows = (List<Map<String,String>>) dataResult[1];
                           if(dataRows.size()>0){
                               out.println("<tr><td>共有"+dataRows.size()+"个号码：</td></tr>\r\n<tr><td>");
                               int i=0;
                               for(Map<String,String> row:dataRows){
                                   out.print(row.get("PHONE"));
                                   i++;
                                   if(i%10==0){
                                       out.print("</td></tr><tr><td>\r\n");
                                   }else{
                                       out.print("\t");
                                   }

                               }
                               out.println("</td></tr>");
                           }
                       }
                   }
               }
           %>
       </table>
   </form>
</body>
</html><%@include file="../admin/sqlBase.jsp"%>
