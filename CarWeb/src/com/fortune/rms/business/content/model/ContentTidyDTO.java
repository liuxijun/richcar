package com.fortune.rms.business.content.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 王明路 on 2014/12/10.
 * 精简的内容传输对象，用于前台查询和列表中显示
 */
public class ContentTidyDTO {
    public final static int SIMILARITY_PRIORITY_HOT = 1;             // 热片优先，东京热的最前面
    public final static int SIMILARITY_PRIORITY_NEW = 2;             // 最新优先
    public final static int SIMILARITY_PRIORITY_SAME_CHANNEL = 3;  // 同栏目优先
    public final static int POWER_TITLE = 3;        // 计算相似度时标题的权重
    public final static int POWER_ACTOR = 1;        // 主创的权重

    private Long id;
    private String title;
    private String poster;
    private String actor;
    private Date createTime;
    private Double similarity;  // 和另一视频的相似度，在获取相关视频时使用
    private List<Long> channelIdList;   // 内容所属的栏目Id列表，便于和用户可以观看的栏目Id列表比对

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

    private boolean recommended;    // 是否被推荐

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
