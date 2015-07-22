package com.fortune.rms.business.log.model;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-6-24
 * Time: ÏÂÎç2:28
 * To change this template use File | Settings | File Templates.
 */
public class DailyStaticsLog implements java.io.Serializable{
    private long id;
    private Date dateStatics;
    private Long allNetFlow;
    private Long mobileNetFlow;
    private Long elseNetFlow;
    private Long allNetFlowPad;
    private Long allNetFlowPhone;
    private Long allLiveNetFlow;
    private Long allLiveNetFlowPad;
    private Long allLiveNetFlowPhone;
    private Long allContentNetFlow;
    private Long allContentNetFlowPad;
    private Long allContentNetFlowPhone;
    private Long wasuNetFlow;
    private Long vooleNetFlow;
    private Long bestvNetFlow;
    private Long wasuLadongNetFlow;
    private Long vooleLadongNetFlow;
    private Long bestvLadongNetFlow;
    private Long onlineUser;
    private Long onlineUserNetFlow;
    private String quarterBingFa;
    private String maxBingFa;
    private Date createTime;
    private Long allLength;
    private Long allCount;
    private Long mobileLength;
    private Long mobileCount;
    private Long padLength;
    private Long padCount;
    private Long phoneLength;
    private Long phoneCount;
    private Long liveLength;
    private Long liveCount;
    private Long livePadLength;
    private Long livePadCount;
    private Long livePhoneLength;
    private Long livePhoneCount;
    private Long contentLength;
    private Long contentCount;
    private Long contentPadLength;
    private Long contentPadCount;
    private Long contentPhoneLength;
    private Long contentPhoneCount;
    private Long wasuLength;
    private Long wasuCount;
    private Long vooleLength;
    private Long vooleCount;
    private Long bestvLength;
    private Long bestvCount;
    private Long elseCount;
    private Long elseLength;


    public DailyStaticsLog(){

    }

    public DailyStaticsLog(long id, Date dateStatics, Long allNetFlow, Long mobileNetFlow, Long elseNetFlow, Long allNetFlowPad,
                           Long allNetFlowPhone, Long allLiveNetFlow, Long allLiveNetFlowPad, Long allLiveNetFlowPhone,
                           Long allContentNetFlow, Long allContentNetFlowPad, Long allContentNetFlowPhone, Long wasuNetFlow,
                           Long vooleNetFlow, Long bestvNetFlow, Long wasuLadongNetFlow, Long vooleLadongNetFlow, Long bestvLadongNetFlow,
                           Long onlineUser, Long onlineUserNetFlow, String quarterBingFa, String maxBingFa, Date createTime, Long allLength,
                           Long allCount, Long mobileLength, Long mobileCount, Long padLength, Long padCount, Long phoneLength, Long phoneCount,
                           Long liveLength, Long liveCount, Long livePadLength, Long livePadCount, Long livePhoneLength, Long livePhoneCount,
                           Long contentLength, Long contentCount, Long contentPadLength, Long contentPadCount, Long contentPhoneLength,
                           Long contentPhoneCount, Long wasuLength, Long wasuCount, Long vooleLength, Long vooleCount, Long bestvLength,
                           Long bestvCount,Long elseCount,Long elseLength) {
        this.id = id;
        this.dateStatics = dateStatics;
        this.allNetFlow = allNetFlow;
        this.mobileNetFlow = mobileNetFlow;
        this.elseNetFlow = elseNetFlow;
        this.allNetFlowPad = allNetFlowPad;
        this.allNetFlowPhone = allNetFlowPhone;
        this.allLiveNetFlow = allLiveNetFlow;
        this.allLiveNetFlowPad = allLiveNetFlowPad;
        this.allLiveNetFlowPhone = allLiveNetFlowPhone;
        this.allContentNetFlow = allContentNetFlow;
        this.allContentNetFlowPad = allContentNetFlowPad;
        this.allContentNetFlowPhone = allContentNetFlowPhone;
        this.wasuNetFlow = wasuNetFlow;
        this.vooleNetFlow = vooleNetFlow;
        this.bestvNetFlow = bestvNetFlow;
        this.wasuLadongNetFlow = wasuLadongNetFlow;
        this.vooleLadongNetFlow = vooleLadongNetFlow;
        this.bestvLadongNetFlow = bestvLadongNetFlow;
        this.onlineUser = onlineUser;
        this.onlineUserNetFlow = onlineUserNetFlow;
        this.quarterBingFa = quarterBingFa;
        this.maxBingFa = maxBingFa;
        this.createTime = createTime;
        this.allLength = allLength;
        this.allCount = allCount;
        this.mobileLength = mobileLength;
        this.mobileCount = mobileCount;
        this.padLength = padLength;
        this.padCount = padCount;
        this.phoneLength = phoneLength;
        this.phoneCount = phoneCount;
        this.liveLength = liveLength;
        this.liveCount = liveCount;
        this.livePadLength = livePadLength;
        this.livePadCount = livePadCount;
        this.livePhoneLength = livePhoneLength;
        this.livePhoneCount = livePhoneCount;
        this.contentLength = contentLength;
        this.contentCount = contentCount;
        this.contentPadLength = contentPadLength;
        this.contentPadCount = contentPadCount;
        this.contentPhoneLength = contentPhoneLength;
        this.contentPhoneCount = contentPhoneCount;
        this.wasuLength = wasuLength;
        this.wasuCount =wasuCount;
        this.vooleLength = vooleLength;
        this.vooleCount = vooleCount;
        this.bestvLength = bestvLength;
        this.bestvCount = bestvCount;
        this.elseCount = elseCount ;
        this.elseLength  = elseLength;
    }

