package com.electric.noinvade.util;

import java.util.Calendar;

public class TimeUtil {

    public static long nano = 1000000000L;

    private static void  clearTime(Calendar cal) {
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
    }

    public static long getMonthFirstDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        clearTime(calendar);
        return calendar.getTimeInMillis()/1000*nano;
    }

    public static long getDayZeroTime(int dayBefore){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-dayBefore);
        clearTime(calendar);
        return calendar.getTimeInMillis()/1000*nano;
    }

    public static long getTodayHourTime(int hour){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTimeInMillis()/1000*nano;
    }

    public static int getHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public static int getDay(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
