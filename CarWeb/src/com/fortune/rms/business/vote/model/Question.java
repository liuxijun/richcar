package com.fortune.rms.business.vote.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 * ���⣬ͶƱ���ʾ���ʹ��
 */
public class Question  implements java.io.Serializable{
    private Long id;
    private String title;
    private Date createTime;
    private int maxOption;
    private Long sequence;      // question���ʾ��е���ţ������л�

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    private List<Option> optionList;    // ѡ���б������������ݣ������л�

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Question() {
    }

    public Question(Long id, String title, Date createTime, int maxOption) {
        this.id = id;
        this.title = title;
        this.createTime = createTime;
        this.maxOption = maxOption;
    }

    public void addOption(Option option){
        if(option == null) return;
        if(optionList == null) optionList = new ArrayList<Option>();
        optionList.add(option);
    }

    /**
     * ����ѡ��ĵ�Ʊ��
     * @param optionId  ѡ��Id
     * @param count     Ʊ��
     * @return �ҵ�ѡ���true�����򷵻�false
     */
    public boolean setOptionPoll(Long optionId, Long count){
        if( optionList == null ) return false;

        for(Option option : optionList){
            if(option.getId().equals(optionId)){
                option.setTicketCount(count); return true;
            }
        }
        return false;
    }

    /**
     * ����ѡ��Ϊѡ��״̬
     * @param optionId ѡ��Id
     * @return �Ƿ��ҵ�ѡ��
     */
    public boolean setOptionSelected(Long optionId){
        if( optionList == null ) return false;

        for(Option option : optionList){
            if(option.getId().equals(optionId)){
                option.setSelected(true); return true;
            }
        }
        return false;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getMaxOption() {
        return maxOption;
    }

    public void setMaxOption(int maxOption) {
        this.maxOption = maxOption;
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
