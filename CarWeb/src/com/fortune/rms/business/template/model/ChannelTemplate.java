package com.fortune.rms.business.template.model;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 2011-6-16
 * Time: 10:10:22
 * ÆµµÀÄ£°å
 */
public class ChannelTemplate {
    private long channelId;
    private String createor;
    private Long indexTemplate;
    private Long listTemplate;
    private Long detailTemplate;


    public ChannelTemplate() {

    }

    public ChannelTemplate(long channelId, String createor, Long indexTemplate, Long listTemplate, Long detailTemplate) {
        this.channelId = channelId;
        this.createor = createor;
        this.indexTemplate = indexTemplate;
        this.listTemplate = listTemplate;
        this.detailTemplate = detailTemplate;
    }

 

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getCreateor() {
        return createor;
    }

    public void setCreateor(String createor) {
        this.createor = createor;
    }

    public Long getIndexTemplate() {
        return indexTemplate;
    }

    public void setIndexTemplate(Long indexTemplate) {
        this.indexTemplate = indexTemplate;
    }

    public Long getListTemplate() {
        return listTemplate;
    }

    public void setListTemplate(Long listTemplate) {
        this.listTemplate = listTemplate;
    }

    public Long getDetailTemplate() {
        return detailTemplate;
    }

    public void setDetailTemplate(Long detailTemplate) {
        this.detailTemplate = detailTemplate;
    }

    /**
     * toString return json format string of this bean
     *
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
        return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
    }

}
