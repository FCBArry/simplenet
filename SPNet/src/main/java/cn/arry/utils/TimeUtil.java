package cn.arry.utils;

import cn.arry.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 */
public class TimeUtil {
    private static final ThreadLocal<SimpleDateFormat> dateTimeFormat;

    private static final ThreadLocal<SimpleDateFormat> dateFormat;

    public static final String DEFAULT_TIME = "1970-01-01 00:00:00";

    static {
        dateTimeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    }

    /**
     * 获得当前时间的毫秒数
     */
    public static long getDDTCurTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前Date
     */
    public static Date getCurrentDate() {
        Calendar cal = getCalendar();
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 获取系统时间
     */
    private static Calendar getCalendar() {
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());
        return nowCalendar;
    }

    /**
     * 获取指定的时间
     */
    public static Calendar getCalendar(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    public static Date toDate(String date) {
        if (StringUtil.isNullOrEmpty(date)) {
            return null;
        }

        try {
            return dateTimeFormat.get().parse(date);
        } catch (Exception e) {
            Log.error("TimeUtil->toDate error, date:{}", date, e);
            return null;
        }
    }

    /**
     * 判断时间是否在当前时间段内
     */
    public static boolean betweenDate(Date startDate, Date endDate, Date nowDate) {
        if (startDate == null || endDate == null || nowDate == null)
            return false;

        Calendar cStart = getCalendar(startDate);
        Calendar cEnd = getCalendar(endDate);
        Calendar cNow = getCalendar(nowDate);
        return cStart.getTimeInMillis() <= cNow.getTimeInMillis() && cNow.getTimeInMillis() <= cEnd.getTimeInMillis();
    }

    /**
     * 判断时间是否在当前时间段内
     */
    public static boolean betweenDate(String startDate, String endDate, long time) {
        if (startDate == null || endDate == null) {
            return false;
        }

        if (startDate.equals("0") && endDate.equals("0")) {
            return true;
        }

        if (startDate.equals("0") && !endDate.equals("0")) {
            Date date = toDate(endDate);
            return date.getTime() >= time;
        }

        if (!startDate.equals("0") && endDate.equals("0")) {
            Date date = toDate(startDate);
            return date.getTime() <= time;
        }

        return betweenDate(toDate(startDate), toDate(endDate), new Date(time));
    }

    public static boolean betweenDate(String startDate, String endDate) {
        return betweenDate(startDate, endDate, getDDTCurTimeMillis());
    }
}
