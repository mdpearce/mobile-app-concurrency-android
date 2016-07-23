package com.neaniesoft.concurrency.data.remote;

import com.neaniesoft.concurrency.data.Currency;

import java.util.List;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class FixerIOResponse {
    String base;
    List<Currency> rates;

    public String getBase() {
        return base;
    }

    public List<Currency> getRates() {
        return rates;
    }
}
