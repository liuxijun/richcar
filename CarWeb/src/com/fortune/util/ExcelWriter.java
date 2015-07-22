package com.fortune.util;

import jxl.Workbook;
import jxl.write.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xjliu on 14-12-17.
 * 导出Excel
 */
public class ExcelWriter {
    private Logger logger = Logger.getLogger(getClass());
    public List<ExcelCell> createRow(Object[] dataArray,ExcelCellStyle style){
        List<ExcelCell> row = new ArrayList<ExcelCell>(dataArray.length);
        for(Object o:dataArray){
            String value =null;
            if(o!=null){
                value = o.toString();
            }
            row.add(new ExcelCell(1,1,value,o,style));
        }
        return row;
    }
    public boolean createExcel(String fileName,List<ExcelSheet> data){
        logger.debug("准备输出到："+fileName);
        try {
            File file = new File(fileName);
            File path = file.getParentFile();
            if(!path.exists()){
                if(path.mkdirs()){
                   logger.debug("目录创建成功："+path.getAbsolutePath());
                }else{
                    logger.debug("无法创建目录，不能生成Excel了："+path.getAbsolutePath());
                    return false;
                }
            }
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
///*
            //字体
            //字体1：居中，14，黑体，有边框
            WritableFont wr = new WritableFont(WritableFont.TIMES,14,WritableFont.BOLD);
            WritableCellFormat fontFormat1 = new WritableCellFormat(wr);
            fontFormat1.setAlignment(jxl.format.Alignment.CENTRE);
            fontFormat1.setBorder(jxl.format.Border.NONE,jxl.format.BorderLineStyle.THIN);
            //字体2：居中，12，有边框
            WritableFont  wr1 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat2 = new WritableCellFormat(wr1);
            fontFormat2.setAlignment(jxl.format.Alignment.LEFT);
            fontFormat2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体3：左对齐，12，有边框
            WritableFont  wr2 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat3 = new WritableCellFormat(wr2);
            fontFormat3.setAlignment(jxl.format.Alignment.LEFT);
            fontFormat3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体4：右对齐，12，有边框
            WritableFont  wr3 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat4 = new WritableCellFormat(wr3);
            fontFormat4.setAlignment(Alignment.RIGHT);
            fontFormat4.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            //时间
            jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd");
            WritableCellFormat dateFormat = new WritableCellFormat(df);
            dateFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            dateFormat.setAlignment(jxl.format.Alignment.CENTRE);

            //数字
            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("###,###,###,###,###");
            WritableCellFormat numberFormat = new WritableCellFormat(nf);
            numberFormat.setAlignment(jxl.format.Alignment.CENTRE);
            numberFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("###,###,###,###,###");
            WritableCellFormat numberFormat1 = new WritableCellFormat(nf1);
            numberFormat1.setAlignment(Alignment.RIGHT);
            numberFormat1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
//*/
            int idx = 0,len = data.size();
            for(;idx<len;idx++){
                ExcelSheet sheetData= data.get(idx);
                WritableSheet sheet = workbook.createSheet(sheetData.getTitle(),idx);
///*
                sheet.mergeCells(0,0,sheetData.getColCount()-1,0);
                sheet.addCell(new Label(0,0,sheetData.getTitle(),fontFormat1));
                int col_no=1;
                Integer[] cellWidths = sheetData.getCellWidths();
                if(cellWidths!=null){
                    for(int i=0;i<cellWidths.length;i++){
                        Integer width = cellWidths[i];
                        sheet.setColumnView(i,width);
                    }
                }
                for(List<ExcelCell> row:sheetData.getRows()){
                    int row_no=0;
                    for(ExcelCell col:row){
                        String val = "";
                        Object o = col.getVal();
                        if(o!=null){
                            if(o instanceof Date){
                                val = StringUtils.date2string((Date)o);
                            }else{
                                val = o.toString();
                            }
                        }
                        int colSpan = col.getColSpan();
                        int rowSpan = col.getRowSpan();
                        if(colSpan>1||rowSpan>1){
                            sheet.mergeCells(row_no,col_no,row_no+rowSpan-1,col_no+colSpan-1);
                        }
                        WritableCellFormat format;
                        if(ExcelCellStyle.clear.equals(col.getStyle())){
                            format = fontFormat1;
                        }else{
                            format = fontFormat2;
                        }
                        Label label = new Label(row_no,col_no,val,format);
                        sheet.addCell(label);
                        row_no+=col.getColSpan();
                    }
                    col_no++;
                }
//*/
            }
            workbook.write();
            workbook.close();
            return true;
        } catch (WriteException wre) {
            logger.error("无法正确处理excel文件："+fileName+","+wre.getMessage());
            wre.printStackTrace();
        } catch (IOException e) {
            logger.error("无法正确处理excel文件："+fileName+","+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public enum ExcelCellStyle{
        clear,thin
    }
    public class ExcelCell{
        private int colSpan=1;
        private int rowSpan=1;
        private ExcelCellStyle style=ExcelCellStyle.clear;
        private String value;
        private Object val;

        public ExcelCell(int colSpan, int rowSpan , String value, Object val, ExcelCellStyle style) {
            this.colSpan = colSpan;
            this.rowSpan = rowSpan;
            this.style = style;
            this.value = value;
            this.val = val;
        }

        public int getColSpan() {
            return colSpan;
        }

        public void setColSpan(int colSpan) {
            this.colSpan = colSpan;
        }

        public int getRowSpan() {
            return rowSpan;
        }

        public void setRowSpan(int rowSpan) {
            this.rowSpan = rowSpan;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }

        public ExcelCellStyle getStyle() {
            return style;
        }

        public void setStyle(ExcelCellStyle style) {
            this.style = style;
        }
    }
    public class ExcelSheet{
        private String title;
        private int colCount;
        private Integer[] cellWidths;
        private List<List<ExcelCell>> rows;

        public ExcelSheet(String title, int colCount, List<List<ExcelCell>> rows) {
            this.title = title;
            this.colCount = colCount;
            this.rows = rows;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getColCount() {
            return colCount;
        }

        public void setColCount(int colCount) {
            this.colCount = colCount;
        }

        public List<List<ExcelCell>> getRows() {
            return rows;
        }

        public void setRows(List<List<ExcelCell>> rows) {
            this.rows = rows;
        }

        public Integer[] getCellWidths() {
            return cellWidths;
        }

        public void setCellWidths(Integer[] cellWidths) {
            this.cellWidths = cellWidths;
        }
    }
    public static void main(String[] args){
        ExcelWriter writer = new ExcelWriter();
        List<ExcelSheet> data = new ArrayList<ExcelSheet>(1);
        List<List<ExcelCell>> rows = new ArrayList<List<ExcelCell>>();
        List<ExcelCell> row = new ArrayList<ExcelCell>(1);
        row.add(writer.new ExcelCell(1,4,null,"测试在："+StringUtils.date2string(new Date()),ExcelCellStyle.thin));
        rows.add(row);
        rows.add(writer.createRow(new Object[]{"内容","帐号","IP","时间"},ExcelCellStyle.thin));
        rows.add(writer.createRow(new Object[]{"开会","root","127.0.0.1",new Date()},ExcelCellStyle.thin));
        ExcelSheet sheet = writer.new ExcelSheet("测试",4,rows);
        sheet.setCellWidths(new Integer[]{80,10,15,20});
        data.add(sheet);
        writer.createExcel("E:/1.xls",data);
    }
}
