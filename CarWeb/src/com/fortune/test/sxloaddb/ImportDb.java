package com.fortune.test.sxloaddb;

import com.fortune.util.sql.SqlUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-5-26
 * Time: 10:58:58
 * To change this template use File | Settings | File Templates.
 */
public class ImportDb {
    public static Connection getConn4(){
        try{
             //装载JDBC驱动程序
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //连接数据库
/*
            String url = "jdbc:oracle:thin:@192.168.10.22:1521:media";
            String user="mediastack";
            String pwd = "mediastack";
*/
            String url = "jdbc:oracle:thin:@192.168.1.190:1521:orcl";
            String user="mediastack_sx";
            String pwd = "mediastack_sx";

            return java.sql.DriverManager.getConnection(url,user,pwd);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConn5(){
        try{
             //装载JDBC驱动程序
            java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //连接数据库
            return java.sql.DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:ora9i","fortune_rms1","fortune_rms1");
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String[][] getColType(String sql) throws Exception{
        String selectSql = "select "+ sql.substring(sql.indexOf("(")+1, sql.indexOf(")")) +" from "+sql.substring(0,sql.indexOf("("));
        String cols[][] = SqlUtils.getTableCols(selectSql,getConn5());
        return cols;
    }
    public static String getInsertSql(String sql,int colCount, boolean idFlag){
        String insertSql = new String(sql);
        insertSql = "insert into "+sql+" values(";
        if (idFlag){
            insertSql = insertSql.substring(0,insertSql.indexOf("(")+1) + "id,"
                    + insertSql.substring(insertSql.indexOf("(")+1) + "FORTUNE_GLOBAL_SEQ.nextval,";
        }

        for (int i=0; i<colCount; i++){
            insertSql += "?,";
        }
        insertSql = insertSql.substring(0,insertSql.length()-1);
        insertSql += ")";
        return insertSql;
    }
    public static String[] getParam(Object obj,String colType) throws Exception{
        if ("NUMBER".equals(colType)){
            if (obj==null){
                return new String[]{"long","0"};
            }else{
                return new String[]{"long",""+obj};
            }
        }
        if ("VARCHAR2".equals(colType)){
            if (obj==null){
                return new String[]{"String",""};
            }else{
                return new String[]{"String",""+obj};
            }
        }
        if ("DATE".equals(colType)){
            if (obj==null){
                return new String[]{"Date",null};
            }else{
                return new String[]{"Date",""+obj};
            }
        }
        return null;
    }

    public static void importDo(String selectSql,String insertSql,boolean idFlag) throws Exception{
        //String selectSql = "select id,name,address,case when type=2 then 1 else 0 end,case when type=3 then 1 else 0 end from B_ORGANIZE where state=1 and type in (2,3)";
        //String insertSql = "csp(id,name,address,is_sp,is_cp)";
        String cols[][] = getColType(insertSql);
        insertSql = getInsertSql(insertSql,cols.length, idFlag);

        List paramList = new ArrayList();

        List selectList = SqlUtils.getRecords(selectSql,0,1000000l,getConn4());
        if (selectList!=null){
            System.out.println("start:"+insertSql);
            for (int i=1;i<selectList.size(); i++){
                Object objs[] = (Object[])selectList.get(i);
                String params[][] = new String[cols.length][2];
                for (int j=0; j<objs.length; j++){
                    Object obj = objs[j];
                    params[j] = getParam(obj,cols[j][1]);
                }
                paramList.add(params);
                try{
                    //SqlUtils.executePrepared(insertSql,params,getDesertConnection());
                }catch(Exception e){
                    for (int k=0; k<cols.length; k++){
                        System.out.print(cols[k][0]+":"+params[k][1]+"  ");
                    }
                    System.out.println();
                    System.out.println(e.getMessage());
                    //e.printStackTrace();
                }
            }
            SqlUtils.executePreparedBatch(insertSql,paramList,getConn5());
            System.out.println("end:"+(selectList.size()-1)+"条");
        }
    }
    public static void importDo1(String selectSql,String insertSql,boolean idFlag) throws Exception{
        //String selectSql = "select id,name,address,case when type=2 then 1 else 0 end,case when type=3 then 1 else 0 end from B_ORGANIZE where state=1 and type in (2,3)";
        //String insertSql = "csp(id,name,address,is_sp,is_cp)";
        String cols[][] = getColType(insertSql);
        insertSql = getInsertSql(insertSql,cols.length, idFlag);

        List paramList = new ArrayList();

        List selectList = SqlUtils.getRecords(selectSql,0,1000000l,getConn5());
        if (selectList!=null){
            System.out.println("start:"+insertSql);
            for (int i=1;i<selectList.size(); i++){
                Object objs[] = (Object[])selectList.get(i);
                String params[][] = new String[cols.length][2];
                for (int j=0; j<objs.length; j++){
                    Object obj = objs[j];
                    params[j] = getParam(obj,cols[j][1]);
                }
                paramList.add(params);
                try{
                    //SqlUtils.executePrepared(insertSql,params,getDesertConnection());
                }catch(Exception e){
                    for (int k=0; k<cols.length; k++){
                        System.out.print(cols[k][0]+":"+params[k][1]+"  ");
                    }
                    System.out.println();
                    System.out.println(e.getMessage());
                    //e.printStackTrace();
                }
            }
            SqlUtils.executePreparedBatch(insertSql,paramList,getConn5());
            System.out.println("end:"+(selectList.size()-1)+"条");
        }
    }

    public static void csp() throws Exception{
        SqlUtils.executeSQL("delete from csp",getConn5());
        String selectSql = "select icp_id,icp_name,'1','0' from icp";
        String insertSql = "csp(id,name,is_sp,is_cp)";
        importDo(selectSql, insertSql, false);
        selectSql = "select imp_id*10,imp_name,'0','1' from imp";

        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update csp set status=1",getConn5());
    }
    public static void device() throws Exception{
        SqlUtils.executeSQL("delete from device",getConn5());
        String selectSql = "select server_id,server_name,server_ip,server_url,server_ftpport,server_ftpusrname,server_ftppassword,server_ftppath from server where server_typeid=1 and server_id in (select server_id from imp_server)";
        String insertSql = "device(id,name,ip,url,ftp_port,ftp_user,ftp_pwd,ftp_path)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update device set status=1,type=1",getConn5());
    }
    public static void csp_device() throws Exception{
        SqlUtils.executeSQL("delete from csp_device",getConn5());
        String selectSql = "select imp_id*10,server_id from imp_server";
        String insertSql = "csp_device(csp_id,device_id)";
        importDo(selectSql, insertSql, true);
    }
    public static void csp_csp() throws Exception{
        SqlUtils.executeSQL("delete from csp_csp",getConn5());
        String selectSql = "select ICPIMP_ID,icp_id,imp_id*10 from icp_imp";
        String insertSql = "csp_csp(id,master_csp_id,csp_id)";
        importDo(selectSql, insertSql, false);
    }
    public static void csp_module() throws Exception{
        SqlUtils.executeSQL("delete from csp_module",getConn5());
        SqlUtils.executeSQL("insert into csp_module(id,csp_id,module_id,is_default) (select id,id,1,1 from csp)",getConn5());
    }
    public static void module() throws Exception{
        SqlUtils.executeSQL("delete from module",getConn5());
        SqlUtils.executeSQL("insert into module(id,name,status)values(1,'视频',1)",getConn5());
    }
    public static void property() throws Exception{
        SqlUtils.executeSQL("delete from property",getConn5());

SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(1,'片名','MEDIA_NAME',1,1)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(2,'原名','MEDIA_ORIGINALNAME',1,1)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(3,'演员','MEDIA_ACTORS',1,1)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(4,'导演','MEDIA_DIRECTORS',1,1)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(5,'海报一','MEDIA_POSTER1',1,11)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(6,'海报二','MEDIA_POSTER2',1,11)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(7,'海报三','MEDIA_POSTER3',1,11)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(8,'海报四','MEDIA_POSTER4',1,11)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(9,'格式','MEDIA_FORMATTYPE',1,5)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,data_type,is_multi_line,is_null,desp,display_order,module_id,is_main,column_name,related_table,status,is_merge,max_size) values(10,'设备','DEVICE',5,0,0,'',1,1,1,'deviceId',1,1,0,100)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_multi_line) values(11,'片段地址','MEDIA_CLIP',1,8,1)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(12,'公司','MEDIA_CORPORATION',1,1)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(13,'产地','MEDIA_HOMETOWNID',1,5)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(14,'级别','MEDIA_LEVELID',1,5)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(15,'语言','MEDIA_LANGUAGEID',1,5)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(16,'服务类型','MEDIA_SERVICE_TYPE',1,5)",getConn5());
SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(17,'简介','MEDIA_INTRO',1,2)",getConn5());

        SqlUtils.executeSQL("update property set display_order=id",getConn5());

        SqlUtils.executeSQL("update property set is_multi_line=0 where is_multi_line is null",getConn5());

        SqlUtils.executeSQL("update property set status=1,is_merge=0,max_size=200,is_main=0,is_null=1",getConn5());

        SqlUtils.executeSQL("update property set is_main=1,column_name='name',is_null=0 where id in (1)",getConn5());
        SqlUtils.executeSQL("update property set is_main=1,column_name='actors',is_merge=1 where id in (3)",getConn5());
        SqlUtils.executeSQL("update property set is_main=1,column_name='directors',is_merge=1 where id in (4)",getConn5());
        SqlUtils.executeSQL("update property set max_size=1000 where id in (17)",getConn5());
        SqlUtils.executeSQL("update property set is_main=1 where id in (10)",getConn5());
    }
    public static void property_select() throws Exception{
        SqlUtils.executeSQL("delete from property_select",getConn5());

        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(91,9,'media')",getConn5());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(92,9,'real')",getConn5());

        SqlUtils.executeSQL("update content_property set string_value=91 where string_value=1 and property_id=9",getConn5());

SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1301,13,'中国')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1302,13,'美国')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1303,13,'英国')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1304,13,'韩国')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1305,13,'日本')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1306,13,'其它')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1307,13,'德国')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1308,13,'法国')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1309,13,'意大利')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1310,13,'西班牙')",getConn5());

