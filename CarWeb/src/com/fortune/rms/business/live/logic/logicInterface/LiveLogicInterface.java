package com.fortune.rms.business.live.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.live.model.Live;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by 王明路 on 2015/2/26.
 * 直播Logic
 */
public interface LiveLogicInterface extends BaseLogicInterface<Live> {
    public static final int ERROR_CODE_INVALID_TASK_ID=-1;
    public static final int ERROR_CODE_LIVE_NOT_EXISTS=-2;
    public static final int ERROR_CODE_LIVE_HAS_STARTED=-3;
    public static final int ERROR_CODE_LIVE_HAS_STOPPED=-4;
    public List<Live> searchLive(String channels, String searchValue, PageBean pageBean);
    public List<Live> searchRecord(String channels, String searchValue, PageBean pageBean);
    public Live saveLive(Live live, List<Long> channelIdList, String channelName, String serverIp);
    public long removeLive(Live live);
    public Live loadLive(Long id);
    public String getRandomStreamName();
    public int startLive(Long taskId, Long liveId);
    public int stopLive(Long taskId, String filePath, Long liveId);
    public String getLiveURL(Long liveId);
    public Long start(Long id);
    public Long stop(Long id);
}
