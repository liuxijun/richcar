package com.fortune.rms.business.publish.model;

import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentTidyDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王明路 on 2014/12/12.
 * 推荐信息及推荐内容列表
 */
public class RecommendDTO {
    private long id;
    private String code;
    private String name;
    private Long type;
    private Long channelId;

    public List<ContentTidyDTO> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentTidyDTO> contentList) {
        this.contentList = contentList;
    }

    private List<ContentTidyDTO> contentList;

    public RecommendDTO() {
        contentList = new ArrayList<ContentTidyDTO>();
    }

    public RecommendDTO(Recommend recommend){
        if(recommend != null) {
            id = recommend.getId();
            code = recommend.getCode();
            name = recommend.getName();
            if(name!=null){
                int p = name.indexOf(":");
                if(p>0){
                    name = name.substring(p+1);
                }
                p = name.indexOf("：");
                if(p>0){
                    name = name.substring(p+1);
                }
            }
            type = recommend.getType();
            channelId = recommend.getChannelId();
        }
        contentList = new ArrayList<ContentTidyDTO>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public void addRecommendContent(Content content){
        if( content != null ){
            addRecommendContent(new ContentTidyDTO(content));
        }
    }

    public void addRecommendContent(ContentTidyDTO content){
        if( content != null ){
            contentList.add(content);
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
