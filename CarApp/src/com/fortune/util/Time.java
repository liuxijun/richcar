package com.fortune.util;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-9-8
 * Time: 上午11:09
 * 日期格式基础类: 00:00:00格式，到秒
 *
 */
public class Time {
    private int seconds;
    public Time(int seconds){
        this.seconds = seconds;
    }

    public Time(String secondsStr){
        this.seconds = 0;
        if(secondsStr!=null){
            String[] secArray = secondsStr.split(":");
            for(String data:secArray){
                try {
                    int intValue = Integer.parseInt(data);
                    if(intValue>=60){
                        intValue = 59;
                    }
                    if(intValue<0){
                        intValue = 0;
                    }
                    seconds = intValue+seconds*60;
                } catch (NumberFormatException e) {
                    break;
                }

            }
        }

    }
    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    public String toString(){
        String result = "";
        int tempSeconds = seconds;
        int repeatTimes = 0;
        while(repeatTimes<3){
            if(!"".equals(result)){
                result = ":"+result;
            }
            repeatTimes ++;

            String tempStr = ""+tempSeconds %60;
            while(tempStr.length()<2){
                tempStr = "0"+tempStr;
            }
            result=tempStr+result;
            tempSeconds /=60;
            /* if(seconds == 0){
                break;
            }*/
        }
        return result;
    }
}
