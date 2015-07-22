<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ page
        import="java.sql.Connection" %><%@page
        import="java.sql.Statement" %><%@page
        import="java.sql.ResultSet" %><%@page
        import="java.sql.SQLException" %><%@ page
        import="java.text.SimpleDateFormat" %><%@ page
        import="java.text.ParseException,java.util.Date" %><%@ page
        import="org.apache.log4j.Logger,java.util.HashMap,java.util.Map,java.util.ArrayList,java.util.List" %><%!
    String jdbcDriver=null;
    String jdbcUrl = null;
    String jdbcUser = null;
    String jdbcPwd = null;
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.sqlBase.jsp");
    public List<Object> executeSql(String sql,String jdbcDriver,String jdbcUrl,String jdbcUser,String jdbcPwd){
        List<Object> executeResult = new ArrayList<Object>();
        if(sql == null || "".equals(sql)){
            executeResult.add("输入条件为空");
            return executeResult;
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rst;
        try {
            conn = getConnection(/*jdbcDriver,jdbcUrl,jdbcUser,jdbcPwd*/);
            stmt = conn.createStatement();
            String[] st = sql.split(";");
            for(String subSql:st){
                if(subSql == null){
                    continue;
                }
                if("".equals(subSql.trim())){
                    continue;
                }
                try {
                    int strLen = subSql.length();
                    if(strLen > 100){
                        strLen = 100;
                    }
                    if(subSql.toUpperCase().trim().indexOf("SELECT ")==0){
                        rst = stmt.executeQuery(sql);
                        java.sql.ResultSetMetaData metaData = rst.getMetaData();
                        if(metaData != null){
                            int colCount = metaData.getColumnCount();
                            String[] colNames = new String[colCount];
                            for(int i=1;i<=colCount;i++){
                                colNames[i-1] = metaData.getColumnName(i);
                            }
                            Object[] dataResult = new Object[2];
                            dataResult[0] = colNames;
                            List<Map<String,String>> rows =new ArrayList<Map<String,String>>();
                            dataResult[1] = rows;
                            int rowCount=0;
                            while(rst.next()){
                                Map<String,String> row = new HashMap<String,String>();
                                for(int i=1;i<=colCount;i++){
                                    String value = rst.getString(i);
                                    String name = colNames[i-1];
                                    row.put(name,value);
                                    //out.println("<!--"+name+"-->\n");
                                }
                                rows.add(row);
                                rowCount++;
                                // logger.debug("数据条数："+rowCount);
                            }
                            executeResult.add(dataResult);
                        }
                        rst.close();
                    }else{
                        executeResult.add("成功处理"+
                                stmt.executeUpdate(subSql)+
                                "条记录["+subSql.substring(0,strLen)+"...]");
                    }
                } catch (SQLException e) {
                    executeResult.add("0");
                    executeResult.add("error-msg:"+e.getMessage()+"["+subSql+"]");
                    executeResult.add("不能处理处理"+
                            //stmt.executeUpdate(subSql)+
                            "SQL:"+e.getMessage()+"["+subSql+"...]");
                    logger.error("执行sql时抛出异常："+e.getMessage()+"\nsql="+subSql);
                    //e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                }
            }
        } catch (SQLException e) {
            executeResult.add("error-msg"+e.getMessage());
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
        return executeResult;
    }
    public String checkValue(String inputStr) {
        if (inputStr == null) {
            return "";
        } else {
            return inputStr;
        }
    }

    public String date2string(Date date) {
        return date2string(date, "yyyy-MM-dd HH:mm:ss");
    }

    public  String date2string(String date) {
        try {
            return date2string(Long.parseLong(date));
        } catch (NumberFormatException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return date;
    }

    public  String date2string(long date) {
        java.util.Date theTime = new java.util.Date(date);
        return date2string(theTime);
    }

    public  String date2string(Date date, String format) {
        if (date == null) {
            date = new java.util.Date();
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }
    public  Date string2date(String dateStr, String format,Date defaultValue) {
        if (dateStr == null) {
            return defaultValue;
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            return sf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
    public Date string2date(String dateStr){
        return string2date(dateStr,"yyyy-MM-dd HH:mm:ss",new Date());
    }
    public String getClearFileName(String fileName) {
        int i = fileName.lastIndexOf("/");
        if (i >= 0 && i < fileName.length() - 1) {
            fileName = fileName.substring(i + 1);
        }
        return fileName;
    }
    public Date getDateParameter(String parameter,Date defaultValue){
        if(parameter == null){
            return defaultValue;
        }
        return string2date(parameter,null,defaultValue);
    }
    public long getLongParameter(Map dataSet , String parameterName,long defaultValue){
        if(parameterName == null){
            return defaultValue;
        }
        String parameterValue = (String) dataSet.get(parameterName);
        if(parameterValue==null || "".equals(parameterValue.trim())){
            return defaultValue;
        }
        parameterValue = parameterValue.trim();
        try {
            return Long.parseLong(parameterValue);
        } catch (NumberFormatException e) {
            System.err.println("param:"+parameterName+",value:"+parameterValue+",error:"+e.getMessage());
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e){
            System.err.println("param:"+parameterName+",value:"+parameterValue+",error:"+e.getMessage());
        }
        return defaultValue;
    }
    public long getLongParameter(String parameter,long defaultValue){
        if(parameter == null){
            return defaultValue;
        }
        try {
            return Long.parseLong(parameter);
        } catch (NumberFormatException e) {
            System.err.println("param:"+parameter+",value:");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }
    public int getIntParameter(String parameter,int defaultValue){
        return(int) getLongParameter(parameter,defaultValue);
    }
    public float getFloatParameter(String parameter,float defaultValue){
        if(parameter ==null){
            return defaultValue;
        }
        try {
            return Float.parseFloat(parameter);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }

    public Connection getConnection(/*String jdbcDriver ,String jdbcUrl,String jdbcUser,String jdbcPwd*/) {
        try {
            com.fortune.util.AppConfigurator config = com.fortune.util.AppConfigurator.getInstance("/jdbc.properties");
            if(jdbcDriver == null){
                jdbcDriver =config.getConfig("jdbc.driverClassName","com.mysql.jdbc.Driver");
            }
            if(jdbcUrl == null){
                jdbcUrl = config.getConfig("jdbc.url","jdbc:mysql://localhost:3306/redex");
            }
            if(jdbcUser==null){
                jdbcUser = config.getConfig("jdbc.username","redex");
            }
            if(jdbcPwd==null){
                jdbcPwd = config.getConfig("jdbc.password","redex");
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