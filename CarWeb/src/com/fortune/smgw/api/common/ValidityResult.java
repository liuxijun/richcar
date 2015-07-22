package com.fortune.smgw.api.common;

public class ValidityResult
{
    private int code;
    private String describe;

    public int getCode()
    {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ValidityResult() {
        this.code = 0;
        this.describe = "";
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}