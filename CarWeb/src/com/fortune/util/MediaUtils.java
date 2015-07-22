package com.fortune.util;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-7-22
 * Time: 13:50:40
 */
@SuppressWarnings("unused")
public class MediaUtils implements MediaUtilsCallback, CommandRunnerOutlineCallbackable {
    public static final int ERROR_FILE_NOT_EXISTS = 404;
    public static final int ERROR_FILE_SIZE_TOO_SMALL = 405;
    public static final int ERROR_FILE_FORMAT_NOT_KNOWN = 500;
    public static final int ERROR_START_TIME = 501;
    public static final int ERROR_DURATION = 502;
    public static final int ERROR_OUT_FILE_ERROR = 504;
    public static final int ERROR_UNKNOWN = 505;
    public static final int ERROR_COMMAND_LINE = 506;
    public static final int ERROR_OUT_FILE_TIME_ERROR = 507;
    public static final int ERROR_UNKNOWN_EXCEPTION = 508;
    public static final int ERROR_IO_EXCEPTION = 509;
    public static final int ERROR_SKIP = 510;
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_WAITING = 2;
    public static final int STATUS_FINISHED = 3;
    public static final int LOG_LEVEL_DEBUG = 990;
    public static final int LOG_LEVEL_WARN = 991;
    public static final int LOG_LEVEL_INFO = 992;
    public static final int LOG_LEVEL_ERROR = 993;
    public static final int SUCCESS = 0;
    public static Map<Integer, String> errorCodes;

    static {
        errorCodes = new HashMap<Integer, String>();
        errorCodes.put(0, "成功");
        errorCodes.put(STATUS_RUNNING,"运行中");
        errorCodes.put(STATUS_WAITING,"等待中");
        errorCodes.put(STATUS_FINISHED,"已完成");
        errorCodes.put(ERROR_FILE_NOT_EXISTS, "文件不存在");
        errorCodes.put(ERROR_FILE_SIZE_TOO_SMALL, "文件小于设定值，默认是1MB");
        errorCodes.put(ERROR_FILE_FORMAT_NOT_KNOWN, "文件格式不能识别");
        errorCodes.put(ERROR_COMMAND_LINE, "命令行错误");
        errorCodes.put(ERROR_START_TIME, "起始时间超过影片片长");
        errorCodes.put(ERROR_DURATION, "时长错误");
        errorCodes.put(ERROR_OUT_FILE_ERROR, "输出文件错误");
        errorCodes.put(ERROR_UNKNOWN_EXCEPTION, "未知异常");
        errorCodes.put(ERROR_IO_EXCEPTION, "未知IO异常");
        errorCodes.put(ERROR_UNKNOWN, "未知错误");
        errorCodes.put(ERROR_OUT_FILE_TIME_ERROR, "生成文件时间比当前时间早，可能是生成时发生了错误！");
        errorCodes.put(ERROR_SKIP,"有相同的编码文件，已经跳过此次编码任务！");
    }

    private MediaUtilsCallback callback;
    private String lastLog;

    public String getLastLog() {
        return lastLog;
    }

    public void setLastLog(String lastLog) {
        this.lastLog = lastLog;
    }

    public void setLastLog(int logLevel, String lastLog) {
        if (logLevel != LOG_LEVEL_DEBUG) {
            if (logLevel == LOG_LEVEL_WARN) {
                logger.warn(lastLog);
            } else if (logLevel == LOG_LEVEL_ERROR) {
                logger.error(lastLog);
            } else if (logLevel == LOG_LEVEL_INFO) {
                logger.info(lastLog);
            }
            this.lastLog += "\r\n" + StringUtils.date2string(new Date()) + "-" + lastLog;
            if (callback != null && !(callback instanceof MediaUtils)) {
                callback.onLog(lastLog);
            }
        } else {
            logger.debug(lastLog);
        }
    }

    public int reindex(String sourceFileName, String saveFileName) {
        File file = FileUtils.newFile(sourceFileName);
        if (!file.exists()) {
            setLastLog(LOG_LEVEL_ERROR, "文件不存在：" + sourceFileName);
            return ERROR_FILE_NOT_EXISTS;
        }
        String cmdLine = AppConfigurator.getInstance().getConfig("system.tools.reindex.cmdLine", "C:/FFModules/mp4box.exe -add %sourceFile% -new %saveFile%");
        if (cmdLine != null) {
            CommandRunner runner = new CommandRunner();
            cmdLine = cmdLine.replace("%sourceFile%", sourceFileName);
            cmdLine = cmdLine.replace("%saveFile%", saveFileName);
            try {
                run(cmdLine);
                File saveFile = FileUtils.newFile(saveFileName);
                if (saveFile.exists()) {
                    Date now = new Date();
                    if (saveFile.lastModified() - now.getTime() <= 60 * 1000) {

                    } else {
                        setLastLog(LOG_LEVEL_WARN, "文件" + saveFile.getAbsolutePath() +
                                "虽然存在，但生成日期不正常！应该是在当前时间附近：当前（" + StringUtils.date2string(now) + "," +
                                "文件时间(" + StringUtils.date2string(saveFile.lastModified()) + ")");
                        return ERROR_OUT_FILE_TIME_ERROR;
                    }
                } else {
                    setLastLog(LOG_LEVEL_ERROR, "文件" + saveFile.getAbsolutePath() +
                            "生成失败！");
                    return ERROR_OUT_FILE_ERROR;
                }
            } catch (IOException e) {
                e.printStackTrace();
                setLastLog(LOG_LEVEL_ERROR, "重新建立索引时发生异常：" + e.getMessage());
            }
        }
        return SUCCESS;
    }

    public boolean isFileValidate(int sourceDuration, String fileName) {
        SimpleFileInfo fileInfo = new SimpleFileInfo(FileUtils.newFile(fileName));
        if (FileUtils.setFileMediaInfo(fileName, fileInfo, FileType.video)) {
            if(sourceDuration>=72000){
                return true;
            }
            long diffLimit = AppConfigurator.getInstance().getIntConfig("system.tools.encode.validateTimeDiff", 60);
            if (Math.abs(fileInfo.getLength() - sourceDuration) < diffLimit) {//如果生成的文件时间是在30秒之内，是允许的
                return true;
            } else {
                setLastLog(LOG_LEVEL_ERROR, "检查文件是否相同时发现时间不一致，两者差别过大，源时间：" + sourceDuration + ",转码后时间：" + fileInfo.getLength());
            }
        } else {
            setLastLog(LOG_LEVEL_ERROR, "检查文件是否相同时发现时间不一致时无法读取文件信息，文件是：" + fileName);
        }
        return false;
    }

    public int cut(String sourceFile, String saveFile, int startSeconds, int duration) {
        File file = FileUtils.newFile(sourceFile);
        if (!file.exists()) {
            setLastLog(LOG_LEVEL_ERROR, "文件不存在：" + sourceFile);
            return ERROR_FILE_NOT_EXISTS;
        }
        String cmdLine = AppConfigurator.getInstance().getConfig("system.tools.cut.cmdLine", "E:/UserData/lxj/project/Fortune/redex/02_Source/bin/asfcut.exe -i %sourceFile% -o %saveFile% -start %startTime% -dur %duration% -y");
        if (cmdLine != null) {
            CommandRunner runner = new CommandRunner();
            cmdLine = cmdLine.replace("%sourceFile%", sourceFile);
            cmdLine = cmdLine.replace("%saveFile%", saveFile);
            cmdLine = cmdLine.replace("%startTime%", "" + startSeconds);
            cmdLine = cmdLine.replace("%duration%", "" + duration);
            try {
                int result = run(cmdLine);
                if (result == 0) {

                } else {

                }
                return SUCCESS;
            } catch (IOException e) {
                setLastLog(LOG_LEVEL_ERROR, e.getMessage());
            }
            return ERROR_UNKNOWN;
        } else {
            return ERROR_COMMAND_LINE;
        }
    }

