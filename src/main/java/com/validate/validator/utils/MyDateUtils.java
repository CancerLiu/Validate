package com.validate.validator.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyDateUtils {
    public static long RequestDelayTime = 600000L;

    public MyDateUtils() {
    }

    public static XMLGregorianCalendar toXMLCalendar(Date date) {
        DatatypeFactory df = null;

        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException var3) {
            System.out.println(var3.getMessage());
        }

        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTime(date);
        return df.newXMLGregorianCalendar(cal1);
    }

    public static Date fromXMLCalendar(XMLGregorianCalendar c) {
        return c == null ? null : c.toGregorianCalendar().getTime();
    }

    public static String toString(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }
    }

    public static String toCNString(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            return sdf.format(date);
        }
    }

    public static String toString(Date date, String format) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }
    }

    public static Date getDay(Date date, int index) {
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        date = DateUtils.setMilliseconds(date, 0);
        return DateUtils.addDays(date, index);
    }

    public static Date today() {
        return getDay(0);
    }

    public static Date tomorrow() {
        return getDay(1);
    }

    public static Date yestoday() {
        return getDay(-1);
    }

    public static Date getDay(int index) {
        Date date = new Date();
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        date = DateUtils.setMilliseconds(date, 0);
        return DateUtils.addDays(date, index);
    }

    public static Date getCurrentMonth() {
        Date date = new Date();
        date = DateUtils.setDays(date, 1);
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        date = DateUtils.setMilliseconds(date, 0);
        return date;
    }

    public static Date fromString(String dateStr) {
        dateStr = dateStr.replaceAll("-", "/");
        return fromString(dateStr, "yyyy/MM/dd");
    }

    public static Date fromStringWithTime(String dateStr) {
        dateStr = StringUtils.replaceEach(dateStr, new String[]{"-", "\n"}, new String[]{"/", ""});
        return StringUtils.contains(dateStr, ":") ? fromString(dateStr, "yyyy/MM/dd HH:mm:ss") : fromString(dateStr, "yyyy/MM/dd");
    }

    public static Date fromString(String dateStr, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException var5) {
            throw new RuntimeException("错误的时间：" + dateStr);
        }

        if (null == date) {
            throw new RuntimeException("错误的时间：" + dateStr);
        } else {
            return date;
        }
    }

    public static Integer dateDiff(Date end, Date begin) {
        long diff = end.getTime() - begin.getTime();
        Long day = diff / 86400000L;
        return day.intValue();
    }

    public static Date max(Date a, Date b) {
        if (b == null) {
            return a;
        } else if (a == null) {
            return b;
        } else {
            return a.before(b) ? b : a;
        }
    }

    public static Date min(Date a, Date b) {
        if (b == null) {
            return a;
        } else if (a == null) {
            return b;
        } else {
            return a.after(b) ? b : a;
        }
    }

    public static Boolean isBetween(Date time, Date startTime, Date endTime) {
        return time.after(startTime) && time.before(endTime);
    }

    public static Long getTime(Date date) {
        return date == null ? null : date.getTime();
    }
}
