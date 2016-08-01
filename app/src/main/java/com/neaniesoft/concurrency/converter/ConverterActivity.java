package com.neaniesoft.concurrency.converter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.neaniesoft.concurrency.Injection;
import com.neaniesoft.concurrency.Prefs;
import com.neaniesoft.concurrency.R;

import java.util.HashMap;
import java.util.Map;

public class ConverterActivity extends AppCompatActivity {

    private ConverterContract.Presenter mConverterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        setTitle(R.string.converter_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String initialText = getInitialTextFromIntent(getIntent());

        ConverterFragment converterFragment = (ConverterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        if (converterFragment == null) {
            converterFragment = ConverterFragment.newInstance(initialText);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment, converterFragment)
                    .commit();
        }

        mConverterPresenter = new ConverterPresenter(
                converterFragment,
                Injection.provideCurrenciesRepository(getApplicationContext()),
                new Prefs(),
                getCurrenciesMap());


    }

    private Map<String,String> getCurrenciesMap() {
        String[] codes = getResources().getStringArray(R.array.currency_codes);
        String[] names = getResources().getStringArray(R.array.currency_names);

        if (codes.length != names.length) {
            throw new IllegalStateException("Codes array does not match names!");
        }

        Map<String, String> currenciesMap = new HashMap<>(codes.length);
        for (int i = 0; i < codes.length; i++) {
            currenciesMap.put(codes[i], names[i]);
        }

        return currenciesMap;
    }

    private String getInitialTextFromIntent(@NonNull Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                return intent.getStringExtra(Intent.EXTRA_TEXT);
            }
        }
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String intentText = getInitialTextFromIntent(intent);
        mConverterPresenter.handleSharedText(intentText);
    }
}
