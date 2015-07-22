package com.fortune.rms.business.ad.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.ad.dao.daoInterface.AdLogDaoInterface;
import com.fortune.rms.business.ad.model.AdLog;
import com.fortune.util.PageBean;
import com.fortune.util.SearchCondition;
import org.hibernate.type.DateType;
import org.hibernate.type.StringType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AdLogDaoAccess extends BaseDaoAccess<AdLog, Long>
		implements
			AdLogDaoInterface {

	public AdLogDaoAccess() {
		super(AdLog.class);
	}

    public List<Map<String, Object>> stateForAd(String adName,Date startTime, Date stopTime, Integer trainId,PageBean pageBean) {
        String hql = "select count(*) alc,al.AD_ID from AD_LOG al where 1=1";
        if(trainId!=null&&trainId>0){
            hql += " and al.TRAIN_ID="+trainId;
        }
        List<Object> parameters = new ArrayList<Object>();
        if(startTime!=null){
            hql+=" and al.visitTime>=?";
            parameters.add(startTime);
        }
        if(stopTime!=null){
            hql+=" and al.visitTime<=?";
            parameters.add(stopTime);
        }

        hql += " group by al.AD_ID";
        hql = "select a.NAME name,a.ID id,b.alc logCount from ("+hql+") b,AD a where a.ID = b.AD_ID";
        if(adName!=null&&!adName.trim().equals("")){
            hql +=" and  a.name like ?";
            parameters.add("%"+adName+"%");
        }
        return searchSQL(hql, parameters.toArray(), pageBean);
    }
    public List<Map<String, Object>> listLogs(String adName,String contentName,Long channelId,String lineName,
                                              Date startTime, Date stopTime, Integer adId,Integer trainId,PageBean pageBean) {
        String tables = "AD_LOG al,AD a,CONTENT c,CHANNEL ch";
        String tablesRelateSql = "al.AD_ID=a.ID and al.CONTENT_ID = c.ID and al.CHANNEL_ID = ch.ID";
        String fields = "al.ID id,al.AD_ID adId,al.VISIT_TIME visitTime,al.SP_ID spId,al.AD_RANGE_ID adRangeId," +
                "al.CHANNEL_ID channelId,al.CONTENT_ID contentId,al.TRAIN_ID trainId,ch.NAME channelName," +
                "c.NAME contentName,a.NAME adName";
        if(lineName!=null&&!"".equals(lineName)){
            tables +=",TRAIN_LINE l";
            fields +=",l.NAME lineName,l.ID lineId";
            tablesRelateSql +=" and l.ID=al.TRAIN_ID";
        }
        String sql = "select " +fields+
                " from " + tables+
                " where "+tablesRelateSql;
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSqlStr(sql);
        searchCondition.appendAndSqlCondition("a.NAME like ?", adName, StringType.INSTANCE,true);
        searchCondition.appendAndSqlCondition("c.NAME like ?", contentName, StringType.INSTANCE,true);
        searchCondition.appendAndSqlCondition("l.Name like ?", lineName, StringType.INSTANCE,true);
        searchCondition.appendAndSqlCondition("al.TRAIN_ID = ?",trainId, IntegerType.INSTANCE,true);
        searchCondition.appendAndSqlCondition("a.ID = ?",adId, IntegerType.INSTANCE,true);
        searchCondition.appendAndSqlCondition("ch.ID = ?",channelId, LongType.INSTANCE,true);
        searchCondition.appendAndSqlCondition("al.VISIT_TYPE >= ?",startTime, DateType.INSTANCE,true);
        searchCondition.appendAndSqlCondition("al.VISIT_TYPE <= ?",stopTime, DateType.INSTANCE,true);
        return searchSQL(searchCondition.getSqlStr(), searchCondition.getObjectArrayParamValues(), pageBean);
    }

    public List<Map<String, Object>> stateForTrain(String lineName,Date startTime, Date stopTime, Integer adId,PageBean pageBean) {
        return null;
    }

}
