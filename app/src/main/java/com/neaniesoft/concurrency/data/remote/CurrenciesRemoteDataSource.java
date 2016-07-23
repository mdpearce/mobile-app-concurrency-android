package com.neaniesoft.concurrency.data.remote;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.Currency;

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
                .registerTypeAdapter(RatesDeserializer.class, new RatesDeserializer())
                .create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        Retrofit retrofit = new Retrofit.Builder()
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
                    callback.onCurrenciesLoaded(fixerIOResponse.getRates(), fixerIOResponse.getDate());
                }
            }

            @Override
            public void onFailure(Call<FixerIOResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void saveCurrency(@NonNull Currency currency) {
        // Not needed for remote read-only repo
    }
}
