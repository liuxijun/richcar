package com.fortune.test.hbloaddb;

import com.fortune.util.AppConfigurator;
import com.fortune.util.StringUtils;
import com.fortune.util.sql.SqlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-11-23
 * Time: 15:20:29
 * 导入山西数据
 */
public class BjImportor extends ImportDb {
    public BjImportor() {
        AppConfigurator config = AppConfigurator.getInstance();
        sourceConnStr = config.getConfig("importor.sourceConnStr","jdbc:oracle:thin:@192.168.10.89:1521:media");
        sourceUser = config.getConfig("importor.sourceUser", "mediastack");
        sourcePwd =  config.getConfig("importor.sourcePwd","mediastack");
        connStr =  config.getConfig("importor.desertConnStr","jdbc:oracle:thin:@192.168.10.89:1521:orcl");
        user =  config.getConfig("importor.desertUser","rms_bj");
        pwd =  config.getConfig("importor.desertPwd","rms_bj");
/*
        sourceConnStr = "jdbc:oracle:thin:@localhost:1521:XE";
        sourceUser = "mediastack";
        sourcePwd = "mediastack";
        connStr = "jdbc:oracle:thin:@localhost:1521:XE";
        user = "rms_sx";
        pwd = "rms_sx";
        */
    }

    public void admin() throws Exception{

        SqlUtils.executeSQL("delete from admin", getDesertConnection());
        String selectSql = "select rownum+100,admin_id,admin_name,admin_password,admin_tel,admin_email,admin_audited,'0','0' from admin";
        String insertSql = "admin(id,login,realname,password,telephone,email,status,is_root,is_system)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update admin set status=1", getDesertConnection());
        SqlUtils.executeSQL("update admin set id=1,is_root=1,is_system=1 where login like 'root'", getDesertConnection());

