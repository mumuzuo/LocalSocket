package com.zuo.localsocket;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zuo
 * @date 2020/5/21 15:40
 */
public class ThreadFactoryImpl implements ThreadFactory {
    private final AtomicInteger mCount = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "taskTread#" + mCount.getAndIncrement());
    }
}
