package com.steelman.iot.platform.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtils {
    /**
     * 获取周天数 周一，周二
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static String getWeek(String dateStr) {
        Date datel=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(datel);
        if(dateStr.equals(format)){
            return "今日";
        }
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 获取周天数
     * @param date
     * @return
     * @throws Exception
     */
    public static String getWeek(Date date) throws Exception{
        String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 获取过去几天前的日期
     * @param date
     * @param pass
     * @return
     */
    public static String getPastDateStr(Date date,int pass){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH,-pass);
        Date time = instance.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(time);
        return format;
    }

    /**
     * 获取过去几天的日期
     * @param date
     * @param pass
     * @return
     */
    public static Date getPastDate(Date date,int pass){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH,-pass);
        Date time = instance.getTime();
        return time;
    }
    public static Date getLastMonthDate(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MONTH,-1);
        Date time = instance.getTime();
        return time;
    }
    public static Date getLastHalfYearDate(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MONTH,-6);
        Date time = instance.getTime();
        return time;
    }
    public static Date getLastYearDate(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.YEAR,-1);
        Date time = instance.getTime();
        return time;
    }
    public static String getForwardDateStr(Date date,int pass){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH,pass);
        Date time = instance.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(time);
        return format;
    }
    public static Date getForwordDate(Date date,int pass){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH,pass);
        Date time = instance.getTime();
        return time;
    }
    public static Date[] getPastDateArr(Date date,int pass){
        Date[] dates=new Date[pass];
        for (int i = pass; i > 0; i--) {
            Date pastDate = getPastDate(date, i);
            dates[pass-i]=pastDate;
        }
        return dates;
    }
    public static String[] getPastDateStrArr(Date date,int pass){
        String[] dates=new String[pass+1];
        for (int i = pass; i >= 0; i--) {
            String pastDate = getPastDateStr(date, i);
            dates[pass-i]=pastDate;
        }
        return dates;
    }

    public static Boolean isMonthDay(Integer month,Integer day,Integer year){
        Set<Integer> month31=new HashSet<>();
        Set<Integer> month30=new HashSet<>();
        month31.add(1);
        month31.add(3);
        month31.add(5);
        month31.add(7);
        month31.add(8);
        month31.add(10);
        month31.add(12);
        month30.add(4);
        month30.add(6);
        month30.add(9);
        month30.add(11);
        Boolean aBoolean = yunNan(year);
        if(aBoolean){
           if(month==2){
               if(day>29){
                   return false;
               }else{
                   return true;
               }
           }else{
               if(month30.contains(month)){
                   if(day>30){
                       return true;
                   }else{
                       return false;
                   }
               }
               if(month31.contains(month)){
                   if(day>31){
                       return true;
                   }else{
                       return false;
                   }
               }
           }
        }else{
            if(month==2){
                if(day>28){
                    return false;
                }else{
                    return true;
                }
            }
            if(month30.contains(month)){
                if(day>30){
                    return true;
                }else{
                    return false;
                }
            }
            if(month31.contains(month)){
                if(day>31){
                    return true;
                }else{
                    return false;
                }
            }

        }


        return  true;
    }

    public static   Boolean yunNan(Integer year){
        if(year%4==0 && year%100!=0) {
            return true;
        }else if(year%400==0){
            return true;
        }else {
            return  false;
        }
    }
    public static String[] getPastDay(Integer day){
        if(day<7){
            String[] dateDay=new String[day];
            for (int i = 1; i <day+1 ; i++) {
                String s = String.valueOf(i);
                dateDay[i-1]="0"+s;
            }
            return dateDay;
        }else{
            String[] dateDay=new String[7];
            for (int i = 6; i <=0 ; i--) {
                String s = String.valueOf(day - i);
                if(day - i<10){
                    s="0"+s;
                }
                dateDay[6-i]=s;
            }
            return dateDay;
        }

    }
    public static String[] getPastSixYearMonth(){
        Calendar instance = Calendar.getInstance();
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
        String format = simpleDateFormat.format(date);
        String[] yearMonth=new String[6];
        for (int i = 0; i <5 ; i++) {
            instance.add(Calendar.MONTH,-1);
            int month = instance.get(Calendar.MONTH)+1;
            int year = instance.get(Calendar.YEAR);
            StringBuffer yearMonthBuf=new StringBuffer();
            yearMonthBuf.append(year);
            yearMonthBuf.append("-");
            if(month<10){
                yearMonthBuf.append("0");
            }
            yearMonthBuf.append(month);
            yearMonth[4-i]=yearMonthBuf.toString();
        }
        yearMonth[5]=format;
        return yearMonth;


    }

    public static String format(long time, String pattern) {
        Timestamp timestamp=new Timestamp(time);
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(dateTime);
    }

    public static List<String> getDayHour() {
        List<String> dayHourList=new ArrayList<>();
        dayHourList.add("01");
        dayHourList.add("02");
        dayHourList.add("03");
        dayHourList.add("04");
        dayHourList.add("05");
        dayHourList.add("06");
        dayHourList.add("07");
        dayHourList.add("08");
        dayHourList.add("09");
        dayHourList.add("10");
        dayHourList.add("11");
        dayHourList.add("12");
        dayHourList.add("13");
        dayHourList.add("14");
        dayHourList.add("15");
        dayHourList.add("16");
        dayHourList.add("17");
        dayHourList.add("18");
        dayHourList.add("19");
        dayHourList.add("20");
        dayHourList.add("21");
        dayHourList.add("22");
        dayHourList.add("23");
        dayHourList.add("24");
        return dayHourList;
    }

    public static List<String> getMonth() {
        List<String> dayHourList=new ArrayList<>();
        dayHourList.add("01");
        dayHourList.add("02");
        dayHourList.add("03");
        dayHourList.add("04");
        dayHourList.add("05");
        dayHourList.add("06");
        dayHourList.add("07");
        dayHourList.add("08");
        dayHourList.add("09");
        dayHourList.add("10");
        dayHourList.add("11");
        dayHourList.add("12");
        return dayHourList;
    }
}
