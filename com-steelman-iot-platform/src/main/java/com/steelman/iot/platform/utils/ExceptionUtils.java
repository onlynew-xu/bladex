package com.steelman.iot.platform.utils;

public class ExceptionUtils {

    public static String getStackMsg(Exception e,String api) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        String s = sb.toString();
        String msg="接口:"+api+" 发生了异常";
        s="\n"+msg+"\n"+e.toString()+"\n"+s;
        return s ;
    }

    private static String getStackMsg(Throwable e,String api) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        String s=sb.toString();
        String msg="接口:"+api+" 发生了异常";
        s="\n"+msg+"\n"+e.toString()+"\n"+s;
        return  s;
    }





}
