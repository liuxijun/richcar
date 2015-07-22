package com.fortune.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-8-12
 * Time: 11:54:17
 */
public class CommandRunner {
    private String commandLine;
    private String lastError;
    private String lastResult;
    Logger logger = Logger.getLogger(getClass());

    public void setCommandLine(String aCommandLine) {
        commandLine = aCommandLine;
    }

    public void setLastError(String aLastError) {
        lastError = aLastError;
    }

    public void setLastResult(String aLastResult) {
        lastResult = aLastResult;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public String getLastError() {
        return lastError;
    }

    public String getLastResult() {
        return lastResult;
    }
    private Process process=null;
    public void sendCommand(String cmd){
        if(process!=null){
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                bw.write(cmd);
                bw.flush();
                bw.close();
                logger.debug("指令已经发送："+cmd);
            } catch (IOException e) {
                logger.error("无法发送指令："+e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("无法发送指令："+e.getMessage());
                e.printStackTrace();
            }
        }else{
            logger.error("尚未启动进程，无法发送指令："+cmd);
        }
    }

    public String run(String sCommandLine, String currentDirName) {
        String result = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            File currentDir = new File(currentDirName);
            Process ps;
            if (currentDir.exists() && currentDir.isDirectory()) {
                ps = runtime.exec(sCommandLine, null, currentDir);
            } else {
                ps = runtime.exec(sCommandLine);
            }
//           ps.waitFor();
            result = (loadStream(ps.getInputStream()));
            setLastResult(sCommandLine);
            setLastError(loadStream(ps.getErrorStream()));
        } catch (Exception e) {
            setLastError(e.getMessage());
        }
        return result;
    }

    public String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        String Result = new String(buffer.toString().getBytes("iso-8859-1"), "gb2312");
        return Result;
    }

    /**
     * 执行命令行
     *
     * @param cmd 命令行
     * @return 返回数组
     * @throws IOException
     */
    public List<String> runCommand(String cmd,String currentDir, int timeout,final CommandRunnerOutlineCallbackable callbackable) throws IOException, TimeoutException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        List<String> commandLine = new ArrayList<String>();
        final List<String> result = new ArrayList<String>();
        //String[] allCmd = cmd.split(" ");
        //Collections.addAll(commandLine, allCmd);
        if(cmd==null){
            return null;
        }
        cmd = cmd.trim();
        if(currentDir==null){
            currentDir = "/";
        }
        builder.directory(new File(currentDir));

/*
        int p=cmd.indexOf(" ");
        if(p>0){
            String parameters = cmd.substring(p+1).trim();
            cmd = cmd.substring(0,p);
            commandLine.add(cmd);
            commandLine.add(parameters);
        }else{
            commandLine.add(cmd);
        }
*/
        //扫描命令行，分解出一个个的参数
        int i=0,l=cmd.length();
        String param = "";
        boolean paramStart = false;
        while(i<l){
            char ch = cmd.charAt(i);
            if(ch=='"'){
                if(paramStart){
                    paramStart = false;
                    commandLine.add(param);
                }else{
                    paramStart = true;
                }
                param = "";
            }else if(ch==' '){
                if(paramStart){
                    param+=ch;
                }else{
                    if(!"".equals(param)){
                        commandLine.add(param);
                        param = "";
                    }
                }
            }else{
                param += ch;
            }
            i++;
        }
        if(!"".equals(param)){
            commandLine.add(param);
        }
/*
        for(String c:commandLine){
             logger.debug("执行参数："+c);
        }
*/
        logger.debug("启动：" + cmd);
        builder.command(commandLine);
        process = builder.start();
        Date startTime = new Date();
        final InputStream is1 = process.getInputStream();
        final InputStream is2 = process.getErrorStream();
        //final OutputStream out = process.getOutputStream();
        new Thread() {
            public void run() {
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(is1));
                try {
                    String lineB;
                    while ((lineB = br.readLine()) != null) {
                        //logger.debug("inputStream - "+lineB);
                        if(callbackable!=null){
                           callbackable.processLine(lineB);
                        }else{
                            result.add(lineB);
                            logger.debug("inputStream - "+lineB);
                        }
                        //result.add(lineB);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            public void run() {
                BufferedReader br2 = new BufferedReader(new
                        InputStreamReader(is2));
                try {
                    String lineC;
                    while ((lineC = br2.readLine()) != null) {
                        //logger.debug("errorStream - "+lineC);
                        if(callbackable!=null){
                            callbackable.processLine(lineC);
                        }else{
                            result.add(lineC);
                            logger.debug("inputStream - "+lineC);
                        }
                        //result.add(lineC);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        if(callbackable!=null){
            result.add("结果已经通过callback返回！Result has been returned by callback interface!");
        }
        //int resultCode = -2;
        Worker worker = new Worker(process);
        //startTime = new Date();
        worker.start();
        try {
            if(timeout<=0){
                timeout = Integer.MAX_VALUE;
            }
            worker.join(timeout);
            if (worker.exit != null) {

            } else {
                throw new TimeoutException("执行命令超时："+cmd+",启动时间："+StringUtils.date2string(startTime)+"," +
                        "当前时间："+StringUtils.date2string(new Date()));
            }
        } catch (InterruptedException ex) {
            logger.error("发生中断，可能是超时错误:" +cmd+
                    ",执行启动时间："+StringUtils.date2string(startTime)+",exception="+ex.getMessage());
            worker.interrupt();
            Thread.currentThread().interrupt();
            //resultCode = -2;
            throw ex;
        } finally {
            try {
                is1.close();
                is2.close();
                process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.debug("执行完毕："+cmd);
            process = null;
        }

        return result;
    }

    private static class Worker extends Thread {

        private final Process process;

        private Integer exit;

        private Worker(Process process) {
            this.process = process;
        }

        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException ignore) {
            }
        }
    }

    public static void main(String args[]) {

    }
}