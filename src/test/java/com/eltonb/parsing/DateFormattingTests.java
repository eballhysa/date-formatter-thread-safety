package com.eltonb.parsing;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;


/**
 *
 * @author elton.ballhysa
 */
public class DateFormattingTests {
    
    private static final DateTimeFormatter threadSafeFormatter= DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final SimpleDateFormat nonThreadSafeformatter = new SimpleDateFormat("dd.MM.yyyy");
    private static final int MAX_TRIES = 100;

    @Test
    public void threadSafeTest() {
        Callable<Boolean> testCall = this::threadSafeFormatter;
        long successCount = runParallelParses(testCall);
        Assertions.assertEquals(successCount, MAX_TRIES);
    }

    @Test
    public void nonThreadSafeTest() {
        Callable<Boolean> testCall = this::nonThreadSafeFormatter;
        long successCount = runParallelParses(testCall);
        Assertions.assertEquals(successCount, MAX_TRIES);
    }

    private long runParallelParses(Callable<Boolean> parseTest) {
        List<Future<Boolean>> futures = new ArrayList<>();
        ExecutorService exec = Executors.newFixedThreadPool(MAX_TRIES);
        IntStream
                .range(0, MAX_TRIES)
                .mapToObj(i->parseTest)
                .forEach(call -> {
                            Future<Boolean> fut = exec.submit(call);
                            futures.add(fut);
                        }
                );
        exec.shutdown();
        return futures
                .stream()
                .map(Utils::getSilently)
                .filter(Boolean.TRUE::equals)
                .count();
    }
    
    private boolean threadSafeFormatter() {
        String strDate = Utils.randomDate();
        try {
            TemporalAccessor date = threadSafeFormatter.parse(strDate);
            return Utils.sameDate(date, strDate);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean nonThreadSafeFormatter() {
        String strDate = Utils.randomDate();
        try {
            Date date = nonThreadSafeformatter.parse(strDate);
            return Utils.sameDate(date, strDate);
        } catch (Exception e) {
            return false;
        }
    }
}
