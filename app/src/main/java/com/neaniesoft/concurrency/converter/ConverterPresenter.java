package com.neaniesoft.concurrency.converter;

import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.CurrenciesRepository;
import com.neaniesoft.concurrency.data.Currency;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class ConverterPresenter implements ConverterContract.Presenter {

    private final ConverterContract.View mConverterView;
    private final CurrenciesRepository mCurrenciesRepository;

    public ConverterPresenter(@NonNull ConverterContract.View converterView, @NonNull CurrenciesRepository currenciesRepository) {
        mConverterView = checkNotNull(converterView);
        mCurrenciesRepository = checkNotNull(currenciesRepository);

        mConverterView.setPresenter(this);
    }

    @Override
    public void loadCurrencies() {
        mCurrenciesRepository.getCurrencies(new CurrenciesDataSource.LoadCurrenciesCallback() {
            @Override
            public void onCurrenciesLoaded(List<Currency> currencies, Date date) {
                if (!mConverterView.isActive()) {
                    return;
                }
                if (currencies == null || currencies.isEmpty()) {
                    mConverterView.showNoCurrenciesError();
                } else {
                    mConverterView.setAvailableCurrencies(currencies);
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void fromAmountChanged() {

    }

    @Override
    public void fromCurrencyChanged(@NonNull Currency fromCurrency) {

    }

    @Override
    public void toCurrencyChanged(@NonNull Currency toCurrency) {

    }

    @Override
    public void start() {
        loadCurrencies();
    }
}
