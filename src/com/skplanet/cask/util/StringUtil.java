package com.skplanet.cask.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
    
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static String DATE_FORMAT = "yyyyMMdd";
    
    public static String timestamp2Str(Timestamp ts) {
        if(ts == null) {
            return null;
        }
        else {
            SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
            return formatter.format(ts);
        }
    }
    public static String date2Str(Date date) {
        if(date == null) {
            return null;
        }
        else {
            SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
            return formatter.format(date);
        }
    }
    public static String dateDate2Str(Date date) {
        if(date == null) {
            return null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            return formatter.format(date);
        }
    }
    public static String exception2Str(Exception e) {
       StringWriter out = new StringWriter();
       e.printStackTrace(new PrintWriter(out));
       return out.toString();
    }
    
    public static Timestamp str2Timestamp(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
        Date parsed = formatter.parse(date);
        return new Timestamp(parsed.getTime());
    }
    public static String class2Str(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = null;
        StringBuilder buf = new StringBuilder();
        try {
            fields = clazz.getDeclaredFields();            
            AccessibleObject.setAccessible(fields, true);
            
            for (int i = 0; i < fields.length; i++) {
                buf.append(fields[i].getName());
                buf.append(" = ");
                buf.append(fields[i].get(obj));
                if(i < fields.length - 1) {
                    buf.append(", ");
                }
            }
        } catch (Exception e) {
            return exception2Str(e);
        }
        return buf.toString();
    }
    public static String getLastDir(String dir) {
        int index = dir.lastIndexOf("/");
        if(index == dir.length() - 1) {
            index = dir.lastIndexOf("/", index - 1);    
        }
        String last =  dir.substring(index + 1, dir.length());
        return last;
    }
}