        //准备导入admin_csp
        List admins = SqlUtils.getRecords("select login,id,realname from admin",0,100000L, getDesertConnection());
        Map<String,Long> adminIds = new HashMap<String,Long>();
        if(admins!=null){
            for(Object o:admins){
                if(o!=null && (o instanceof Object[])){
                    Object[] data = (Object[])o;
                    adminIds.put(data[0].toString(), StringUtils.string2long(data[1].toString(),-1));
                }
            }
        }
        selectSql = "select admin_id,case when  ADMIN_CORPORATIONTYPE=0 then " +
                "ADMIN_CORPORATIONID when  ADMIN_CORPORATIONTYPE=2 then ADMIN_CORPORATIONID*1000" +
                "  else " +
                "ADMIN_CORPORATIONID end,'1' from admin";
        List adminCsp = SqlUtils.getRecords(selectSql,0,100000L, getSourceConnection());
        List<String> insertSqls = new ArrayList<String>();
        Map<String,Long> adminCsps = new HashMap<String,Long>();
        int adminCspId=1;
        if(adminCsp!=null){
            for(Object o:adminCsp){
                if(!(o instanceof Object[])){
                    continue;
                }
                Object[] data = (Object[]) o;
                int cspId = StringUtils.string2int(data[1].toString(),-1);
                if(cspId>=1){ // todo cspId=1是系统账号，小于等于0是有问题
                    insertSqls.add("insert into admin_csp(id,admin_id,csp_id,is_default_csp) values(" +
                            adminCspId+","+adminIds.get(data[0].toString())+","+data[1]+",1)");
                    adminCsps.put(data[0].toString(),StringUtils.string2long(data[1].toString(),-1));
                    adminCspId++;
                }else{
                    logger.error("admin "+data[0]+" 的csp_id是小于0的，有问题:"+data[1]);
                }
            }
        }
        logger.debug("清理admin_csp");
        SqlUtils.executeSQL("delete from admin_csp", getDesertConnection());
        SqlUtils.executeBatch(insertSqls, getDesertConnection());
        logger.debug("插入admin_csp表"+insertSqls.size()+"条记录");
        insertSqls.clear();
        //导入角色分配
        List adminRoleList = SqlUtils.getRecords("select admin_id,role_id from admin_role",0,100000L, getSourceConnection());
        if(adminRoleList!=null){
            Map<Integer,Integer> roleMaps = new HashMap<Integer,Integer>();
            roleMaps.put(	109	,	2647	);
            roleMaps.put(	172	,	2647	);
            roleMaps.put(	705	,	0	);
            roleMaps.put(	702	,	0	);
            roleMaps.put(	701	,	0	);
            roleMaps.put(	703	,	0	);
            roleMaps.put(	704	,	0	);
            roleMaps.put(	0	,	3603	);
            roleMaps.put(	1	,	3603	);
            roleMaps.put(	2	,	3603	);
            roleMaps.put(	3	,	3603	);
            roleMaps.put(	4	,	3603	);
            roleMaps.put(	5	,	3603	);
            roleMaps.put(	6	,	3603	);
            roleMaps.put(	8	,	3603	);
            roleMaps.put(	9	,	0	);
            roleMaps.put(	103	,	2647	);
            roleMaps.put(	105	,	0	);
            roleMaps.put(	107	,	2647	);
            roleMaps.put(	112	,	2647	);
            roleMaps.put(	203	,	2648	);
            roleMaps.put(	206	,	0	);
            roleMaps.put(	305	,	3603	);
            roleMaps.put(	306	,	3603	);
            roleMaps.put(	70	,	3603	);
            roleMaps.put(	170	,	3603	);
            roleMaps.put(	7	,	3603	);
            roleMaps.put(	160	,	2647	);
            roleMaps.put(	171	,	2647	);
            Map<String,String> hasBeenAdded = new HashMap<String,String>();
            int id=1;
            for(Object o:adminRoleList){
                if(!(o instanceof Object[])){
                    continue;
                }
                Object[] data=(Object[]) o;
                String login = data[0].toString();
                Long adminId = adminIds.get(login);
                Integer roleId = roleMaps.get(StringUtils.string2int(data[1].toString(),-1));
                String key = adminId+"_"+roleId;
                if(hasBeenAdded.get(key)==null){
                    hasBeenAdded.put(key,key);
                    Long cspId = adminCsps.get(login);
                    if(cspId==null){
                        cspId=-1L;
                    }
                    insertSqls.add("insert into admin_role(id,admin_id,csp_id,role_id) values("
                            +id+"," +
                            adminId+","+cspId+","+roleId+")");
                    id++;
                }
            }
            logger.debug("清除admin_role表");
            SqlUtils.executeSQL("delete from admin_role", getDesertConnection());
            SqlUtils.executeBatch(insertSqls, getDesertConnection());
            logger.debug("插入admin_role表"+insertSqls.size()+"条记录");
        }
    }

    public void csp() throws Exception {
        SqlUtils.executeSQL("delete from csp", getDesertConnection());
        String selectSql = "select icp_id,icp_name,'1','0',0,-1 from icp";
        String insertSql = "csp(id,name,is_sp,is_cp,type,sp_id)";
        importDo(selectSql, insertSql, false);
        selectSql = "select imp_id*1000,imp_name,'0','1'," +
                "case when imp_email like '是' then 1 else 0 end cp_type," +
                "case when imp_email like '是' then imp_address else '-1' end from imp";

        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("insert into csp(id,name) values(1,'系统管理')", getDesertConnection());
        SqlUtils.executeSQL("update csp set status=1", getDesertConnection());
    }

    public void device() throws Exception {
        SqlUtils.executeSQL("delete from device", getDesertConnection());
        String selectSql = "select server_id,server_name,server_ip,server_url,server_ftpport,server_ftpusrname,server_ftppassword,server_ftppath from server where server_typeid=1 and server_id in (select server_id from imp_server)";
        String insertSql = "device(id,name,ip,url,ftp_port,ftp_user,ftp_pwd,ftp_path)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update device set status=1,type=1", getDesertConnection());
    }

    public void csp_device() throws Exception {
        SqlUtils.executeSQL("delete from csp_device", getDesertConnection());
        String selectSql = "select imp_id*1000,server_id from imp_server";
        String insertSql = "csp_device(csp_id,device_id)";
        importDo(selectSql, insertSql, true);
    }

    public void csp_csp() throws Exception {
        SqlUtils.executeSQL("delete from csp_csp", getDesertConnection());
        String selectSql = "select ICPIMP_ID,icp_id,imp_id*1000 from icp_imp";
        String insertSql = "csp_csp(id,master_csp_id,csp_id)";
        importDo(selectSql, insertSql, false);
    }

    public void csp_module() throws Exception {
        SqlUtils.executeSQL("delete from csp_module", getDesertConnection());
        SqlUtils.executeSQL("insert into csp_module(id,csp_id,module_id,is_default) (select id,id,1,1 from csp)", getDesertConnection());
    }

    public void module() throws Exception {
        SqlUtils.executeSQL("delete from module", getDesertConnection());
        SqlUtils.executeSQL("insert into module(id,name,status)values(1,'视频',1)", getDesertConnection());
    }

    public void property() throws Exception {
        SqlUtils.executeSQL("delete from property", getDesertConnection());

        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(1,'片名','MEDIA_NAME',1,1)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(2,'原名','MEDIA_ORIGINALNAME',1,1)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(3,'演员','MEDIA_ACTORS',1,1)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(4,'导演','MEDIA_DIRECTORS',1,1)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(5,'海报一','MEDIA_POSTER1',1,11,1,'post1Url')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(6,'海报二','MEDIA_POSTER2',1,11,1,'post2Url')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(7,'海报三','MEDIA_POSTER3',1,11)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type) values(8,'海报四','MEDIA_POSTER4',1,11)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name) " +
                "values(9,'格式','MEDIA_FORMATTYPE',1,5,0,'')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,data_type,is_multi_line,is_null,desp,display_order,module_id,is_main,column_name,related_table,status,is_merge,max_size) values(10,'设备','DEVICE',5,0,0,'',1,1,1,'deviceId',1,1,0,100)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_multi_line) values(11,'片段地址','MEDIA_CLIP',1,8,1)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(12,'公司','MEDIA_CORPORATION',1,1,1,'property3')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(13,'产地','MEDIA_HOMETOWN',1,5,1,'property4')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(14,'级别','MEDIA_LEVEL',1,5,1,'property5')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(15,'语言','MEDIA_LANGUAGE',1,5,1,'property6')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(16,'服务类型','MEDIA_SERVICE_TYPE',1,5,1,'property7')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name,max_size)" +
                " values(17,'简介','MEDIA_INTRO',1,2,1,'intro',1000)", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(18,'时长','MEDIA_LENGTH',1,1,1,'property1')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(19,'带宽','MEDIA_BANDWIDTH',1,1,1,'property2')", getDesertConnection());
        SqlUtils.executeSQL("insert into property(id,name,code,module_id,data_type,is_main,column_name)" +
                " values(20,'分类','MEDIA_TYPE',1,1,1,'property8')", getDesertConnection());

        SqlUtils.executeSQL("update property set display_order=id", getDesertConnection());

        SqlUtils.executeSQL("update property set is_multi_line=0 where is_multi_line is null", getDesertConnection());

        SqlUtils.executeSQL("update property set status=1,is_merge=0,max_size=200,is_main=0,is_null=1 where is_main is null", getDesertConnection());

        SqlUtils.executeSQL("update property set is_main=1,column_name='name',is_null=0 where id in (1)", getDesertConnection());
        SqlUtils.executeSQL("update property set is_main=1,column_name='actors',is_merge=1 where id in (3)", getDesertConnection());
        SqlUtils.executeSQL("update property set is_main=1,column_name='directors',is_merge=1 where id in (4)", getDesertConnection());
        //SqlUtils.executeSQL("update property set is_main=1,column_name='post1Url'  where id in (5)", getDesertConnection());
        //SqlUtils.executeSQL("update property set is_main=1,column_name='post2Url'  where id in (6)", getDesertConnection());
        //SqlUtils.executeSQL("update property set max_size=1000,is_main=1,column_name='intro' where id in (17)", getDesertConnection());
        //SqlUtils.executeSQL("update property set is_main=1,column_name='property1' where id in (18)", getDesertConnection());
        SqlUtils.executeSQL("update property set is_main=1 where id in (10)", getDesertConnection());
        SqlUtils.executeSQL("update property set is_main=1 where is_main is null", getDesertConnection());
        SqlUtils.executeSQL("update property set is_merge=0 where is_merge is null", getDesertConnection());
        SqlUtils.executeSQL("update property set is_null=1 where is_null is null", getDesertConnection());
        SqlUtils.executeSQL("update property set max_size=200 where max_size is null", getDesertConnection());
        SqlUtils.executeSQL("update property set status=1 where status is null", getDesertConnection());
    }

    public void property_select() throws Exception {
        SqlUtils.executeSQL("delete from property_select", getDesertConnection());

        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(91,9,'media')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(92,9,'real')", getDesertConnection());

        SqlUtils.executeSQL("update content_property set string_value=91 where string_value=1 and property_id=9", getDesertConnection());

        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1301,13,'中国')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1302,13,'美国')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1303,13,'英国')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1304,13,'韩国')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1305,13,'日本')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1306,13,'其它')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1307,13,'德国')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1308,13,'法国')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1309,13,'意大利')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1310,13,'西班牙')", getDesertConnection());
        //现在hometown_id改到property4了
        SqlUtils.executeSQL("update content set property4=1301 where property4=0  ", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1302 where property4=100", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1303 where property4=101", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1304 where property4=102", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1305 where property4=103", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1306 where property4=104", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1307 where property4=106", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1308 where property4=107", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1309 where property4=109", getDesertConnection());
        SqlUtils.executeSQL("update content set property4=1310 where property4=110", getDesertConnection());

        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1501,15,'英语')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1502,15,'国语')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1503,15,'韩语')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1504,15,'日语')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1505,15,'粤语')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1506,15,'其他')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1507,15,'法语')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1508,15,'拉丁语')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1509,15,'德语')", getDesertConnection());

        SqlUtils.executeSQL("update content set property6='1501' where property6='106'", getDesertConnection());
        SqlUtils.executeSQL("update content set property6='1502' where property6='107'", getDesertConnection());
        SqlUtils.executeSQL("update content set property6='1503' where property6='108'", getDesertConnection());
        SqlUtils.executeSQL("update content set property6='1504' where property6='109'", getDesertConnection());
        SqlUtils.executeSQL("update content set property6='1505' where property6='110'", getDesertConnection());
        SqlUtils.executeSQL("update content set property6='1506' where property6='113'", getDesertConnection());
        SqlUtils.executeSQL("update content set property6='1507' where property6='114'", getDesertConnection());
        SqlUtils.executeSQL("update content set property6='1508' where property6='115'", getDesertConnection());
        SqlUtils.executeSQL("update content set property6='1509' where property6='116'", getDesertConnection());

        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1401,14,'一级')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1402,14,'二级')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1403,14,'三级')", getDesertConnection());

        SqlUtils.executeSQL("update content set property5='1401' where property5='0'", getDesertConnection());


        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1601,16,'媒体点播')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1602,16,'媒体下载')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1603,16,'点播下载')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1604,16,'媒体直播')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1605,16,'DRM')", getDesertConnection());

        SqlUtils.executeSQL("update content set property7='1601' where property7='1'", getDesertConnection());
        SqlUtils.executeSQL("update content set property7='1602' where property7='2'", getDesertConnection());
        SqlUtils.executeSQL("update content set property7='1603' where property7='3'", getDesertConnection());
        SqlUtils.executeSQL("update content set property7='1604' where property7='4'", getDesertConnection());
        SqlUtils.executeSQL("update content set property7='1605' where property7='5'", getDesertConnection());
