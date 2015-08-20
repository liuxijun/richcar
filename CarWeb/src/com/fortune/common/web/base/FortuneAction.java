package com.fortune.common.web.base;

import com.fortune.util.JsonUtils;
import com.fortune.util.PageBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-10-18
 * Time: 13:48:14
 *
 */
@SuppressWarnings("unused")
public class FortuneAction  extends ActionSupport implements Preparable, SessionAware {
    protected Map<String,Object> session;
    protected Log log = LogFactory.getLog(this.getClass());
    protected String userId;
    protected PageBean pageBean = new PageBean(1, 10, null, null);
    protected String keyId;
    private boolean success = true;
    protected List<String> keys=new ArrayList<String>();
    public void prepare() {
        userId =(String) session.get("userId");
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String getListString(Collection<String> strings,String lineCR){
        if(strings==null){
            return "";
        }
        StringBuilder result=new StringBuilder();
        int i=0;
        for(String s:strings){
            if(i>0){
               result.append(lineCR); 
            }
            i++;
            result.append(s);
        }
        String r = result.toString();
        r = r.replace("\"","\\\"");
        r = r.replace("\r","\\r");
        return r;
    }


    public String getJsonList(List data){
        JsonUtils utils = new JsonUtils();
        String result = utils.getJsonArray(data);
        if ("".equals(result) || null == result) {
            result = "[]";
        }
        return result;
    }
    public int getTotalCount() {
        return pageBean.getRowCount();
    }

    @SuppressWarnings("unused")
    public void setTotalCount(int totalCount) {
        pageBean.setRowCount(totalCount);
    }

    @SuppressWarnings("unused")
    public void setLimit(int limit) {
        //log.debug("limit="+limit);
        pageBean.setPageSize(limit);
    }

    @SuppressWarnings("unused")
    public void setStart(int start) {
        //log.debug("start="+start);
        pageBean.setStartRow(start);
    }

    @SuppressWarnings("unused")
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getJsonArray(Collection datas){
        JsonUtils utils = new JsonUtils();
        String result = utils.getJsonArray(datas);
        if ("".equals(result) || null == result) {
            result = "[]";
        }
        return result;
    }

    @SuppressWarnings("unused")
    public String getJsonActionError(){
        Collection<String> errors = getActionErrors();
        if(errors==null||errors.size()<=0){
            errors = getActionMessages();
        }
        return getJsonArray(errors);
    }

    @SuppressWarnings("unused")
    public String getJsonActionMessage(){
        return getJsonArray(this.getActionMessages());
    }

    @SuppressWarnings("unused")
    public void setKeyIds(String keyIds){
        if(keyIds!=null){
            String[] ids = keyIds.split(",");
            keys.clear();
            keys.addAll(Arrays.asList(ids));
        }
    }
    @SuppressWarnings("unused")
    public String getRequestParam(String paramName , String defaultValue){
        HttpServletRequest request = ServletActionContext.getRequest();
        String value = request.getParameter(paramName);
        if (value==null||"".equals(value)){
            return defaultValue;
        } else {
            return value;
        }
    }

    @SuppressWarnings("unused")
    public int getRequestIntParam(String paramName, int defaultValue){
        HttpServletRequest request = ServletActionContext.getRequest ();
        if (request.getParameter(paramName)==null){
            return defaultValue;
        } else {
            try{
                return Integer.parseInt(request.getParameter(paramName));
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }
    @SuppressWarnings("unused")
    public void directOut(String result){
        try{
            HttpServletResponse response = ServletActionContext.getResponse ();
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Cache-Control","no-cache");
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(response.getOutputStream(),"utf-8"));

            pw.print(result);
            pw.close();
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

}