    public DailyStaticsLog(long id){
        this.id = id;
    }

    public Long getElseCount() {
        return elseCount;
    }

    public void setElseCount(Long elseCount) {
        this.elseCount = elseCount;
    }

    public Long getElseLength() {
        return elseLength;
    }

    public void setElseLength(Long elseLength) {
        this.elseLength = elseLength;
    }

    public Long getAllCount() {
        return allCount;
    }

    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

    public Long getMobileLength() {
        return mobileLength;
    }

    public void setMobileLength(Long mobileLength) {
        this.mobileLength = mobileLength;
    }

    public Long getMobileCount() {
        return mobileCount;
    }

    public void setMobileCount(Long mobileCount) {
        this.mobileCount = mobileCount;
    }

    public Long getPadLength() {
        return padLength;
    }

    public void setPadLength(Long padLength) {
        this.padLength = padLength;
    }

    public Long getPadCount() {
        return padCount;
    }

    public void setPadCount(Long padCount) {
        this.padCount = padCount;
    }

    public Long getPhoneLength() {
        return phoneLength;
    }

    public void setPhoneLength(Long phoneLength) {
        this.phoneLength = phoneLength;
    }

    public Long getPhoneCount() {
        return phoneCount;
    }

    public void setPhoneCount(Long phoneCount) {
        this.phoneCount = phoneCount;
    }

    public Long getLiveLength() {
        return liveLength;
    }

    public void setLiveLength(Long liveLength) {
        this.liveLength = liveLength;
    }

    public Long getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(Long liveCount) {
        this.liveCount = liveCount;
    }

    public Long getLivePadLength() {
        return livePadLength;
    }

    public void setLivePadLength(Long livePadLength) {
        this.livePadLength = livePadLength;
    }

    public Long getLivePadCount() {
        return livePadCount;
    }

    public void setLivePadCount(Long livePadCount) {
        this.livePadCount = livePadCount;
    }

    public Long getLivePhoneLength() {
        return livePhoneLength;
    }

    public void setLivePhoneLength(Long livePhoneLength) {
        this.livePhoneLength = livePhoneLength;
    }

    public Long getLivePhoneCount() {
        return livePhoneCount;
    }

