package com.fortune.common;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-5
 * Time: 18:47:17
 *  ���е�ҵ���������
 */
public enum OperationType {

    SAVE("SAVE"),
    SEARCH("SEARCH"),
    DELETE("DELETE"),
    UPDATE("UPDATE"),
    ADD("ADD"),
    LOGIN("LOGIN"),
    LOGINOUT("LOGOUT");

    private String name;
    private OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
