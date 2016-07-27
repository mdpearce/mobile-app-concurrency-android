package com.neaniesoft.concurrency.converter;

import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.BasePresenter;
import com.neaniesoft.concurrency.BaseView;
import com.neaniesoft.concurrency.data.Currency;

import java.util.List;

/**
 * Created by mdpearce on 23/07/2016.
 */

public interface ConverterContract {
    interface View extends BaseView<Presenter> {
        String getFromText();

        void updateToAmount(String toAmount);

        void setLoadingIndicator(boolean visible);

        boolean isActive();

        void showNoCurrenciesError();

        void setAvailableCurrencies(List<Currency> currencies);
    }

    interface Presenter extends BasePresenter {
        void loadCurrencies();

        void fromAmountChanged();

        void fromCurrencyChanged(@NonNull Currency fromCurrency);

        void toCurrencyChanged(@NonNull Currency toCurrency);
    }
}
