package com.jsy.utility

/**
 * 日期时间工具类
 * Created by lioa on 2015/5/14.
 */
class DateUtility {
    public static boolean areSameDay(Date dateA, Date dateB) {
        if (dateA == null || dateB == null) return false
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR) && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH) && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期部分,日期的零点时间
     * @param date
     * @return
     */
    public static Date lastDayWholePointDate(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        if ((gc.get(gc.HOUR_OF_DAY) == 0) && (gc.get(gc.MINUTE) == 0)
                && (gc.get(gc.SECOND) == 0)) {
//            return new Date(date.getTime() - (24 * 60 * 60 * 1000));
            return new Date(date.getTime());
        } else {
            Date date2 = new Date(date.getTime() - gc.get(gc.HOUR_OF_DAY) * 60 * 60 * 1000 - gc.get(gc.MINUTE) * 60 * 1000 - gc.get(gc.SECOND) * 1000);
            return date2;
        }
    }

    /**
     * 对日期增加几个月
     * @param date 原日期
     * @param months 要增加的月数
     * @return 返回新的日期（没有时间信息）
     */
    public static Date addMonth(Date date, int months) {
        def rightNow = Calendar.getInstance()
        rightNow.setTime(date)
        rightNow.add(Calendar.MONTH, months)
        return lastDayWholePointDate(rightNow.getTime())
    }
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getCurrYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getCurrYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }
}
