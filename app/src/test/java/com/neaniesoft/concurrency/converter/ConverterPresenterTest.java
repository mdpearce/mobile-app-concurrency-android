package com.neaniesoft.concurrency.converter;

import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.CurrenciesRepository;
import com.neaniesoft.concurrency.data.Currency;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by mdpearce on 27/07/2016.
 */

public class ConverterPresenterTest {

    private static Map<String, String> CURRENCIES_MAP;
    private static List<Currency> CURRENCIES;
    private static Date DATE;

    private static double DELTA = 0.001;

    @Mock
    CurrenciesRepository mCurrenciesRepository;

    @Mock
    ConverterContract.View mConverterView;

    @Captor
    private ArgumentCaptor<CurrenciesDataSource.LoadCurrenciesCallback> mLoadCurrenciesCallbackCaptor;

    private ConverterPresenter mConverterPresenter;

    @Before
    public void setupConverterPresenter() {
        MockitoAnnotations.initMocks(this);

        DATE = new Date();

        CURRENCIES_MAP = new HashMap<>();
        CURRENCIES_MAP.put("USD", "US Dollar");
        CURRENCIES_MAP.put("AUD", "Australian Dollar");
        CURRENCIES_MAP.put("GBP", "Pound Sterling");
        CURRENCIES_MAP.put("EUR", "Euro");

        CURRENCIES = new ArrayList<>();
        CURRENCIES.add(new Currency("USD", 1.0));
        CURRENCIES.add(new Currency("AUD", 1.3302));
        CURRENCIES.add(new Currency("GBP", 0.75947));
        CURRENCIES.add(new Currency("EUR", 0.89985));

        // Always return true for isActive
        when(mConverterView.isActive()).thenReturn(true);

        mConverterPresenter = new ConverterPresenter(mConverterView, mCurrenciesRepository, CURRENCIES_MAP);
    }

    @Test
    public void loadCurrenciesFromRepositoryAndLoadIntoView() {
        mConverterPresenter.loadCurrencies();
        verify(mCurrenciesRepository).getCurrencies(mLoadCurrenciesCallbackCaptor.capture());
        mLoadCurrenciesCallbackCaptor.getValue().onCurrenciesLoaded(CURRENCIES, DATE);

        ArgumentCaptor<List> showCurrenciesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mConverterView).setAvailableCurrencies(showCurrenciesArgumentCaptor.capture(), CURRENCIES_MAP);
        assertTrue(showCurrenciesArgumentCaptor.getValue().size() == 4);
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
