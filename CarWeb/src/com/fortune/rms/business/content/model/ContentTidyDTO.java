package com.fortune.rms.business.content.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ����· on 2014/12/10.
 * ��������ݴ����������ǰ̨��ѯ���б�����ʾ
 */
public class ContentTidyDTO {
    public final static int SIMILARITY_PRIORITY_HOT = 1;             // ��Ƭ���ȣ������ȵ���ǰ��
    public final static int SIMILARITY_PRIORITY_NEW = 2;             // ��������
    public final static int SIMILARITY_PRIORITY_SAME_CHANNEL = 3;  // ͬ��Ŀ����
    public final static int POWER_TITLE = 3;        // �������ƶ�ʱ�����Ȩ��
    public final static int POWER_ACTOR = 1;        // ������Ȩ��

    private Long id;
    private String title;
    private String poster;
    private String actor;
    private Date createTime;
    private Double similarity;  // ����һ��Ƶ�����ƶȣ��ڻ�ȡ�����Ƶʱʹ��
    private List<Long> channelIdList;   // ������������ĿId�б����ں��û����Թۿ�����ĿId�б�ȶ�

    public List<Long> getChannelIdList() {
        return channelIdList;
    }

    public void setChannelIdList(List<Long> channelIdList) {
        this.channelIdList = channelIdList;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private boolean recommended;    // �Ƿ��Ƽ�

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public ContentTidyDTO() {
    }

    public ContentTidyDTO(Content content){
        if( content != null){
            id = content.getId();
            title = content.getName();
            String poster1Url = content.getPost1Url();
            if(poster1Url==null){
                poster1Url = content.getPost2Url();
                if(poster1Url==null){
                    String[] codes = new String[]{"PC_MEDIA_POSTER_BIG","PC_MEDIA_POSTER_HORIZONTAL_BIG","MEDIA_PIC_RECOM2",
                            "PHONE_MEDIA_POSTER_SMALL"};
                    Map<String,Object> properties = content.getProperties();
                    if(properties!=null){
                        for(String code:codes){
                            Object v = properties.get(code);
                            if(v!=null){
                                poster1Url = v.toString();
                                break;
                            }
                        }
                    }
                }
            }
            poster = poster1Url;
            actor = content.getActors();
            createTime = content.getCreateTime();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
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
