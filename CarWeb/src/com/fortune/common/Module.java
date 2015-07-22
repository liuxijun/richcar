package com.fortune.common;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-5
 * Time: 18:43:12
 * 系统模块定义
 */
public enum Module {
    SYSTEM_ADMIN("SysAdmin");
    private String name;
    private Module(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