    public void setLivePhoneCount(Long livePhoneCount) {
        this.livePhoneCount = livePhoneCount;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public Long getContentCount() {
        return contentCount;
    }

    public void setContentCount(Long contentCount) {
        this.contentCount = contentCount;
    }

    public Long getContentPadLength() {
        return contentPadLength;
    }

    public void setContentPadLength(Long contentPadLength) {
        this.contentPadLength = contentPadLength;
    }

    public Long getContentPadCount() {
        return contentPadCount;
    }

    public void setContentPadCount(Long contentPadCount) {
        this.contentPadCount = contentPadCount;
    }

    public Long getContentPhoneLength() {
        return contentPhoneLength;
    }

    public void setContentPhoneLength(Long contentPhoneLength) {
        this.contentPhoneLength = contentPhoneLength;
    }

    public Long getContentPhoneCount() {
        return contentPhoneCount;
    }

    public void setContentPhoneCount(Long contentPhoneCount) {
        this.contentPhoneCount = contentPhoneCount;
    }

    public Long getWasuLength() {
        return wasuLength;
    }

    public void setWasuLength(Long wasuLength) {
        this.wasuLength = wasuLength;
    }

    public Long getWasuCount() {
        return wasuCount;
    }

    public void setWasuCount(Long wasuCount) {
        this.wasuCount = wasuCount;
    }

    public Long getVooleLength() {
        return vooleLength;
    }

    public void setVooleLength(Long vooleLength) {
        this.vooleLength = vooleLength;
    }

    public Long getVooleCount() {
        return vooleCount;
    }

    public void setVooleCount(Long vooleCount) {
        this.vooleCount = vooleCount;
    }

    public Long getBestvLength() {
        return bestvLength;
    }

    public void setBestvLength(Long bestvLength) {
        this.bestvLength = bestvLength;
    }

    public Long getBestvCount() {
        return bestvCount;
    }

    public void setBestvCount(Long bestvCount) {
        this.bestvCount = bestvCount;
    }

    public Long getAllLength() {
        return allLength;
    }

    public void setAllLength(Long allLength) {
        this.allLength = allLength;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateStatics() {
        return dateStatics;
    }

    public void setDateStatics(Date dateStatics) {
        this.dateStatics = dateStatics;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Long getAllNetFlow() {
        return allNetFlow;
    }

    public void setAllNetFlow(Long allNetFlow) {
        this.allNetFlow = allNetFlow;
    }

    public Long getAllLiveNetFlow() {
        return allLiveNetFlow;
    }

    public void setAllLiveNetFlow(Long allLiveNetFlow) {
        this.allLiveNetFlow = allLiveNetFlow;
    }

    public Long getAllContentNetFlow() {
        return allContentNetFlow;
    }

    public void setAllContentNetFlow(Long allContentNetFlow) {
        this.allContentNetFlow = allContentNetFlow;
    }

    public Long getVooleNetFlow() {
        return vooleNetFlow;
    }

    public void setVooleNetFlow(Long vooleNetFlow) {
        this.vooleNetFlow = vooleNetFlow;
    }

    public Long getWasuNetFlow() {
        return wasuNetFlow;
    }

    public void setWasuNetFlow(Long wasuNetFlow) {
        this.wasuNetFlow = wasuNetFlow;
    }

    public Long getBestvNetFlow() {
        return bestvNetFlow;
    }

    public void setBestvNetFlow(Long bestvNetFlow) {
        this.bestvNetFlow = bestvNetFlow;
    }

    public Long getOnlineUser() {
        return onlineUser;
    }

    public void setOnlineUser(Long onlineUser) {
        this.onlineUser = onlineUser;
    }

    public Long getOnlineUserNetFlow() {
        return onlineUserNetFlow;
    }

    public void setOnlineUserNetFlow(Long onlineUserNetFlow) {
        this.onlineUserNetFlow = onlineUserNetFlow;
    }

    public String getQuarterBingFa() {
        return quarterBingFa;
    }

    public void setQuarterBingFa(String quarterBingFa) {
        this.quarterBingFa = quarterBingFa;
    }

    public String getMaxBingFa() {
        return maxBingFa;
    }

    public void setMaxBingFa(String maxBingFa) {
        this.maxBingFa = maxBingFa;
    }
    public Long getWasuLadongNetFlow() {
        return wasuLadongNetFlow;
    }

    public void setWasuLadongNetFlow(Long wasuLadongNetFlow) {
        this.wasuLadongNetFlow = wasuLadongNetFlow;
    }

    public Long getVooleLadongNetFlow() {
        return vooleLadongNetFlow;
    }

    public void setVooleLadongNetFlow(Long vooleLadongNetFlow) {
        this.vooleLadongNetFlow = vooleLadongNetFlow;
    }

    public Long getBestvLadongNetFlow() {
        return bestvLadongNetFlow;
    }

    public void setBestvLadongNetFlow(Long bestvLadongNetFlow) {
        this.bestvLadongNetFlow = bestvLadongNetFlow;
    }

    public Long getAllContentNetFlowPhone() {
        return allContentNetFlowPhone;
    }

    public void setAllContentNetFlowPhone(Long allContentNetFlowPhone) {
        this.allContentNetFlowPhone = allContentNetFlowPhone;
    }

    public Long getAllNetFlowPad() {
        return allNetFlowPad;
    }

    public void setAllNetFlowPad(Long allNetFlowPad) {
        this.allNetFlowPad = allNetFlowPad;
    }

    public Long getAllNetFlowPhone() {
        return allNetFlowPhone;
    }

    public void setAllNetFlowPhone(Long allNetFlowPhone) {
        this.allNetFlowPhone = allNetFlowPhone;
    }

    public Long getAllLiveNetFlowPad() {
        return allLiveNetFlowPad;
    }

    public void setAllLiveNetFlowPad(Long allLiveNetFlowPad) {
        this.allLiveNetFlowPad = allLiveNetFlowPad;
    }

    public Long getAllLiveNetFlowPhone() {
        return allLiveNetFlowPhone;
    }

    public void setAllLiveNetFlowPhone(Long allLiveNetFlowPhone) {
        this.allLiveNetFlowPhone = allLiveNetFlowPhone;
    }

    public Long getAllContentNetFlowPad() {
        return allContentNetFlowPad;
    }

    public void setAllContentNetFlowPad(Long allContentNetFlowPad) {
        this.allContentNetFlowPad = allContentNetFlowPad;
    }

    public Long getMobileNetFlow() {
        return mobileNetFlow;
    }

    public void setMobileNetFlow(Long mobileNetFlow) {
        this.mobileNetFlow = mobileNetFlow;
    }

    public Long getElseNetFlow() {
        return elseNetFlow;
    }

    public void setElseNetFlow(Long elseNetFlow) {
        this.elseNetFlow = elseNetFlow;
    }

    /**
     * toString return json format string of this bean
     * @return String
     */
    public String toString() {
        return com.fortune.util.JsonUtils.getJsonString(this);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getSimpleJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, null);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getObjJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
    }
}

