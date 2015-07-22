package com.fortune.rms.business.portal.model;

import com.fortune.rms.business.content.model.ContentTidyDTO;

import java.util.Date;

/**
 * Created by 王明路 on 2015/1/20.
 * Redex使用的收藏类型
 */
public class RedexFavorite {
    private ContentTidyDTO contentDTO;
    private Long favoriteId;
    private Date favoriteTime;

    public RedexFavorite() {
    }

    public RedexFavorite(UserFavorites favorites){
        if(favorites != null){
            favoriteId = favorites.getId();
            favoriteTime = favorites.getTime();
        }
    }

    public ContentTidyDTO getContentDTO() {
        return contentDTO;
    }

    public void setContentDTO(ContentTidyDTO contentDTO) {
        this.contentDTO = contentDTO;
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Date getFavoriteTime() {
        return favoriteTime;
    }

    public void setFavoriteTime(Date favoriteTime) {
        this.favoriteTime = favoriteTime;
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
