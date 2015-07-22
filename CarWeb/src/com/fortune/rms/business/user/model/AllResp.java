package com.fortune.rms.business.user.model;

import java.util.List;


public class AllResp<E> {
    private Integer resultCode;
    private String reason;
    private String url;
    private List<E> list;

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AllResp(){
    }

    public AllResp(Integer resultCode,String reason){
        this.resultCode = resultCode;
        this.reason = reason;
    }

    public AllResp(Integer resultCode,String reason, List<E> list){
        this.resultCode = resultCode;
        this.reason = reason;
        this.list = list;
    }

    public AllResp(Integer resultCode,String reason,String url){
        this.resultCode = resultCode;
        this.reason = reason;
        this.url = url;
    }

    public AllResp(Integer resultCode){
        this.resultCode = resultCode;
    }

    public Integer getTotalCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    @SuppressWarnings("unused")
    public void setTotalCount(Integer totalCount) {
        
    }

    public boolean getSuccess(){
        return (resultCode!=null&&resultCode==1);
    }
}
