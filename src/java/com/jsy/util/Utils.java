package com.jsy.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by oswaldl on 3/13/15.
 */
public class Utils {

    public static Date addYears(final java.sql.Timestamp date, final int years) {
        Date calculatedDate = null;

        if (date != null) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, years);
            calculatedDate = calendar.getTime();
        }

        return calculatedDate;
    }

    public static Date addYears(final java.util.Date date, final int years) {
        Date calculatedDate = null;

        if (date != null) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, years);
            calculatedDate = calendar.getTime();
        }

        return calculatedDate;
    }

    public static long dayDifferent(Date dateStart,Date dateStop) throws Exception{
        if(dateStart.after(dateStop)){
            throw new Exception("dateStart after dateStop");
        }

        //毫秒ms
        long diff = dateStop.getTime() - dateStart.getTime();

        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffDays + 1; // 每个都加1天，黎姐要求
    }

}
