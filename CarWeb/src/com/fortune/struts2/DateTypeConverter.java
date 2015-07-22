package com.fortune.struts2;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-12-21
 * Time: 11:27:36
 *
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

public class DateTypeConverter extends StrutsTypeConverter {

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger log = Logger.getLogger(DateTypeConverter.class);

    public DateTypeConverter() {
    }

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {

        if (values[0] == null || values[0].trim().equals(""))
            return null;
        try {
            return dateFormat.parse(values[0]);
        } catch (ParseException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
        }
        try {
            return dateFormat.parse(values[0]+" 00:00:00");
        } catch (ParseException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if (o instanceof Date) {
            return dateFormat.format((Date) o);
        }
        return "";
    }

}
