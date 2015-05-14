package com.jsy.utility

/**
 * ����ʱ�乤����
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
     * ��ȡ���ڲ���,���ڵ����ʱ��
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
            Date date2 = new Date(date.getTime() - gc.get(gc.HOUR_OF_DAY) * 60 * 60
                    * 1000 - gc.get(gc.MINUTE) * 60 * 1000 - gc.get(gc.SECOND)
                    * 1000 - 24 * 60 * 60 * 1000);
            return date2;
        }
    }
}
