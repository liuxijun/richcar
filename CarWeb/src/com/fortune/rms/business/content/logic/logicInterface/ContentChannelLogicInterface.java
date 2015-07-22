package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentChannel;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.PageBean;

import java.util.List;

public interface ContentChannelLogicInterface
		extends
			BaseLogicInterface<ContentChannel> {
    public List<Content> list(Long cspId, Long channelId, String contentName, String directors, String actors,
                              List<ContentProperty> searchValues, PageBean pageBean);

    public List<Channel> getChannelsByContentId(Long contentId);
    public boolean setPublishStatus(long contentId, long channelId, long status);
    public boolean publishContent(long contentId, long channelId);
    public boolean unPublishContent(long contentId, long channelId);
    public boolean isExists(Long channelId, Long contentId);
    public List<Content> scanChannelContents(List<Channel> channels, PageBean exportPageBean, String[] mediaUrlPropertyCodes);
    public List<Content> scanChannelContents(Long[] channelIds, PageBean exportPageBean);
    public List<Content> exportContents(List<Channel> channels, String exportDir, PageBean pageBean, String posterDir);
    public List<Content> exportContents(Long[] channelIds, String exportDir, PageBean pageBean, String posterDir);
    public List<String> getExportLogs();
    public int getProcess();
    public long getWillCopyMediaFileSize();
    public long getWillCopyMediaFileCount();
    public long getExportedMediaFileSize();
    public  int getExportedPicFileCount();

    List<ContentChannel> getContentPublishedChannels(long contentId);
}
