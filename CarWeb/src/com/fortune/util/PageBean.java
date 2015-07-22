package com.fortune.util;

import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-3-31
 * Time: 11:12:04
 * 
 */
public class PageBean {
    private int pageNo;        //当前页号
    private int pageCount;     //总页数 
    private int rowCount;      //总记录数 
    private int pageSize;      //每页显示记录数 
    private int startRow;      //当前页 开始记录号
    private int endRow;
    private String orderBy;
    private String orderDir;

    public PageBean() {
        pageSize = 10;
        pageNo = 1;
        startRow = 0;
        rowCount = 0;
    }
    public PageBean(int pageNo,int pageSize,String orderBy,String orderDir){
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.orderBy = orderBy;
        this.orderDir = orderDir;
        setPageNo(pageNo);
    }

    public PageBean(String json){
        JsonUtils jsonUtils = new JsonUtils();
        PageBean bean = (PageBean)jsonUtils.getObjectFromJsonString(PageBean.class,json);
        if(bean!=null){
            this.pageNo = bean.getPageNo();
            this.pageCount = bean.getPageCount();
            this.pageSize = bean.getPageSize();
            this.orderBy = bean.getOrderBy();
            this.orderDir = bean.getOrderDir();
        }
    }

    public int getPageNo() {
        if(pageNo<=0){
            pageNo = 1;
        }
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        startRow = (pageNo - 1)*pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        if(pageSize <=0){
            pageSize = 10;
        }
        pageCount = (rowCount+pageSize-1) / pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        setPageNo(pageNo);
    }

    public int getStartRow() {
        if(startRow <0){
            startRow = 0;
        }
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        this.endRow = startRow + pageSize -1;
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public String toString(){
        JsonUtils jsonUtils = new JsonUtils();
        return jsonUtils.getJson(this);
        //return "_pageBean_"+pageNo+"_"+pageSize+"_"+rowCount+"_"+orderDir+"_"+orderDir+"_" ;
    }
}
