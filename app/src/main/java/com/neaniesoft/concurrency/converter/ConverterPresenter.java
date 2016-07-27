package com.neaniesoft.concurrency.converter;

import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.CurrenciesRepository;
import com.neaniesoft.concurrency.data.Currency;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class ConverterPresenter implements ConverterContract.Presenter {

    private final ConverterContract.View mConverterView;
    private final CurrenciesRepository mCurrenciesRepository;
    private final Map<String, String> mCurrenciesMap;

    public ConverterPresenter(@NonNull ConverterContract.View converterView, @NonNull CurrenciesRepository currenciesRepository, @NonNull Map<String, String> currenciesMap) {
        mConverterView = checkNotNull(converterView);
        mCurrenciesRepository = checkNotNull(currenciesRepository);
        mCurrenciesMap = checkNotNull(currenciesMap);

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
                    mConverterView.setAvailableCurrencies(currencies, mCurrenciesMap);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mConverterView.showNoCurrenciesError();
            }
        });
    }

    @Override
    public void fromAmountChanged() {
        calculate();
    }

    @Override
    public void fromCurrencyChanged() {
        calculate();
    }

    @Override
    public void toCurrencyChanged() {
        calculate();
    }

    private void calculate() {
        double fromRate = mConverterView.getFromCurrency().getRate();
        double toRate = mConverterView.getToCurrency().getRate();

        double amount = 0;
        try {
            amount = Double.valueOf(mConverterView.getFromText());
        } catch (NumberFormatException e) {
        }

        double converted = (amount / fromRate) * toRate;
        String convertedString = String.format(Locale.getDefault(), "%.2f", converted);
        mConverterView.updateToAmount(convertedString);
    }

    @Override
    public void start() {
        loadCurrencies();
    }
}
