package com.fortune.rms.business.content.model;

import com.fortune.common.business.base.model.BaseModel;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 14-9-15
 * Time: 下午6:49
 * 直播时间表
 */
public class Living extends BaseModel{
    private long id;
    private String name;
    private Long contentId;
    private Date startTime;
    private Date stopTime;
    private Long livingServerId;
    private String livingChannel;
    private String description;
    private Long publishChannelId;
    private Long cspId;
    private Long status;
    private Long recordType;
    private Long recordServerId;
    private String recordSessionId;

}
