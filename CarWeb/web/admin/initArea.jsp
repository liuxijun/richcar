<%@ page import="java.util.*" %><%@ page import="java.sql.*" %><%@ page import="org.hibernate.cfg.Configuration" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-3-22
  Time: 19:06:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Simple jsp page</title></head>
  <body><pre><%=executeSql()%></pre></body>
</html> <%!
    public List<String> getAColResult(String sql){
        List<Map<String,String>> tempResult = getRowResultset(sql);
        List<String> result = new ArrayList<String>();
        for(Map<String,String> row:tempResult){
            for(String key:row.keySet()){
                result.add(row.get(key));
                break;
            }
        }
        return result;
    }
    public List<Map<String,String>> getRowResultset(String sql){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rst = null;
        List<Map<String,String>> result=new ArrayList<Map<String,String>>();
        try {
            conn = getConnection("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/doshow",
                    "doshow","doshow");
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            java.sql.ResultSetMetaData metaData = rst.getMetaData();
            if(metaData != null){
                int colCount = metaData.getColumnCount();
                String[] colNames = new String[colCount];
                for(int i=1;i<=colCount;i++){
                   colNames[i-1] = metaData.getColumnName(i);
                }
                while(rst.next()){
                    Map<String,String> row = new HashMap<String,String>();
                    for(int i=1;i<=colCount;i++){
                        String value = rst.getString(i);
                        String name = colNames[i-1];
                        row.put(name,value);
                        //out.println("<!--"+name+"-->\n");
                    }
                    result.add(row);
                }
            }
            rst.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally{
            try {
                if(stmt!=null){
                    stmt.close();
                }
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return result;
    }
    public String executeSql(){
        StringBuffer executeResult = new StringBuffer();
        StringBuffer operatorMacSql = new StringBuffer();
        StringBuffer operatorLssSql = new StringBuffer();
        String sqlHeader = "insert into sys_area(area_id,name,parent_id,code,description) values(";

        String oprSqlHeader = "insert into security_operator" +
                "(status,operatorid,realname,login,area_id,password) values(1,";
        List<String> regions = getAColResult("select distinct(Region) reg from sys_4s");
        int id = 1;
        int macId = 100;
        int proId=1000;
        int cityId=10000;
        int dealerId=100000;
        int operatorId=100;
        int lssOperatorId=1000;
            for(String regionName:regions){
                id++;
                /*
                executeResult.append(sqlHeader).append(id).append(",").append("\"")
                        .append(regionName).append("\",-1,\"").append(id).append("\");\r\n");
//  */
/*
                List<String> provinces = getAColResult("select distinct(Province) pro from sys_4s where Region like '"+regionName+"'");
                for(String provinceName:provinces){
                    executeResult.append(sqlHeader).append(proId).append(",").append("\"")
                        .append(provinceName).append("\",").append(id).append(",\"pro").append(proId).append("\");\r\n");
                    List<String> cities = getAColResult("select distinct(City) city from sys_4s where Province like '"+provinceName+"'");
                    for(String cityName:cities){
                        executeResult.append(sqlHeader).append(cityId).append(",").append("\"")
                            .append(cityName).append("\",").append(proId).append(",\"").append(cityId).append("\");\r\n");
                        List<String> dealers = getAColResult("select distinct(DealerName) from sys_4s where City like '"+cityName+"'");
                        for(String dealerName:dealers){
                           // String dealerName=dealer.get("DealerName");
                            List<String> codes = getAColResult("select Code from sys_4s where DealerName like '"+dealerName+"'");
                            String code="";
                            if(codes.size()>0){
                                code = codes.get(0);
                            }
                            executeResult.append(sqlHeader).append(dealerId).append(",").append("\"")
                                .append(dealerName).append("\",").append(cityId).append(",\"").append(code).append("\");\r\n");
                            dealerId++;
                        }
                        cityId++;
                    }
                    proId++;
                }
//*/
///*
                List<String>  macs = getAColResult("select distinct(MAC) pro from sys_4s where Region like '"+regionName+"'" +
                        " and MAC not like ''");
                for(String mac:macs){
                    List<String> cities = getAColResult("select distinct(City) city from sys_4s where MAC like '"+mac+"'");
                    String cityNames = "";
                    for(String cityName:cities){
                        if(!"".equals(cityNames)){
                            cityNames +=",";
                        }
                        cityNames+=cityName;
/*
                        executeResult.append(sqlHeader).append(cityId).append(",").append("\"")
                            .append(cityName).append("\",").append(proId).append(",\"").append(cityId).append("\");\r\n");
*/
                        executeResult.append("update sys_area set parent_id=").append(macId)
                                .append(" where name like '").append(cityName).append("' and area_id>=10000;\n");
                        String sql ="select a.area_id area_id,s.code,s.DealerName code from sys_area a,sys_4s s where " +
                                " a.name = s.DealerName and a.area_id>=100000 and a.parent_id in " +
                                " (select area_id from sys_area where name like '"+cityName+"')";
//                        System.out.println(sql);
                        List<Map<String,String>> dealers =  getRowResultset(sql);
                        for(Map<String,String> dealerInfo:dealers){
                           // String dealerName=dealer.get("DealerName");
                            String code = dealerInfo.get("Code").toLowerCase();
                            String areaId=dealerInfo.get("area_id");
                            operatorLssSql.append(oprSqlHeader).append(lssOperatorId++).append(",'").append(dealerInfo.get("DealerName")).append(code).append("','")
                                    .append(code).append("',")
                                    .append(areaId).append(",md5('").append(code).append("')");
                            operatorLssSql.append(");\n");
                            dealerId++;
                        }
                        cityId++;
                    }
                    //operatorid,realname,login,area_id,password
                    String pinyin = com.fortune.util.HzUtils.getFullSpell(mac);
                    operatorMacSql.append(oprSqlHeader).append(operatorId++).append(",'").append(mac).append("','")
                            .append(pinyin).append("',")
                            .append(macId).append(",'12345678").append(pinyin).append("'");
                    operatorMacSql.append(");\n");
                    executeResult.append(sqlHeader).append(macId).append(",").append("\"MAC")
                            .append(macId).append("(").append(mac).append(")\",").append(id).append(",\"mac")
                            .append(macId).append("\",\"主管").append(cityNames).append("\");\r\n");
                    macId++;
                }
//*/
            }
        executeResult.append("\n").append(operatorMacSql).append("\n").append(operatorLssSql);
        return executeResult.toString();
    }
   static Configuration hibernateConfig = new Configuration();
   static String driverClass = hibernateConfig.getProperty("jdbc.driverClassName");
   static String connectionUrl = hibernateConfig.getProperty("jdbc.url");
   static String username= hibernateConfig.getProperty("jdbc.username");
   static String password=hibernateConfig.getProperty("jdbc.password");

   public Connection getConnection(String jdbcDriver ,String jdbcUrl,String jdbcUser,String jdbcPwd) {
       try {
           if(jdbcDriver == null){
               jdbcDriver = "com.mysql.jdbc.Driver";
           }
           if(jdbcUrl == null){
               jdbcUrl = "jdbc:mysql://localhost:3306/redex";
           }
           if(jdbcUser==null){
               jdbcUser = "redex";
           }
           if(jdbcPwd==null){
               jdbcPwd = "redex";
           }
           Class.forName(jdbcDriver).newInstance();
           return java.sql.DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPwd);
       } catch (InstantiationException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }
%>