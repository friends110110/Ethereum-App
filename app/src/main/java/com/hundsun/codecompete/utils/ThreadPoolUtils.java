package com.hundsun.codecompete.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ThreadPoolUtils {
    static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
    public static void execute(Runnable runnable){
        fixedThreadPool.execute(runnable);
    }
}
