package com.fortune.rms.business.publish.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 王明路 on 2014/12/17.
 * 和客户端交互的频道信息，包含一个频道和一个频道列表
 * 可能的使用方法：
 * 1 获取某节点和其子节点，channel为节点，channelList为其子节点
 * 2 获取某节点父节点，channel为父节点，channelList为其父节点的兄弟节点
 * 3 获取某节点和其兄弟节点，channel为节点，channelList为其兄弟节点
 */
public class ChannelDTO {
    private Channel channel;
    private List<Channel> channelList;
    private boolean isTop;      // 是不是顶级了

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
        Collections.sort(this.channelList, new ComparatorChannel());
    }

    public ChannelDTO() {

    }

    public class ComparatorChannel implements Comparator {
        public int compare(Object arg0, Object arg1) {
            Channel ch0 = (Channel) arg0;
            Channel ch1 = (Channel) arg1;

            //根据grade排序
            return ch0.getGrade().compareTo(ch1.getGrade());
        }

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
        return com.fortune.util.JsonUtils.getJsonString(this, "");
    }
}
