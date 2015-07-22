package com.fortune.util;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-7
 * Time: 20:51:22
 * ËÑË÷½á¹û
 */
public class SearchResult<E> implements Serializable{
    private int rowCount;
    private List<E> rows;

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<E> getRows() {
        return rows;
    }

    public void setRows(List<E> rows) {
        this.rows = rows;
    }
}
