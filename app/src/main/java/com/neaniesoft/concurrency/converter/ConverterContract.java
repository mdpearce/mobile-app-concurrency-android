package com.neaniesoft.concurrency.converter;

import com.neaniesoft.concurrency.BasePresenter;
import com.neaniesoft.concurrency.BaseView;
import com.neaniesoft.concurrency.data.Currency;

import java.util.List;
import java.util.Map;

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

        void setAvailableCurrencies(List<Currency> currencies, Map<String, String> currenciesMap);

        Currency getFromCurrency();

        Currency getToCurrency();

        void setSelectedFromCurrency(Currency currency);

        void setSelectedToCurrency(Currency currency);

        void setFromAmount(String fromAmount);
    }

    interface Presenter extends BasePresenter {
        void loadCurrencies();

        void fromAmountChanged();

        void fromCurrencyChanged();

        void toCurrencyChanged();

        void handleSharedText(String sharedText);
    }
}
