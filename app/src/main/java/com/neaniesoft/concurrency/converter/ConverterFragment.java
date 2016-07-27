package com.neaniesoft.concurrency.converter;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neaniesoft.concurrency.R;
import com.neaniesoft.concurrency.data.Currency;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConverterFragment extends Fragment implements ConverterContract.View {

    public ConverterFragment() {
    }

    public static ConverterFragment newInstance() {
        return new ConverterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public String getFromText() {
        return null;
    }

    @Override
    public void updateToAmount(String toAmount) {

    }

    @Override
    public void setLoadingIndicator(boolean visible) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showNoCurrenciesError() {

    }

    @Override
    public void setAvailableCurrencies(List<Currency> currencies) {

    }

    @Override
    public void setPresenter(ConverterContract.Presenter presenter) {

    }
}