    public int encodeTranscoder(String sourceFile, String saveFile, String title,
                                String videoCodec, String videoProfile, int profileLevel, int videoBandwith,
                                int keyframeDuration, int fps,
                                int videoWidth, int videoHeight,
                                String audioCodec, int audioBandwith,
                                int audioSampleHz, int audioChannelCount,
                                int startSeconds, int duration, MediaUtilsCallback callback) {
        this.callback = callback;
        File file = FileUtils.newFile(sourceFile);
        if (!file.exists()) {
            setLastLog(LOG_LEVEL_ERROR, "文件不存在：" + sourceFile);
            return ERROR_FILE_NOT_EXISTS;
        }
        SimpleFileInfo fileInfo = new SimpleFileInfo(file.getAbsolutePath(), file.length(), new Date(file.lastModified()),
                file.isDirectory(), FileType.video);
        int clipWidth = 0, clipHeight = 0;

        if (FileUtils.setFileMediaInfo(sourceFile, fileInfo, FileType.video)) {
            clipHeight = fileInfo.getHeight();
            clipWidth = fileInfo.getWidth();
            //如果影片的长宽比例不合适，取最大的一边。例如要求640x480，实际上是1920x1080，那么结果就是640x360，不改变原有影片的比例
            float rate1 = clipWidth * 1.0f / clipHeight;
            float rate2 = videoWidth * 1.0f / videoHeight;
            if (rate1 > rate2) {
                clipWidth = videoWidth;
                clipHeight = Math.round(clipWidth / rate1);
            } else {
                clipHeight = videoHeight;
                clipWidth = Math.round(clipHeight * rate2);
            }
            if (callback != null) {
                callback.onSetLength(Math.round(fileInfo.getLength()), new Date(file.lastModified()), file.length());
            }
        }
        if (clipHeight == 0) {
            clipHeight = videoHeight;

        }
        if (clipWidth == 0) {
            clipWidth = videoWidth;
        }
        AppConfigurator config = AppConfigurator.getInstance();
        File desertFile = FileUtils.newFile(saveFile);
        saveFile = desertFile.getAbsolutePath();
        String tempFileNameHeader = config.getConfig("system.tools.encode.tempDir",
                FileUtils.extractFilePath(saveFile, File.separator));
        File tempDir = FileUtils.newFile(tempFileNameHeader);
        if (!tempDir.exists()) {
            if (tempDir.mkdirs()) {

            }
        }
        if (videoCodec.contains("264")) {
            videoCodec = "h264";
        }
        String transcoderPath = config.getConfig("system.tools.encode.transcoder.configPath", "D:/fortune/transcoder/");
        String encodeXmlFileName = transcoderPath + "/conf/v_" + videoCodec + "_" + videoProfile + "_" + profileLevel + "_" + videoBandwith + "_" + clipWidth + "x" + clipHeight + "_" + fps + "_" + keyframeDuration + "_a_";
        if (audioCodec.toLowerCase().contains("aac")) {
            audioCodec = "aac";
        }
        encodeXmlFileName += audioCodec + "_" + audioBandwith + "_" + audioChannelCount + "_" + audioSampleHz + ".xml";
        File xmlFile = FileUtils.newFile(encodeXmlFileName);
        if (!xmlFile.exists()) {
            try {
                ThreadUtils.getInstance().acquire(encodeXmlFileName);
                if (!xmlFile.exists()) {
                    String profile = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                            "<req version=\"1.0\">\n" +
                            "<task type=\"transc\" target=\"start\">\n" +
                            "<character version=\"1.0\">\n" +
                            "\t<video_codec>\n" +
                            "\t\t<param n=\"name\" v=\"" + videoCodec + "\" />\n" +
                            "\t\t<param n=\"profile\" v=\"" + videoProfile + "\" />\n" +
                            "\t\t<param n=\"level\"           v=\"" + profileLevel +
                            "\" />\n" +
                            "\t\t<param n=\"width\"           v=\"" + clipWidth + "\" />\n" +
                            "\t\t<param n=\"height\"          v=\"" + clipHeight + "\" />\n" +
                            "\t\t<param n=\"bitrate\"         v=\"" + videoBandwith + "\" />\n" +
                            "\t\t<param n=\"bitratectrl\"     v=\"0.8\" />\n" +
                            "\t\t<param n=\"framerate\"       v=\"" + fps + "\" />\n" +
                            "\t\t<param n=\"keyframerate\"    v=\"1\" />\n" +
                            "\t\t<param n=\"referenceframes\" v=\"3\" />\n" +
                            "\t\t<param n=\"bframes\"         v=\"3\" />\n" +
                            "\t\t<param n=\"multithreads\"    v=\"4\" />\n" +
                            "\t</video_codec>\n" +
                            "\t<audio_codec>\n" +
                            "\t\t<param n=\"name\"        v=\"" + audioCodec + "\" />\n" +
                            "\t\t<param n=\"samplerate\"  v=\"" + audioSampleHz + "\" />\n" +
                            "\t\t<param n=\"channels\"    v=\"" + audioChannelCount + "\" />\n" +
                            "\t\t<param n=\"bitrate\"     v=\"" + audioBandwith + "\" />\n" +
                            "\t</audio_codec>\n" +
                            "\t<miscellaneous>\n" +
                            "\t\t<_param n=\"syncqueue_deep\" v=\"0\" />\n" +
                            "\t\t<param n=\"independent\"    v=\"1\" />\n" +
                            "\t\t<param n=\"show_ratio\"     v=\"1\" />\n" +
                            "\t\t<_param n=\"ts_cbrmode\"     v=\"1\" />\n" +
                            "\t</miscellaneous>\n" +
                            "</character>\n" +
                            "</task>\n" +
                            "</req>\n";
                    FileUtils.writeNew(xmlFile.getAbsolutePath(), profile);
                    ThreadUtils.getInstance().release(encodeXmlFileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String cmdLine = "\"D:/fortune/transcoder/ft.bat\" \"%sourceFile%\" mp4 %desertFile% %profile%";
        //no8x8dct:nocabac:weightp=0:bframes=0
        cmdLine = config.getConfig("system.tools.encode.cmdLine", cmdLine);
        if (cmdLine != null) {
            CommandRunner runner = new CommandRunner();
            cmdLine = cmdLine.replace("%sourceFile%", sourceFile);
            cmdLine = cmdLine.replace("%profile%", "" + xmlFile.getAbsolutePath());
            cmdLine = cmdLine.replace("%desertFile%", desertFile.getAbsolutePath());
            try {
                if (desertFile.exists()) {
                    setLastLog(LOG_LEVEL_WARN, "原来的文件存在，需要删除才行！");
                } else {

                }
                File parent = desertFile.getParentFile();
                if (!parent.exists()) {
                    setLastLog(LOG_LEVEL_DEBUG, "目录不存在，必须建目录：" + parent.getAbsolutePath());
                    if (!parent.mkdirs()) {
                        setLastLog(LOG_LEVEL_WARN, "无法建立目录：" + parent.getAbsolutePath());
                    }
                }
                String dirPath = "";
                int i = 0;
                while (parent != null && i < 3) {
                    i++;
                    if (!"".equals(dirPath)) {
                        dirPath = " " + dirPath;
                    }
                    dirPath = parent.getName() + dirPath;
                    parent = parent.getParentFile();
                }
                cmdLine += " " + dirPath;
                int result = run(cmdLine, transcoderPath);
                if (result == 0) {
                } else {
                }
                if (FileUtils.fileExists(saveFile)) {
                    if (isFileValidate(Math.round(fileInfo.getLength()), saveFile)) {
                        return SUCCESS;
                    } else {
                        return ERROR_DURATION;
                    }
                } else {
                    return ERROR_COMMAND_LINE;
                }
                //return SUCCESS;
            } catch (IOException e) {
                setLastLog(LOG_LEVEL_ERROR, e.getMessage());
            }
            return ERROR_UNKNOWN;
        } else {
            return ERROR_COMMAND_LINE;
        }
    }

    public int encodeMencoder(String sourceFileName, String saveFileName, String title,
                              String videoCodec, String videoProfile, int profileLevel, int videoBandwith,
                              int keyframeDuration, int fps,
                              int videoWidth, int videoHeight,
                              String audioCodec, int audioBandwith,
                              int videoSampleHz, int audioChannelCount,
                              int startSeconds, int duration, MediaUtilsCallback callback) {
        if(videoCodec.contains("264")){
            videoCodec = "x264";
        }
        if(audioCodec.contains("aac")){
            audioCodec = "faac";
        }

        return encodeMedia(sourceFileName, saveFileName, title, videoCodec, videoProfile, profileLevel, videoBandwith,
                keyframeDuration, fps,
                videoWidth, videoHeight,
                audioCodec, audioBandwith,
                videoSampleHz, audioChannelCount,
                startSeconds, duration, callback,
                AppConfigurator.getInstance().getBoolConfig("system.encoder.skipIfExists", true),"mp4");
    }

    public int encodeMedia(String sourceFileName, String saveFileName, String title,
                           String videoCodec, String videoProfile, int profileLevel, int videoBandwith,
                           int keyframeDuration, int fps,
                           int videoWidth, int videoHeight,
                           String audioCodec, int audioBandwith,
                           int audioSampleHz, int audioChannelCount,
                           int startSeconds, int duration, MediaUtilsCallback callback, boolean skipIfExists,String fileFormat) {
        this.callback = callback;
        String errorLog = "有错误";
        AppConfigurator config = AppConfigurator.getInstance();
        boolean isStreamSource = sourceFileName.contains("://");
        String osName = System.getProperties().getProperty("os.name");
        if (osName == null) {
            osName = "";
        }
        boolean isWindows = osName.toLowerCase().contains("windows");
        boolean sourceExists = isStreamSource;
        int clipWidth = 0, clipHeight = 0, clipDuration = 0;
        if(!isStreamSource){
            File file = FileUtils.newFile(sourceFileName);
            sourceExists = file.exists() && !file.isDirectory();
            if (!sourceExists) {
                //文件不存在，如果是linux环境的话，尝试进行目录与本地挂载的目录进行匹配
                if (!isWindows) {
                    if (sourceFileName.startsWith("\\\\") || sourceFileName.startsWith("////")) {
                        String tempFileName = guessServerPath(sourceFileName);
                        if (tempFileName != null) {
                            file = FileUtils.newFile(tempFileName);
                            sourceExists = file.exists() && !file.isDirectory();
                        } else {
                            errorLog += ",无法获取替换目录：" + sourceFileName;
                        }
                    } else {
                        errorLog += ",不是双斜杠开头或者双反斜杠开头:" + sourceFileName;
                    }
                } else {
                    errorLog += ",是windows平台，不存在就不存在吧";
                }
            }
            if (!sourceExists) {
                setLastLog(LOG_LEVEL_ERROR, "文件不存在：" + file.getAbsolutePath()+"("+sourceFileName + "),isWindows=" + isWindows + "," + errorLog);
                return ERROR_FILE_NOT_EXISTS;
            }
            long encodeFileSizeAtLast = config.getIntConfig("system.tools.encode.minSize", 256) * 1024;//K为单位
            boolean fileTooSmall = (encodeFileSizeAtLast > 0 && file.length() < encodeFileSizeAtLast);//文件大小要大于1MB，否则也认为文件是错误的;
            if (fileTooSmall) {
                errorLog = "文件尺寸太小：" + sourceFileName + "，只有：" + StringUtils.formatBytes(file.length()) +
                        ",设定最小值：" + StringUtils.formatBytes(encodeFileSizeAtLast);
                setLastLog(LOG_LEVEL_ERROR, errorLog);
                errorCodes.put(ERROR_FILE_SIZE_TOO_SMALL, errorLog);
                return ERROR_FILE_SIZE_TOO_SMALL;
            }
            sourceFileName = file.getAbsolutePath();
            SimpleFileInfo fileInfo = new SimpleFileInfo(file.getAbsolutePath(), file.length(), new Date(file.lastModified()),
                    file.isDirectory(), FileType.video);

            if (FileUtils.setFileMediaInfo(sourceFileName, fileInfo, FileType.video)) {
                clipHeight = fileInfo.getHeight();
                clipWidth = fileInfo.getWidth();
                if (clipHeight <= 0 || clipWidth <= 0) {
                    setLastLog(LOG_LEVEL_ERROR, "无法获取影片的参数信息！后续操作可能会有问题！");
                } else {
                    //如果影片的长宽比例不合适，留黑边就要在这里完成。
                    //根据最新的想法，不留黑边。20130531，by xjliu
                    float rate1 = clipWidth * 1.0f / clipHeight;
                    float rate2 = videoWidth * 1.0f / videoHeight;
                    if (rate1 > rate2) {
                        clipWidth = videoWidth;
                        clipHeight = Math.round(clipWidth / rate1);
                    } else {
                        clipHeight = videoHeight;
                        clipWidth = Math.round(clipHeight * rate2);
                    }
                    if (callback != null) {
                        callback.onSetLength(Math.round(fileInfo.getLength()), new Date(file.lastModified()), file.length());
                    }
                }
                clipDuration = Math.round(fileInfo.getLength());
                if (clipDuration > 0) {
                    if (startSeconds >= clipDuration) {
                        setLastLog(LOG_LEVEL_ERROR, "起始时间超过了影片片长，拒绝此次转码任务！");
                        return ERROR_START_TIME;
                    }
                    if ((startSeconds + duration) > clipDuration) {
                        duration = Math.round(fileInfo.getLength()) - startSeconds;this.duration = duration;
                    }
                } else {
                    setLastLog(LOG_LEVEL_ERROR, "无法获取影片长度信息，将可能造成问题：" + sourceFileName);
                }
            } else {
                setLastLog(LOG_LEVEL_ERROR, "无法获取媒体信息，无法进行后续操作！");
            }
        }
        if ((!isWindows) && (saveFileName.startsWith("\\\\") || saveFileName.startsWith("////"))) {
            String tempFileName = guessServerPath(saveFileName);
            if (tempFileName != null) {
                saveFileName = tempFileName;
            } else {
                setLastLog(LOG_LEVEL_ERROR, "保存的文件目录映射无法找到，不能继续进行：" + saveFileName);
                return ERROR_OUT_FILE_ERROR;
            }
        }
        if (clipHeight == 0) {
            clipHeight = videoHeight;
        }
        if (clipWidth == 0) {
            clipWidth = videoWidth;
        }
        //不知什么原因,ffmpeg在做变形时，如果出现奇数，就会报错
        if(clipHeight%2==1){
            clipHeight--;
        }
        if(clipWidth%2==1){
            clipWidth--;
        }
        //不留黑边，所以videoHeight和videoWidth和clipHeight和clipWidth相同，20130531
        if (config.getBoolConfig("system.tools.encode.keepClipRateWithSource", true)) {
            videoWidth = clipWidth;
            videoHeight = clipHeight;
        }
        if(videoHeight%2==1){
            videoHeight--;
        }
        if(videoWidth%2==1){
            videoWidth--;
        }
        File desertFile = FileUtils.newFile(saveFileName);
        if(desertFile.exists()){
            //如果和源文件时间一致就可以跳过了
            if(skipIfExists && isFileValidate(duration,desertFile.getAbsolutePath())){
                return ERROR_SKIP;
            }
        }
        //saveFileName = desertFile.getAbsolutePath();
        String tempFileNameHeader = config.getConfig("system.tools.encode.tempDir",
                desertFile.getParentFile().getAbsolutePath());
        if (tempFileNameHeader.startsWith("\\\\")) {
            tempFileNameHeader = "C:\\FFModules\\";
        }
        File tempDir = FileUtils.newFile(tempFileNameHeader);
        if("mp4".equals(fileFormat)){
            if (!tempDir.exists()) {
                setLastLog(LOG_LEVEL_WARN, "临时转码文件保存目录不存在，尝试进行创建：" + tempDir.getAbsolutePath());
                if (tempDir.mkdirs()) {
                    setLastLog(LOG_LEVEL_DEBUG, "临时转码文件保存目录创建成功：" + tempDir.getAbsolutePath());
                } else {
                    setLastLog(LOG_LEVEL_ERROR, "临时转码文件保存目录不存在，创建失败：" + tempDir.getAbsolutePath());
                }
            }

        }
        String profile = "";
        if (videoProfile == null) {
            videoProfile = "baseline";
        } else {
            videoProfile = videoProfile.toLowerCase();
        }

        if ("high_profile".equals(videoProfile) || "highprofile".equals(videoProfile) || "high".equals(videoProfile)) {
            profile += ":8x8dct";
            videoProfile = "high";
        } else if ("main_profile".equals(videoProfile) || "mainprofile".equals(videoProfile) || "main".equals(videoProfile)) {
            profile += ":no8x8dct:weightp=2:cabac:bframes=3";//profile=main";
            videoProfile = "main";
        } else {
            profile += ":no8x8dct:nocabac:weightp=0:bframes=0";
            videoProfile = "baseline";
        }
        if (profileLevel > 0) {
            profile += ":level_idc=" + profileLevel;
        } else {
            profile += ":level_idc=21";
        }
        String tempFileName = tempFileNameHeader + "/_temp" + StringUtils.date2string(new Date(), "yyyyMMddHHmmss") +
                "_" + Math.round(Math.random() * 10000);
        tempFileName = tempFileName.replace('/', File.separatorChar);
        tempFileName = tempFileName.replace('\\', File.separatorChar);
        String tempMp4FileName = tempFileName + ".t.mp4";
        FileType fileType = FileUtils.getFileType(sourceFileName);
        String cmdLine = "\"C:\\FFMpeg\\bin\\ffmpeg.exe\" " +
                " -y -i \"%sourceFile%\" -ss %startTime% " +
                " -pix_fmt yuv420p" +
                " -vcodec %videoCodec% -profile:v %h264VideoProfile% -level %h264ProfileLevel% -vb %videoBandwith%k " +
                " -r %fps% -s %videoWidth%x%videoHeight%" +
                "  -acodec %audioCodec% -strict -2 -ab %audioBandwith%k -ac %audioChannelCount% -ar %audioSampleHz% -bsf:a aac_adtstoasc";
        //no8x8dct:nocabac:weightp=0:bframes=0
        if(fileFormat!=null){
            fileFormat = fileFormat.toLowerCase();
        }
        boolean isM3U8 = (fileFormat!=null&&fileFormat.contains("m3u8"))||saveFileName.toLowerCase().contains("m3u8");
        if(fileType.equals(FileType.sound)){
            cmdLine = config.getConfig("system.tools.encode.cmdLine4sound", "/usr/local/bin/ffmpeg" +
                    " -y -i \"%sourceFile%\" -bsf:a aac_adtstoasc -acodec aac -strict -2 -ab %audioBandwith%k -ac %audioChannelCount% " +
                    " -ar %audioSampleHz% -flags -global_header -segment_format mpegts -map 0 -map -0:d -map -0:s " +
                    " -f segment -segment_list \"%saveFile%.m3u8\" -segment_time 30 " +
                    " -segment_list_entry_prefix \"%prefix%/\"" +
                    " \"%savePath%/%prefix%/%04d.ts\"");
        }else{
            cmdLine = config.getConfig("system.tools.encode.cmdLine", cmdLine);
            if(isM3U8){
                cmdLine+= " "+ config.getConfig("system.tools.encode.cmdLine.m3u8ExtParameter","-flags -global_header -segment_format mpegts -map 0 -map -0:d -map -0:s -f segment" +
                        "  -segment_list \"%saveFile%.m3u8\" " +
                        "  -segment_time %segmentDuration% -segment_list_flags +live " +
                        "  -segment_list_entry_prefix \"%prefix%/\" " +
                        "  -bsf h264_mp4toannexb \"%savePath%/%prefix%/%04d.ts\"");
            }else{
                cmdLine +=" "+ config.getConfig("system.tools.encode.cmdLine.extParameter","-map 0 -map -0:d -map -0:s \"%saveFile%\"");
            }
        }
        if (cmdLine != null) {
            CommandRunner runner = new CommandRunner();
            String tempAviFileName = tempFileName + ".avi";
            String prefix = desertFile.getName().replace('.','_');
            String savePath = desertFile.getParentFile().getAbsolutePath().replace('\\','/');
            cmdLine = cmdLine.replace("%sourceFile%", sourceFileName);
            cmdLine = cmdLine.replace("%tempFileName%", tempFileName + ".avi");
            cmdLine = cmdLine.replace("%videoCodec%", "" + videoCodec);
            if("copy".equals(videoCodec)){
                cmdLine = cmdLine.replace("-pix_fmt yuv420p","");
                cmdLine = cmdLine.replace("-profile:v %h264VideoProfile%","");
                cmdLine = cmdLine.replace("-level %h264ProfileLevel%","");
                cmdLine = cmdLine.replace("-vcodec %videoCodec%","");
                cmdLine = cmdLine.replace("-vb %videoBandwith%k","");
                cmdLine = cmdLine.replace("-r %fps%","");
                cmdLine = cmdLine.replace("-s %videoWidth%x%videoHeight%","");
            }else{
                cmdLine = cmdLine.replace("%videoBandwith%", "" + videoBandwith);
                cmdLine = cmdLine.replace("%keyframeDuration%", "" + keyframeDuration);
                cmdLine = cmdLine.replace("%fps%", "" + fps);
                cmdLine = cmdLine.replace("%videoWidth%", "" + videoWidth);
                cmdLine = cmdLine.replace("%videoHeight%", "" + videoHeight);
                cmdLine = cmdLine.replace("%clipHeight%", "" + clipHeight);
                cmdLine = cmdLine.replace("%clipWidth%", "" + clipWidth);
                cmdLine = cmdLine.replace("%clipHeight%", "" + clipHeight);
                cmdLine = cmdLine.replace("%clipWidth%", "" + clipWidth);
                cmdLine = cmdLine.replace("%fps%", "" + fps);
                cmdLine = cmdLine.replace("%videoCodecProfile%", profile);
                cmdLine = cmdLine.replace("%h264VideoProfile%", videoProfile);
                cmdLine = cmdLine.replace("%h264ProfileLevel%",""+profileLevel);
            }
            cmdLine = cmdLine.replace("%audioCodec%", "" + audioCodec);
            if("copy".equals(audioCodec)){
                cmdLine = cmdLine.replace("-strict -2","");
                cmdLine = cmdLine.replace("-ab %audioBandwith%k","");
                cmdLine = cmdLine.replace("-ar %audioSampleHz%","");
                cmdLine = cmdLine.replace("-ac %audioChannelCount%","");
            }else{
                cmdLine = cmdLine.replace("%audioBandwith%", "" + audioBandwith);
                cmdLine = cmdLine.replace("%audioSampleHz%", "" + audioSampleHz);
                cmdLine = cmdLine.replace("%videoSampleHz%", "" + audioSampleHz);//这是以前的笔误，放在这里只是兼容
                cmdLine = cmdLine.replace("%audioChannelCount%", "" + audioChannelCount);
            }
            cmdLine = cmdLine.replace("%title%", title);
            cmdLine = cmdLine.replace("%startTime%", "" + startSeconds);
            cmdLine = cmdLine.replace("%duration%", "" + duration);
            cmdLine = cmdLine.replace("%segmentDuration%",""+config.getIntConfig("system.tools.encode.segmentDuration",10));
            cmdLine = cmdLine.replaceAll("%prefix%", prefix);
            cmdLine = cmdLine.replaceAll("%savePath%", savePath);
            String tempAACFileName = tempFileName + ".aac";
            String tempH264FileName = tempFileName + ".264";
            cmdLine = cmdLine.replace("%aacFileName%", tempAACFileName);
            if(isM3U8){
                cmdLine = cmdLine.replace("%saveFile%", desertFile.getAbsolutePath());
            }else{
                cmdLine = cmdLine.replace("%saveFile%", tempMp4FileName);
            }
            cmdLine = cmdLine.replace("%tempH264FileName%", tempH264FileName);
            cmdLine = cmdLine.replace("-s -1x-1"," ");
            cmdLine = cmdLine.replace("-ss 0 "," ");
            try {
                File desertM3U8File = FileUtils.newFile(desertFile.getAbsolutePath()+".m3u8");
                if(isM3U8){
                    if(desertM3U8File.exists()){
                        setLastLog(LOG_LEVEL_WARN, "原来的文件存在，需要删除才行！");
                    }
                    File tsPath = FileUtils.newFile(savePath+"/"+prefix);
                    if(tsPath.exists()&&!tsPath.isDirectory()){
                        setLastLog(LOG_LEVEL_ERROR,"存在一个文件叫‘" +tsPath.getAbsolutePath()+
                                "’，但应该是一个目录，所以删除它！");
                        if(!tsPath.delete()){
                            setLastLog(LOG_LEVEL_ERROR,"存在一个文件叫‘" +tsPath.getAbsolutePath()+
                                    "’，应该是目录，必须删除重建，但是删除失败！");
                        }
                    }
                    if(!tsPath.exists()){
                        if(!tsPath.mkdirs()){
                            setLastLog(LOG_LEVEL_ERROR,"创建目录失败：" +tsPath.getAbsolutePath()+
                                    "");
                        }
                    }
                }else{
                    if (desertFile.exists()) {
                        setLastLog(LOG_LEVEL_WARN, "原来的文件存在，需要删除才行！");
                    }
                    File parent = desertFile.getParentFile();
                    if (!parent.exists()) {
                        setLastLog(LOG_LEVEL_DEBUG, "目录不存在，必须建目录：" + parent.getAbsolutePath());
                        if (!parent.mkdirs()) {
                            setLastLog(LOG_LEVEL_WARN, "无法建立目录：" + parent.getAbsolutePath());
                        }
                    }
                }
                setLastLog(LOG_LEVEL_INFO, "准备执行脚本：" + cmdLine);
                int result = run(cmdLine, tempDir.getAbsolutePath());
                if(isM3U8){
                    if(desertM3U8File.exists()&&desertM3U8File.length()>0){
                        return SUCCESS;
                    }else{
                        setLastLog(LOG_LEVEL_ERROR, "没有生成文件：" + desertM3U8File.getAbsolutePath());
                        return ERROR_COMMAND_LINE;
                    }
                }else{
                    File tempMp4File = FileUtils.newFile(tempMp4FileName);
                    if (tempMp4File.exists()) {
                        int outResult;
                        if (isFileValidate(duration, tempMp4File.getAbsolutePath())) {
                            File resultFile = FileUtils.copy(tempMp4File, desertFile.getParentFile().getAbsolutePath(), desertFile.getName());
                            if (resultFile.exists() && resultFile.length() == tempMp4File.length()) {
                                outResult = SUCCESS;
                            } else {
                                outResult = ERROR_OUT_FILE_ERROR;
                            }
                        } else {
                            setLastLog(LOG_LEVEL_ERROR, "转码结果文件与源文件时长不对，认定这次的转码失败：" + tempMp4File.getAbsolutePath());
                            outResult = ERROR_DURATION;
                        }
                        if (tempMp4File.delete()) {
                            setLastLog(LOG_LEVEL_DEBUG, "临时文件已经删除：" + tempMp4File.getAbsolutePath());
                        } else {
                            setLastLog(LOG_LEVEL_WARN, "临时文件无法删除：" + tempMp4File.getAbsolutePath());
                        }
                        return outResult;
                    } else {
                        setLastLog(LOG_LEVEL_ERROR, "没有生成文件：" + tempMp4File.getAbsolutePath());
                        return ERROR_COMMAND_LINE;
                    }
                }
                //return SUCCESS;
            } catch (IOException e) {
                setLastLog(LOG_LEVEL_ERROR, "发生IO错误：" + e.getMessage());
                e.printStackTrace();
                return ERROR_IO_EXCEPTION;
            } catch (Exception e) {
                setLastLog(LOG_LEVEL_ERROR, "发生未知异常：" + e.getMessage());
                e.printStackTrace();
                return ERROR_UNKNOWN_EXCEPTION;
            }
        } else {
            setLastLog(LOG_LEVEL_ERROR, "命令行为空，不能继续！");
            return ERROR_COMMAND_LINE;
        }
    }

    private int process = 0;
    private int speed = 0;
    private int size = 0;
    private int pos = 0;
    private int duration=100;

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int processLine(String line) {
        //logger.debug("收到："+line);
        int result = 0;
        if (line != null) {
            int i = line.indexOf("Pos:");
            if (i >= 0) {
                line = line.substring(i + 4);
                i = line.indexOf(".");
                if (i > 0) {
                    pos = StringUtils.string2int(line.substring(0, i).trim(), -1);
                    line = line.substring(i + 1);
                    i = line.indexOf("f (");
                    if (i >= 0) {
                        line = line.substring(i + 3);
                        i = line.indexOf(")");
                        if (i > 0) {
                            process = StringUtils.string2int(line.substring(0, i - 1).trim(), -1);
                        }
                    }
                }
            } else {
                i = line.indexOf("%");
                if (i >= 0) {
                    String tempStr = line.substring(0, i).trim();
                    i = tempStr.lastIndexOf(" ");
                    if (i >= 0) {
                        tempStr = tempStr.substring(i);
                    }
                    process = StringUtils.string2int(tempStr.trim(), -1);
                } else {
                    //针对ffmpeg的输出
                    i=line.indexOf("time=");
                    if(i>=0){
                        String tempStr = line.substring(i+5).trim();
                        i = tempStr.indexOf(" ");
                        if (i >= 0) {
                            int time = 0;
                            try {
                                String[] data = tempStr.substring(0,i).trim().split(":");
                                //这是一个类似于 00:00:35 时分秒的一个格式
                                for(String v:data){
                                    try {
                                        time += time*60+Float.parseFloat(v);
                                    } catch (NumberFormatException e) {
                                        logger.error("转换时间发生错误：" + tempStr + "->" + v + "\n" + e.getMessage());
                                    }
                                }
                                pos = time;
                                if(oldPos!=pos){
                                    oldPos = pos;
                                    //logger.debug("pos="+pos);
                                    if(duration>0){
                                        process =pos*100/duration;
                                    }
                                }
                            } catch (NumberFormatException e) {
                                logger.error("转换时间发生错误："+line+"\n"+e.getMessage());
                            }
                        }
                    }
                    //setLastLog(LOG_LEVEL_DEBUG,line);
                }
            }
        }
        if (callback != null) {
            callback.onProcess(process, pos, size, 0, 0);
        } else {
            if (oldProcess != process) {
                oldProcess = process;
                setLastLog(LOG_LEVEL_DEBUG, "process:" + process + ",pos=" + pos + ",size=" + size);
            }
        }
        return result;
    }

    private int oldProcess = -1, oldPos = -1;

    public int run(String cmd) throws IOException {
        return run(cmd, null);
    }
    private CommandRunner runner = new CommandRunner();
    public int run(String cmd, String currentDir) throws IOException {
        int result = 0;
        try {
            runner.runCommand(cmd, currentDir, -1, this);
        } catch (TimeoutException e) {
            setLastLog(LOG_LEVEL_ERROR, "超时错误：" + e.getMessage());
            e.printStackTrace();
            result = -1;
        } catch (InterruptedException e) {
            setLastLog(LOG_LEVEL_ERROR, "中断错误：" + e.getMessage());
            e.printStackTrace();
            result = -2;
        }
        return result;
    }

    /**
     * 获取本机目录映射信息
     * @return 映射
     */
    @SuppressWarnings("unchecked")
    public Map<String,String> getMountedPaths(boolean refreshMaps){
        String cacheName = "allLocalMachineMountPoints";
        if(refreshMaps){
            CacheUtils.clear(cacheName);
        }
        return (Map<String,String>) CacheUtils.get("allLocalMachineMountPoints",cacheName,
                new DataInitWorker(){
                    public  Object init(Object key,String cacheName){
                        Map<String,String> result = new HashMap<String,String>();
                        try {
                            CommandRunner runner = new CommandRunner();
                            List<String> mountInfo = runner.runCommand("mount", null, 10000, null);
                            if (mountInfo != null) {
                                for (String info : mountInfo) {
                                    String[] paths = info.split(" ");
                                    if (paths.length > 3) {
                                        //第一个就是目录
                                        String deviceInfo = paths[0];
                                        String localMountPoint = paths[2];
                                        result.put(deviceInfo,localMountPoint);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            logger.error("无法获取发布点信息"+e.getMessage());
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            logger.error("无法获取发布点信息"+e.getMessage());
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            logger.error("无法获取发布点信息"+e.getMessage());
                            e.printStackTrace();
                        }
                        return result;
                    }
                });
    }

    /**
     * 猜测一个存储上的位置和本机映射后的位置
     * @param fullFileName 存储上的文件名，例如\\192.168.1.20\vod\wasu\1.mp4
     * @return 本机映射后的名字 /20ipsan/wasu/1.mp4
     */
    public String tryGuessServerPath(String fullFileName,boolean refreshPathMaps){
        if (fullFileName == null || "".equals(fullFileName)) {
            return null;
        }
        fullFileName = fullFileName.trim().replace("\\", "/");
        Map<String,String> maps = getMountedPaths(refreshPathMaps);
        for(String deviceInfo :maps.keySet()){
            if (fullFileName.startsWith(deviceInfo)) {
                String localMountPoint = maps.get(deviceInfo);
                fullFileName = localMountPoint + "/" + fullFileName.substring(deviceInfo.length());
                return fullFileName;
            }
        }
        return null;
    }

    /**
     * 多次调用tryGuessServerPath获取最终的数据。某些时候该方法调用会失败，所以尝试3次
     * @param fullFileName 原始名
     * @return 结果
     */
    public String guessServerPath(String fullFileName) {
        int tryTimes = 0;
        String result = null;
        while(tryTimes<3&&result==null){
            tryTimes ++;
            result = tryGuessServerPath(fullFileName,tryTimes>1);
        }
        return result;
    }
    public int encode(String sourceFileName, String saveFileName, String title,
                      String videoCodec, String videoProfile, int profileLevel, int videoBandwith,
                      int keyframeDuration, int fps,
                      int videoWidth, int videoHeight,
                      String audioCodec, int audioBandwith,
                      int audioSampleHz, int audioChannelCount,
                      int startSeconds, int duration, MediaUtilsCallback callback, boolean skipIfExists,
                      String fileFormat){
        AppConfigurator config = AppConfigurator.getInstance();
        if(videoCodec.contains("264")){
            videoCodec = config.getConfig("system.tools.encode.defaultH264Encoder","libx264");
        }
        if(videoCodec.contains("transparent")){
            videoCodec = config.getConfig("system.tools.encode.defaultTransparentName","copy");
        }
        if(audioCodec.contains("aac")){
            audioCodec = config.getConfig("system.tools.encode.defaultAACEncoder","aac");
        }
        if(audioCodec.contains("transparent")){
            audioCodec = config.getConfig("system.tools.encode.defaultTransparentName","copy");
        }
        return encodeMedia(sourceFileName, saveFileName, title, videoCodec, videoProfile, profileLevel, videoBandwith,
                keyframeDuration, fps, videoWidth, videoHeight, audioCodec, audioBandwith, audioSampleHz, audioChannelCount,
                startSeconds, duration, callback, skipIfExists,fileFormat);
    }

    public int encodeFFMPEG(String sourceFileName, String saveFileName, String title,
                            String videoCodec, String videoProfile, int profileLevel, int videoBandwith,
                            int keyframeDuration, int fps,
                            int videoWidth, int videoHeight,
                            String audioCodec, int audioBandwith,
                            int audioSampleHz, int audioChannelCount,
                            int startSeconds, int duration, MediaUtilsCallback callback, boolean skipIfExists){
        if(videoCodec.contains("264")){
            videoCodec = "libx264";
        }
        if(audioCodec.contains("aac")){
            audioCodec = "aac";
        }
        return encodeMedia(sourceFileName, saveFileName, title, videoCodec, videoProfile, profileLevel, videoBandwith,
                keyframeDuration, fps, videoWidth, videoHeight, audioCodec, audioBandwith, audioSampleHz, audioChannelCount,
                startSeconds, duration, callback, skipIfExists,"m3u8");
    }
    public int stopEncode(){
        logger.debug("发送停止指令！");
        if(runner!=null){
            runner.sendCommand("q\r\n");
            logger.debug("停止指令已经发送");
        }else{
            logger.warn("可能是尚未启动，不能发送指令！");
        }
        return SUCCESS;
    }
    public int snap(String fileName, String picFileName, int cutTime) throws Exception {
        if (picFileName == null || "".equals(picFileName)) {
            return ERROR_FILE_NOT_EXISTS;
        }
        AppConfigurator appConfig = AppConfigurator.getInstance();
        //setLastLog(LOG_LEVEL_DEBUG,"将会保存的文件名："+picFileName);
        //最终文件名。因为mplayer生成的都是00000001.jpg，所以需要我们处理一下。
        String snapToolsExeCmdLine = appConfig.getConfig("system.tools.snap.cmdLine",
                "C:/FFModules/mplayer.exe" +
                        " -nosound -vo jpeg -frames 1 -ss %startTime% \"%sourceFile%\"");

        String result = "";
        try {

            fileName = fileName.replace("/", File.separator);
            //setLastLog(LOG_LEVEL_DEBUG,"将要截取的文件为："+fileName);
            File sourceFile = FileUtils.newFile(fileName);
            if (sourceFile.exists()) {
                String desertPicFileName = FileUtils.extractFileName(picFileName, "/");
                String picFilePath = FileUtils.extractFilePath(picFileName, "/");
                String snapDirName = picFilePath + File.separator;//appConfig.getConfig("snap.Dir", application.getRealPath("/"))+picFilePath+ File.separator ;
                snapDirName = snapDirName.replace("/", File.separator);
                snapDirName = snapDirName.replace("/", File.separator);
                File picFile = FileUtils.newFile(picFileName);
                File path = picFile.getParentFile();
                if(!path.exists()){
                    if(path.mkdirs()){
                        setLastLog(LOG_LEVEL_DEBUG,"创建目录成功:"+path.getAbsolutePath());
                    }else{
                        setLastLog(LOG_LEVEL_ERROR,"创建目录失败："+path.getAbsolutePath());
                        result+=""+path.getAbsolutePath();
                        throw new Exception(result);
                    }
                }else{
                    setLastLog(LOG_LEVEL_DEBUG,"目录已经存在："+path.getAbsolutePath());
                }

                String commandLine = snapToolsExeCmdLine.replaceAll("%startTime%", "" + cutTime).replace("%sourceFile%", sourceFile.getAbsolutePath()).replace("%resultFile%",picFile.getAbsolutePath());
                setLastLog(LOG_LEVEL_DEBUG, "将要执行的命令行：" + commandLine);
                if(!snapToolsExeCmdLine.toLowerCase().contains("mplayer")){
                    run(commandLine, snapDirName);
                    //picFile = FileUtils.newFile(picFi);
                    if(picFile.exists()){
                        setLastLog(LOG_LEVEL_DEBUG,"文件生成："+picFile.getAbsolutePath());
                        return SUCCESS;
                    }else{
                        result+=("无法生成截图文件，命令行为："+commandLine);
                    }
                }else{
                    String snapTempDirName = snapDirName + "temp_" + System.currentTimeMillis() + "_" + Math.round(Math.random() * 10000000);
                    String autoPicFileName = snapTempDirName + File.separator + "00000001.jpg";
                    //setLastLog(LOG_LEVEL_DEBUG,"自动生成的文件名："+autoPicFileName);
                    File snapDir = FileUtils.newFile(snapTempDirName);
                    if (!snapDir.exists()) {
                        setLastLog(LOG_LEVEL_DEBUG, "目录不存在，正在生成...：" + snapTempDirName);
                        if (snapDir.mkdirs()) {
                            setLastLog(LOG_LEVEL_DEBUG,"创建目录成功："+snapDir.getAbsolutePath());
                        } else {
                            setLastLog(LOG_LEVEL_WARN, "创建目录失败：" + snapDir.getAbsolutePath());
                        }
                    }

                    run(commandLine, snapDir.getAbsolutePath());
                    picFile = FileUtils.newFile(autoPicFileName);
                    if (picFile.exists()) {
                        //response.sendRedirect(picFileName);
                        File desertPicFile = FileUtils.newFile(snapDirName + desertPicFileName);
                        if (desertPicFile.exists()) {
                            if (desertPicFile.delete()) {
                                setLastLog(LOG_LEVEL_DEBUG, "原来的文件删除，做替换：" + desertPicFile.getAbsolutePath());
                            } else {
                                setLastLog(LOG_LEVEL_WARN, "删除失败，后续操作可能无法完成：" + desertPicFile.getAbsolutePath());
                            }
                        }

                        setLastLog(LOG_LEVEL_DEBUG, "准备重命名：[" + picFile.getAbsolutePath() + "]->[" + desertPicFile.getAbsolutePath() + "]");
                        if (picFile.renameTo(desertPicFile)) {
                            setLastLog(LOG_LEVEL_DEBUG, "重命名完成：" + picFile.getAbsolutePath());
                        } else {
                            setLastLog(LOG_LEVEL_ERROR, "重命名失败：" + picFile.getAbsolutePath());
                        }
                        if (snapDir.delete()) {
                            setLastLog(LOG_LEVEL_DEBUG, "临时文件删除完成：" + snapDir.getAbsolutePath());
                        } else {
                            setLastLog(LOG_LEVEL_ERROR, "临时文件删除：" + snapDir.getAbsolutePath());
                        }
                        return SUCCESS;
                    } else {
                        if (snapDir.delete()) {
                            setLastLog(LOG_LEVEL_DEBUG, "临时文件删除完成：" + snapDir.getAbsolutePath());
                        } else {
                            setLastLog(LOG_LEVEL_ERROR, "临时文件删除：" + snapDir.getAbsolutePath());
                        }
                        result = "生成" + picFileName + "错误：" + result;
                        setLastLog(LOG_LEVEL_ERROR, "因为某些原因生成文件错误，生成日志：" + result);
                    }
                }
            } else {
                setLastLog(LOG_LEVEL_ERROR, "WMV文件不存在：" + fileName);
                result = "WMV文件不存在：" + fileName;
            }
        } catch (Exception e) {
            setLastLog(LOG_LEVEL_ERROR, "发生异常：" + e.getMessage());
            result += "异常：" + e.getMessage();
            //e.printStackTrace();
        }
        throw new Exception(result);
    }

    public static SimpleFileInfo getMediaFileInfoByFFPROBE(File file,SimpleFileInfo fileInfo){
        if(!file.exists()){
            System.err.println("File not exist:"+file.getAbsolutePath());
            return null;
        }
        if(fileInfo==null){
            fileInfo = new SimpleFileInfo(file);
        }
        String cmdOfGetFileInfo = AppConfigurator.getInstance().getConfig("system.tools.cmdOfGetFileInfoXmlFormat",
                "/usr/local/bin/ffprobe -i \"%sourceFile%\" -v quiet -show_streams -print_format xml");
        String cmdLine = cmdOfGetFileInfo.replace("%sourceFile%",file.getAbsolutePath());
        CommandRunner runner = new CommandRunner();

        try {
            List<String> runResult = runner.runCommand(cmdLine,null,15000,null);
            StringBuilder sb = new StringBuilder();
            boolean foundTail = false;
            String tail = "</ffprobe>";
            for(String line:runResult){
                if(line.contains(tail)){
                    foundTail = true;
                }
                sb.append(line).append("\r\n");
            }
            if(!foundTail){
                sb.append(tail);
            }

            Element root = XmlUtils.getRootFromXmlStr(sb.toString());
            if(root!=null){
                List streams = root.selectNodes("streams/stream");
                float length = 0.0f;
                for(Object o:streams){
                    Node stream = (Node)o;
                    String codecType = XmlUtils.getValue(stream,"@codec_type",null);
                    if("audio".equals(codecType)){
                        fileInfo.setAudioCodec(XmlUtils.getValue(stream,"@codec_name",null));
                        String durationString = XmlUtils.getValue(stream,"@duration",null);
                        if(durationString!=null&&!"".equals(durationString.trim())&&!"0.0".equals(durationString)){
                            length = Float.parseFloat(durationString);
                        }
                    }else if("video".equals(codecType)){
                        fileInfo.setVideoCodec(XmlUtils.getValue(stream,"@codec_name",null));
                        fileInfo.setWidth(XmlUtils.getIntValue(stream,"@width",-1));
                        fileInfo.setHeight(XmlUtils.getIntValue(stream,"@height",-1));
                        fileInfo.setLength(Float.parseFloat(XmlUtils.getValue(stream,"@duration","0.0")));
                    }
                }
                if(fileInfo.getLength()<=0.0f){
                    fileInfo.setLength(length);
                }
                Node format = root.selectSingleNode("format");
                if(format!=null){
                    fileInfo.setFormat(XmlUtils.getValue(format,"@format_name",null));
                }
            }
            return fileInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean snapContentPosterFromMediaFile(String sourceClipUrl,
                                                  String saveImageFileName,
                                                  int width, int height, int snapPosSeconds) {

        try {
            String tempFileName = saveImageFileName + ".temp.jpg";
            int result = snap(sourceClipUrl, tempFileName, snapPosSeconds);
            if (result != SUCCESS) {
                return false;
            }
            File picFile = FileUtils.newFile(tempFileName);
            if (picFile.exists()) {
                ImageControl ic = new ImageControl();
                BufferedImage img = ic.loadImageLocal(tempFileName);
                int picWidth = img.getWidth();
                int picHeight = img.getHeight();
                //计算缩放，如果目标长度小于按比例缩放后的长度，则进行裁剪。
                float rate0 = picWidth * 1.0f / picHeight;
                float rate1 = width * 1.0f / height;
                int newWidth, newHeight, posX, posY;
                if (rate0 > rate1) {//如果目标图片比例偏竖型，则以目的高度为准，进行裁剪
                    newHeight = height;
                    newWidth = picWidth * height / picHeight;
                    posY = 0;
                    posX = (newWidth - width) / 2;
                } else {
                    newWidth = width;
                    newHeight = picHeight * width / picWidth;
                    posX = 0;
                    posY = (newHeight - height) / 2;
                }
                BufferedImage resizeImg = ic.modifySize(img, newWidth, newHeight);
                //ic.writeImageLocal(resizeImg,saveImageFileName+"." +newWidth+"x"+newHeight+".jpg");
                BufferedImage cutImg = ic.cutImage(resizeImg, posX, posY, width, height);
                ic.writeImageLocal(cutImg, saveImageFileName);
                picFile.delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastLog(LOG_LEVEL_ERROR, "发生异常：" + e.getMessage());
        }
        return false;
    }

    private Logger logger = Logger.getLogger(this.getClass());

    public void onProcess(int process, int pos, int size, int cpu, int memory) {
        if (oldProcess != process) {
            oldProcess = process;
            setLastLog(LOG_LEVEL_DEBUG, "process:" + process + ",pos=" + pos + ",size=" + size);
        }
    }

    public void onSetLength(int length, Date fileDate, long fileSize) {
        setLastLog(LOG_LEVEL_DEBUG, "影片长度：" + StringUtils.formatTime(length) + ",影片文件大小：" +
                StringUtils.formatBytes(fileSize) + "" + StringUtils.date2string(fileDate));
    }

    public void onLog(String log) {
        setLastLog(LOG_LEVEL_DEBUG, log);
    }

    public static void main(String[] params) {
        MediaUtils mediaUtils = new MediaUtils();
        //logger.info("wmvSplit:" + cut("E:/TDDownload/Adult/201204/0001.wmv", "c:/0002.wmv", 20, 60));
///*
        int duration = 300;
        int startTime = 0;
        //String[] sources = new String[]{"E:/movie/input/1.wmv","E:/movie/input/2.wmv","E:/movie/input/3.wmv"};
        String[] sources = new String[]{"F:\\movie\\wHHLPFJI_20150627113046_68123.mp4"};//,"E:/movie/mp4/007GoldEye.mp4","E:/movie/mp4/科学怪狗-1200.mp4"};
        String savePath = "F:\\temp\\";
/*
        mediaUtils.encodeTranscoder(source,
                "E:/temp/变形金刚2.bl.320x180.mp4", "测试",
                "x264", "baseline", 31, 300, 2, 20, 320, 180, "faac", 32, 48000, 2, startTime, duration, null);
*/

///*
        int width = 800, height = 480;
        int blLevel = 31;
        int blFrameRate = 25;
        int videoBandwidth = 1200;
        int audioBandwidth = 128;
        int[][] codecs = new int[][]{
                {width,height,blLevel,blFrameRate,videoBandwidth,audioBandwidth},
                {width/2,height/2,blLevel,15,300,64}
        };
        for (String source : sources) {
            String exportFile = HzUtils.getFirstSpell(FileUtils.extractFileName(source, "/"));
            for(int[] codec:codecs){
                width = codec[0];
                height= codec[1];
                blLevel = codec[2];
                blFrameRate = codec[3];
                videoBandwidth = codec[4];
                audioBandwidth = codec[5];
                int encodeResult = mediaUtils.encode(source,
                        savePath + exportFile +
                                ".high" + blLevel +
                                "." + width + "x" + height +
                                ".v" + videoBandwidth + "k" + ".a" + audioBandwidth +
                                "k." + blFrameRate +
                                "fps.mp4", "测试",
                        "x264", "high", blLevel, videoBandwidth, 2, blFrameRate, width, height,
                        "faac", audioBandwidth, 48000, 1, startTime, duration, mediaUtils,false,"m3u8");
                System.out.println(
                        "encodeResult=" + encodeResult + "," +
                                MediaUtils.errorCodes.get(encodeResult));
            }
/*
            System.out.println(MediaUtils.errorCodes.get(mediaUtils.encodeMedia(source,
                    savePath +exportFile+
                            ".mp.320x240.30.mp4", "测试",
                    "x264","main_profile",30, 320, 2, 15, 320,240, "faac",64, 48000, 1, startTime, duration,mediaUtils)));
            System.out.println(MediaUtils.errorCodes.get(mediaUtils.encodeMedia(source,
                    savePath +exportFile+
                            ".hp.320x240.31.mp4", "测试",
                    "x264","high_profile",31, 320, 2, 15, 320,240, "faac",64, 48000, 1, startTime, duration,mediaUtils)));
*/
        }
/*
        mediaUtils.encodeMedia(source,
                "E:/temp/0002.500.mp4", "测试",
                "x264","main_profile", 450, 2, 20, 640, 360, "faac", 64, 48000, 2, startTime, duration,null);
        mediaUtils.encodeMedia(source,
                "E:/temp/0002.1024.mp4", "测试",
                "x264","high_profile", 900, 2, 30,1280,720, "faac", 128, 48000, 2, startTime, duration,null);
*/
//*/
//*/
/*
        try {
            mediaUtils.run("E:/temp/worker.bat  E:/TDDownload/Adult/201110/75575AC7d01.mp4  " +
                    "\"E:/temp/_FFtemp20120728063423_7491.avi\" " +
                    "\"-vf scale=640:360,expand=640:360,harddup -af channels=2 -ofps 25 -srate 48000" +
                    " -oac faac -faacopts br=64:mpeg=4:object=2 -ovc x264 -ffourcc avc1" +
                    " -x264encopts bitrate=450:me=hex:level_idc=21:keyint=250:frameref=1" +
                    ":bframes=0:nocabac:threads=auto -mc 3" +
                    " -subfont C:/Windows/Fonts/simhei.ttf -subpos 100 -subcp cp936,UTF-8,UTF-16" +
                    " -subfont-text-scale 4" +
                    " -ss 0 -endpos 10\" e:/temp/1.aac e:/temp/1.264 e:/temp/1.mp4 25");
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
}
