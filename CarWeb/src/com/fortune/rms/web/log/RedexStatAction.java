package com.fortune.rms.web.log;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.log.logic.logicImpl.VisitLogLogicImpl;
import com.fortune.rms.business.log.logic.logicInterface.VisitLogLogicInterface;
import com.fortune.rms.business.log.model.RedexStatChannelVisit;
import com.fortune.rms.business.log.model.RedexStatConcurrent;
import com.fortune.rms.business.log.model.RedexStatVisit;
import com.fortune.rms.business.log.model.VisitLog;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import com.fortune.util.net.URLEncoder;
import org.apache.struts2.convention.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by 王明路 on 2014/11/18.
 */
@Namespace("/log")
@ParentPackage("default")

public class RedexStatAction  extends BaseAction<VisitLog> {
    public RedexStatAction() {
        super(VisitLog.class);
    }

    private VisitLogLogicInterface visitLogLogicInterface;
    private ChannelLogicInterface channelLogicInterface;

    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    private Long channelId;
    private Channel channel;

    public String getDefaultName() {
        return defaultName;
    }

    private String defaultName;

    public Channel getChannel() {
        return channel;
    }

    public void setVisitLogLogicInterface(VisitLogLogicInterface visitLogLogicInterface) {
        this.visitLogLogicInterface = visitLogLogicInterface;
    }

    private Date startTime;
    private Date endTime;

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    private List<RedexStatVisit> statVisitList;
    private List<RedexStatChannelVisit> statChannelVisitList;
    private List<RedexStatConcurrent> statConcurrentList;

    public List<RedexStatConcurrent> getStatConcurrentList() {
        return statConcurrentList;
    }

    public List<RedexStatChannelVisit> getStatChannelVisitList() {
        return statChannelVisitList;
    }

    public List<RedexStatVisit> getStatVisitList() {
        return statVisitList;
    }

    /**

     * 媒体访问量统计，可能包含的统计参数包括频道范围、开始时间、结束时间及分页信息
     * @return List
     */
    @Action(value = "statVisit",results = {
            @Result(name = "statVisit",location = "/man/jsonStat.jsp")
    })
    public String contentVisitCount(){
        if(pageBean == null){
            pageBean = new PageBean();
            pageBean.setPageSize( 50 );
            pageBean.setPageNo( 1 );
        }
        statVisitList = visitLogLogicInterface.getRedexContentVisitStat(channelId, startTime, endTime, pageBean);
        setTotalCount(pageBean.getRowCount());
        return "statVisit";
    }

    /**
     * 导出媒体访问量排行数据，过滤条件同contentVisitCount
     * @return "exportVisit"
     */
    @Action(value = "exportVisit",results = {
            @Result(name = "exportVisit",location = "/man/exportVisit.jsp")
    })
    public String exportContentVisitCount() throws Exception{
        if(pageBean == null){
            pageBean = new PageBean();
            pageBean.setPageSize( 50 );
            pageBean.setPageNo( 1 );
        }
        statVisitList = visitLogLogicInterface.getRedexContentVisitStat(channelId, startTime, endTime, pageBean);
        setTotalCount(pageBean.getRowCount());
        if( channelId != null && channelId > 0 ){
            try{
                channel = channelLogicInterface.get(channelId);
            }catch (Exception e){e.printStackTrace();}
        }

        defaultName = "访问统计";
        if( startTime != null || endTime != null ){
            defaultName += "(";
            if( startTime != null ) defaultName += StringUtils.date2string(startTime, "yyyy-MM-dd");
            if( endTime != null ) {
                defaultName += "至";
                defaultName += StringUtils.date2string(startTime, "yyyy-MM-dd");
            }else{
                defaultName += "起";
            }
            defaultName += ")";
        }
        if( pageBean.getPageNo() > 1 ){
            defaultName += "_" + pageBean.getPageNo();
        }
        defaultName += ".xls";
/*
        try{
            defaultName =  URLEncoder.encode(defaultName, "UTF-8");
        }catch (Exception e){e.printStackTrace();}
*/
        //session.put("exportFileName", new String(defaultName.getBytes("GBK"), "UTF-8"));
        session.put("exportFileName", defaultName);

        return "exportVisit";
    }

    @Action(value = "statChannel",results = {
            @Result(name = "statChannel",location = "/man/jsonStatChannel.jsp")
    })
    public String channelVisitCount(){
        statChannelVisitList = visitLogLogicInterface.getRedexChannelVisitStat(startTime, endTime);
        setTotalCount( statChannelVisitList == null ? 0 : statChannelVisitList.size());
        return "statChannel";
    }

    @Action(value = "statConcurrent",results = {
            @Result(name = "statConcurrent",location = "/man/jsonStatConcurrent.jsp")
    })
    public String concurrentStat(){
        statConcurrentList = visitLogLogicInterface.getRedexConcurrentStat(startTime, endTime);
        setTotalCount( statConcurrentList == null ? 0: statConcurrentList.size());
        return "statConcurrent";
    }
}