SqlUtils.executeSQL("update content_property set string_value=1301 where string_value=0     and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1302 where string_value=100   and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1303 where string_value=101   and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1304 where string_value=102   and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1305 where string_value=103   and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1306 where string_value=104   and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1307 where string_value=106   and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1308 where string_value=107   and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1309 where string_value=109   and property_id=13",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1310 where string_value=110   and property_id=13",getConn5());

SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1501,15,'英语')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1502,15,'国语')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1503,15,'韩语')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1504,15,'日语')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1505,15,'粤语')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1506,15,'其他')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1507,15,'法语')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1508,15,'拉丁语')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1509,15,'德语')",getConn5());

SqlUtils.executeSQL("update content_property set string_value=1501 where string_value=106   and property_id=15",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1502 where string_value=107   and property_id=15",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1503 where string_value=108   and property_id=15",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1504 where string_value=109   and property_id=15",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1505 where string_value=110   and property_id=15",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1506 where string_value=113   and property_id=15",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1507 where string_value=114   and property_id=15",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1508 where string_value=115   and property_id=15",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1509 where string_value=116   and property_id=15",getConn5());

SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1401,14,'一级')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1402,14,'二级')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1403,14,'三级')",getConn5());

SqlUtils.executeSQL("update content_property set string_value=1401 where string_value=0   and property_id=14",getConn5());


SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1601,16,'媒体点播')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1602,16,'媒体下载')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1603,16,'点播下载')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1604,16,'媒体直播')",getConn5());
SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1605,16,'DRM')",getConn5());

SqlUtils.executeSQL("update content_property set string_value=1601 where string_value=1   and property_id=16",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1602 where string_value=2   and property_id=16",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1603 where string_value=3   and property_id=16",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1604 where string_value=4   and property_id=16",getConn5());
SqlUtils.executeSQL("update content_property set string_value=1605 where string_value=5   and property_id=16",getConn5());

        SqlUtils.executeSQL("update content_property set display_order=id",getConn5());

        System.out.println("end: property_select");

        //importDo(selectSql, insertSql, false);
    }
    public static void content() throws Exception{
        SqlUtils.executeSQL("delete from content",getConn5());
        String selectSql = "select m.media_id,1,ms.server_id,media_impid*10 from media m,media_server ms where m.media_id=ms.media_id and m.media_id in (select media_id from media_icp where media_published=1)";
        String insertSql = "content(id,module_id,device_id,csp_id)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update content set status=2,status_time=create_time",getConn5());

    }

    public static void content_property() throws Exception{
        SqlUtils.executeSQL("delete from content_property",getConn5());
        String selectSql = "select id,property_id,resource_id,substr(value,0,500) from  R_SUBJECT_PROPERTY where resource_id in (select id from r_subject where state=4 )";
        String insertSql = "content_property(id,property_id,content_id,string_value)";
        importDo(selectSql, insertSql, false);
    }

    public static void content_channel() throws Exception{
        SqlUtils.executeSQL("delete from content_channel",getConn5());
        String selectSql = "select mediaicp_id,media_channelid,media_id from media_icp where media_published=1";
        String insertSql = "content_channel(id,channel_id,content_id)";
        importDo(selectSql, insertSql, false);
    }

    public static void content_csp() throws Exception{
        SqlUtils.executeSQL("delete from content_csp",getConn5());
        String selectSql = "select distinct media_id,icp_id from media_icp where media_published=1";
        String insertSql = "content_csp(content_id,csp_id)";
        importDo(selectSql, insertSql, true);
        SqlUtils.executeSQL("update content_csp set status=2",getConn5());
    }
    public static void channel() throws Exception{
        SqlUtils.executeSQL("delete from channel",getConn5());
        String selectSql = "select channel_id,channel_name,icp_id,channel_parentid from channel where icp_id>0";
        String insertSql = "channel(id,name,csp_id,parent_id)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update channel set parent_id=-1 where parent_id=0",getConn5());
        SqlUtils.executeSQL("update channel set status=1,type=1",getConn5());
    }

    public static void content_property_exec() throws Exception{
        System.out.println("start content_property_exec");
        SqlUtils.executeSQL("delete from content_property",getConn5());


        HashMap propertyMap = new HashMap();
        List propertyList = SqlUtils.getRecords("select id,name,data_type,is_multi_line,is_merge,is_main,column_name,code from property",0,10000000l,getConn5());
        for (int i=1; i<propertyList.size(); i++){
            Object objs[] = (Object[])propertyList.get(i);
            propertyMap.put(objs[7].toString(),objs);
        }

        List sqlList = new ArrayList();
        String sql = "select media_id,MEDIA_NAME,MEDIA_ORIGINALNAME,MEDIA_ACTORS,MEDIA_DIRECTORS,MEDIA_POSTER1,MEDIA_POSTER2,MEDIA_POSTER3,MEDIA_POSTER4,MEDIA_FORMATTYPE,MEDIA_CORPORATION,MEDIA_HOMETOWNID,MEDIA_LEVELID,MEDIA_LANGUAGEID,MEDIA_SERVICE_TYPE,MEDIA_INTRO from media where media_id in (select media_id from media_icp where media_published=1)";
        String colName[][] = SqlUtils.getTableCols(sql,getConn4());
        List mediaList = SqlUtils.getRecords(sql,0,10000000l,getConn4());
        for (int i=1; i<mediaList.size(); i++){
            Object objs[] = (Object[])mediaList.get(i);
            long id = Long.parseLong(objs[0].toString());

            List mediaClipList = SqlUtils.getRecords("select mediaclip_url from media_clip where mediaclip_mediaid="+id,0,10000000000l,getConn4());
            for (int j=1; j<mediaClipList.size(); j++){
                String url = (String)mediaClipList.get(j);
                if (url==null) continue;
                sqlList.add("insert into content_property(id,content_id,property_id,string_value) values(FORTUNE_GLOBAL_SEQ.nextval,"+id+",11,'"+url.replaceAll("'","''")+"')");
            }

            for (int j=1; j<colName.length; j++){
                Object obj = objs[j];
                if (obj==null){
                    continue;
                }
                String propertyCode = colName[j][0];
                Object property[] = (Object[])propertyMap.get(propertyCode);
                if (property==null){
                    System.out.println("id:"+id);
                    continue;
                }
                long propertyId = Long.parseLong(property[0].toString());
                long dataType = Long.parseLong(property[2].toString());
                long isMultiLine = Long.parseLong(property[3].toString());
                long isMerge = Long.parseLong(property[4].toString());
                long isMain = Long.parseLong(property[5].toString());
                String columnName = "";
                if (property[6]!=null){
                    columnName = property[6].toString();
                }

                if (isMain==1 && columnName!=null && !"".equals(columnName)){
                    if (isMerge==1 && isMultiLine==1){
                        String obj1 = (String)obj;
                        sqlList.add("update content set "+columnName+"='"+obj1.replaceAll("'","''").replaceAll("，",";").replaceAll(",",";")+"' where id="+id);
                        //System.out.println("update content set "+columnName+"="+columnName+" || '"+stringValue+";' where id="+contentId);
                        //SqlUtils.executeSQL("update content set "+columnName+"="+columnName+" || '"+stringValue+";' where id="+contentId,getDesertConnection());
                    }else{
                        String obj1 = (String)obj;
                        sqlList.add("update content set "+columnName+"='"+obj1.replaceAll("'","''")+"' where id="+id);
                        //System.out.println("update content set "+columnName+"='"+stringValue+"' where id="+contentId);
                        //SqlUtils.executeSQL("update content set "+columnName+"='"+stringValue+"' where id="+contentId,getDesertConnection());

                    }
                    sqlList.add("delete from content_property where id="+id);
                    //System.out.println("delete from content_property where id="+id);
                    //SqlUtils.executeSQL("delete from content_property where id="+id,getDesertConnection());
                }else{
                        String obj1 = obj.toString();
                    sqlList.add("insert into content_property(id,content_id,property_id,string_value) values(FORTUNE_GLOBAL_SEQ.nextval,"+id+","+propertyId+",'"+obj1.replaceAll("'","''")+"')");
                }

            }

        }
        SqlUtils.executeBatch(sqlList,getConn5());
        System.out.println("end content_property_exec");
    }

    public static void product() throws Exception{
        SqlUtils.executeSQL("delete from product",getConn5());
        String selectSql = "select c.channel_id,i.icp_name ||'-'|| c.channel_name || '-包月',c.CATEGORY_ID,CHANNEL_MONTHLENGTH,1,0,0,CHANNEL_MONTHFEE,1 from channel c, icp i where c.icp_id=i.icp_id and c.channel_status=1 ";
        String insertSql = "product(id,name,pay_product_no,valid_length,length_unit,auto_pay,search_extra,price,type)";
        importDo(selectSql, insertSql, false);

        selectSql = "select distinct '' || pp.price || '元按次购买',p.service_id,pp.price,'2' from o_product p,o_product_price pp  where p.id=pp.product_id and p.type=3 and pp.id in (select product_id from O_PRODUCT_RESOURCE)";
        insertSql = "product(name,pay_product_no,price,type)";
        //importDo(selectSql, insertSql, true);
        SqlUtils.executeSQL("update product set status=1",getConn5());
    }

    public static void service_product() throws Exception{
        SqlUtils.executeSQL("delete from service_product",getConn5());
        String selectSql = "select c.channel_id,c.channel_id,i.icp_name ||'-'|| c.channel_name || '-包月',c.icp_id,1 from channel c, icp i where c.icp_id=i.icp_id and c.channel_status=1";
        String insertSql = "service_product(id,product_id,name,csp_id,type)";
        importDo(selectSql, insertSql, false);

    }

    public static void csp_product() throws Exception{
        SqlUtils.executeSQL("delete from csp_product",getConn5());
        String selectSql = "select distinct csp_id,product_id from service_product";
        String insertSql = "csp_product(csp_id,product_id)";
        importDo1(selectSql, insertSql, true);
    }


    public static void content_service_product() throws Exception{
        SqlUtils.executeSQL("delete from content_service_product",getConn5());
        String selectSql = "select distinct mi.media_id,c.channel_id from media_icp mi, channel c, icp i where i.icp_id=c.icp_id and c.channel_status=1 and  c.channel_id in (select channel_id from channel start with channel_id=mi.media_channelid connect by prior channel_parentid=channel_id)";
        String insertSql = "content_service_product(content_id,service_product_id)";
        importDo(selectSql, insertSql, true);
        selectSql = "select distinct cs.subject_id,pr.product_id from o_product_resource pr,r_channel_subject cs where pr.resource_id=cs.id and pr.resource_type=2 and cs.state=3";
        insertSql = "content_service_product(content_id,service_product_id)";
        //importDo(selectSql, insertSql, true);
    }

    public static void recommend() throws Exception{
        SqlUtils.executeSQL("delete from recommend",getConn5());
        String selectSql = "select id,name,org_id,case when type=0 then 1 else 2 end from R_RECOMMENDATION";
        String insertSql = "recommend(id,name,csp_id,type)";
        importDo(selectSql, insertSql, false);
    }

    public static void content_recommend() throws Exception{
        SqlUtils.executeSQL("delete from content_recommend",getConn5());
        String selectSql = "select distinct cr.id,cr.channel_id,cr.type,cs.subject_id  from R_CHANNEL_RECOMMENDATION cr,r_channel_subject cs where cr.channel_subject_id=cs.id";
        String insertSql = "content_recommend(id,channel_id,recommend_id,content_id)";
        importDo(selectSql, insertSql, false);
    }

    public static void main(String args[]){
        try{
            ///csp();
            //device();
            //csp_device();
            ///csp_csp();
            //csp_module();
            //module();
            //property();
            //property_select();
            content();
            content_property();
            content_channel();
            content_csp();
            channel();
            content_property_exec();

            property_select();
            
            product();
            service_product();
            csp_product();
            content_service_product();
            recommend();
            content_recommend();


        }catch(Exception e){
            e.printStackTrace();
        }


    }

}
