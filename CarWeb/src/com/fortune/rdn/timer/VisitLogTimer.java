package com.fortune.rdn.timer;


import com.fortune.rms.business.log.model.VisitLog;
import com.fortune.util.StringUtils;
import com.fortune.util.config.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Alen
 * Date: 11-8-4
 * Time: ÏÂÎç5:45
 */
public class VisitLogTimer {
    static Logger logger = LoggerFactory.getLogger("VisitLogTimer.class");

    public static String getVisitLogPath() {
        return Config.getStrValue("visitlog.path", "/");
    }

    public static void searchVisitLog() {
        String filePath = getVisitLogPath();
        String fullName;
        File root = new File(filePath);
        File[] files = root.listFiles();
        Arrays.sort(files);
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith("log") && file.getName().startsWith("WMS")) {
                String nowDate = StringUtils.date2string(new Date(), "yyyyMMdd");
                if (file.getName().contains(nowDate)) {
                    continue;
                }
                fullName = filePath + "/" + file.getName();
                readVisitLog(fullName);
            }

        }
    }


    public static void readVisitLog(String fullName) {

        int sendVisitLogStatus = 0;
        StringBuilder sb = new StringBuilder();
        File file = new File(fullName);
        BufferedReader br = null;
        int count = 0;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("#")) {
                    continue;
                } else {
                    String visitLogArray[] = line.split(" ");
                    if (visitLogArray[8].equals("404")) {
                        continue;
                    }
                    if (visitLogArray[47].contains("fortuneTestUrl")) {
                        continue;
                    }
                    count++;
                    sb.append(line);
                }

                sb.append("/n");
                if (count == 100) {
                    sendVisitLogStatus = sendVisitLog(sb.toString());
                    count = 0;
                    sb.setLength(0);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (count > 0) {
            sendVisitLogStatus = sendVisitLog(sb.toString());
        }
        if (sendVisitLogStatus > 0) {
            if (file.delete()) {

            }
        }


    }

    public static int sendVisitLog(String visitLogsString) {
        int sendVisitLogStatus = 1;
        String urlStr = Config.getStrValue("visitlog.sentUrl", "http://127.0.0.1:8080/interface/saveLog.jsp");
        URL url;
        URLConnection con;
        OutputStreamWriter out = null;
        BufferedReader br = null;

        try {
            url = new URL(urlStr);
            con = url.openConnection();
            con.setDoOutput(true);
            out = new OutputStreamWriter(con.getOutputStream());
            out.write(new String(visitLogsString.getBytes(Config.getStrValue("web.encoding","GBK"))));
            out.flush();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            Log log = LogFactory.getLog(VisitLog.class);
            for (line = br.readLine(); line != null; line = br.readLine()) {

                log.debug(line);
            }

        } catch (MalformedURLException e) {
            sendVisitLogStatus = 0;
            logger.error(e.getMessage());
        } catch (IOException e) {
            sendVisitLogStatus = 0;
            logger.error(e.getMessage());
        } finally {

            try {
                if (out != null) {
                    out.close();

                }
                if (br != null) {
                    br.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sendVisitLogStatus;
    }

    public static void main(String args[]) {
        searchVisitLog();

    }
}
