package com.neaniesoft.concurrency.data.remote;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.model.Currency;

import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class CurrenciesRemoteDataSource implements CurrenciesDataSource {

    private static CurrenciesRemoteDataSource sInstance;

    private FixerIOService mFixerIOService;

    public static CurrenciesRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new CurrenciesRemoteDataSource();
        }
        return sInstance;
    }

    private CurrenciesRemoteDataSource() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FixerIOResponse.class, new RatesDeserializer())
                .create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(
                        new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(FixerIOService.BASEURL)
                .build();

        mFixerIOService = retrofit.create(FixerIOService.class);
    }

    @Override
    public void getCurrencies(@NonNull final LoadCurrenciesCallback callback) {
        checkNotNull(callback);

        mFixerIOService.latestInUSD().enqueue(new Callback<FixerIOResponse>() {
            @Override
            public void onResponse(Call<FixerIOResponse> call, Response<FixerIOResponse> response) {
                if (response != null && response.isSuccessful()) {
                    FixerIOResponse fixerIOResponse = response.body();
                    List<Currency> currencies = fixerIOResponse.getRates();
                    currencies.add(new Currency("USD", 1)); // Base currency which is not included in the api response
                    callback.onCurrenciesLoaded(currencies, new Date());
                }
            }

            @Override
            public void onFailure(Call<FixerIOResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void saveCurrencies(@NonNull List<Currency> currencies) {
        // Not needed for remote read-only repo
    }

    @Override
    public void refreshCurrencies() {
        // Not required
    }
}
