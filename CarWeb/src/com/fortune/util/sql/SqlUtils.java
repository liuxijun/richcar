package com.fortune.util.sql;

import com.fortune.util.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-25
 * Time: 15:45:32
 * To change this template use File | Settings | File Templates.
 */
public class SqlUtils {

    //���ر���ֶ����ƣ���������
    public static Logger logger = Logger.getLogger("com.fortune.util.sql.SqlUtils");
    public static String[][] getTableCols(String sql, Connection conn) throws Exception{
        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.Statement sqlStmt=null; //SQL������
        java.sql.ResultSet sqlRst=null; //���������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);
            //����һ�����Թ�����ֻ����SQL������
            sqlStmt = sqlCon.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);

            sqlRst = sqlStmt.executeQuery(sql);

            java.sql.ResultSetMetaData rsm=sqlRst.getMetaData();
            String result[][] = new String[rsm.getColumnCount()][2];
            for (int i=0; i<rsm.getColumnCount(); i++){
                result[i][0] = rsm.getColumnName(i+1);
                result[i][1] = rsm.getColumnTypeName(i+1);
            }

            return result;

        }catch (Exception e) {
            logger.error("�޷�ִ��SQL����"+sql+"����������Ϣ��"+e.getMessage());
            e.printStackTrace();
            throw e;
        }finally {
            if(sqlRst != null){
                sqlRst.close();
            }
            if(sqlStmt != null){
                sqlStmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }
    }

    //��ѯsql��start��0��ʼ,��PreparedStatement��ѯ
    //new String[][]{ {"long","1"},{"long","2"},{"String","ss"} }

    public static List getPreparedRecords(String sql,String[][] params, long start, long range, Connection conn) throws Exception {
        List list = new ArrayList();
        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.PreparedStatement sqlPstmt=null; //SQL������
        java.sql.ResultSet sqlRst=null; //���������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);


            //����һ�����Թ�����ֻ����SQL������
            sqlPstmt = sqlCon.prepareStatement(sql,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);

            for (int i=0;i<params.length;i++){
                String colType = params[i][0];
                String colValue= params[i][1];
                if ("long".equals(colType)){
                    long paramValue = 0;
                    try{
                        paramValue = Long.parseLong(colValue);
                    }catch(Exception e){}
                    sqlPstmt.setLong(i+1,paramValue);
                }
                if ("String".equals(colType)){
                    sqlPstmt.setString(i+1,colValue);
                }
                if ("Date".equals(colType)){
                    if (colValue==null || "".equals(colValue)){
                        sqlPstmt.setTimestamp(i+1, null);
                    } else if ("sysdate".equals(colValue)){
                        sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(System.currentTimeMillis()));
                    } else {
                        if (colValue.indexOf(":")>-1){
                            Date colDate = StringUtils.string2date(colValue,"yyyy-MM-dd HH:mm:ss");
                            sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(colDate.getTime()));
                        }else {
                            Date colDate = StringUtils.string2date(colValue,"yyyy-MM-dd");
                            sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(colDate.getTime()));
                        }
                    }
                }
            }

            sqlRst = sqlPstmt.executeQuery();
            sqlRst.last();
            list.add(new Long(sqlRst.getRow()));
            if (start>0){
                sqlRst.absolute((int)start);
            }else{
                sqlRst.beforeFirst();
            }
            java.sql.ResultSetMetaData rsm=sqlRst.getMetaData();
            while(sqlRst.next() && (range-->0)){
                if (rsm.getColumnCount()==1){
                    Object obj = sqlRst.getObject(1);
                    list.add(obj);
                }else{
                    Object[] objs = new Object[rsm.getColumnCount()];
                    for (int i=0; i<rsm.getColumnCount(); i++){
                        objs[i]=sqlRst.getObject(i+1);
                    }
                    list.add(objs);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            //System.err.println(e.getMessage());
            throw e;
        }finally {
            if(sqlRst != null){
                sqlRst.close();
            }
            if(sqlPstmt != null){
                sqlPstmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }
        return list;
    }

    //��ѯsql������һ����ֵ,��PreparedStatement��ѯ
    //new String[][]{ {"long","1"},{"long","2"},{"String","ss"} }
    public static long getPreparedLong(String sql,String[][] params, Connection conn) throws Exception {

        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.PreparedStatement sqlPstmt=null; //SQL������
        java.sql.ResultSet sqlRst=null; //���������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);

            //����һ�����Թ�����ֻ����SQL������
            sqlPstmt = sqlCon.prepareStatement(sql,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);

            for (int i=0;i<params.length;i++){
                String colType = params[i][0];
                String colValue= params[i][1];
                if ("long".equals(colType)){
                    long paramValue = 0;
                    try{
                        paramValue = Long.parseLong(colValue);
                    }catch(Exception e){}
                    sqlPstmt.setLong(i+1,paramValue);
                }
                if ("String".equals(colType)){
                    sqlPstmt.setString(i+1,colValue);
                }
                if ("Date".equals(colType)){
                    if (colValue==null || "".equals(colValue)){
                        sqlPstmt.setTimestamp(i+1, null);
                    } else if ("sysdate".equals(colValue)){
                        sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(System.currentTimeMillis()));
                    } else {
                        if (colValue.indexOf(":")>-1){
                            Date colDate = StringUtils.string2date(colValue,"yyyy-MM-dd HH:mm:ss");
                            sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(colDate.getTime()));
                        }else {
                            Date colDate = StringUtils.string2date(colValue,"yyyy-MM-dd");
                            sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(colDate.getTime()));
                        }
                    }
                }
            }

            sqlRst = sqlPstmt.executeQuery();

            if (sqlRst.next()){
                return sqlRst.getLong(1);
            }

        }catch (Exception e) {
            e.printStackTrace();
            //System.err.println(e.getMessage());
            throw e;
        }finally {
            if(sqlRst != null){
                sqlRst.close();
            }
            if(sqlPstmt != null){
                sqlPstmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }
        return 0;
    }


    //��ѯsql������һ����ֵ,��PreparedStatement��ѯ
    //new String[][]{ {"long","1"},{"long","2"},{"String","ss"} }
    public static int executePrepared(String sql,String[][] params, Connection conn) throws Exception {

        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.PreparedStatement sqlPstmt=null; //SQL������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);

            //����һ�����Թ�����ֻ����SQL������
            sqlPstmt = sqlCon.prepareStatement(sql,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);

            for (int i=0;i<params.length;i++){
                String colType = params[i][0];
                String colValue= params[i][1];
                if ("long".equals(colType)){
                    long paramValue = 0;
                    try{
                        paramValue = Long.parseLong(colValue);
                    }catch(Exception e){}
                    sqlPstmt.setLong(i+1,paramValue);
                }
                if ("double".equals(colType)){
                    double paramValue = 0;
                    try{
                        paramValue = Double.parseDouble(colValue);
                    }catch(Exception e){}
                    sqlPstmt.setDouble(i+1,paramValue);
                }
                if ("String".equals(colType)){
                    sqlPstmt.setString(i+1,colValue);
                }
                if ("Date".equals(colType)){
                    if (colValue==null || "".equals(colValue)){
                        sqlPstmt.setTimestamp(i+1, null);
                    } else if ("sysdate".equals(colValue)){
                        sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(System.currentTimeMillis()));
                    } else {
                        if (colValue.indexOf(":")>-1){
                            Date colDate = StringUtils.string2date(colValue,"yyyy-MM-dd HH:mm:ss");
                            sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(colDate.getTime()));
                        }else {
                            Date colDate = StringUtils.string2date(colValue,"yyyy-MM-dd");
                            sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(colDate.getTime()));
                        }
                    }
                }
            }
            return sqlPstmt.executeUpdate();

        }catch (Exception e) {
            //e.printStackTrace();
            //System.err.println(e.getMessage());
            throw e;
        }finally {
            if(sqlPstmt != null){
                sqlPstmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }
    }

    //��ѯsql������һ����ֵ,��PreparedStatement��ѯ
    //new Object[]{ 1,"ss" }
    public static void executePreparedBatch(String sql,List params, Connection conn) throws Exception {

        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.PreparedStatement sqlPstmt=null; //SQL������
        int j=0;
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);

            //����һ�����Թ�����ֻ����SQL������
            sqlPstmt = sqlCon.prepareStatement(sql,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);

            for (; j<params.size(); j++){
                Object oneParams[] = (Object[])params.get(j);

                for (int i=0;i<oneParams.length;i++){
                    Object param = oneParams[i];
                    if (param instanceof Integer) {
                        long paramValue = 0;
                        try{
                            paramValue = (Long)param;
                        }catch(Exception e){}
                        sqlPstmt.setLong(i+1,paramValue);
                    } else if (param instanceof String) {
                        String paramValue = "";
                        try{
                            paramValue = (String)param;
                        }catch(Exception e){}
                        sqlPstmt.setString(i+1,paramValue);
                    } else if (param instanceof Date) {
                        Date paramValue = null;
                        try{
                            paramValue = (Date)param;
                        }catch(Exception e){}
                        sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(paramValue.getTime()));
                    } else if (param instanceof Float) {
                        Float paramValue = 0.0f;
                        try{
                            paramValue = (Float)param;
                        }catch(Exception e){}
                        sqlPstmt.setFloat(i+1,paramValue);
                    } else if (param instanceof Long) {
                        long paramValue = 0;
                        try{
                            paramValue = (Long)param;
                        }catch(Exception e){}
                        sqlPstmt.setLong(i+1,paramValue);
                    }

                }

                sqlPstmt.addBatch();

                if (j % 10 ==0){
                    logger.debug("��1000����¼��ִ��һ��executeBatch: ����="+j);
                    sqlPstmt.executeBatch();
                }

            }

            sqlPstmt.executeBatch();

        }catch (Exception e) {
            e.printStackTrace();
            logger.error("��������ʱ�����쳣��"+e.getMessage()+"��������ʱ�������£�");
            for(int i=0;i<10&&j>=0;i++,j--){
                Object[] data =(Object[]) params.get(j);
                int k=0;
                String row = "";
                for(;k<data.length;k++){
                    Object d = data[k];
                    row+=("data["+j+"]["+k+"]="+d+"\t");
                }
                logger.error(row);
                
            }
            //System.err.println(e.getMessage());
            throw e;
        }finally {
            if(sqlPstmt != null){
                sqlPstmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }
    }



    //��ѯsql������һ����ֵ,��PreparedStatement��ѯ
    //new String[][]{ {"long","1"},{"long","2"},{"String","ss"} }
    public static void executePreparedBatch1(String sql,List params, Connection conn) throws Exception {

        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.PreparedStatement sqlPstmt=null; //SQL������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);

            //����һ�����Թ�����ֻ����SQL������
            sqlPstmt = sqlCon.prepareStatement(sql,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);

            for (int j=0; j<params.size(); j++){
                String oneParams[][] = (String[][])params.get(j);

                for (int i=0;i<oneParams.length;i++){
                    String colType = oneParams[i][0];
                    String colValue= oneParams[i][1];
                    if ("long".equals(colType)){
                        long paramValue = 0;
                        try{
                            paramValue = Long.parseLong(colValue);
                        }catch(Exception e){}
                        sqlPstmt.setLong(i+1,paramValue);
                    }
                    if ("String".equals(colType)){
                        sqlPstmt.setString(i+1,colValue);
                    }
                    if ("Date".equals(colType)){
                        if (colValue==null || "".equals(colValue)){
                            sqlPstmt.setTimestamp(i+1, null);
                        } else if ("sysdate".equals(colValue)){
                            sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(System.currentTimeMillis()));
                        } else {
                            if (colValue.indexOf(":")>-1){
                                Date colDate = StringUtils.string2date(colValue,"yyyy-MM-dd HH:mm:ss");
                                sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(colDate.getTime()));
                            }else {
                                Date colDate = StringUtils.string2date(colValue,"yyyy-MM-dd");
                                sqlPstmt.setTimestamp(i+1, new java.sql.Timestamp(colDate.getTime()));
                            }
                        }
                    }
                }

                sqlPstmt.addBatch();

                if (j % 1000 ==0){
                    sqlPstmt.executeBatch();
                }

            }

            sqlPstmt.executeBatch();

        }catch (Exception e) {
            e.printStackTrace();
            //System.err.println(e.getMessage());
            throw e;
        }finally {
            if(sqlPstmt != null){
                sqlPstmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }
    }
    //��ѯsql������һ����ֵ,��PreparedStatement��ѯ
    //new String[][]{ {"long","1"},{"long","2"},{"String","ss"} }
    public static void executeBatch(List sqlList, Connection conn) throws Exception {

        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.Statement sqlStmt=null; //SQL������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);

            sqlStmt= sqlCon.createStatement();

            for (int i=0; i<sqlList.size(); i++){
                String sql = (String)sqlList.get(i);
                sqlStmt.addBatch(sql);
                if (i % 1000 ==0){
                    sqlStmt.executeBatch();
                }
            }
            sqlStmt.executeBatch();

        }catch (Exception e) {
            e.printStackTrace();
            //System.err.println(e.getMessage());
            throw e;
        }finally {
            if(sqlStmt != null){
                sqlStmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }
    }

    //��ѯsql��start��0��ʼ
    public static List getRecords(String sql, long start, long range, Connection conn)  throws Exception {
        List list = new ArrayList();
        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.Statement sqlStmt=null; //SQL������
        java.sql.ResultSet sqlRst=null; //���������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);
            //����һ�����Թ�����ֻ����SQL������
            sqlStmt = sqlCon.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);

            sqlRst = sqlStmt.executeQuery(sql);
            sqlRst.last();
            list.add(new Long(sqlRst.getRow()));
            if (start>0){
                sqlRst.absolute((int)start);
            }else{
                sqlRst.beforeFirst();
            }
            java.sql.ResultSetMetaData rsm=sqlRst.getMetaData();
            while(sqlRst.next() && (range-->0)){
                if (rsm.getColumnCount()==1){
                    Object obj = sqlRst.getObject(1);
                    list.add(obj);
                }else{
                    Object[] objs = new Object[rsm.getColumnCount()];
                    for (int i=0; i<rsm.getColumnCount(); i++){
                        objs[i]=sqlRst.getObject(i+1);
                    };
                    list.add(objs);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            if(sqlRst != null){
                sqlRst.close();
            }
            if(sqlStmt != null){
                sqlStmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }

        return list;

    }

    //��ѯsql��������ֵ
    public static long getLong(String sql, Connection conn)  throws Exception {
        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.Statement sqlStmt=null; //SQL������
        java.sql.ResultSet sqlRst=null; //���������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);
            //����һ�����Թ�����ֻ����SQL������
            sqlStmt = sqlCon.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);

            sqlRst = sqlStmt.executeQuery(sql);

            if (sqlRst.next()){
                return sqlRst.getLong(1);
            }

        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            if(sqlRst != null){
                sqlRst.close();
            }
            if(sqlStmt != null){
                sqlStmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }
        return 0;
    }

    //ִ��sql
    public static int executeSQL(String sql, Connection conn) throws Exception{

        Connection sqlCon=null; //���ݿ����Ӷ���
        java.sql.Statement sqlStmt=null; //SQL������
        try{
            //װ��JDBC��������
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //�������ݿ�
            sqlCon = conn;
            sqlCon.setAutoCommit(true);
            //����һ�����Թ�����ֻ����SQL������
            sqlStmt = sqlCon.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);
            return sqlStmt.executeUpdate(sql);

        }catch (Exception e) {
            System.err.println(sql);
            e.printStackTrace();
            throw e;
        }finally {
            if(sqlStmt != null){
                sqlStmt.close();
            }
            if(sqlCon != null){
                sqlCon.close();
            }
        }


    }


}
