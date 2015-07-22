package com.fortune.rms.web.user;

import com.fortune.common.web.base.FortuneAction;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-6-25
 * Time: 11:56:07
 * »ù´¡Àà
 */
@SuppressWarnings("unused")
public class UnicomAction<E>  extends FortuneAction {
    protected int resultCode;
    protected String resultDesc;
    protected List<E> objs;
    private Class<E> persistentClass;
    protected E obj;
    protected PageBean pageBean;
    protected boolean success;

    public UnicomAction(Class<E> persistentClass) {

        this.persistentClass = persistentClass;
        try {
            obj = this.persistentClass.newInstance();

        } catch (InstantiationException e) {
            log.error(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public List<E> getObjs() {
        return objs;
    }

    public void setObjs(List<E> objs) {
        this.objs = objs;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public E getObj() {
        return obj;
    }

    public void setObj(E obj) {
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getJsonObjs() {
        return getJsonList(objs);
    }
    public int getTotalCount() {
        return pageBean.getRowCount();
    }

    public void setTotalCount(int totalCount) {
        pageBean.setRowCount(totalCount);
    }

    public void setLimit(int limit) {
        pageBean.setPageSize(limit);
    }

    public void setStart(int start) {
        pageBean.setStartRow(start);
    }
}
