<%@ page import="java.sql.*" %><%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2010-12-7
  Time: 11:36:16
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String connectionUrl = "jdbc:sqlserver://localhost:1433;databasename=mediamex_xjxy;selectMethod=cursor";
    String username = "mediamex_xjxy";
    String password="mediamex_xjxy";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rst;
    try {
        try {
            Class.forName(driverClass).newInstance();
            conn = java.sql.DriverManager.getConnection(connectionUrl,username,password);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
/*
        String sqlStr = "select 'insert into res_media(channel_id,create_time,online_date,offline_date,name,description,url,pub_flag)"+
                " values('+convert(char,channel_id)+','''+convert(nvarchar,[submit_time],20)+''','''+convert(nvarchar,[submit_time],20)+"+
                "''','''+convert(nvarchar,[expire_time],20)+''','''+content_name+''','''+content_description+''','''+CONTENT_ACCESS_URL+''',2);' FROM content_info where content_status=7";
*/
        String sqlStr = "select * from wmv where channel_id in('26','31') and submit_time>'2010-12-08 00:00:00'";
        stmt = conn.createStatement();
        rst = stmt.executeQuery(sqlStr);
        Map<String,String> channelIds = new HashMap<String,String>();
        channelIds.put("1","1001");
        channelIds.put("2","1003");
        channelIds.put("35","1002");
        channelIds.put("26","1004");
        channelIds.put("6","1012");
        channelIds.put("11","1005");
        channelIds.put("19","1008");
        channelIds.put("24","1009");
        channelIds.put("30","1011");
        channelIds.put("31","1010");
        channelIds.put("34","1013");
        while(rst.next()){
            String channelId = rst.getString("channel_id");
            String name = rst.getString("content_name");
            String onlineDate = rst.getString("submit_time");
            String offlineDate = rst.getString("expire_time");
            String desc = rst.getString("content_description");
            String url = rst.getString("CONTENT_ACCESS_URL");
            
            String pubFlag="2";
            if(channelId==null){
                channelId="-1";
            }else{
                String newChannelId = channelIds.get(channelId);
                if(newChannelId!=null){
                    channelId = newChannelId;
                }
            }
            String insertSQL = "insert into res_media(channel_id,create_time,online_date,offline_date,name," +
                    "description,url,pub_flag,device_id)" +
                    " values('"+channelId+"','"+onlineDate+"','"+onlineDate+"','"+offlineDate+"',"+
                    "'"+name+"','"+desc+"','"+url+"',"+pubFlag+",5);";
            out.println(insertSQL);
        }
        rst.close();
    } catch (SQLException e) {
        e.printStackTrace();  //To change body of catch statement use Options | File Templates.
    } finally {
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
        }
    }
%>