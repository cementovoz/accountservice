package com.bitbucket.pelenthium.accountservice.client.data;

import java.util.Map;

/**
 * Created by cementovoz on 23.08.14.
 */
public class Statistic {

    public long countGet;
    public long countAdd;
    public long time;


    public Statistic(Map<String, Long> data) {
        countAdd = data.get("countAdd");
        countGet = data.get("countGet");
        time = data.get("time");
    }

    public long getCountGet() {
        return countGet;
    }

    public void setCountGet(long countGet) {
        this.countGet = countGet;
    }

    public long getCountAdd() {
        return countAdd;
    }

    public void setCountAdd(long countAdd) {
        this.countAdd = countAdd;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