//        SqlUtils.executeSQL("update content_property set display_order=id", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1701,20,'偶像')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1702,20,'爱情')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1703,20,'古装')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1704,20,'警匪')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1705,20,'武侠')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1706,20,'家庭')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1707,20,'商战')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1708,20,'喜剧')", getDesertConnection());
        SqlUtils.executeSQL("insert into property_select(id,property_id,name) values(1709,20,'动作')", getDesertConnection());


        SqlUtils.executeSQL("update property_select set status=1 where status is null",getDesertConnection());
        SqlUtils.executeSQL("update property_select set code=id where code is null",getDesertConnection());
        logger.debug("end: property_select");

        //importDo(selectSql, insertSql, false);
    }

    public void ipRange() throws Exception {
        SqlUtils.executeSQL("delete from AREA", getDesertConnection());
        String selectSql = "select ID,NAME,'属于'||NAME||'的区域' from USER_NET";
        String insertSql = "AREA(id,name,desp)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("delete from IP_RANGE", getDesertConnection());
        List netMaskList = SqlUtils.getRecords("select nm.IP,nm.IP_MASK,un.NAME,un.ID,nm.ID from NET_MASK nm,USER_NET un where nm.NET_ID=un.ID",0,100000L, getSourceConnection());
        if(netMaskList!=null){
            List<String> insertSqls = new ArrayList<String>();
           for(Object o:netMaskList){
               if(o instanceof Object[]){
                   Object[] data=(Object[])o;
                   String ipStr=data[0].toString(); //61.55.144.81
                   String maskStr = data[1].toString();//255.255.255.0
                   long ip =StringUtils.string2IpLong(ipStr);
                   long mask=StringUtils.string2IpLong(maskStr);
                   long ipFrom = ip&mask;
                   long ipTo = (ip |(~mask&0xFFFF));
                   String name = data[2].toString()+"("+StringUtils.longIp2String(ipFrom)+"-"+StringUtils.longIp2String(ipTo)+")";
                   String sql ="insert into IP_RANGE(id,AREA_ID,DESP,IP_FROM,IP_TO,NAME) values("+data[4]+
                           ","+data[3]+",'"+name+",IP="+data[0]+"，掩码"+data[1]+"',"+ipFrom+","+ipTo+",'"+name+"')";
                   insertSqls.add(sql);
                   logger.debug("区域添加："+sql);
               }
           }
           SqlUtils.executeBatch(insertSqls, getDesertConnection());
        }

    }
    
    public void content() throws Exception {
        logger.debug("删除老的媒体...");
        SqlUtils.executeSQL("delete from content", getDesertConnection());
        String selectSql = "select distinct(m.media_id),1,ms.server_id,media_impid*1000," +
                "m.media_name,m.media_actors,m.media_directors," +
                "m.media_length,m.media_intro,m.media_poster1,m.media_poster2" +
                ",m.media_cid,ms.media_uploaddate,m.MEDIA_CORPORATION" +
                ",m.MEDIA_HOMETOWNID,m.MEDIA_LEVELID,m.MEDIA_LANGUAGEID,m.MEDIA_SERVICE_TYPE,m.MEDIA_BANDWIDTH"+
                " from media m,media_server ms where m.media_id=ms.media_id order by media_id asc";
        String insertSql = "content(id,module_id,device_id,csp_id," +
                "name,actors,directors,property1,intro,post1_url,post2_url," +
                "content_id,create_time,property3,property4,property5,property6,property7,property2)";
        logger.debug("准备导入媒体...");
        importDo(selectSql, insertSql, false);
        logger.debug("更改媒体数据默认状态");
        SqlUtils.executeSQL("update content set status=2,status_time=create_time", getDesertConnection());
        logger.debug("媒体导入完成");

    }

    public void content_property() throws Exception {
        SqlUtils.executeSQL("delete from content_property", getDesertConnection());
        String selectSql = "select id,property_id,resource_id,substr(value,0,500) from  R_SUBJECT_PROPERTY where resource_id in (select id from r_subject where state=4 )";
        String insertSql = "content_property(id,property_id,content_id,string_value)";
        importDo(selectSql, insertSql, false);
    }

    public void content_channel() throws Exception {
        SqlUtils.executeSQL("delete from content_channel", getDesertConnection());
        String selectSql = "select mediaicp_id,media_channelid,media_id from media_icp where media_id in (select media_id from media) and icp_id in (select icp_id from icp)";
        String insertSql = "content_channel(id,channel_id,content_id)";
        importDo(selectSql, insertSql, false);
    }

    public void content_csp() throws Exception {
        SqlUtils.executeSQL("delete from content_csp", getDesertConnection());
        String selectSql = "select distinct media_id,icp_id, media_published,1 from media_icp";
        String insertSql = "content_csp(content_id,csp_id,status,content_audit_id)";
        importDo(selectSql, insertSql, true);
        SqlUtils.executeSQL("update content_csp set status=10 where status=2", getDesertConnection());
        SqlUtils.executeSQL("update content_csp set status=2 where status=1", getDesertConnection());
        SqlUtils.executeSQL("update content_csp set status=1 where status=0", getDesertConnection());
    }

    public void channel() throws Exception {
        SqlUtils.executeSQL("delete from channel", getDesertConnection());
        String selectSql = "select channel_id,channel_name,icp_id,channel_parentid from channel where icp_id>0";
        String insertSql = "channel(id,name,csp_id,parent_id)";
        importDo(selectSql, insertSql, false);
        SqlUtils.executeSQL("update channel set parent_id=-1 where parent_id=0", getDesertConnection());
        SqlUtils.executeSQL("update channel set status=1,type=1", getDesertConnection());
    }

    public void content_property_exec() throws Exception {
        logger.debug("start content_property_exec");
        SqlUtils.executeSQL("delete from content_property", getDesertConnection());

        //先导入影片连接信息
        String selectSql = "select " +user+//利用目标数据账号的序列，注意，源数据帐号要有DBA权限！
                ".FORTUNE_GLOBAL_SEQ.nextval,mediaclip_mediaid,11,mediaclip_volume,mediaclip_url,mediaclip_length,substr(mediaclip_name,1,49) from media_clip" +
                " where mediaclip_mediaid in (select media_id from media)";
        String insertSql = "content_property(id,content_id,property_id,int_value,string_value,length,name)";
        importDo(selectSql, insertSql, false);



        HashMap<String,Object> propertyMap = new HashMap<String,Object>();
        List propertyList = SqlUtils.getRecords("select id,name,data_type,is_multi_line,is_merge,is_main,column_name,code from property", 0, 10000000l, getDesertConnection());
        for (int i = 1; i < propertyList.size(); i++) {
            Object objs[] = (Object[]) propertyList.get(i);
            propertyMap.put(objs[7].toString(), objs);
        }
        int totalInserted = 0;
        List<String> sqlList = new ArrayList<String>();
        String sql = "select media_id,MEDIA_ORIGINALNAME,MEDIA_ACTORS,MEDIA_DIRECTORS,MEDIA_POSTER3,MEDIA_POSTER4," +
                "MEDIA_FORMATTYPE from media";
        String colName[][] = SqlUtils.getTableCols(sql, getSourceConnection());
        List mediaList = SqlUtils.getRecords(sql, 0, 10000000l, getSourceConnection());
        int mediaCount = mediaList.size();
        long startTime = System.currentTimeMillis();
        for (int i = 1; i < mediaCount; i++) {
            Object objs[] = (Object[]) mediaList.get(i);
            long id = Long.parseLong(objs[0].toString());

/*
            List mediaClipList = SqlUtils.getRecords("select mediaclip_url,mediaclip_volume,mediaclip_name," +
                    "mediaclip_length from media_clip where mediaclip_mediaid=" + id, 0, 10000000000l, getSourceConnection());
            for (int j = 1; j < mediaClipList.size(); j++) {

                Object[] data = (Object[]) mediaClipList.get(j);
                if(data[0]!=null){
                    String url = data[0].toString();
                    int clipNo = StringUtils.string2int(data[1].toString(),-1);
                    sqlList.add("insert into content_property(id,content_id,property_id,int_value,string_value,length,name)" +
                            " values(FORTUNE_GLOBAL_SEQ.nextval," + id + ",11," +clipNo+
                            ",'" + url.replaceAll("'", "''") + "'," + data[3]+",'"+data[2]+"'"+
                            ")");
                }
            }
*/

            for (int j = 1; j < colName.length; j++) {
                Object obj = objs[j];
                if (obj == null) {
                    continue;
                }
                String propertyCode = colName[j][0];
                Object property[] = (Object[]) propertyMap.get(propertyCode);
                if (property == null) {
                    logger.warn("property is null ! id:" + id);
                    continue;
                }
                long propertyId = Long.parseLong(property[0].toString());
                //long dataType = Long.parseLong(property[2].toString());
                long isMultiLine = Long.parseLong(property[3].toString());
                long isMerge = Long.parseLong(property[4].toString());
                long isMain = Long.parseLong(property[5].toString());
                String columnName = "";
                if (property[6] != null) {
                    columnName = property[6].toString();
                }

                if (isMain == 1 && columnName != null && !"".equals(columnName)) {
                    if (isMerge == 1) {//如果是多个合并，就处理一下
                        String obj1 = (String) obj;
                        //String value = obj1.replaceAll("'", "''").replaceAll(" ", ";").replaceAll("，", ";").replaceAll(",", ";");
                        //空格不能替换，否则好多外国人遭殃了
                        String value = obj1.replaceAll("'", "''").replaceAll("，", ";").replaceAll(",", ";");
                        sqlList.add("update content set " + columnName + "='" + value + "' where id=" + id);
                        //logger.debug("update content set "+columnName+"="+columnName+" || '"+stringValue+";' where id="+contentId);
                        //SqlUtils.executeSQL("update content set "+columnName+"="+columnName+" || '"+stringValue+";' where id="+contentId,getDesertConnection());
                    } else {
                        String obj1 = (String) obj;
//                        String value = obj1.replaceAll("'", "''").replaceAll(" ", ";").replaceAll("，", ";").replaceAll(",", ";");
                        sqlList.add("update content set " + columnName + "='" + obj1.replaceAll("'", "''") + "' where id=" + id);
                        //logger.debug("update content set "+columnName+"='"+stringValue+"' where id="+contentId);
                        //SqlUtils.executeSQL("update content set "+columnName+"='"+stringValue+"' where id="+contentId,getDesertConnection());

                    }
                    //sqlList.add("delete from content_property where id=" + id);
                    //logger.debug("delete from content_property where id="+id);
                    //SqlUtils.executeSQL("delete from content_property where id="+id,getDesertConnection());
                } else {
                    String obj1 = obj.toString();
                    sqlList.add("insert into content_property(id,content_id,property_id,string_value) values(FORTUNE_GLOBAL_SEQ.nextval," + id + "," + propertyId + ",'" + obj1.replaceAll("'", "''") + "')");
                }

            }
            if(sqlList.size()>100){
                totalInserted+=sqlList.size();
                logger.debug((System.currentTimeMillis()-startTime)/1000+" seconds,Run batch sql :"+sqlList.size()+",total:"+totalInserted+",percent:"+(i*100/mediaCount)+"%");
                SqlUtils.executeBatch(sqlList, getDesertConnection());
                sqlList.clear();
            }
        }
        totalInserted+=sqlList.size();
        logger.debug("Run batch sql :"+sqlList.size()+",total:"+totalInserted);
        SqlUtils.executeBatch(sqlList, getDesertConnection());
        logger.debug("end content_property_exec");
    }

    public void product() throws Exception {
        SqlUtils.executeSQL("delete from product", getDesertConnection());
        String selectSql = "select c.channel_id,i.icp_name ||'-'|| c.channel_name || '-包月',c.CATEGORY_ID,CHANNEL_MONTHLENGTH,1,0,0,CHANNEL_MONTHFEE,1 from channel c, icp i where c.icp_id=i.icp_id and c.channel_status=1 ";
        String insertSql = "product(id,name,pay_product_no,valid_length,length_unit,auto_pay,search_extra,price,type)";
        importDo(selectSql, insertSql, false);

/*
        selectSql = "select distinct '' || pp.price || '元按次购买',p.service_id,pp.price,'2' from o_product p,o_product_price pp  where p.id=pp.product_id and p.type=3 and pp.id in (select product_id from O_PRODUCT_RESOURCE)";
        insertSql = "product(name,pay_product_no,price,type)";
        importDo(selectSql, insertSql, true);
*/
        SqlUtils.executeSQL("update product set status=1", getDesertConnection());
    }

    public void service_product() throws Exception {
        SqlUtils.executeSQL("delete from service_product", getDesertConnection());
        String selectSql = "select c.channel_id,c.channel_id,i.icp_name ||'-'|| c.channel_name || '-包月',c.icp_id,1 from channel c, icp i where c.icp_id=i.icp_id and c.channel_status=1";
        String insertSql = "service_product(id,product_id,name,csp_id,type)";
        importDo(selectSql, insertSql, false);

    }

    public void csp_product() throws Exception {
        SqlUtils.executeSQL("delete from csp_product", getDesertConnection());
        String selectSql = "select distinct csp_id,product_id from service_product";
        String insertSql = "csp_product(csp_id,product_id)";
        importDo1(selectSql, insertSql, true);
    }


    public void content_service_product() throws Exception {
        SqlUtils.executeSQL("delete from content_service_product", getDesertConnection());
        String selectSql = "select distinct mi.media_id,c.channel_id from media_icp mi, channel c, icp i where i.icp_id=c.icp_id and c.channel_status=1 and  c.channel_id in (select channel_id from channel start with channel_id=mi.media_channelid connect by prior channel_parentid=channel_id)";
        String insertSql = "content_service_product(content_id,service_product_id)";
        importDo(selectSql, insertSql, true);
/*
        selectSql = "select distinct cs.subject_id,pr.product_id from o_product_resource pr,r_channel_subject cs where pr.resource_id=cs.id and pr.resource_type=2 and cs.state=3";
        insertSql = "content_service_product(content_id,service_product_id)";
        importDo(selectSql, insertSql, true);
*/
    }

    public void recommend() throws Exception {
        SqlUtils.executeSQL("delete from recommend", getDesertConnection());
        String selectSql = "select id,name,org_id,case when type=0 then 1 else 2 end from R_RECOMMENDATION";
        String insertSql = "recommend(id,name,csp_id,type)";
        importDo(selectSql, insertSql, false);
    }

    public void content_recommend() throws Exception {
        SqlUtils.executeSQL("delete from content_recommend", getDesertConnection());
        String selectSql = "select distinct cr.id,cr.channel_id,cr.type,cs.subject_id  from R_CHANNEL_RECOMMENDATION cr,r_channel_subject cs where cr.channel_subject_id=cs.id";
        String insertSql = "content_recommend(id,channel_id,recommend_id,content_id)";
        importDo(selectSql, insertSql, false);
    }

    public void doImport() {
        try {
/*
            csp();
            device();
            csp_device();
            csp_csp();
            admin();
*/
//            csp_module();
//            module();
///*
            property();
            property_select();
//            channel();
//            content();
//            content_channel();
//            content_csp();
//             content_property(); // todo 这个不用做，山西的数据不需要
            content_property_exec();


//            product();
//            service_product();
//            csp_product();
//            content_service_product();
          //recommend();
            //content_recommend();
//*/
//            ipRange();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        BjImportor importor = new BjImportor();
        importor.doImport();
    }

}
