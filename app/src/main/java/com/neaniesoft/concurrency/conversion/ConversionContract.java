package com.neaniesoft.concurrency.conversion;

import com.neaniesoft.concurrency.model.Currency;

/**
 * Created by mdpearce on 21/04/16.
 */
public interface ConversionContract {
    interface View {
        void showResult(String result);
        void setFromCurrencyDisplay(Currency from);
        void setToCurrencyDisplay(Currency to);
    }

    interface UserActionsListener {
        void performConversion(String amountString);
        void swapCurrencies(Currency from, Currency to);
        void setFromCurrency(Currency from);
        void setToCurrency(Currency to);
    }
}
