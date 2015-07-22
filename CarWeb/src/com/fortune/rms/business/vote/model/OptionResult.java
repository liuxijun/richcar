package com.fortune.rms.business.vote.model;

/**
 * Created by 王明路 on 2014/12/24.
 * 选项结果
 */
public class OptionResult  implements java.io.Serializable {
    public static final long RESULT_TYPE_VOTE = 1L;   // 投票
    public static final long RESULT_TYPE_INVEST = 2L;  // 问卷
    private Long id;
    private Long resultId;  // 结果Id， 投票或问卷结果Id
    private Long optionId;  // 选中的选项Id
    private Long type;       // 类型，投票结果/问卷结果

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
