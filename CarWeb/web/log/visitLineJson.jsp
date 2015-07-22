<%@ page import="com.fortune.util.SpringUtils" %><%@ page
        import="java.sql.Connection" %><%@ page
        import="org.springframework.jdbc.datasource.DataSourceUtils" %><%@ page
        import="javax.sql.DataSource" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.sql.Statement" %><%@ page
        import="java.sql.ResultSet" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="java.sql.SQLException" %><%@ page
        import="java.util.*" %><%@ page
        import="com.fortune.util.JsonUtils" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2012-4-1
  Time: 14:12:40
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Date now = new Date();
    Date yesterday = new Date(now.getTime()-24*3600*1000L);
    String startTimeStr = request.getParameter("startTime");
    if(startTimeStr==null){
        startTimeStr = StringUtils.date2string(yesterday,"yyyy-MM-dd 00:00:00");
    }else{
        logger.debug("输入的起始时间："+startTimeStr);
    }
    String stopTimeStr = request.getParameter("stopTime");
    if(stopTimeStr==null){
        stopTimeStr = StringUtils.date2string(now,"yyyy-MM-dd 00:00:00");
    }else{
        logger.debug("输入的结束时间："+stopTimeStr);

    }
    int walkLength = StringUtils.string2int(request.getParameter("walkLength"),10);
    Date startTime = StringUtils.string2date(startTimeStr);
    Date stopTime = StringUtils.string2date(stopTimeStr);
    List<Map<String,Object>> visitLine = getVisitLine(startTime,stopTime,walkLength);
    out.println(JsonUtils.getListJsonString("visitLine",visitLine,"totalCount",visitLine.size()));
%><%!
    Logger logger = Logger.getLogger("com.fortune.rms.Jsp.visitLineJson.jsp");
    public Connection getConnection()
            throws Exception {
        return DataSourceUtils.getConnection((DataSource) SpringUtils.getBean("dataSource"));
    }

    
    public List<Map<String,Object>> getVisitLine(Date startTime,Date stopTime,int walkLengthMinute){
        Connection conn = null;
        Statement stmt = null;
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>> ();
        try {
            conn = getConnection();
            if(conn!=null){
                stmt = conn.createStatement();
                if(stmt!=null){
                    if(startTime.after(stopTime)){
                        Date temp = startTime;
                        startTime = stopTime;
                        stopTime = temp;
                    }
                    logger.debug("准备统计并发数据："+StringUtils.date2string(startTime) +","+StringUtils.date2string(stopTime));
                    ResultSet rst = null;
                    if(false){
                        rst = stmt.executeQuery("select count(*) from VISIT_LOG where start_time<=TO_DATE('" +
                                StringUtils.date2string(stopTime)+"','YYYY-MM-DD HH24:MI:SS') and end_time>TO_DATE('" +
                                StringUtils.date2string(startTime)+"','YYYY-MM-DD HH24:MI:SS')");
                        if(rst.next()){
                            Map<String,Object> data = new HashMap<String, Object>();
                            data.put("total",rst.getInt(1));
                            result.add(data);
                        }
                        rst.close();
                    }
                    for(long time = startTime.getTime();time<=stopTime.getTime();time+=walkLengthMinute*60*1000){
                        Date calTime = new Date(time);
                        rst = stmt.executeQuery("select count(*) from VISIT_LOG where start_time<=TO_DATE('" +
                                StringUtils.date2string(calTime)+"','YYYY-MM-DD HH24:MI:SS') and end_time>TO_DATE('" +
                                StringUtils.date2string(calTime)+"','YYYY-MM-DD HH24:MI:SS')");
                        if(rst.next()){
                            Map<String,Object> data = new HashMap<String, Object>();
                            data.put("dateTime",calTime);
                            data.put("time",StringUtils.date2string(calTime,"HH:mm"));
                            data.put("count",rst.getInt(1));
                            result.add(data);
                        }
                        rst.close();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if(stmt!=null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return result;
    }
%>