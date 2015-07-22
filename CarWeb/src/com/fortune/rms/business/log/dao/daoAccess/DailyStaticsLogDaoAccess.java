package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.DailyStaticsLogDaoInterface;
import com.fortune.rms.business.log.model.DailyStaticsLog;
import com.fortune.util.HibernateUtils;
import com.fortune.util.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DailyStaticsLogDaoAccess extends BaseDaoAccess<DailyStaticsLog,Long> implements DailyStaticsLogDaoInterface {
    public DailyStaticsLogDaoAccess() {
        super(DailyStaticsLog.class);
    }
    public Map getNetFlowLogsFromVisitLog(Date startDate,Long liveChannelId,List cspIdList){
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        Map<String,String> map = new HashMap<String, String>();
        map.put("dateStatics",startTime);
        Session session = getSession();
        Query query;
        try {
            String allNetFlowSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss')";
            query = session.createSQLQuery(allNetFlowSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("allNetFlow",query.list().get(0).toString());
            }  else {
                map.put("allNetFlow","0");
            }
            String allLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
            query = session.createSQLQuery(allLengthSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("allLength",query.list().get(0).toString());
            }else {
                map.put("allLength","0");
            }
//            String allCountSql= "select count(*) from VISIT_LOG where START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_Id !='null' and channel_Id ="+liveChannelId;
            String allCountSql = "select COUNT(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss')";
            query = session.createSQLQuery(allCountSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("allCount",query.list().get(0).toString());
            }else {
                map.put("allCount","0");
            }
            String mobileNetFlowSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') " +
                    "and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3))";
            query = session.createSQLQuery(mobileNetFlowSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("mobileNetFlow",query.list().get(0).toString());
            }  else {
                map.put("mobileNetFlow","0");
            }
            String mobileLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') " +
                    "and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3))";
            query = session.createSQLQuery(mobileLengthSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("mobileLength",query.list().get(0).toString());
            }  else {
                map.put("mobileLength","0");
            }
            String mobileCountSql = "select count(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') " +
                    "and user_id in (select login from Usr_user_Login where login_time>= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and login_time<=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and login_status in (1,2,3))";
            query = session.createSQLQuery(mobileCountSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("mobileCount",query.list().get(0).toString());
            }  else {
                map.put("mobileCount","0");
            }
            //
            String allNetFlowPadSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') ";
            query = session.createSQLQuery(allNetFlowPadSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("allNetFlowPad",query.list().get(0).toString());
            }  else {
                map.put("allNetFlowPad","0");
            }
            String padCountSql = "select COUNT(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') ";
            query = session.createSQLQuery(padCountSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("padCount",query.list().get(0).toString());
            }  else {
                map.put("padCount","0");
            }
            String padLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') ";
            query = session.createSQLQuery(padLengthSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("padLength",query.list().get(0).toString());
            }  else {
                map.put("padLength","0");
            }
            //
            String allNetFlowPhoneSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null)";
            query = session.createSQLQuery(allNetFlowPhoneSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("allNetFlowPhone",query.list().get(0).toString());
            }  else {
                map.put("allNetFlowPhone","0");
            }
            String phoneCountSql = "select count(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null)";
            query = session.createSQLQuery(phoneCountSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("phoneCount",query.list().get(0).toString());
            }  else {
                map.put("phoneCount","0");
            }
            String phoneLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null)";
            query = session.createSQLQuery(phoneLengthSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("phoneLength",query.list().get(0).toString());
            }  else {
                map.put("phoneLength","0");
            }
            //
            String allNetFlowLiveSql = "select SUM(BYTES_SEND) from VISIT_LOG where channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(allNetFlowLiveSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("allNetFlowLive",query.list().get(0).toString());
            }else{
                map.put("allNetFlowLive","0");
            }
            String liveCountSql = "select count(*) from VISIT_LOG where channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(liveCountSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("liveCount",query.list().get(0).toString());
            }else{
                map.put("liveCount","0");
            }
            String liveLengthSql = "select SUM(Length) from VISIT_LOG where channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(liveLengthSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("liveLength",query.list().get(0).toString());
            }else{
                map.put("liveLength","0");
            }
            //
            String allNetFlowLivePadSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+") ";
            query = session.createSQLQuery(allNetFlowLivePadSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("allNetFlowLivePad",query.list().get(0).toString());
            }  else {
                map.put("allNetFlowLivePad","0");
            }
            String livePadCountSql = "select COUNT(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+") ";
            query = session.createSQLQuery(livePadCountSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("livePadCount",query.list().get(0).toString());
            }  else {
                map.put("livePadCount","0");
            }
            String livePadLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+") ";
            query = session.createSQLQuery(livePadLengthSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("livePadLength",query.list().get(0).toString());
            }  else {
                map.put("livePadLength","0");
            }
            //
            String allNetFlowLivePhoneSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(allNetFlowLivePhoneSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("allNetFlowLivePhone",query.list().get(0).toString());
            }  else {
                map.put("allNetFlowLivePhone","0");
            }
            String livePhoneCountSql = "select Count(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(livePhoneCountSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("livePhoneCount",query.list().get(0).toString());
            }  else {
                map.put("livePhoneCount","0");
            }
            String livePhoneLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and   START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) and content_id in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(livePhoneLengthSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("livePhoneLength",query.list().get(0).toString());
            }  else {
                map.put("livePhoneLength","0");
            }
            //
            String allNetFlowContentSql = "select SUM(BYTES_SEND) from VISIT_LOG where channel_Id != -1 and content_Id != -1 and  START_TIME  >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id = "+liveChannelId+")";
            query = session.createSQLQuery(allNetFlowContentSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("allNetFlowContent",query.list().get(0).toString());
            } else{
                map.put("allNetFlowContent","0");
            }
            String contentCountSql = "select COUNT(*) from VISIT_LOG where channel_Id != -1 and content_Id != -1 and  START_TIME  >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id = "+liveChannelId+")";
            query = session.createSQLQuery(contentCountSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("contentCount",query.list().get(0).toString());
            } else{
                map.put("contentCount","0");
            }
            String contentLengthSql = "select SUM(Length) from VISIT_LOG where channel_Id != -1 and content_Id != -1 and  START_TIME  >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id = "+liveChannelId+")";
            query = session.createSQLQuery(contentLengthSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("contentLength",query.list().get(0).toString());
            } else{
                map.put("contentLength","0");
            }
             //
            String allNetFlowContentPadSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+") ";
            query = session.createSQLQuery(allNetFlowContentPadSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("allNetFlowContentPad",query.list().get(0).toString());
            }  else {
                map.put("allNetFlowContentPad","0");
            }
            String contentPadCountSql = "select COUNT(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+") ";
            query = session.createSQLQuery(contentPadCountSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("contentPadCount",query.list().get(0).toString());
            }  else {
                map.put("contentPadCount","0");
            }
            String contentPadLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (user_agent  like '%Pad%' or user_agent  like '%pad%') and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+") ";
            query = session.createSQLQuery(contentPadLengthSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("contentPadLength",query.list().get(0).toString());
            }  else {
                map.put("contentPadLength","0");
            }
            //
            String allNetFlowContentPhoneSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(allNetFlowContentPhoneSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("allNetFlowContentPhone",query.list().get(0).toString());
            }  else {
                map.put("allNetFlowContentPhone","0");
            }
            String contentPhoneCountSql = "select COUNT(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(contentPhoneCountSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("contentPhoneCount",query.list().get(0).toString());
            }  else {
                map.put("contentPhoneCount","0");
            }
            String contentPhoneLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) and content_id not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id ="+liveChannelId+")";
            query = session.createSQLQuery(contentPhoneLengthSql);
            if(!"[null]".equals(query.list().toString())) {
                map.put("contentPhoneLength",query.list().get(0).toString());
            }  else {
                map.put("contentPhoneLength","0");
            }
            //
            String wasuNetFlowSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(0)+" and content_id  not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id = "+liveChannelId+")";
            query = session.createSQLQuery(wasuNetFlowSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("wasuNetFlow",query.list().get(0).toString());
            }else{
                map.put("wasuNetFlow","0");
            }
            String wasuCountSql = "select COUNT(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(0)+" and content_id  not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id = "+liveChannelId+")";
            query = session.createSQLQuery(wasuCountSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("wasuCount",query.list().get(0).toString());
            }else{
                map.put("wasuCount","0");
            }
            String wasuLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(0)+" and content_id  not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id = "+liveChannelId+")";
            query = session.createSQLQuery(wasuLengthSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("wasuLength",query.list().get(0).toString());
            }else{
                map.put("wasuLength","0");
            }
            //
            String wasuLadongNetFlowSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_Id = 'null' and CP_ID="+cspIdList.get(0)+" and content_id  not in (select CONTENT_ID from CONTENT_CHANNEL where channel_id = "+liveChannelId+")";
            query = session.createSQLQuery(wasuLadongNetFlowSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("wasuLadongNetFlow",query.list().get(0).toString());
            }else{
                map.put("wasuLadongNetFlow","0");
            }
            //
            String vooleNetFlowSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(1);
            query = session.createSQLQuery(vooleNetFlowSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("vooleNetFlow",query.list().get(0).toString());
            }else {
                map.put("vooleNetFlow","0");
            }
            String vooleCountSql = "select COUNT(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(1);
            query = session.createSQLQuery(vooleCountSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("vooleCount",query.list().get(0).toString());
            }else {
                map.put("vooleCount","0");
            }
            String vooleLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(1);
            query = session.createSQLQuery(vooleLengthSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("vooleLength",query.list().get(0).toString());
            }else {
                map.put("vooleLength","0");
            }
            //
            String vooleLadongNetFlowSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and  START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_Id = 'null' and  CP_ID="+cspIdList.get(1);
            query = session.createSQLQuery(vooleLadongNetFlowSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("vooleLadongNetFlow",query.list().get(0).toString());
            }else {
                map.put("vooleLadongNetFlow","0");
            }
            //
            String bestvNetFlowSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(2);
            query = session.createSQLQuery(bestvNetFlowSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("bestvNetFlow",query.list().get(0).toString());
            } else{
                map.put("bestvNetFlow","0");
            }
            String bestvCountSql = "select COUNT(*) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(2);
            query = session.createSQLQuery(bestvCountSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("bestvCount",query.list().get(0).toString());
            } else{
                map.put("bestvCount","0");
            }
            String bestvLengthSql = "select SUM(Length) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and CP_ID="+cspIdList.get(2);
            query = session.createSQLQuery(bestvLengthSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("bestvLength",query.list().get(0).toString());
            } else{
                map.put("bestvLength","0");
            }

            String bestvLadongNetFlowSql = "select SUM(BYTES_SEND) FROM VISIT_LOG WHERE channel_Id != -1 and content_Id != -1 and START_TIME >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_Id = 'null' and CP_ID="+cspIdList.get(2);
            query = session.createSQLQuery(bestvLadongNetFlowSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("bestvLadongNetFlow",query.list().get(0).toString());
            } else{
                map.put("bestvLadongNetFlow","0");
            }
            String onlineUserSql = "select count(distinct(USER_ID)) from VISIT_LOG where channel_Id != -1 and content_Id != -1 and start_time >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id != 'null'";
            query = session.createSQLQuery(onlineUserSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("onlineUser",query.list().get(0).toString());
            } else {
                map.put("onlineUser","0");
            }
            String onlineUserNetFlowSql = "select SUM(bytes_send) from VISIT_LOG where channel_Id != -1 and content_Id != -1 and start_time >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+startTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id != 'null'";
            query = session.createSQLQuery(onlineUserNetFlowSql);
            if(!"[null]".equals(query.list().toString())){
                map.put("onlineUserNetFlow",query.list().get(0).toString());
            }else {
                map.put("onlineUserNetFlow","0");
            }
            return map;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }
    public List getAllTimeDataFromVisitLog(Date startDate){
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        Session session = getSession();
        try {
            String sql = "select TO_CHAR(START_TIME,'yyyy-mm-dd hh24:mi:ss') ,TO_CHAR(END_TIME,'yyyy-mm-dd hh24:mi:ss') from VISIT_LOG where channel_Id != -1 and content_Id != -1 and START_TIME >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and  START_TIME <= TO_DATE('"+startTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }
}
