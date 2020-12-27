package com.eltonb.parsing;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.security.SecureRandom;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author elton.ballhysa
 */
public class Utils {
    
    private Utils() {
        
    }
    
    public static <T> T getSilently(Future<T> fut) {
        try {
            return fut.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String randomDate() {
        SecureRandom random = new SecureRandom();
        int day = random.nextInt(28) + 1;
        int month = random.nextInt(12) + 1;
        int year = random.nextInt(10) + 2020;
        String sd = day < 10 ? ("0" + day) : ("" + day);
        String sm = month < 10 ? ("0" + month) : ("" + month);
        String sy = "" + year;
        return sd + "." + sm + "." + sy;
    }
    
    public static boolean sameDate(TemporalAccessor date, String strDate) {
        int day = Integer.parseInt(strDate.substring(0, 2));
        int month = Integer.parseInt(strDate.substring(3, 5));
        int year = Integer.parseInt(strDate.substring(6));
        
        int dd = date.get(ChronoField.DAY_OF_MONTH);
        int dm = date.get(ChronoField.MONTH_OF_YEAR);
        int dy = date.get(ChronoField.YEAR);

        return dd == day && dm == month && dy == year;
    }
    
    public static boolean sameDate(Date date, String strDate) {
        int day = Integer.parseInt(strDate.substring(0, 2));
        int month = Integer.parseInt(strDate.substring(3, 5));
        int year = Integer.parseInt(strDate.substring(6));

        int dd = date.getDate();
        int dm = date.getMonth() + 1;
        int dy = date.getYear() + 1900;

        return dd == day && dm == month && dy == year;
    }
}
