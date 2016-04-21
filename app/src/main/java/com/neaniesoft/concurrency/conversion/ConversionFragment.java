package com.neaniesoft.concurrency.conversion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neaniesoft.concurrency.R;
import com.neaniesoft.concurrency.model.Currency;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversionFragment extends Fragment implements ConversionContract.View {


    public ConversionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversion, container, false);
    }

    @Override
    public void showResult(String result) {

    }

    @Override
    public void setFromCurrencyDisplay(Currency from) {

    }

    @Override
    public void setToCurrencyDisplay(Currency to) {

    }

    @Override
    public void setFromCurrencyList(List<Currency> currencyList) {

    }

    @Override
    public void setToCurrencyList(List<Currency> currencyList) {

    }
}
