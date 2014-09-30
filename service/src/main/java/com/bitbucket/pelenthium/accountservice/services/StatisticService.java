package com.bitbucket.pelenthium.accountservice.services;


/**
 * Created by cementovoz on 19.08.14.
 */
public interface StatisticService {

    void incrementGet();

    void incrementAdd();

    long getCountGet();

    long getCountAdd();

    void clearCounter();

    long getTime();
}
