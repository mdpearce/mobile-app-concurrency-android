package com.neaniesoft.concurrency.converter;

import com.neaniesoft.concurrency.Prefs;
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

    @Mock
    Prefs mPrefs;

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

        when(mPrefs.getFromCurrencyCode()).thenReturn("USD");
        when(mPrefs.getToCurrencyCode()).thenReturn("AUD");

        when(mConverterView.getFromCurrency()).thenReturn(CURRENCIES.get(0));
        when(mConverterView.getToCurrency()).thenReturn(CURRENCIES.get(1));

        when(mConverterView.getFromText()).thenReturn("1.00");

        mConverterPresenter = new ConverterPresenter(mConverterView, mCurrenciesRepository, mPrefs, CURRENCIES_MAP);
    }

    @Test
    public void loadCurrenciesFromRepositoryAndLoadIntoView() {
        mConverterPresenter.loadCurrencies();
        verify(mCurrenciesRepository).getCurrencies(mLoadCurrenciesCallbackCaptor.capture());
        mLoadCurrenciesCallbackCaptor.getValue().onCurrenciesLoaded(CURRENCIES, DATE);

        ArgumentCaptor<List> showCurrenciesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Map> currenciesMapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mConverterView).setAvailableCurrencies(showCurrenciesArgumentCaptor.capture(), currenciesMapArgumentCaptor.capture());
        assertTrue(showCurrenciesArgumentCaptor.getValue().size() == 4);
    }

    @Test
    public void setSelectedCurrenciesWhenLoadingFromRepository() {
        mConverterPresenter.loadCurrencies();
        verify(mCurrenciesRepository).getCurrencies(mLoadCurrenciesCallbackCaptor.capture());
        mLoadCurrenciesCallbackCaptor.getValue().onCurrenciesLoaded(CURRENCIES, DATE);

        ArgumentCaptor<List> showCurrenciesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Map> currenciesMapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mConverterView).setAvailableCurrencies(showCurrenciesArgumentCaptor.capture(), currenciesMapArgumentCaptor.capture());

        verify(mConverterView).setSelectedFromCurrency(CURRENCIES.get(0));
        verify(mConverterView).setSelectedToCurrency(CURRENCIES.get(1));
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

    @Test
    public void updateToTextWhenFromTextChanges() {
        mConverterPresenter.fromAmountChanged();
        ArgumentCaptor<String> toAmountArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mConverterView).updateToAmount(toAmountArgumentCaptor.capture());
        assertTrue(toAmountArgumentCaptor.getValue().length() > 0);
    }

    @Test
    public void saveFromCurrencyWhenChanged() {
        ArgumentCaptor<Currency> fromCurrencyArgumentCaptor = ArgumentCaptor.forClass(Currency.class);
        mConverterPresenter.fromCurrencyChanged();
        verify(mPrefs).setFromCurrency(fromCurrencyArgumentCaptor.capture());
        assertEquals(CURRENCIES.get(0), fromCurrencyArgumentCaptor.getValue());
    }

    @Test
    public void saveToCurrencyWhenChanged() {
        ArgumentCaptor<Currency> toCurrencyArgumentCaptor = ArgumentCaptor.forClass(Currency.class);
        mConverterPresenter.toCurrencyChanged();
        verify(mPrefs).setToCurrency(toCurrencyArgumentCaptor.capture());
        assertEquals(CURRENCIES.get(1), toCurrencyArgumentCaptor.getValue());
    }

    @Test
    public void updateToTextWhenFromCurrencyChanges() {
        mConverterPresenter.fromCurrencyChanged();
        ArgumentCaptor<String> toAmountArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mConverterView).updateToAmount(toAmountArgumentCaptor.capture());
        assertTrue(toAmountArgumentCaptor.getValue().length() > 0);
    }

    @Test
    public void updateToTextWhenToCurrencyChanges() {
        mConverterPresenter.toCurrencyChanged();
        ArgumentCaptor<String> toAmountArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mConverterView).updateToAmount(toAmountArgumentCaptor.capture());
        assertTrue(toAmountArgumentCaptor.getValue().length() > 0);
    }

    @Test
    public void calculateValueGivenRates() {
        // USD to AUD
        double input = 1.0;
        double fromRate = 1.0;
        double toRate = 1.3302;
        double output = mConverterPresenter.getCalculatedAmount(input, fromRate, toRate);
        assertEquals(1.3302, output, DELTA);

        input = 1.5;
        output = mConverterPresenter.getCalculatedAmount(input, fromRate, toRate);
        assertEquals(1.9953, output, DELTA);

        input = 0;
        output = mConverterPresenter.getCalculatedAmount(input, fromRate, toRate);
        assertEquals(0, output, DELTA);

        input = 250;
        output = mConverterPresenter.getCalculatedAmount(input, fromRate, toRate);
        assertEquals(332.55, output, DELTA);

        // EUR to GBP
        fromRate = 0.89985;
        toRate = 0.75947;
        input = 1.0;
        output = mConverterPresenter.getCalculatedAmount(input, fromRate, toRate);
        assertEquals(0.8440, output, DELTA);

        input = 1.5;
        output = mConverterPresenter.getCalculatedAmount(input, fromRate, toRate);
        assertEquals(1.2660, output, DELTA);

        input = 0;
        output = mConverterPresenter.getCalculatedAmount(input, fromRate, toRate);
        assertEquals(0, output, DELTA);

        input = 250;
        output = mConverterPresenter.getCalculatedAmount(input, fromRate, toRate);
        assertEquals(210.9991, output, DELTA);
    }
}
