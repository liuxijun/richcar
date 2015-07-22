package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-22
 * Time: 上午10:20
 * 基础标签类型描述
 */

public class Tag{
    protected String name;
    protected int size;
    protected int type;
    protected boolean mustOutPut;

    public Tag(String name, int size,int type,boolean mustOutPut) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.mustOutPut = mustOutPut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isMustOutPut() {
        return mustOutPut;
    }

    public void setMustOutPut(boolean mustOutPut) {
        this.mustOutPut = mustOutPut;
    }
}
