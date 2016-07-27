package com.neaniesoft.concurrency.converter;

import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.Prefs;
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
                    setSelectedCurrencies(currencies);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mConverterView.showNoCurrenciesError();
            }
        });
    }

    private void setSelectedCurrencies(List<Currency> currencies) {
        String selectedFromCode = Prefs.getInstance().getFromCurrencyCode();
        for (Currency currency : currencies) {
            if (currency.getCode().equalsIgnoreCase(selectedFromCode)) {
                mConverterView.setSelectedFromCurrency(currency);
                break;
            }
        }

        String selectedToCode = Prefs.getInstance().getToCurrencyCode();
        for (Currency currency : currencies) {
            if (currency.getCode().equalsIgnoreCase(selectedToCode)) {
                mConverterView.setSelectedToCurrency(currency);
                break;
            }
        }
    }

    @Override
    public void fromAmountChanged() {
        calculate();
    }

    @Override
    public void fromCurrencyChanged() {
        saveFromCurrency(mConverterView.getFromCurrency());
        calculate();
    }

    @Override
    public void toCurrencyChanged() {
        saveToCurrency(mConverterView.getToCurrency());
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

    private void saveFromCurrency(Currency currency) {
        Prefs.getInstance().setFromCurrency(currency);
    }

    private void saveToCurrency(Currency currency) {
        Prefs.getInstance().setToCurrency(currency);
    }
}
