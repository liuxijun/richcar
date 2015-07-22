package com.fortune.rms.business.csp.model;

/**
 * Created by IntelliJ IDEA.
 * User: wang
 * Date: 13-1-29
 * Time: ÉÏÎç9:24
 */
public class CspChannel {
    private long id;
    private Long cspId;
    private Long channelId;



    public CspChannel() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCspId() {
        return cspId;
    }

    public void setCspId(Long cspId) {
        this.cspId = cspId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public CspChannel(long id, Long cspId, Long channelId) {
        this.id = id;
        this.cspId = cspId;
        this.channelId = channelId;
    }
}
