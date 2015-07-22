package com.fortune.rms.business.vote.model;

import java.util.List;

/**
 * Created by 王明路 on 2015/1/6.
 * 投票的结果，包括投票的基本信息
 */
public class VoteStat {
    private Long id;                    // id
    private String title;              // 标题
    private long totalTicketCount;     // 总票数
    private List<Option> optionList;  // 选项，包括得票数
    private int maxOption;


    public VoteStat() {
        totalTicketCount = 0;
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

    public int getMaxOption() {
        return maxOption;
    }

    public void setMaxOption(int maxOption) {
        this.maxOption = maxOption;
    }

    public long getTotalTicketCount() {
        return totalTicketCount;
    }

    public void setTotalTicketCount(long totalTicketCount) {
        this.totalTicketCount = totalTicketCount;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
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
