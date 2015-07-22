package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.ChannelDemandLogDaoInterface;
import com.fortune.rms.business.log.model.ChannelDemandLog;
import com.fortune.util.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-20
 * Time: ÉÏÎç10:36
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class ChannelDemandLogDaoAccess extends BaseDaoAccess<ChannelDemandLog,Long> implements ChannelDemandLogDaoInterface{

    public ChannelDemandLogDaoAccess() {
        super(ChannelDemandLog.class);
    }
    public List getChannelDemandFromVisitLog(Date dateStatics){
        String startTime = StringUtils.date2string(dateStatics,"yyyy-MM-dd");
        Session session = getSession();
        String sql = "";
        try{
            sql = "select channel_Id,count(*),sum(LENGTH),sum(Bytes_Send) from VISIT_LOG where start_time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')and start_time<=TO_DATE('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi;ss') and channel_id in (select id from Channel) group by channel_Id order by count(*) Desc";
            Query query  = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }
}
