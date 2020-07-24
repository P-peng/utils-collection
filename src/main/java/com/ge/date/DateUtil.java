package com.ge.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 */
public class DateUtil {

    /**
     * 日期加减天数
     *
     * @param date 时间
     * @param day  移动的天数
     * @return
     */
    public Date moveTime(Date date, int day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(calendar.DATE, day);
        return calendar.getTime();
    }


    /**
     * 通过时间秒毫秒数判断两个时间的间隔天数
     *
     * @param date1
     * @param date2
     * @return 返回相差的天数
     */
    public static int differentDaysByMillisecond(Date date1,Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
    }
}
