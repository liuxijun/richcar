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
        String title = "����ϵͳ����Ա�Ĳ�����־";
        String searchLog = systemLog.getLog();
        if(searchLog!=null&&!"".equals(searchLog.trim())){
            title+="����־���ݰ�����" +searchLog+"��";
        }
        if(startTime!=null){
           title+="����"+StringUtils.date2string(startTime)+"��ʼ";
        }
        if(startTime!=null){
            title+="����ֹ��"+StringUtils.date2string(stopTime);
        }
        String adminName = systemLog.getAdminName();
        if(adminName!=null&&!"".equals(adminName)){
            title+="������ԱΪ��"+systemLog.getAdminName();
        }
        title+="������"+pageBean.getRowCount()+"�������ε���"+result.size()+"�����ӵ�" +
                (pageBean.getStartRow()+1)+"����"+(pageBean.getEndRow()+1)+"��"+
                "��";
        ExcelWriter.ExcelCell firstLine = writer.new ExcelCell(1,4,null,
                title,
                ExcelWriter.ExcelCellStyle.thin);
        List<List<ExcelWriter.ExcelCell>> rows = new ArrayList<List<ExcelWriter.ExcelCell>>();
        List<ExcelWriter.ExcelCell> row = new ArrayList<ExcelWriter.ExcelCell>(1);
        row.add(firstLine);
        rows.add(row);

        //���ñ��ͷ��Ϣ
        rows.add(writer.createRow(new Object[]{"����","�ʺ�","ʱ��","IP"}, ExcelWriter.ExcelCellStyle.thin));
        Integer[] cellWidth = new Integer[]{80,15,20,15};//���ÿ�ȣ���λ�����������ǵ�����
        //����������
        for(SystemLog log:result){
            rows.add(writer.createRow(new Object[]{log.getLog(),log.getAdminName(),log.getLogTime(),log.getAdminIp()}, ExcelWriter.ExcelCellStyle.thin));
        }
        //��ҳ���
        ExcelWriter.ExcelSheet sheet = writer.new ExcelSheet("��ȫ��־",4,rows);//���⣬4�У��ӵڶ��п�ʼ��������
        sheet.setCellWidths(cellWidth);//�����п�
        sheets.add(sheet);
        String tempExcelFileName = "/home/fortune/SecurityLog"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+".xls";
        File tempFile = new File(tempExcelFileName);
        writer.createExcel(tempFile.getAbsolutePath(),sheets);
        VisitLogLogicImpl.downLoadFile(tempFile.getAbsolutePath());
        if(!tempFile.delete()){
            logger.warn("������־��ɾ����ʱ�ļ�ʧ�ܣ���ʱ�ļ���Ϊ��"+tempFile.getAbsolutePath());
            this.saveMachineLog("������־��ɾ����ʱ�ļ�ʧ�ܣ���ʱ�ļ���Ϊ��"+tempFile.getAbsolutePath());
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
            logger.error("�޷���ȡ��־����ʱ�ͻ��˵�IP��ʹ��Ĭ�ϵı�����"+ip);
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
