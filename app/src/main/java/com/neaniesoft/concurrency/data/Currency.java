package com.neaniesoft.concurrency.data;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class Currency {

    String code;
    double rate;

    public Currency(String code, double rate) {
        this.code = code;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public double getRate() {
        return rate;
    }
}
