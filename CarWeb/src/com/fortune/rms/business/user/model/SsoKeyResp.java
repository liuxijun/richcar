package com.fortune.rms.business.user.model;

public class SsoKeyResp {
    private int seqNo = 0;
    private int result = 0;
    private String resultDesc;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public SsoKeyResp(int seqNo, int result, String resultDesc) {
        this.seqNo = seqNo;
        this.result = result;
        this.resultDesc = resultDesc;
    }

    public SsoKeyResp() {
    }
}
