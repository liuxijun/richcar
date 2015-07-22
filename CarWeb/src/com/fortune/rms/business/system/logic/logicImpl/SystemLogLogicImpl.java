package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.AdminDaoInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.log.logic.logicImpl.VisitLogLogicImpl;
import com.fortune.rms.business.system.dao.daoInterface.SystemLogDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.SystemLog;
import com.fortune.util.ExcelWriter;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("systemLogLogicInterface")
public class SystemLogLogicImpl extends BaseLogicImpl<SystemLog>
		implements
			SystemLogLogicInterface {
	private SystemLogDaoInterface systemLogDaoInterface;
    private AdminDaoInterface adminDaoInterface;
	/**
	 * @param systemLogDaoInterface the systemLogDaoInterface to set
	 */
    @Autowired
	public void setSystemLogDaoInterface(
			SystemLogDaoInterface systemLogDaoInterface) {
		this.systemLogDaoInterface = systemLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.systemLogDaoInterface;
	}

    @Autowired
    public void setAdminDaoInterface(AdminDaoInterface adminDaoInterface) {
        this.adminDaoInterface = adminDaoInterface;
    }

    public List<SystemLog> getSystemLogAll(SystemLog systemLog, Date startTime,Date stopTime,PageBean pageBean) {
        List<Object[]> tempResult = systemLogDaoInterface.getSystemLogAll(systemLog,startTime,stopTime,pageBean);
        List<SystemLog> result = new ArrayList<SystemLog>();
        for (Object[] objs : tempResult) {
            SystemLog sl = (SystemLog) objs[0];
            Admin a= (Admin) objs[1];
            sl.setAdminName(a.getLogin());
            result.add(sl);
        }
        return result;
    }

    public List<SystemLog> export(SystemLog systemLog, Date startTime, Date stopTime, PageBean pageBean) {
        List<SystemLog> result = getSystemLogAll(systemLog, startTime, stopTime, pageBean);
        ExcelWriter writer = new ExcelWriter();
        List<ExcelWriter.ExcelSheet> sheets = new ArrayList<ExcelWriter.ExcelSheet>(1);
        String title = "导出系统管理员的操作日志";
        String searchLog = systemLog.getLog();
        if(searchLog!=null&&!"".equals(searchLog.trim())){
            title+="，日志内容包括“" +searchLog+"”";
        }
        if(startTime!=null){
           title+="，从"+StringUtils.date2string(startTime)+"开始";
        }
        if(startTime!=null){
            title+="，截止到"+StringUtils.date2string(stopTime);
        }
        String adminName = systemLog.getAdminName();
        if(adminName!=null&&!"".equals(adminName)){
            title+="，管理员为："+systemLog.getAdminName();
        }
        title+="，共有"+pageBean.getRowCount()+"条，本次导出"+result.size()+"条，从第" +
                (pageBean.getStartRow()+1)+"条到"+(pageBean.getEndRow()+1)+"条"+
                "。";
        ExcelWriter.ExcelCell firstLine = writer.new ExcelCell(1,4,null,
                title,
                ExcelWriter.ExcelCellStyle.thin);
        List<List<ExcelWriter.ExcelCell>> rows = new ArrayList<List<ExcelWriter.ExcelCell>>();
        List<ExcelWriter.ExcelCell> row = new ArrayList<ExcelWriter.ExcelCell>(1);
        row.add(firstLine);
        rows.add(row);

        //设置表格头信息
        rows.add(writer.createRow(new Object[]{"内容","帐号","时间","IP"}, ExcelWriter.ExcelCellStyle.thin));
        Integer[] cellWidth = new Integer[]{80,15,20,15};//设置宽度，单位是字数，不是点阵数
        //输出表格内容
        for(SystemLog log:result){
            rows.add(writer.createRow(new Object[]{log.getLog(),log.getAdminName(),log.getLogTime(),log.getAdminIp()}, ExcelWriter.ExcelCellStyle.thin));
        }
        //单页表格
        ExcelWriter.ExcelSheet sheet = writer.new ExcelSheet("安全日志",4,rows);//标题，4列，从第二行开始的行数据
        sheet.setCellWidths(cellWidth);//设置列宽
        sheets.add(sheet);
        String tempExcelFileName = "/home/fortune/SecurityLog"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+".xls";
        File tempFile = new File(tempExcelFileName);
        writer.createExcel(tempFile.getAbsolutePath(),sheets);
        VisitLogLogicImpl.downLoadFile(tempFile.getAbsolutePath());
        if(!tempFile.delete()){
            logger.warn("导出日志后删除临时文件失败，临时文件名为："+tempFile.getAbsolutePath());
            this.saveMachineLog("导出日志后删除临时文件失败，临时文件名为："+tempFile.getAbsolutePath());
        }
        return result;
    }



    public SystemLog getSystemLogId(Long systemLogId) {
        SystemLog sl = systemLogDaoInterface.get(systemLogId);
        if (sl != null) {
            String adminName = "";
            if (sl.getAdminId() != null) {
                Long adminId = sl.getAdminId();
                Integer adminIds = adminId.intValue();
                try {
                    Admin admin = adminDaoInterface.get(adminIds);
                    if(admin!=null){
                        adminName = admin.getLogin();
                    }
                } catch (Exception e) {
                    sl.setAdminName("ID:" + adminId);
                }
            } else{
                return sl;
            }
            sl.setAdminName(adminName);
        }
        return sl;
    }

    public void saveDebugLog(SystemLog systemLog) {
        this.systemLogDaoInterface.save(systemLog);
    }
    public SystemLog saveMachineLog(String logInfo){
        SystemLog sysLog = new SystemLog();
        String ip = "localhost";
        try {
            ActionContext ctx = ServletActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest) ctx
                    .get(ServletActionContext.HTTP_REQUEST);
            ip = request.getRemoteHost();
        } catch (Exception e) {
            logger.error("无法获取日志发生时客户端的IP，使用默认的本机："+ip);
        }
        sysLog.setAdminIp(ip);
        sysLog.setLog(logInfo);
        sysLog.setAdminId(1L);
        sysLog.setLogTime(new Date());
        sysLog.setSystemLogAction("com.fortune.rms.machine");
        return save(sysLog);
    }
    public SystemLog saveLog(String clientIp, Admin admin, String action, String logInfo) {
        SystemLog sysLog = new SystemLog();
        sysLog.setAdminIp(clientIp);
        sysLog.setLog(logInfo);
        sysLog.setAdminId(admin.getId().longValue());
        sysLog.setLogTime(new Date());
        sysLog.setSystemLogAction(action);
        return save(sysLog);
    }
}
