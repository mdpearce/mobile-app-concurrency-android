package com.neaniesoft.concurrency.conversion;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.neaniesoft.concurrency.R;
import com.neaniesoft.concurrency.model.Currency;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversionFragment extends Fragment implements ConversionContract.View {

    @Bind(R.id.spinner_from_currency)
    Spinner mSpinnerFrom;

    @Bind(R.id.spinner_to_currency)
    Spinner mSpinnerTo;

    @Bind(R.id.edit_from)
    EditText mEditTextFrom;

    @Bind(R.id.text_result)
    TextView mTextResult;

    private ConversionContract.UserActionsListener mActionsListener;

    public ConversionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        mActionsListener = new ConversionPresenter(this);
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
