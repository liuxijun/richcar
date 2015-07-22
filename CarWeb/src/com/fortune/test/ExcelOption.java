package com.fortune.test;

import com.fortune.rms.business.log.logic.logicInterface.VisitLogLogicInterface;
import com.fortune.rms.business.log.model.VisitLog;
import com.fortune.util.SpringUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12-7-2
 * Time: ÉÏÎç10:38
 */
public class ExcelOption {
    public static void main(String args[]) {
        // getVisitLogs();
        try {
            List<VisitLog> visitLogs = getVisitLogs();
            insert(visitLogs);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void insert(List<VisitLog> visitLogs) {
        try {
            VisitLogLogicInterface visitLogLogicInterface = (VisitLogLogicInterface) SpringUtils.getBean("visitLogLogicInterface");

            if (visitLogs != null && visitLogs.size() != 0) {
                for (int i = 0; i < visitLogs.size(); i++) {

                    visitLogLogicInterface.save(visitLogs.get(i));
                    System.out.println(i);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<VisitLog> getVisitLogs() throws ParseException {
        List<VisitLog> visitLogs = new ArrayList<VisitLog>();
        VisitLog visitLog = null;
        Workbook workbook = null;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");

        try {
            workbook = Workbook.getWorkbook(new File("D:\\work1.xls"));
            Sheet sheet = workbook.getSheet(0);
            String content;
            Cell cell = null;
            for (int y = 1; y <= 1989; y++) {
                visitLog = new VisitLog();
                for (int x = 1; x <= 19; x++) {
                    cell = sheet.getCell(x, y);
                    content = cell.getContents();
                    System.out.println("x:" + x + ",y:" + y + ",content:" + content);

//                    if(x==1){ visitLog.setId(Long.parseLong(content));continue; }
                    if (x == 2) {
                        visitLog.setSpId(Long.parseLong(content));
                        continue;
                    }
                    if (x == 3) {
                        visitLog.setCpId(Long.parseLong(content));
                        continue;
                    }
                    if (x == 4) {
                        visitLog.setChannelId(Long.parseLong(content));
                        continue;
                    }
                    if (x == 5) {
                        visitLog.setContentId(Long.parseLong(content));
                        continue;
                    }
                    if (x == 6) {
                        visitLog.setContentPropertyId(Long.parseLong(content));
                        continue;
                    }
                    if (x == 7) {
                        visitLog.setUrl(content);
                        continue;
                    }
                    if (x == 8) {
                        visitLog.setUserId(content);
                        continue;
                    }
                    if (x == 9) {
                        visitLog.setUserIp(content);
                        continue;
                    }
                    if (x == 10) {
                        visitLog.setAreaId(Long.parseLong(content));
                        continue;
                    }
                    if (x == 11) {
                        visitLog.setIsFree(Long.parseLong(content));
                        continue;
                    }
                    if (x == 12) {
                        System.out.println("hello");
                        visitLog.setStartTime(
                                simpleDateFormat.parse(content));
                        continue;
                    }
                    if (x == 13) {

                        visitLog.setEndTime(simpleDateFormat.parse(content));
                        continue;
                    }
                    if (x == 14) {
                        visitLog.setLength(Long.parseLong(content));
                        continue;
                    }
                    if (x == 15) {
                        visitLog.setStatus(Long.parseLong(content));
                        continue;
                    }
                    if (x == 16) {
                        visitLog.setPlayerVersion(content);
                        continue;
                    }
                    if (x == 17) {
                        visitLog.setUserAgent(content);
                        continue;
                    }
                    if (x == 18) {
                        visitLog.setAvgBandwidth(Long.parseLong(content));
                        continue;
                    }
                    if (x == 19) {
                        visitLog.setsIp(content);
                        continue;
                    }
//                    System.out.println("x:"+x+",y:"+y+",content:"+content);
                }
                System.out.println();
                visitLogs.add(visitLog);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return visitLogs;
    }
}
