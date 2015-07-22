package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.AreaDemandLogDaoInterface;
import com.fortune.rms.business.log.model.AreaDemandLog;
import com.fortune.util.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AreaDemandLogDaoAccess extends  BaseDaoAccess<AreaDemandLog, Long> implements  AreaDemandLogDaoInterface {

    public AreaDemandLogDaoAccess() {
        super(AreaDemandLog.class);
    }
    public Map getAreaDemandFromVisitLog(Date dateStatics,long type){
        String startTime = StringUtils.date2string(dateStatics,"yyyy-MM-dd");
        Session session = getSession();
        String sql = "";
        String sqlPad = "";
        String sqlPhone = "";
        try{
            if(type==1){
                sql = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where area_Id in (select id from Area)  and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and user_Id !='null' group by area_Id";
                sqlPad = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where area_Id in (select id from Area) and  start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and user_Id !='null' and (user_agent  like '%Pad%' or user_agent  like '%pad%') group by area_Id";
                sqlPhone = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where area_Id in (select id from Area) and  start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and user_Id !='null' and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) group by area_Id";
            }
            if(type==2){
                sql = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where area_Id in (select id from Area) and  start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and user_Id ='null' and area_Id != -1 group by area_Id" ;
                sqlPad = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where area_Id in (select id from Area) and  start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and user_Id ='null' and area_Id != -1 and (user_agent  like '%Pad%' or user_agent  like '%pad%') group by area_Id" ;
                sqlPhone = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where area_Id in (select id from Area) and  start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and user_Id ='null' and area_Id != -1 and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) group by area_Id" ;
            }
            if(type==3){
                sql = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where  start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and channel_Id != -1 and content_Id != -1 and area_Id = -1 group by area_Id";
                sqlPad = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where  start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and channel_Id != -1 and content_Id != -1 and area_Id = -1 and (user_agent  like '%Pad%' or user_agent  like '%pad%') group by area_Id";
                sqlPhone = "select area_Id,count(*),sum(length),sum(bytes_Send) from Visit_Log where  start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and channel_Id != -1 and content_Id != -1 and area_Id = -1 and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) group by area_Id";
            }
            Query query = session.createSQLQuery(sql);
            Query query1 = session.createSQLQuery(sqlPad);
            Query query2 = session.createSQLQuery(sqlPhone);
            List list = query.list();
            List listPad = query1.list();
            List listPhone = query2.list();
            Map<String,List> map = new HashMap<String, List>();
            map.put("allAreaDemand",list);
            map.put("padAreaDemand",listPad);
            map.put("phoneAreaDemand",listPhone);
            return map;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }

    public long getAreaDemandByAreaId(Date dateStatics,long areaId,long type,long demandType) {
        String startTime = StringUtils.date2string(dateStatics,"yyyy-MM-dd");
        Session session = getSession();
        String sql = "";
        try {
            if(type == 1) {
                if(demandType == 1) { //获取移动总流量
                    sql = "select sum(bytes_send) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and user_Id !='null' and area_id ="+areaId;
                }

                if(demandType == 2) { //获取移动次数
                    sql = "select count(*) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and user_Id !='null' and area_id ="+areaId;
                }

                if(demandType == 3) { //获取移动长度
                    sql = "select sum(length) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and user_Id !='null' and area_id ="+areaId;
                }

                if(demandType == 4) {//获取点播人数
                    sql = "select count(distinct(USER_ID)) from VISIT_LOG where start_time >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id != 'null' and area_id ="+areaId;
                }

            }
            if(type == 2) {
                if(demandType == 1) {
                    sql = "select sum(bytes_send) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and user_Id ='null' and area_Id != -1 and area_id ="+areaId;
                }

                if(demandType == 2) {
                    sql = "select count(*) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and user_Id ='null' and area_Id != -1 and area_id ="+areaId;
                }

                if(demandType == 3) {
                    sql = "select sum(length) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and user_Id ='null' and area_Id != -1 and area_id ="+areaId;
                }

                if(demandType == 4) {
                    sql = "select count(distinct(USER_ID)) from VISIT_LOG where start_time >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id != 'null' and area_Id != -1 and area_id ="+areaId;
                }
            }
            if(type == 3) {
                if(demandType == 1) {
                    sql = "select sum(bytes_send) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and channel_Id != -1 and content_Id != -1 and area_Id = -1";
                }

                if(demandType == 2) {
                    sql = "select count(*) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and channel_Id != -1 and content_Id != -1 and area_Id = -1";
                }

                if(demandType == 3) {
                    sql = "select sum(length) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3)) and channel_Id != -1 and content_Id != -1 and area_Id = -1";
                }

                if(demandType == 4) {
                    sql = "select count(distinct(USER_ID)) from VISIT_LOG where start_time >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and channel_Id != -1 and content_Id != -1  and area_Id = -1";
                }

            }
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return (list != null && list.size() > 0 && list.get(0)!= null)?Long.valueOf(list.get(0).toString()):0;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
      return 0;
    }

}
