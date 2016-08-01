package com.neaniesoft.concurrency.converter;

import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.Prefs;
import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.CurrenciesRepository;
import com.neaniesoft.concurrency.data.Currency;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class ConverterPresenter implements ConverterContract.Presenter {

    private final ConverterContract.View mConverterView;
    private final CurrenciesRepository mCurrenciesRepository;
    private final Map<String, String> mCurrenciesMap;
    private final Prefs mPrefs;

    private final Pattern mDecimalParsePattern;

    public ConverterPresenter(@NonNull ConverterContract.View converterView, @NonNull CurrenciesRepository currenciesRepository, @NonNull Prefs prefs, @NonNull Map<String, String> currenciesMap) {
        mConverterView = checkNotNull(converterView);
        mCurrenciesRepository = checkNotNull(currenciesRepository);
        mCurrenciesMap = checkNotNull(currenciesMap);
        mPrefs = checkNotNull(prefs);

        mDecimalParsePattern = Pattern.compile("^\\D*(\\d[\\d ,]*(?:\\.\\d+)?)");

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
        String selectedFromCode = mPrefs.getFromCurrencyCode();
        for (Currency currency : currencies) {
            if (currency.getCode().equalsIgnoreCase(selectedFromCode)) {
                mConverterView.setSelectedFromCurrency(currency);
                break;
            }
        }

        String selectedToCode = mPrefs.getToCurrencyCode();
        for (Currency currency : currencies) {
            if (currency.getCode().equalsIgnoreCase(selectedToCode)) {
                mConverterView.setSelectedToCurrency(currency);
                break;
            }
        }
    }

    private double getAmountFromView() {
        double amount = 0;
        try {
            amount = Double.valueOf(mConverterView.getFromText());
        } catch (NumberFormatException e) {
        }
        return amount;
    }

    @Override
    public void fromAmountChanged() {
        calculate(getAmountFromView());
    }

    @Override
    public void fromCurrencyChanged() {
        saveFromCurrency(mConverterView.getFromCurrency());
        calculate(getAmountFromView());
    }

    @Override
    public void toCurrencyChanged() {
        saveToCurrency(mConverterView.getToCurrency());
        calculate(getAmountFromView());
    }

    @Override
    public void handleSharedText(String sharedText) {
        double value = parseNumberFromString(sharedText);
        mConverterView.setFromAmount(String.valueOf(value));
    }

    double parseNumberFromString(String text) {
        if (text == null) {
            return 0;
        }
        Matcher matcher = mDecimalParsePattern.matcher(text);
        if (matcher.matches()) {
            String numberString = matcher.group(1).replace(" ", "");
            NumberFormat nf = NumberFormat.getNumberInstance();
            try {
                Number number = nf.parse(numberString);
                return number.doubleValue();
            } catch (ParseException e) {
            }
        }
        return 0;

    }

    void calculate(double amount) {
        double fromRate = mConverterView.getFromCurrency().getRate();
        double toRate = mConverterView.getToCurrency().getRate();
        double converted = getCalculatedAmount(amount, fromRate, toRate);
        String convertedString = String.format(Locale.getDefault(), "%.2f", converted);
        mConverterView.updateToAmount(convertedString);
    }

    double getCalculatedAmount(double amount, double fromRate, double toRate) {
        return (amount / fromRate) * toRate;
    }

    @Override
    public void start() {
        loadCurrencies();
    }

    private void saveFromCurrency(Currency currency) {
        mPrefs.setFromCurrency(currency);
    }

    private void saveToCurrency(Currency currency) {
        mPrefs.setToCurrency(currency);
    }
}
