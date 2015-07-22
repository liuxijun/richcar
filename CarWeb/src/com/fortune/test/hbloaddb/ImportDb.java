package com.fortune.test.hbloaddb;


import com.fortune.util.StringUtils;
import com.fortune.util.sql.SqlUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-5-26
 * Time: 10:58:58
 * 数据库导入
 */
public class ImportDb {
    protected String sourceConnStr = "jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=yes)(failover=yes)(ADDRESS=(PROTOCOL=TCP)(HOST=mstk1.guanghua.com)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=mstk2.guanghua.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=mediastk.guanghua.com)))";
    //连接数据库的用户名
    protected String sourceUser = "mediastack4";
    //连接数据库的密码
    protected String sourcePwd = "stack4.2008";
    //装载JDBC驱动程序
    public Connection getSourceConnection() {
        try {
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //连接数据库
            return java.sql.DriverManager.getConnection(sourceConnStr, sourceUser, sourcePwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String connStr = "jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=yes)(failover=yes)(ADDRESS=(PROTOCOL=TCP)(HOST=mstk1.guanghua.com)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=mstk2.guanghua.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=mediastk.guanghua.com)))";
    //连接数据库的用户名
    protected String user = "rms";
    //连接数据库的密码
    protected String pwd = "rms";
    public Connection getDesertConnection() {
        try {
            //装载JDBC驱动程序
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //连接数据库
            return java.sql.DriverManager.getConnection(connStr, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[][] getColType(String sql) throws Exception {
        String selectSql = "select " + sql.substring(sql.indexOf("(") + 1, sql.indexOf(")")) + " from " + sql.substring(0, sql.indexOf("("));
        return SqlUtils.getTableCols(selectSql, getDesertConnection());
    }

    public String getInsertSql(String sql, int colCount, boolean idFlag) {
        String insertSql;
        insertSql = "insert into " + sql + " values(";
        if (idFlag) {
            insertSql = insertSql.substring(0, insertSql.indexOf("(") + 1) + "id,"
                    + insertSql.substring(insertSql.indexOf("(") + 1) + "FORTUNE_GLOBAL_SEQ.nextval,";
        }

        for (int i = 0; i < colCount; i++) {
            insertSql += "?,";
        }
        insertSql = insertSql.substring(0, insertSql.length() - 1);
        insertSql += ")";
        return insertSql;
    }

    public Object getParam(Object obj, String colType) throws Exception {
        if ("NUMBER".equals(colType)) {
            if (obj == null) {
                return 0L;
            } else {
                String str=obj.toString().trim();
                if(str.indexOf(".")>=0&&!str.endsWith(".")){
                    return new Float(str);
                }
                return new Long(str);
            }
        }
        if ("VARCHAR2".equals(colType)) {
            if (obj == null) {
                return "";
            } else {
                return obj.toString();
            }
        }
        if ("DATE".equals(colType)) {
            if (obj == null) {
                return new java.util.Date(0);
            } else {
                //todo need check obj type.is type is date,this code will failed!
                return StringUtils.string2date(obj.toString());
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public String[] getParamZBXue(Object obj, String colType) throws Exception {
        if ("NUMBER".equals(colType)) {
            if (obj == null) {
                return new String[]{"long", "0"};
            } else {
                return new String[]{"long", "" + obj};
            }
        }
        if ("VARCHAR2".equals(colType)) {
            if (obj == null) {
                return new String[]{"String", ""};
            } else {
                return new String[]{"String", "" + obj};
            }
        }
        if ("DATE".equals(colType)) {
            if (obj == null) {
                return new String[]{"Date", null};
            } else {
                return new String[]{"Date", "" + obj};
            }
        }
        return null;
    }

    public void importDo(String selectSql, String insertSql, boolean idFlag) throws Exception {
        //String selectSql = "select id,name,address,case when type=2 then 1 else 0 end,case when type=3 then 1 else 0 end from B_ORGANIZE where state=1 and type in (2,3)";
        //String insertSql = "csp(id,name,address,is_sp,is_cp)";
        String cols[][] = getColType(insertSql);
        insertSql = getInsertSql(insertSql, cols.length, idFlag);

        List<Object> paramList = new ArrayList<Object>();
        logger.debug("准备获取数据："+selectSql);
        List selectList = SqlUtils.getRecords(selectSql, 0, 1000000l, getSourceConnection());
        if(selectList!=null){
            logger.debug("获取到了"+(selectList.size()-1)+"条记录！");
        }else{
            logger.warn("发生异常，无法获取数据！");
        }
        if (selectList != null) {
            logger.debug("start:" + insertSql);
            for (int i = 1; i < selectList.size(); i++) {
                Object objs[] = (Object[]) selectList.get(i);
                //String params[][] = new String[cols.length][2];
                Object params[] = new Object[cols.length];
                for (int j = 0; j < objs.length; j++) {
                    Object obj = objs[j];
                    params[j] = getParam(obj, cols[j][1]);
                }
                paramList.add(params);
/*
                try {
                    //SqlUtils.executePrepared(insertSql,params,getDesertConnection());
                } catch (Exception e) {
                    for (int k = 0; k < cols.length; k++) {
                        logger.debug(cols[k][0] + ":" + params[k] + "  ");
                    }
                    logger.debug(e.getMessage());
                    //e.printStackTrace();
                }
*/
            }
            SqlUtils.executePreparedBatch(insertSql, paramList, getDesertConnection());
            logger.debug("end:" + (selectList.size() - 1) + "条");
        }
    }

    public void importDo1(String selectSql, String insertSql, boolean idFlag) throws Exception {
        //String selectSql = "select id,name,address,case when type=2 then 1 else 0 end,case when type=3 then 1 else 0 end from B_ORGANIZE where state=1 and type in (2,3)";
        //String insertSql = "csp(id,name,address,is_sp,is_cp)";
        String cols[][] = getColType(insertSql);
        insertSql = getInsertSql(insertSql, cols.length, idFlag);

        List<Object> paramList = new ArrayList<Object>();

        List selectList = SqlUtils.getRecords(selectSql, 0, 1000000l, getDesertConnection());
        if (selectList != null) {
            logger.debug("start:" + insertSql);
            for (int i = 1; i < selectList.size(); i++) {
                Object objs[] = (Object[]) selectList.get(i);
                Object params[] = new Object[cols.length];
                for (int j = 0; j < objs.length; j++) {
                    Object obj = objs[j];
                    Object param = getParam(obj,cols[j][1]);
                    params[j] = param;
                }
                paramList.add(params);
/*
                try {
                    //SqlUtils.executePrepared(insertSql,params,getDesertConnection());
                } catch (Exception e) {
                    for (int k = 0; k < cols.length; k++) {
                        logger.debug(cols[k][0] + ":" + params[k] + "  ");
                    }
                    logger.debug(e.getMessage());
                    //e.printStackTrace();
                }
*/
            }
            SqlUtils.executePreparedBatch(insertSql, paramList, getDesertConnection());
            logger.debug("end:" + (selectList.size() - 1) + "条");
        }
    }

    public void csp() throws Exception {
        SqlUtils.executeSQL("delete from csp", getDesertConnection());
        String selectSql = "select id,name,address,case when type=2 then 1 else 0 end,case when type=3 then 1 else 0 end from B_ORGANIZE where state=1 and type in (2,3)";
        String insertSql = "csp(id,name,address,is_sp,is_cp)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update csp set status=1", getDesertConnection());
    }

    public void device() throws Exception {
        SqlUtils.executeSQL("delete from device", getDesertConnection());
        String selectSql = "select id,name,ip,url,ftp_port,ftp_username,ftp_password,ftp_path from B_SERVER where id in (select server_id from B_ORGANIZE_BIND_SERVER) and type=1";
        String insertSql = "device(id,name,ip,url,ftp_port,ftp_user,ftp_pwd,ftp_path)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update device set status=1,type=1", getDesertConnection());
    }

    @SuppressWarnings("unused")
    public void csp_device() throws Exception {
        SqlUtils.executeSQL("delete from csp_device", getDesertConnection());
        String selectSql = "select ORGANIZE_ID,server_id from B_ORGANIZE_BIND_SERVER";
        String insertSql = "csp_device(csp_id,device_id)";
        importDo(selectSql, insertSql, true);
    }

    @SuppressWarnings("unused")
    public void csp_csp() throws Exception {
        SqlUtils.executeSQL("delete from csp_csp", getDesertConnection());
        String selectSql = "select id,coop_org_id,org_id from B_ORGANIZE_COOPERATE";
        String insertSql = "csp_csp(id,master_csp_id,csp_id)";
        importDo(selectSql, insertSql, false);
    }

    @SuppressWarnings("unused")
    public void csp_module() throws Exception {
        SqlUtils.executeSQL("delete from csp_module", getDesertConnection());
        String selectSql = "select distinct ORGANIZE_ID,TEMPLATE_ID from R_ORGANIZE_TEMPLATE";
        String insertSql = "csp_module(csp_id,module_id)";
        importDo(selectSql, insertSql, true);
        SqlUtils.executeSQL("update csp_module set is_default=1 where id in (select min(id) from csp_module group by csp_id)", getDesertConnection());
    }

    public void module() throws Exception {
        SqlUtils.executeSQL("delete from module", getDesertConnection());
        String selectSql = "select id,name,DESCRIPTION from R_TEMPLATE ";
        String insertSql = "module(id,name,desp)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update module set status=1", getDesertConnection());
    }

    public void property() throws Exception {
        SqlUtils.executeSQL("delete from property", getDesertConnection());
        String selectSql = "select p.id,p.name,p.english_name," +
                "case " +
                "when p.type=1 and p.INPUT_MODE=1 then 1  " +
                "when p.type=1 and p.INPUT_MODE=2 then 2 " +
                "when p.type=1 and p.INPUT_MODE=3 then 3 " +
                "when p.type=1 and p.INPUT_MODE=4 then 4 " +
                "when p.type=1 and p.INPUT_MODE=5 then 5 " +
                "when p.type=1 and p.INPUT_MODE=6 then 6 " +
                "when p.type=1 and p.INPUT_MODE=7 then 7 " +
                "when p.type=2 and p.INPUT_MODE=3 then 8 " +
                "when p.type=2 and p.INPUT_MODE=4 then 9 " +
                "when p.type=2 and p.INPUT_MODE=5 then 10" +
                "when p.type=2 and p.INPUT_MODE=2 then 11" +
                "when p.type=2 and p.INPUT_MODE=1 then 12" +
                "else 1" +
                " end " +
                " data_type,case when p.pattern=1 then 0 else 1 end,case when tbp.is_required=0 then 1 else 0 end,p.description,tbp.display_order,tbp.template_id " +
                " from R_TEMPLATE_BIND_PROPERTY tbp,r_property p where tbp.property_id=p.id and tbp.is_deleted=0 order by display_order asc";
        String insertSql = "property(id,name,code,data_type,is_multi_line,is_null,desp,display_order,module_id)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update property set status=1,is_merge=0,max_size=100,is_main=0", getDesertConnection());
        SqlUtils.executeSQL("update property set is_main=1,column_name='name' where id in (5050,5618,5800,5810,5815,5823)", getDesertConnection());
        SqlUtils.executeSQL("update property set is_main=1,column_name='actors',is_merge=1 where id in (5055)", getDesertConnection());
        SqlUtils.executeSQL("update property set is_main=1,column_name='directors',is_merge=1 where id in (5056)", getDesertConnection());

        SqlUtils.executeSQL("insert into property(id,name,code,data_type,is_multi_line,is_null,desp,display_order,module_id,is_main,column_name,related_table,status,is_merge,max_size) values(FORTUNE_GLOBAL_SEQ.nextval,'设备','DEVICE',5,0,0,'',1,5000,1,'deviceId',1,1,0,100)", getDesertConnection());

    }

    @SuppressWarnings("unused")
    public void property_select() throws Exception {
        SqlUtils.executeSQL("delete from property_select", getDesertConnection());
        String selectSql = "select id,ENUMVAULE from r_property where ENUMVAULE is not null";
        String insertSql = "property_select(property_id,name,display_order)";
        String cols[][] = getColType(insertSql);
        insertSql = getInsertSql(insertSql, cols.length, true);

        List<Object> paramList = new ArrayList<Object>();

        List selectList = SqlUtils.getRecords(selectSql, 0, 1000000l, getSourceConnection());
        if (selectList != null) {
            logger.debug("start:" + insertSql);
            for (int i = 1; i < selectList.size(); i++) {
                Object objs[] = (Object[]) selectList.get(i);
                String params[][];// = new String[cols.length][2];

                String selectValue = (String) objs[1];
                String ss[] = StringUtils.getStrings(selectValue, "_|_");
                for (int j = 0; j < ss.length; j++) {
                    params = new String[][]{{"long", "" + objs[0]}, {"String", ss[j]}, {"long", "" + (j + 1)}};
                    paramList.add(params);
                }
/*
                try {
                    //SqlUtils.executePrepared(insertSql,params,getDesertConnection());
                } catch (Exception e) {
                    for (int k = 0; k < cols.length; k++) {
                        logger.debug(cols[k][0] + ":" + params[k][1] + "  ");
                    }
                    logger.debug(e.getMessage());
                    //e.printStackTrace();
                }
*/
            }
            SqlUtils.executePreparedBatch(insertSql, paramList, getDesertConnection());
            logger.debug("end:" + (selectList.size()) + "条");
        }
        //importDo(selectSql, insertSql, false);
    }

    public void content() throws Exception {
        SqlUtils.executeSQL("delete from content", getDesertConnection());
        String selectSql = "select id,template_id,server_id,org_id,to_char(create_date,'yyyy-MM-dd hh24:mi:ss') from r_subject where state=4 and id in (select subject_id from  r_channel_subject where state=3)";
        String insertSql = "content(id,module_id,device_id,csp_id,create_time)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update content set status=2,status_time=create_time", getDesertConnection());

    }

    @SuppressWarnings("unused")
    public void content_property() throws Exception {
        logger.debug("deleting old content_property");
        SqlUtils.executeSQL("delete from content_property", getDesertConnection());
        String selectSql = "select id,property_id,resource_id,substr(value,0,500),display_order,display_order from  R_SUBJECT_PROPERTY where resource_id in (select id from r_subject where state=4 )";
        String insertSql = "content_property(id,property_id,content_id,string_value,int_value,extra_data)";
        importDo(selectSql, insertSql, false);
    }

    @SuppressWarnings("unused")
    public void content_channel() throws Exception {
        SqlUtils.executeSQL("delete from content_channel", getDesertConnection());
        String selectSql = "select id,channel_id,subject_id from r_channel_subject where state=3";
        String insertSql = "content_channel(id,channel_id,content_id)";
        importDo(selectSql, insertSql, false);
    }

    /**
     * 修复电视剧等多个媒体连接的顺序。
     */
    @SuppressWarnings("unchecked")
    public void fix_clips_display_order(){
        List<String> sqlList = new ArrayList<String>();
        try {
            Connection dstConn = getDesertConnection();
            if(dstConn!=null){
                List<Object> contentIds =(List<Object>) SqlUtils.getRecords("select id from content",0L,10000000L,dstConn);
                int i,total;
                total = contentIds.size();

                String selectSql = "select id,property_id,resource_id,display_order from  R_SUBJECT_PROPERTY where "+
                        " property_id in(select id from r_property where type=2 and input_mode=3) order by resource_id," +
                        " display_order";
                Connection conn = getSourceConnection();
                if(conn==null){
                    logger.error("Source Db Connection is null (/"+total+")");
                }
                List<Object> allContentPropertyValues = (List<Object>) SqlUtils.getRecords(
                        selectSql,
                        0L,100000000L,conn);
                Map<Long,List<Object[]>> allPropertyValues = new HashMap<Long,List<Object[]>>();
                for(int k=1;k<allContentPropertyValues.size();k++){
                    Object[] data = (Object[]) allContentPropertyValues.get(k);
                    Long contentId = StringUtils.string2long(data[2].toString(),-1);
                    if(contentId>0){
                        List<Object[]> dataList = allPropertyValues.get(contentId);
                        if(dataList==null){
                            dataList = new ArrayList<Object[]>();
                            allPropertyValues.put(contentId,dataList);
                        }
                        dataList.add(data);
                        if(k%100==0){
                            logger.debug("准备数据["+k+"/"+allContentPropertyValues.size()+"]");
                        }
                    }else{
                        logger.error("数据准备异常！");
                    }
                }
                for(i=1;i<contentIds.size();i++){

                    //conn.close();
                    //logger.debug("Find rows "+(contentPropertyValues.size()-1));
                    Long contentId = StringUtils.string2long(contentIds.get(i).toString(),-1);
                    List<Object[]> contentPropertyValues = allPropertyValues.get(contentId);
                    if(contentPropertyValues!=null){
                        //int lineNumber = 0;
                        for(int l=0;l<contentPropertyValues.size();l++){
                            Object[] data = contentPropertyValues.get(l);
                            String updateSql = "update content_property set int_value="+l+" where id="+data[0];
                            sqlList.add(updateSql);
                            if(sqlList.size()>100){
                                logger.debug("update content_property "+sqlList.size()+","+i+"/"+total+","+(i*100/total)+"%");
                                SqlUtils.executeBatch(sqlList, getDesertConnection());
                                sqlList.clear();
                            }
                        }
                    }else{
                        logger.warn("contentId="+contentId+"的媒体没有可以用的clips信息！");
                    }
                }
                logger.debug("update content_property "+sqlList.size()+","+i+"/"+total+","+(i*100/total)+"%");
                SqlUtils.executeBatch(sqlList, getDesertConnection());
            }else{
                logger.error("Dst connection is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void content_csp() throws Exception {
        SqlUtils.executeSQL("delete from content_csp", getDesertConnection());
        String selectSql = "select distinct cs.subject_id,c.org_id  from r_subject s,r_channel_subject cs,r_channel c where s.id=cs.subject_id and cs.channel_id=c.id and s.state=4 and cs.state=3";
        String insertSql = "content_csp(content_id,csp_id)";
        importDo(selectSql, insertSql, true);
        SqlUtils.executeSQL("update content_csp set status=2", getDesertConnection());
    }

    @SuppressWarnings("unused")
    public void channel() throws Exception {
        SqlUtils.executeSQL("delete from channel", getDesertConnection());
        String selectSql = "select id,name,org_id,parent_id from r_channel";
        String insertSql = "channel(id,name,csp_id,parent_id)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update channel set parent_id=-1 where parent_id=0", getDesertConnection());
        SqlUtils.executeSQL("update channel set status=1,type=1", getDesertConnection());
    }

    @SuppressWarnings("unused")
    public void content_property_exec() throws Exception {
        logger.debug("start content_property_exec");
        HashMap<Object,Object> propertyMap = new HashMap<Object,Object>();
        List propertyList = SqlUtils.getRecords("select id,name,data_type,is_multi_line,is_merge,is_main,column_name from property", 0, 10000000l, getDesertConnection());
        for (int i = 1; i < propertyList.size(); i++) {
            Object objs[] = (Object[]) propertyList.get(i);
            propertyMap.put(new Long(objs[0].toString()), objs);
        }

        HashMap<Object,Object> propertySelectMap = new HashMap<Object,Object>();
        List propertySelectList = SqlUtils.getRecords("select id,property_id,name from property_select", 0, 10000000l, getDesertConnection());
        for (int i = 1; i < propertySelectList.size(); i++) {

            Object objs[] = (Object[]) propertySelectList.get(i);
            propertySelectMap.put("" + objs[1] + "_" + objs[2], objs[0]);
        }


        List<String> sqlList = new ArrayList<String>();

        List contentPropertyList = SqlUtils.getRecords("select id,content_id,property_id,string_value from content_property", 0, 10000000l, getDesertConnection());
        logger.debug("Has get records of contentPropertyList "+contentPropertyList.size());
        int total = contentPropertyList.size();
        for (int i = 1; i < total; i++) {
            Object objs[] = (Object[]) contentPropertyList.get(i);
            long id = Long.parseLong(objs[0].toString());
            long contentId = Long.parseLong(objs[1].toString());
            long propertyId = Long.parseLong(objs[2].toString());
            String stringValue = objs[3].toString();
            if(i%1000==0){
                logger.debug("Has been process "+i+" c_p,"+i+"/"+total+","+(i*100/total)+"%");
            }
            Object property[] = (Object[]) propertyMap.get(propertyId);
            if (property == null) {
                logger.error("property is null! which id is :" + id);
                continue;
            }
            long dataType = Long.parseLong(property[2].toString());
            long isMultiLine = Long.parseLong(property[3].toString());
            long isMerge = Long.parseLong(property[4].toString());
            long isMain = Long.parseLong(property[5].toString());
            String columnName = "";
            if (property[6] != null) {
                columnName = property[6].toString();
            }
            if (dataType == 5 || dataType == 6 || dataType == 7) {
                BigDecimal propertySelectId = (BigDecimal) propertySelectMap.get("" + propertyId + "_" + stringValue);
                if (propertySelectId == null || propertySelectId.longValue() == 0) {
                    sqlList.add("delete from content_property where id=" + id);
                    //logger.debug("select id:"+id);
                } else {
                    sqlList.add("update content_property set string_value=" + propertySelectId + " where id=" + id);
                    //logger.debug("right select id:"+id);
                }
            }

            if (isMain == 1 && columnName != null && !"".equals(columnName)) {
                if (isMerge == 1 && isMultiLine == 1) {
                    sqlList.add("update content set " + columnName + "='" + stringValue.replaceAll("'", "''").replaceAll("，", ";").replaceAll(",", ";") + "' where id=" + contentId);
                    //logger.debug("update content set "+columnName+"="+columnName+" || '"+stringValue+";' where id="+contentId);
                    //SqlUtils.executeSQL("update content set "+columnName+"="+columnName+" || '"+stringValue+";' where id="+contentId,getDesertConnection());
                } else {
                    sqlList.add("update content set " + columnName + "='" + stringValue.replaceAll("'", "''") + "' where id=" + contentId);
                    //logger.debug("update content set "+columnName+"='"+stringValue+"' where id="+contentId);
                    //SqlUtils.executeSQL("update content set "+columnName+"='"+stringValue+"' where id="+contentId,getDesertConnection());

                }
                sqlList.add("delete from content_property where id=" + id);
                //logger.debug("delete from content_property where id="+id);
                //SqlUtils.executeSQL("delete from content_property where id="+id,getDesertConnection());
            }
            if(sqlList.size()>1000){
                logger.debug("Run batch sql :"+sqlList.size());
                SqlUtils.executeBatch(sqlList, getDesertConnection());
                sqlList.clear();
            }
        }
        logger.debug("Sql script has been generated "+sqlList.size());
        SqlUtils.executeBatch(sqlList, getDesertConnection());
        logger.debug("end content_property_exec");
    }

    public void product() throws Exception {
        SqlUtils.executeSQL("delete from product", getDesertConnection());
        String selectSql = "select p.name,p.service_id,p.term,p.term_time_unit,p.auto_renew,p.query_service,pp.price,'1' from o_product p,o_product_price pp  where p.id=pp.product_id and p.type=1 and pp.id in (select product_id from O_PRODUCT_RESOURCE)";
        String insertSql = "product(name,pay_product_no,valid_length,length_unit,auto_pay,search_extra,price,type)";
        importDo(selectSql, insertSql, true);

        selectSql = "select distinct '' || pp.price || '元按次购买',p.service_id,pp.price,'2' from o_product p,o_product_price pp  where p.id=pp.product_id and p.type=3 and pp.id in (select product_id from O_PRODUCT_RESOURCE)";
        insertSql = "product(name,pay_product_no,price,type)";
        importDo(selectSql, insertSql, true);
        SqlUtils.executeSQL("update product set status=1", getDesertConnection());
    }

    @SuppressWarnings("unused")
    public void service_product() throws Exception {
        SqlUtils.executeSQL("delete from service_product", getDesertConnection());
        String selectSql = "select p.id,p.name,p.service_id,p.org_id,p.term,p.term_time_unit,p.auto_renew,p.query_service,case when p.type=1 then 1 else 2 end from o_product p,o_product_price pp  where p.id=pp.product_id and p.type in (1,3,0) and pp.id in (select product_id from O_PRODUCT_RESOURCE)";
        String insertSql = "service_product(id,name,msg,csp_id,valid_length,length_unit,auto_pay,search_extra,type)";
        importDo(selectSql, insertSql, false);


        HashMap<Object,Object> productMap = new HashMap<Object,Object>();
        List productList = SqlUtils.getRecords("select id,pay_product_no from product", 0, 10000000l, getDesertConnection());
        for (int i = 1; i < productList.size(); i++) {
            Object objs[] = (Object[]) productList.get(i);
            productMap.put("" + objs[1], objs[0]);
        }

        List<String> sqlList = new ArrayList<String>();

        List serviceProductList = SqlUtils.getRecords("select id,msg from service_product where msg is not null", 0, 10000000l, getDesertConnection());
        for (int i = 1; i < serviceProductList.size(); i++) {
            Object objs[] = (Object[]) serviceProductList.get(i);
            long id = Long.parseLong(objs[0].toString());
            String payProductNo = objs[1].toString();

            Object productId = productMap.get(payProductNo);
            sqlList.add("update service_product set product_id=" + productId + " where id=" + id);
        }

        SqlUtils.executeBatch(sqlList, getDesertConnection());

        logger.debug("end service_product");

        SqlUtils.executeSQL("update service_product set is_free=1 where msg is null", getDesertConnection());
        SqlUtils.executeSQL("update service_product set msg=null", getDesertConnection());

    }

    @SuppressWarnings("unused")
    public void csp_product() throws Exception {
        SqlUtils.executeSQL("delete from csp_product", getDesertConnection());
        String selectSql = "select distinct csp_id,product_id from service_product";
        String insertSql = "csp_product(csp_id,product_id)";
        importDo1(selectSql, insertSql, true);
    }


    @SuppressWarnings("unused")
    public void content_service_product() throws Exception {
        SqlUtils.executeSQL("delete from content_service_product", getDesertConnection());
        String selectSql = "select distinct subject_id,pt.product_id from r_channel_subject cs,(select pr.product_id,pr.resource_id,c.id,c.parent_id,c.name from o_product_resource pr,r_channel c where pr.resource_type=1 and c.id in (select id from r_channel start with id=pr.resource_id connect by prior id=parent_id) ) pt where cs.channel_id=pt.id and cs.state=3";
        String insertSql = "content_service_product(content_id,service_product_id)";
        importDo(selectSql, insertSql, true);
        selectSql = "select distinct cs.subject_id,pr.product_id from o_product_resource pr,r_channel_subject cs where pr.resource_id=cs.id and pr.resource_type=2 and cs.state=3";
        insertSql = "content_service_product(content_id,service_product_id)";
        importDo(selectSql, insertSql, true);
    }

    public void recommend() throws Exception {
        SqlUtils.executeSQL("delete from recommend", getDesertConnection());
        String selectSql = "select id,name,org_id,case when type=0 then 1 else 2 end from R_RECOMMENDATION";
        String insertSql = "recommend(id,name,csp_id,type)";
        importDo(selectSql, insertSql, false);
    }

    @SuppressWarnings("unused")
    public void content_recommend() throws Exception {
        SqlUtils.executeSQL("delete from content_recommend", getDesertConnection());
        String selectSql = "select distinct cr.id,cr.channel_id,cr.type,cs.subject_id,cr.RECOMMEND_ORDER from R_CHANNEL_RECOMMENDATION cr,r_channel_subject cs where cr.channel_subject_id=cs.id";
        String insertSql = "content_recommend(id,channel_id,recommend_id,content_id,display_Order)";
        importDo(selectSql, insertSql, false);
    }

    public void area() throws Exception {
        SqlUtils.executeSQL("delete from area", getDesertConnection());
        String selectSql = "select id,name from b_area";
        String insertSql = "area(id,name)";
        importDo(selectSql, insertSql, false);
    }

    @SuppressWarnings("unused")
    public void ip_range() throws Exception {
        SqlUtils.executeSQL("delete from ip_range", getDesertConnection());
        String selectSql = "select id,name,description,ipfrom_value,ipto_value,area_id from b_accesslist";
        String insertSql = "ip_range(id,name,desp,ip_from,ip_to,area_id)";
        importDo(selectSql, insertSql, false);
    }

    public void relationPropertyContent() throws Exception{
       SqlUtils.executeSQL("delete from RELATED_PROPERTY_CONTENT", getDesertConnection());
        String selectSql = "select rs.id, rs.relation_id,cs.subject_id,rs.display_order,c.org_id " +
                "   from R_RELATIONSHIP_SUBJECT rs,R_CHANNEL_SUBJECT cs ,R_CHANNEL c " +
                "     where rs.channel_subject_id = cs.id and cs.channel_id=c.id";
        String insertSql = "RELATED_PROPERTY_CONTENT(id,related_property_id,content_id,display_order,csp_id)";
        importDo(selectSql, insertSql, false);
    }
    @SuppressWarnings("unused")
    public void visit_log() throws Exception {
        SqlUtils.executeSQL("delete from visit_log", getDesertConnection());
        String selectSql = "select mlh.medialog_id,mlh.medialog_icpid,mlh.medialog_impid*10,mlh.medialog_channelid,cs.subject_id,mlh.medialog_mediaurl,mlh.medialog_userid,mlh.medialog_userip,to_char(mlh.starttime,'yyyy-MM-dd hh24:mi:ss'),to_char(mlh.endtime,'yyyy-MM-dd hh24:mi:ss'),mlh.medialog_orderlength from media_log_history mlh,r_channel_subject cs where mlh.medialog_mediaid=cs.id and mlh.starttime>to_date('2011-04-01','yyyy-MM-dd')";
        String insertSql = "visit_log(id,sp_id,cp_id,channel_id,content_id,url,user_id,user_ip,start_time,end_time,length)";
        importDo(selectSql, insertSql, false);
    }

    protected Logger  logger =  Logger.getLogger(this.getClass());
    public  void doImport() {
        try {
            //csp();
            //device();
            //csp_device();
            //csp_csp();
            //csp_module();
            //module();
            //property();
            //property_select();
            //content();
            //content_property();
            //content_channel();
            //content_csp();
            //channel();
            //content_property_exec();
            product();
            service_product();
            csp_product();
            content_service_product();
            //recommend();
            //content_recommend();
            //area();
            //ip_range();
            //visit_log();
            //relationPropertyContent();
            //fix_clips_display_order();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public static void main(String[] args){
        ImportDb importor = new ImportDb();
        importor.doImport();
    }
}
