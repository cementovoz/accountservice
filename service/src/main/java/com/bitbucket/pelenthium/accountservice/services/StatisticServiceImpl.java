package com.bitbucket.pelenthium.accountservice.services;


import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cementovoz on 19.08.14.
 */
@Service
public class StatisticServiceImpl implements StatisticService {

    private static final AtomicLong COUNT_CALL_GET = new AtomicLong(0);
    private static final AtomicLong COUNT_CALL_ADD = new AtomicLong(0);
    private static long time = System.nanoTime();

    @Override
    public void incrementGet() {
        COUNT_CALL_GET.incrementAndGet();
    }

    @Override
    public void incrementAdd() {
        COUNT_CALL_ADD.incrementAndGet();
    }

    @Override
    public long getCountGet() {
        return COUNT_CALL_GET.get();
    }

    @Override
    public long getCountAdd() {
        return COUNT_CALL_ADD.get();
    }

    @Override
    public void clearCounter() {
        COUNT_CALL_ADD.set(0);
        COUNT_CALL_GET.set(0);
        time =  System.nanoTime();
    }

    @Override
    public long getTime() {
        return System.nanoTime() - time;
    }
}
