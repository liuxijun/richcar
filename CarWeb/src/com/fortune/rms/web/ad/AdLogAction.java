package com.fortune.rms.web.ad;

import com.fortune.rms.business.ad.logic.logicInterface.AdLogLogicInterface;
import com.fortune.rms.business.ad.model.AdLog;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Namespace("/ad")
@ParentPackage("default")
@Action(value="adLog")
@Results({
        @Result(name = "state",location = "/common/jsonState.jsp")
})
public class AdLogAction extends BaseAction<AdLog> {
	private static final long serialVersionUID = 3243534534534534l;
	private AdLogLogicInterface adLogLogicInterface;
	@SuppressWarnings("unchecked")
	public AdLogAction() {
		super(AdLog.class);
	}
	/**
	 * @param adLogLogicInterface the adLogLogicInterface to set
	 */
	public void setAdLogLogicInterface(AdLogLogicInterface adLogLogicInterface) {
		this.adLogLogicInterface = adLogLogicInterface;
		setBaseLogicInterface(adLogLogicInterface);
	}

    private List<Map<String,Object>> logs;
    private Date startTime;
    private Date stopTime;
    private Integer trainId;
    private String adName;
    private String contentName;
    private Long channelId;
    private Integer adId;
    private String lineName;
    public String state(){
        logs = adLogLogicInterface.stateForAd(adName,startTime,stopTime,trainId,pageBean);
        return "state";
    }
    public String listLogs(){
        logs = adLogLogicInterface.listLogs(adName,contentName,channelId,lineName,startTime,stopTime,adId,trainId,pageBean);
        return "state";
    }
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getTrainId() {
        return trainId;
    }

    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    public String getJsonState(){
        return getJsonArray(logs);
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
}
