package com.fortune.rms.business.vote.model;

/**
 * Created by ����· on 2014/12/24.
 * ѡ����
 */
public class OptionResult  implements java.io.Serializable {
    public static final long RESULT_TYPE_VOTE = 1L;   // ͶƱ
    public static final long RESULT_TYPE_INVEST = 2L;  // �ʾ�
    private Long id;
    private Long resultId;  // ���Id�� ͶƱ���ʾ���Id
    private Long optionId;  // ѡ�е�ѡ��Id
    private Long type;       // ���ͣ�ͶƱ���/�ʾ���

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public OptionResult(Long id, Long resultId, Long optionId) {
        this.id = id;
        this.resultId = resultId;
        this.optionId = optionId;
    }

    public OptionResult() {

    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
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
