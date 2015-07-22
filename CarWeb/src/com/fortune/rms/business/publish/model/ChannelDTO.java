package com.fortune.rms.business.publish.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ����· on 2014/12/17.
 * �Ϳͻ��˽�����Ƶ����Ϣ������һ��Ƶ����һ��Ƶ���б�
 * ���ܵ�ʹ�÷�����
 * 1 ��ȡĳ�ڵ�����ӽڵ㣬channelΪ�ڵ㣬channelListΪ���ӽڵ�
 * 2 ��ȡĳ�ڵ㸸�ڵ㣬channelΪ���ڵ㣬channelListΪ�丸�ڵ���ֵܽڵ�
 * 3 ��ȡĳ�ڵ�����ֵܽڵ㣬channelΪ�ڵ㣬channelListΪ���ֵܽڵ�
 */
public class ChannelDTO {
    private Channel channel;
    private List<Channel> channelList;
    private boolean isTop;      // �ǲ��Ƕ�����

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

            //����grade����
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
