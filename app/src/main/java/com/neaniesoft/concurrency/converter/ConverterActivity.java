package com.neaniesoft.concurrency.converter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.neaniesoft.concurrency.Injection;
import com.neaniesoft.concurrency.R;

import java.util.HashMap;
import java.util.Map;

public class ConverterActivity extends AppCompatActivity {

    private ConverterContract.Presenter mConverterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConverterFragment converterFragment = (ConverterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        if (converterFragment == null) {
            converterFragment = ConverterFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment, converterFragment)
                    .commit();
        }

        mConverterPresenter = new ConverterPresenter(
                converterFragment,
                Injection.provideCurrenciesRepository(getApplicationContext()),
                getCurrenciesMap());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_converter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
