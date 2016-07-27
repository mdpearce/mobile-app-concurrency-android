package com.neaniesoft.concurrency.converter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.neaniesoft.concurrency.R;
import com.neaniesoft.concurrency.data.Currency;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConverterFragment extends Fragment implements ConverterContract.View {

    @BindView(R.id.spinner_from_currency)
    Spinner spinnerFromCurrency;

    @BindView(R.id.spinner_to_currency)
    Spinner spinnerToCurrency;

    @BindView(R.id.text_from_amount)
    EditText textFromAmount;

    @BindView(R.id.text_to_amount)
    TextView textToAmount;

    ConverterContract.Presenter mPresenter;
    Unbinder mUnbinder;

    CurrencyAdapter mAdapterCurrenciesFrom;
    CurrencyAdapter mAdapterCurrenciesTo;

    private static final String KEY_INITIAL_TEXT = "com.neaniesoft.concurrency.converter.INITIAL_TEXT";

    public ConverterFragment() {
    }

    public static ConverterFragment newInstance(String initialText) {
        ConverterFragment fragment = new ConverterFragment();
        Bundle args = new Bundle();
        args.putString(KEY_INITIAL_TEXT, initialText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_converter, container, false);

        mUnbinder = ButterKnife.bind(this, root);

        spinnerFromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.fromCurrencyChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerToCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.toCurrencyChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
        Bundle args = getArguments();
        if (args != null) {
            String initialText = args.getString(KEY_INITIAL_TEXT, null);
            if (initialText != null) {
                mPresenter.handleSharedText(initialText);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnTextChanged(R.id.text_from_amount)
    void onFromAmountTextChanged(CharSequence text) {
        mPresenter.fromAmountChanged();
    }

    @Override
    public String getFromText() {
        return textFromAmount.getText().toString();
    }

    @Override
    public void updateToAmount(String toAmount) {
        textToAmount.setText(toAmount);
    }

    @Override
    public void setLoadingIndicator(boolean visible) {
        // Unimplemented
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNoCurrenciesError() {
        Toast.makeText(getContext(), "No currencies to convert...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAvailableCurrencies(List<Currency> currencies, Map<String, String> currenciesMap) {
        if (mAdapterCurrenciesFrom == null) {
            mAdapterCurrenciesFrom = new CurrencyAdapter(currencies, currenciesMap);
            spinnerFromCurrency.setAdapter(mAdapterCurrenciesFrom);
        } else {
            mAdapterCurrenciesFrom.replaceData(currencies);
        }
        if (mAdapterCurrenciesTo == null) {
            mAdapterCurrenciesTo = new CurrencyAdapter(currencies, currenciesMap);
            spinnerToCurrency.setAdapter(mAdapterCurrenciesTo);
        } else {
            mAdapterCurrenciesTo.replaceData(currencies);
        }
    }

    @Override
    public Currency getFromCurrency() {
        if (spinnerFromCurrency == null) {
            return new Currency("", 1);
        }
        return (Currency) spinnerFromCurrency.getItemAtPosition(spinnerFromCurrency.getSelectedItemPosition());
    }

    @Override
    public Currency getToCurrency() {
        if (spinnerToCurrency == null) {
            return new Currency("", 1);
        }
        return (Currency) spinnerToCurrency.getItemAtPosition(spinnerToCurrency.getSelectedItemPosition());
    }

    @Override
    public void setSelectedFromCurrency(Currency currency) {
        spinnerFromCurrency.setSelection(mAdapterCurrenciesFrom.getPositionForCurrency(currency));
    }

    @Override
    public void setSelectedToCurrency(Currency currency) {
        spinnerToCurrency.setSelection(mAdapterCurrenciesTo.getPositionForCurrency(currency));
    }

    @Override
    public void setFromAmount(String fromAmount) {
        textFromAmount.setText(fromAmount);
    }

    @Override
    public void setPresenter(@NonNull ConverterContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private static class CurrencyAdapter extends BaseAdapter {

        private List<Currency> mCurrencies;
        private Map<String, String> mCurrencyNames;

        public CurrencyAdapter(List<Currency> currencies, Map<String, String> currencyNames) {
            mCurrencyNames = currencyNames;
            setList(currencies);
        }

        public void replaceData(List<Currency> currencies) {
            setList(currencies);
            notifyDataSetChanged();
        }

        private void setList(List<Currency> currencies) {
            mCurrencies = currencies;
        }

        @Override
        public int getCount() {
            return mCurrencies != null ? mCurrencies.size() : 0;
        }

        @Override
        public Currency getItem(int position) {
            return mCurrencies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
            }
            final Currency currency = getItem(position);

            TextView title = (TextView) view.findViewById(android.R.id.text1);

            title.setText(mCurrencyNames.get(currency.getCode()));
            return view;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }
            final Currency currency = getItem(position);

            TextView title = (TextView) view.findViewById(android.R.id.text1);

            title.setText(mCurrencyNames.get(currency.getCode()));
            return view;
        }

        public int getPositionForCurrency(Currency currency) {
            for (int i = 0; i < mCurrencies.size(); i++) {
                Currency checkCurrency = mCurrencies.get(i);
                if (checkCurrency.equalsCode(currency)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
