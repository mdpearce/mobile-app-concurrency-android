package com.neaniesoft.concurrency.converter;

import com.neaniesoft.concurrency.data.CurrenciesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * Created by mdpearce on 27/07/2016.
 */

public class ConverterPresenterTest {

    private static Map<String, String> CURRENCIES_MAP;

    private static double DELTA = 0.001;

    @Mock
    CurrenciesRepository mCurrenciesRepository;

    @Mock
    ConverterContract.View mConverterView;

    private ConverterPresenter mConverterPresenter;

    @Before
    public void setupConverterPresenter() {
        MockitoAnnotations.initMocks(this);

        CURRENCIES_MAP = new HashMap<>();
        CURRENCIES_MAP.put("USD", "US Dollar");
        CURRENCIES_MAP.put("AUD", "Australian Dollar");
        CURRENCIES_MAP.put("GBP", "Pound Sterling");
        CURRENCIES_MAP.put("EUR", "Euro");

        mConverterPresenter = new ConverterPresenter(mConverterView, mCurrenciesRepository, CURRENCIES_MAP);
    }

    @Test
    public void parseIntegerFromString() {
        String testString = "This costs $42";
        double result = mConverterPresenter.parseNumberFromString(testString);
        assertEquals(42, result, DELTA);

        testString = " 101 40";
        result = mConverterPresenter.parseNumberFromString(testString);
        assertEquals(10140, result, DELTA);

        testString = "This is a test number 304,50";
        result = mConverterPresenter.parseNumberFromString(testString);
        assertEquals(30450, result, DELTA);

        testString = "This string has no number";
        result = mConverterPresenter.parseNumberFromString(testString);
        assertEquals(0, result, DELTA);
    }

    @Test
    public void parseDecimalFromString() {
        String testString = "$42.30";
        double result = mConverterPresenter.parseNumberFromString(testString);
        assertEquals(42.30, result, DELTA);

        testString = " 101.40";
        result = mConverterPresenter.parseNumberFromString(testString);
        assertEquals(101.40, result, DELTA);

        testString = "This is a test number 304,50.24";
        result = mConverterPresenter.parseNumberFromString(testString);
        assertEquals(30450.24, result, DELTA);

        testString = "This string has no number";
        result = mConverterPresenter.parseNumberFromString(testString);
        assertEquals(0, result, DELTA);
    }
}
