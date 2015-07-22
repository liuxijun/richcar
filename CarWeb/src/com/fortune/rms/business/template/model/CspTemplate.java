package com.fortune.rms.business.template.model;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 2011-6-16
 * Time: 9:47:51
 * To change this template use File | Settings | File Templates.
 */
public class CspTemplate implements java.io.Serializable {

	private long cspId;
	private Long templateId;
	private String  createor;

   private String cspName;
    public CspTemplate(){

    }

    public CspTemplate(long  cspId,String createor, Long templateId ) {
        this.cspId = cspId;
        this.createor = createor;
        this.templateId = templateId;
    }


    public String getCspName() {
        return cspName;
    }

    public void setCspName(String cspName) {
        this.cspName = cspName;
    }

    public long getCspId() {
        return cspId;
    }

    public void setCspId(long cspId) {
        this.cspId = cspId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getCreateor() {
        return createor;
    }

    public void setCreateor(String createor) {
        this.createor = createor;
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
		return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
	}


}