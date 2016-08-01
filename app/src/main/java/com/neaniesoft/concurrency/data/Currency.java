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


    public boolean equalsCode(Currency check) {
        return (check.code.equalsIgnoreCase(code));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (Double.compare(currency.rate, rate) != 0) return false;
        return code.equals(currency.code);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = code.hashCode();
        temp = Double.doubleToLongBits(rate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "code='" + code + '\'' +
                ", rate=" + rate +
                '}';
    }
}
