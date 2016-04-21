package com.neaniesoft.concurrency.conversion;

import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.model.Currency;

/**
 * Created by mdpearce on 21/04/16.
 */
public class ConversionPresenter implements ConversionContract.UserActionsListener {

    private final ConversionContract.View mConversionView;

    public ConversionPresenter(@NonNull ConversionContract.View conversionView) {
        mConversionView = conversionView;
    }

    @Override
    public void performConversion(String amountString) {

    }

    @Override
    public void swapCurrencies(Currency from, Currency to) {

    }

    @Override
    public void setFromCurrency(Currency from) {

    }

    @Override
    public void setToCurrency(Currency to) {

    }

    @Override
    public void refreshCurrencies() {

    }
}
