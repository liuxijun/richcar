<%@ page import="java.util.List" %><%@ page
        import="java.util.ArrayList" %>
<%@ page
        import="java.util.*" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.io.*" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-3-19
  Time: 下午3:07
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String sqlStr = request.getParameter("sql");
    if(sqlStr==null){
        sqlStr ="";// "select * from csp_channel where channel_id in(select id from channel where csp_id=1)";
    }
    String tableName = request.getParameter("tableName");
    if(tableName==null){
        int p = sqlStr.toLowerCase().indexOf(" from ");
        if(p>0){
            tableName = sqlStr.substring(p+6);
            p = tableName.indexOf(" ");
            if(p>0){
                tableName = tableName.substring(0,p);
            }
        }
    }

    List<String> sqlStrings=new ArrayList<String>(0);
    if(tableName!=null){
        logger.debug("准备输出"+tableName+"的数据....");
        sqlStrings.addAll(exportData(sqlStr, tableName, application.getRealPath("/export/" + tableName + ".sql")));
    }
%><html>
<head>
    <title></title>
</head>
<body>
<pre>
<%
    for(String sql:sqlStrings){
        out.println(""+sql);
    }
%>
</pre>
<form action="?" method="post">
    <table style="width:900px">
        <tr>
            <td>SQL:</td>
            <td>
                <textarea style="width:800px;height:100px;" name="sql"><%=sqlStr%></textarea>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit">
            </td>
        </tr>
    </table>
</form>
</body>
</html><%!
    @SuppressWarnings("unchecked")
    public List<String> exportData(String sql,String tableName,String fileName){

        //jdbcUser = "rms";
        //jdbcPwd = "rms";
//        FileWriter fw = null;
        OutputStreamWriter fw=null;
        List<String> sqlStrings = null;
        try {
            sqlStrings = new ArrayList<String>();
            if(fileName!=null){
                fw =  new OutputStreamWriter(new FileOutputStream(new File(fileName)),"UTF-8");
                // = new FileOutputStream()
            }
            String countSql = "select count(*) cc from ("+sql+")";
            List<Object> countResult = executeSql(countSql,null,null,null,null);
            int count = 0;
            if(countResult!=null){
                for(Object result:countResult){
                    if(result instanceof Object[]){
                        Object[] dataResult = (Object[]) result;
                        List<Map<String,String>> dataRows = (List<Map<String,String>>) dataResult[1];
                        String[] colNames = (String[]) dataResult[0];
                        count = StringUtils.string2int(dataRows.get(0).get(colNames[0]),0);
                        break;
                    }
                }
            }
            int startRow = 0;
            //每次搜索一万条
            if(fw!=null){
                fw.write("delete from "+tableName+";\r\n");
            }
            while(startRow<count){
                int endRow = startRow+5000;
                sqlStrings.clear();
                String tempSql ="select * from (select tempT.*,rownum pageRN from ("+sql+") tempT where rownum<"+(endRow)+") where pageRN>="+startRow;
                List<Object> executeResult = executeSql(tempSql,null,null,null,null);
                if(executeResult!=null){
                    int i=0;
                    for(Object result:executeResult){
                        i++;
                        if(result instanceof Object[]){
                            Object[] dataResult = (Object[]) result;
                            String[] colNames = (String[]) dataResult[0];
                            List<Map<String,String>> dataRows = (List<Map<String,String>>) dataResult[1];
                            String insertSql = "insert into "+tableName+"(";
                            String values = "";
                            for(String colName:colNames){
                                if("PAGERN".equals(colName.toUpperCase())){
                                    continue;
                                }
                                insertSql+=colName+",";
                            }
                            insertSql=insertSql.substring(0,insertSql.length()-1);
                            insertSql +=")";

                            for(Map<String,String>row :dataRows){
                                values="values(";
                                for(String colName:colNames){
                                    if("PAGERN".equals(colName.toUpperCase())){
                                        continue;
                                    }
                                    String value = row.get(colName);
                                    if(value ==null||"".equals(value)){
                                        values+="null,";
                                    }else{
                                        value = value.replaceAll("'", "’");
                                        value = value.replaceAll("\\\\","\\\\\\\\");
                                        values+="'"+value+"',";
                                    }
                                }
                                values = values.substring(0,values.length()-1)+");";
                                String sqlString = insertSql+values;
                                sqlStrings.add(sqlString);
                                if(fw!=null){
                                    fw.write(sqlString+"\r\n");
                                    if(i%100==0){
                                        fw.flush();
                                    }
                                }
                            }
                        }
                    }
                    if(fw!=null){
                        fw.write("commit;\r\n");
                    }
                }
                startRow = endRow;
            }
            if(fw!=null){
                fw.write("commit;\r\n");
                fw.close();
            }
        } catch (IOException e) {
            sqlStrings.add(e.getMessage());
            e.printStackTrace();
        }
        return sqlStrings;
    }

%><%@include file="sqlBase.jsp"%>