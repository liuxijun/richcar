package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.ContentDemandLogDaoInterface;
import com.fortune.rms.business.log.model.ContentDemandLog;
import com.fortune.util.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-6
 * Time: ÉÏÎç9:36
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ContentDemandLogDaoAccess extends  BaseDaoAccess<ContentDemandLog, Long> implements  ContentDemandLogDaoInterface {

    public ContentDemandLogDaoAccess() {
        super(ContentDemandLog.class);
    }

    public Map getContentDemandFromVisitLog(Date dateStatics, long type,long liveChannelId) {
        String startTime = StringUtils.date2string(dateStatics, "yyyy-MM-dd");
        Session session = getSession();
        String sql = "";
        String sqlPad = "";
        String sqlPhone = "";
        try{
            if(type == 1){
                sql = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log where content_Id in (select id from Content) and sp_Id in (select id from Csp) and channel_Id !="+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')group by sp_Id,content_Id,channel_Id order by count(*) Desc";
                sqlPad = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log where content_Id in (select id from Content) and sp_Id in (select id from Csp) and channel_Id !="+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') group by sp_Id,content_Id,channel_Id order by count(*) Desc";
                sqlPhone = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log where content_Id in (select id from Content) and sp_Id in (select id from Csp) and channel_Id !="+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) group by sp_Id,content_Id,channel_Id order by count(*) Desc";
            }
            if(type == 2){
                sql = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log where content_Id in (select id from Content) and sp_Id in (select id from Csp) and channel_Id = "+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') group by sp_Id,content_Id,channel_Id order by count(*) Desc";
                sqlPad = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log where content_Id in (select id from Content) and sp_Id in (select id from Csp) and channel_Id ="+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') group by sp_Id,content_Id,channel_Id order by count(*) Desc";
                sqlPhone = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log where content_Id in (select id from Content) and sp_Id in (select id from Csp) and channel_Id ="+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  start_Time<=to_date('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) group by sp_Id,content_Id,channel_Id order by count(*) Desc";
            }
            if(type == 3){
                sql = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log  where content_Id in (select id from Content) and sp_Id in (select id from Csp) and user_id = 'null' and channel_Id != "+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and start_Time<= TO_DATE('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') group by sp_Id,content_Id,channel_Id order by count(*) DESC" ;
                sqlPad = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log  where content_Id in (select id from Content) and sp_Id in (select id from Csp) and user_id = 'null' and channel_Id != "+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and start_Time<= TO_DATE('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') group by sp_Id,content_Id,channel_Id order by count(*) DESC" ;
                sqlPhone = "select sp_Id,content_Id,count(*),sum(length),channel_Id,sum(bytes_Send) from Visit_Log  where content_Id in (select id from Content) and sp_Id in (select id from Csp) and user_id = 'null' and channel_Id != "+liveChannelId+" and start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and start_Time<= TO_DATE('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%')  or user_agent is null) group by sp_Id,content_Id,channel_Id order by count(*) DESC" ;
            }
            Query query = session.createSQLQuery(sql);
            Query query1 = session.createSQLQuery(sqlPad);
            Query query2 = session.createSQLQuery(sqlPhone);
            List list = query.list();
            List listPad = query1.list();
            List listPhone = query2.list();
            Map<String,List> map = new HashMap<String, List>();
            map.put("allContentDemand",list);
            map.put("padContentDemand",listPad);
            map.put("phoneContentDemand",listPhone);
            return map;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }
}

