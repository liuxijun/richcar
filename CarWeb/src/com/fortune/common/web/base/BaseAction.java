package com.fortune.common.web.base;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.Constants;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.SystemLog;
import com.fortune.util.*;
import com.fortune.util.net.URLEncoder;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

public class BaseAction<E> extends FortuneAction {
    private static final long serialVersionUID = 3432154234535234566l;
    protected Admin admin;
    private Class<E> persistentClass;
    protected E obj;
    protected BaseLogicInterface<E> baseLogicInterface;
    protected SystemLogLogicInterface systemLogLogicInterface;
    @SuppressWarnings("unchecked")
    protected List<E> objs;
    private String nextUrl;


    public BaseAction(Class<E> persistentClass) {

        this.persistentClass = persistentClass;
        try {
            obj = this.persistentClass.newInstance();

        } catch (InstantiationException e) {
            log.error(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void setBaseLogicInterface(BaseLogicInterface<E> baseLogicInterface) {
        this.baseLogicInterface = baseLogicInterface;

    }

    public void setSystemLogLogicInterface(SystemLogLogicInterface systemLogLogicInterface) {
        this.systemLogLogicInterface = systemLogLogicInterface;
    }

    @SuppressWarnings("unused")
    public String getNextUrl() {
        return nextUrl;
    }

    @SuppressWarnings("unused")
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    @SuppressWarnings("unused")
    public Integer getKeyId() {
        return keyId;
    }

    @SuppressWarnings("unused")
    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    @SuppressWarnings("unused")
    public List<String> getKeys() {
        return keys;
    }

    @SuppressWarnings("unused")
    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    @SuppressWarnings("unused")
    public E getObj() {
        return obj;
    }

    @SuppressWarnings("unused")
    public void setObj(E obj) {
        this.obj = obj;
    }

    @SuppressWarnings("unused")
    public List<E> getObjs() {
        return objs;
    }

    @SuppressWarnings("unused")
    public void setObjs(List<E> objs) {
        this.objs = objs;
    }

    /**
     * This method is called to allow the action to prepare itself.
     */
    public void prepare() {
/*        try {
            userSpId = Long.valueOf((String) ServletActionContext.getContext()
                    .getSession().get(Constants.SESSION_USER_SPID));
            userId = (Long) ServletActionContext.getContext()
                    .getSession().get(Constants.SESSION_USER_ID);
        } catch (NumberFormatException e) {
            log.error("BaseAction中prepare时，试图获取Session中数据发生异常，可能没有登录信息：" + e.getMessage());
        }*/
        admin = (Admin) ServletActionContext.getContext().getSession().get(Constants.SESSION_ADMIN);

    }

    protected String guessName(E ob){
        if(ob==null){
            return null;
        }
        String result = ob.getClass().getSimpleName();
        String[] guessNameProperty = new String[]{"name","title","realname","logInfo","mediaName","contentName","channelName"};
        for(String nameProperty:guessNameProperty){
            Object nameObj = BeanUtils.getProperty(ob,nameProperty);
            if(nameObj!=null){
                result += ":"+nameObj.toString();
                break;
            }
        }
        String[] guessIdProperty = new String[]{"id","operatorid"};
        for(String idProperty:guessIdProperty){
            Object idObj = BeanUtils.getProperty(ob,idProperty);
            if(idObj!=null){
                result += "(主键"+idObj.toString()+")";
                break;
            }
        }
        if(!"".equals(result)){
            return result;
        }
        return ob.getClass().getSimpleName()+":"+ob.toString();
    }

    public String getRemoteAddr(){
        ActionContext ctx = ServletActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        return request.getRemoteHost();
    }
    @SuppressWarnings("unused")
    protected void writeSysLog(String content){
///*
        String clientIp = getRemoteAddr();
        SystemLog sysLog = new SystemLog();
        sysLog.setAdminIp(clientIp);
        if(content == null){
            content = "";
        }
        sysLog.setLog(content);
        if(admin!=null){
            sysLog.setAdminId(admin.getId().longValue());
        }else{
            sysLog.setAdminId(1L);
        }
        sysLog.setLogTime(new Date());
        sysLog.setSystemLogAction(this.getClass().getSimpleName());
        try {
            if(systemLogLogicInterface==null){
                ActionContext ctx = ServletActionContext.getContext();
                HttpServletRequest request = (HttpServletRequest) ctx
                        .get(ServletActionContext.HTTP_REQUEST);
                systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface");
                        //ctx);
            }
            systemLogLogicInterface.save(sysLog);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("无法保存系统操作日志，有可能是日志过长！最大长度：1000，当前长度：" +
                    content.length()+
                    "，错误信息："+e.getMessage());
        }
//*/
    }
/*
    protected void log(Module module, OperationType operationType,
                       String content) {
        ActionContext ctx = ServletActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        String username = (String) ctx.getSession().get(Constants.SESSION_USER_NAME);
        String orgName = (String) ctx.getSession().get(Constants.SESSION_ORG_NAME);
        String clientIp = request.getRemoteHost();
        this.getLogService().log(module.getName(), operationType.getName(),
                content, username, orgName, clientIp);
    }

    private LogService getLogService() {
        ActionContext ctx = ServletActionContext.getContext();
        return (LogService) WebApplicationContextUtils
                .getWebApplicationContext(
                        (ServletContext) ctx
                                .get(ServletActionContext.SERVLET_CONTEXT))
                .getBean("logService");
    }

*/
    @SuppressWarnings("unused")
    public String getOrderBy() {
        return pageBean.getOrderBy();
    }

    @SuppressWarnings("unused")
    public void setOrderBy(String orderBy) {
        setSort(orderBy);
    }

    public void setSort(String orderBy) {

        if(orderBy!=null&&!"".equals(orderBy)){
            if(!orderBy.contains(".")){
                orderBy = orderBy.replace("_DOT_",".");
            }
            if(!orderBy.contains(".")){
                orderBy = "o1."+orderBy;
            }
        }
        this.pageBean.setOrderBy(orderBy);
    }

    @SuppressWarnings("unused")
    public String getOrderDir() {
        return pageBean.getOrderDir();
    }

    public void setOrderDir(String orderDir) {
        this.pageBean.setOrderDir(orderDir);
    }

    @SuppressWarnings("unused")
    public void setDir(String orderDir) {
        setOrderDir(orderDir);
    }

    @SuppressWarnings("unused")
    public int getStartNum() {
        return pageBean.getStartRow();
    }

    @SuppressWarnings("unused")
    public PageBean getPageBean() {
        return pageBean;
    }

    @SuppressWarnings("unused")
    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    @SuppressWarnings("unused")
    public int getEndNum() {
        return pageBean.getRowCount();
    }

    /**
     * @param session the session to set
     */
    @SuppressWarnings("unchecked")
    public void setSession(Map session) {
        this.session = session;
    }

    @SkipValidation
    @SuppressWarnings("unchecked")
    public String search() {
        log.debug("in search");
        objs = baseLogicInterface.search(obj, pageBean, admin);
        return Constants.ACTION_LIST;
    }

    public String save() {
        log.debug("in save");
        try {
            scanUploadFiles();
            obj = baseLogicInterface.save(obj);
            writeSysLog("保存"+guessName(obj));
            super.addActionMessage("成功保存数据！");
        } catch (Exception e) {
            super.addActionError("保存数据发生异常：" + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return Constants.ACTION_SAVE;
    }

    public String update() {

        return Constants.ACTION_UPDATE;
    }

    public String add() {
        return Constants.ACTION_ADD;
    }

    @SkipValidation
    public String view() {
        log.debug("in view");
        try {
            if (keyId != null) {
                log.debug("准备获取数据,主键为：" + keyId);
                obj = getBaseObject(keyId);
            }
        } catch (Exception e) {
            log.error("BaseAction中getBaseObject时，试图获取Bean发生异常：" + e.getMessage());
        }
        return Constants.ACTION_VIEW;
    }

    @SkipValidation
    public String delete() {
        log.debug("in delete");
        E ob=null;
        try {
            ob = getBaseObject(keyId) ;
            writeSysLog("删除"+guessName(ob));
            baseLogicInterface.remove(ob);
            super.addActionMessage("成功删除数据！");
        } catch (Exception e) {
            writeSysLog("删除数据'" +guessName(ob)+
                    "'发生异常：" + e.getMessage());
            super.addActionError("删除数据发生异常：" + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return Constants.ACTION_DELETE;
    }

    @SkipValidation
    @SuppressWarnings("unused")
    public String deleteSelected() {
        log.debug("in deleteSelected");
        String dealMessage = "";
        if(keys!=null){
            for (String aKey : this.keys) {
                try {
                    E ob = getBaseObject(aKey) ;
                    writeSysLog("删除"+guessName(ob));
                    baseLogicInterface.remove(ob);
                    if (!"".equals(dealMessage)) {
                        dealMessage += ",";
                    }
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("无法删除数据" + persistentClass.getName() +
                            ":" + aKey);
                }
            }
        }
        if ("".equals(dealMessage)) {
            super.addActionError("没有删除任何数据");
            setSuccess(false);
        } else {
            super.addActionMessage("已经成功删除选择的数据(" + dealMessage + ")");
            setSuccess(true);
        }

//        baseLogicInterface.remove(keyId);
        return Constants.ACTION_DELETE;
    }

    @SkipValidation
    public String list() {
        return search();
    }

    @SuppressWarnings("unused")
    public String listAll(){
        objs = baseLogicInterface.search(obj, pageBean);
        return Constants.ACTION_LIST;
    }

    @SkipValidation
    public String execute() {
        log.debug("in execute");
        // baseLogicInterface.save(obj);
        return Constants.ACTION_VIEW;
    }

    protected List<String> dealResults;

    @SuppressWarnings("unused")
    public List<String> getDealResults() {
        return dealResults;
    }

    @SuppressWarnings("unused")
    public void setDealResults(List<String> dealResults) {
        this.dealResults = dealResults;
    }

    /**
     * 因为采用<E,PK>的模板方式，对KeyId的赋值，会被自动转成String类型。因此需要我们在这里对这个值
     * 进行检查！如果是字符串，那么如果数据是字符串主键无所谓，如果是整型主键，则手工处理一下！
     *
     * @param keyId 主键
     * @return      对象
     */
    public E getBaseObject(Object keyId) {
        if (baseLogicInterface.isKeyPropertyString()) {
            return baseLogicInterface.get(keyId.toString());
        } else if(baseLogicInterface.isKeyPropertyInteger()){
            Integer key = new Integer(keyId.toString());
            return baseLogicInterface.get(key);
        }else{
            Long key = new Long(keyId.toString());
            return baseLogicInterface.get(key);
        }

        /*
        if (keyId instanceof String) {
            if (baseLogicInterface.isKeyPropertyString()) {
                return baseLogicInterface.get(keyId.toString());
            } else {
                Integer key = new Integer(keyId.toString());
                return baseLogicInterface.get(key);
            }
        } else {
            Integer key = new Integer(keyId.toString());
            return baseLogicInterface.get(key);
        }
        */

    }

    @SuppressWarnings("unused")
    public String getJsonObjs() {
        String properties = getRequestParam("properties",null);
        if(properties!=null&&!"".equals(properties.trim())){
            String[] propertyIds = properties.split(",");
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(Object o:objs){
                Map<String,Object> m = new HashMap<String,Object>();
                for(String p:propertyIds){
                    m.put(p, BeanUtils.getProperty(o, p));
                }
                list.add(m);
            }
            return getJsonArray(list);
        }else{
            return getJsonArray(objs);
        }
    }

    /*
        String sqlTable = "select m.mediaId,m.name,m.length,m.fromSource,m.createTime,m.pubFlag,c.name,d.name " +
                " from " +
                "com.fortune.redex.business.media.model.Media m," +
                "com.fortune.redex.business.media.model.Channel c," +
                "com.fortune.redex.business.system.model.Device d " +
                " where m.channelId=c.channelId and m.deviceId=d.deviceId ";
        String[][] params = new String[][]{
                  数据字段          页面控件名      赋值符   like字符串
                {"m.name",          "",             "like","%?%"},
                {"m.channelId",     "",             "like","?%"},
                {"m.deviceId",      "",             "=",    ""},
                {"m.fromSource",    "",             "=",    ""},
                {"m.onlineDate",    "startDate",    ">=",   ""},
                {"m.onlineDate",    "endDate",      "<=",   ""},
                {"m.pubFlag",        "",                "=",    ""}
        };
    */
    @SuppressWarnings("unused,unchecked,redundant")
    public SearchResult<Object[]> searchObjects(String sqlTable,String whereParams[][]){
        HashMap<String,String> tables = new HashMap<String,String>();

        if (sqlTable!=null && !"".equals(sqlTable)){
            String tempStr = new String(sqlTable.getBytes());
            tempStr = tempStr.replace(',',' ');
            String sqlTables[] = tempStr.split(" ");
            for (int i=0; i<sqlTables.length; i++){
                if (i>0){
                    tables.put(sqlTables[i],sqlTables[i-1]);
                }
            }
        }

        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSqlStr(sqlTable);
        if (whereParams!=null){
            for (String[] param : whereParams) {
                if (param.length==5 && !"".equals(param[4])){
                    String tempStr = new String(param[4].getBytes());
                    tempStr = tempStr.replace(',',' ');
                    String sqlTables[] = tempStr.split(" ");
                    for (int i=0; i<sqlTables.length; i++){
                        if (i>0){
                            tables.put(sqlTables[i],sqlTables[i-1]);
                        }
                    }
                }


                String tableName = param[0].substring(0, param[0].indexOf('.'));
                tableName = tables.get(tableName);
                String propertyName = param[0].substring(param[0].indexOf('.') + 1, param[0].length());
                String inputParamName = param[1];
                if ("".equals(param[1])) {
                    inputParamName = param[0].replace('.','_');
                }
                String inputParamValue = getRequestParam(inputParamName, "");
                if (!"".equals(inputParamValue)) {
                    try {
                        ClassMetadata cm = this.baseLogicInterface.getClassMetadata(Class.forName(tableName).newInstance());
                        Type propertyType = cm.getPropertyType(propertyName);
                        Object pValue = null;
                        if (propertyType instanceof LongType) { //Hibernate.LONG;
                            pValue = new Long(inputParamValue);
                        } else if (propertyType instanceof IntegerType) { //Hibernate.INTEGER;
                            pValue = new Integer(inputParamValue);
                        } else if (propertyType instanceof BigDecimalType) {
                            pValue = new BigDecimal(inputParamValue);
                        } else if (propertyType instanceof DateType || propertyType instanceof TimestampType) {
                            String dateFormat = "yyyy-MM-dd HH:mm:ss";
                            if (inputParamValue.length() == 10) {
                                //dateFormat = "yyyy-MM-dd";
                                inputParamValue += " " + param[3];
                            }
                            Date date = StringUtils.string2date(inputParamValue, dateFormat);
                            pValue = new Timestamp(date.getTime());
                        } else if (propertyType instanceof StringType) {
                            pValue = inputParamValue;
                        } else if (propertyType instanceof BigIntegerType) {
                            pValue = new BigInteger(inputParamValue);
                        }

                        if (param.length==5 && !"".equals(param[4])){
                            String hql = param[4];
                            hql = hql.replaceFirst("[?]",param[0] + " " + param[2] + " ?");
                            if ("like".equals(param[2])) {
                                searchCondition.appendAndSqlCondition( hql, param[3].replaceFirst("[?]", "" + pValue), propertyType);
                            } else {
                                searchCondition.appendAndSqlCondition( hql, pValue, propertyType);
                            }
                        }else{
                            if ("like".equals(param[2])) {
                                searchCondition.appendAndSqlCondition(param[0] + " " + param[2] + " ?", param[3].replaceFirst("[?]", "" + pValue), propertyType);
                            } else {
                                searchCondition.appendAndSqlCondition(param[0] + " " + param[2] + "?", pValue, propertyType);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        String sql = searchCondition.getSqlStr();
        Object[] params = searchCondition.getObjectArrayParamValues();
        Type[] paramTypes = searchCondition.getTypeArray();

        try{
            int count = HibernateUtils.findCount(this.baseLogicInterface.getSession(),sql, params, paramTypes);

            int startNo = getRequestIntParam("start",0);
            int pageSize = getRequestIntParam("limit",10);
            String orderBy = getRequestParam("sort","");
            String orderDir = getRequestParam("dir","");
            if (!"".equals(orderBy) && !"".equals(orderDir)){
                orderBy = orderBy.replace('_','.');
                sql += " order by "+ orderBy + " "+ orderDir;
            }

            List list1 = HibernateUtils.findList(this.baseLogicInterface.getSession(),sql, params, paramTypes,startNo,pageSize);
            SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
            searchResult.setRowCount(count);
            searchResult.setRows(list1);

            return searchResult;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unused")
    public String searchObjectsJbon(String sqlTable,SearchResult<Object[]> searchResult){

        //取得列头
        String columns[];
        if (sqlTable.contains(" from ")){
            sqlTable = sqlTable.substring(0,sqlTable.indexOf(" from "));
        }
        sqlTable = sqlTable.substring(6,sqlTable.length());
        sqlTable = sqlTable.replace('.','_');
        sqlTable = sqlTable.trim();
        columns = sqlTable.split(",");

        String result = "{ \"totalCount\":\""+searchResult.getRowCount()+"\", \"objs\":[";

        List list1 = searchResult.getRows();
        if (list1!=null && list1.size()>0){
            for (Object aList1 : list1) {
                result += "{";
                Object[] obj = (Object[]) aList1;
                for (int j = 0; j < columns.length; j++) {
                    String column = columns[j];
                    if (obj[j] == null) {
                        result += "\"" + column + "\":\"\",";
                    } else {
                        if (obj[j] instanceof Date) {
                            result += "\"" + column + "\":\"" + StringUtils.date2string((Date) obj[j]) + "\",";
                        } else if (obj[j] instanceof String) {
                            String tempStr = String.valueOf(obj[j]);
                            tempStr = tempStr.replaceAll("\"", "\\\\\"");
                            result += "\"" + column + "\":\"" + tempStr + "\",";
                        } else {
                            result += "\"" + column + "\":\"" + obj[j] + "\",";
                        }
                    }
                }
                result = result.substring(0, result.length() - 1);
                result += "},";
            }
            result = result.substring(0,result.length()-1);
        }
        result += "]}";
        return result;

    }
    public String searchObjectsJbon(String columnNames[],SearchResult<Object[]> searchResult){

        String result = "{ \"totalCount\":\""+searchResult.getRowCount()+"\", \"objs\":[";

        List list1 = searchResult.getRows();
        if (list1!=null && list1.size()>0){
            for (Object aList1 : list1) {
                result += "{";
                Object[] obj = (Object[]) aList1;
                for (int j = 0; j < columnNames.length; j++) {
                    String column = columnNames[j];
                    if (obj[j] == null) {
                        result += "\"" + column + "\":\"\",";
                    } else {
                        if (obj[j] instanceof Date) {
                            result += "\"" + column + "\":\"" + StringUtils.date2string((Date) obj[j]) + "\",";
                        } else if (obj[j] instanceof String) {
                            String tempStr = String.valueOf(obj[j]);
                            tempStr = tempStr.replaceAll("\"", "\\\\\"");
                            result += "\"" + column + "\":\"" + tempStr + "\",";
                        } else {
                            result += "\"" + column + "\":\"" + obj[j] + "\",";
                        }
                    }
                }
                result = result.substring(0, result.length() - 1);
                result += "},";
            }
            result = result.substring(0,result.length()-1);
        }
        result += "]}";
        return result;

    }
    @SuppressWarnings("unused")
    public void setRequestObj(Object obj){
        HttpServletRequest request = ServletActionContext.getRequest ();

        ClassMetadata cm = this.baseLogicInterface.getClassMetadata(obj);

        String propertyNames[] = cm.getPropertyNames();

        List<String> pnList = new ArrayList<String>();
        pnList.add(cm.getIdentifierPropertyName());
        pnList.addAll(Arrays.asList(propertyNames));

        for (String propertyName : pnList) {
            String propertyValue = request.getParameter(propertyName);
            if (propertyValue != null && !"".equals(propertyValue)) {
                Type propertyType = cm.getPropertyType(propertyName);
                if (
                        propertyType == IntegerType.INSTANCE
                                || propertyType == LongType.INSTANCE
                                || propertyType == BigIntegerType.INSTANCE
                                || propertyType == BigDecimalType.INSTANCE
                                || propertyType == FloatType.INSTANCE
                                || propertyType == StringType.INSTANCE
                        ) {
                    try {
                        BeanUtils.copyProperty(obj, propertyName, propertyValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (propertyType == TimestampType.INSTANCE) {
                    try {
                        Date date = StringUtils.string2date(propertyValue);
                        Timestamp timestamp = new Timestamp(date.getTime());
                        BeanUtils.copyProperty(obj, propertyName, timestamp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (propertyType == DateType.INSTANCE) {
                    try {
                        BeanUtils.copyProperty(obj, propertyName, propertyValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
/*
                if (propertyType == ObjectType.INSTANCE) {

                }
*/

            }
        }

    }

    @SuppressWarnings("unused")
    public Serializable getPK(String id){

        if (!baseLogicInterface.isKeyPropertyString()){
            return new Integer(id);
        }
        return id;
    }

    private List<File> uploadFiles;
    private List<String> uploadFileNames;
    private List<String> uploadFileFields;
    public List<File> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(List<File> files) {
        this.uploadFiles = files;
    }

    public List<String> getUploadFileNames() {
        return uploadFileNames;
    }

    public void setUploadFileNames(List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
    }

    public List<String> getUploadFileFields() {
        return uploadFileFields;
    }

    public void setFileFields(List<String> uploadFileFields) {
        this.uploadFileFields = uploadFileFields;
    }

    public List<String> scanUploadFiles(){
        List<String> result = new ArrayList<String>();
        if(uploadFiles!=null){
            int fileNameCount = 0;
            int fileFieldCount = 0;
            if(uploadFileFields!=null) fileFieldCount = uploadFileFields.size();
            if(uploadFileNames!=null) fileNameCount=uploadFileNames.size();
            HttpServletRequest request = ServletActionContext.getRequest();
            String saveToFilePath = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/";
            for(int fileIdx=0,fileCount=uploadFiles.size();fileIdx<fileCount;fileIdx++){
                File file=uploadFiles.get(fileIdx);
                if(file!=null){
                    if(file.exists()&&file.length()>0){
                        String fileName =null;
                        if(fileIdx<fileNameCount){
                            fileName = uploadFileNames.get(fileIdx);
                        }
                        if(fileName==null){
                            fileName = "guess_"+fileIdx+".jpg";
                        }
                        String urlName;
                        log.debug("上传来了："+fileName);
                        fileName = System.currentTimeMillis()+fileName;
                        try {
                            fileName = URLEncoder.encode(fileName.replace(' ', '_'), "UTF-8")
                                    .replace("%","_").replace(" ","_").replace("?","_").replace(":","_");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        FileUtils.copy(file, request.getRealPath(saveToFilePath), fileName);
                        urlName = saveToFilePath+fileName;
                        result.add(urlName);
                        if(fileIdx<fileFieldCount){
                            String fieldName = uploadFileFields.get(fileIdx);
                            if(fieldName!=null&&!fieldName.isEmpty()){
                                Object dstObj = obj;
                                if(fieldName.contains(".")){
                                    String[] array = fieldName.split(".");
                                    fieldName = array[1];
                                    dstObj = BeanUtils.getProperty(this,array[0]);
                                }
                                if(dstObj==null){
                                    log.error("上传文件时，无法找到上传后需要设置的对应的对象！");
                                }else{
                                    BeanUtils.setProperty(dstObj,fieldName,urlName);
                                    log.debug("已经设置了"+dstObj.getClass().getSimpleName()+"的属性"+fieldName+"为"+urlName);
                                }
                            }else{
                                log.warn("要设置的属性名为空，不再尝试设置！");
                            }
                        }else{
                            log.warn("文件索引大于设置属性索引，所以不再尝试设置属性内容："+urlName);
                        }
                    }else{
                        log.warn("上传文件不存在或者长度为0，不能继续进行："+file.getAbsolutePath());
                    }
                }else{
                    log.debug("没有上传，索引是fileIdx="+fileIdx);
                }
            }
        }
        return result;
    }
}
