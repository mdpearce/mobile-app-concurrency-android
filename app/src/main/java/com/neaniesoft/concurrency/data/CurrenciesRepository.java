package com.neaniesoft.concurrency.data;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class CurrenciesRepository implements CurrenciesDataSource {

    private static CurrenciesRepository sInstance = null;

    private final CurrenciesDataSource mRemoteDataSource;
    private final CurrenciesDataSource mLocalDataSource;

    List<Currency> mCachedCurrencies;
    Date mCachedFetchDate;

    boolean mCacheIsDirty = false;

    private CurrenciesRepository(@NonNull CurrenciesDataSource remoteDataSource,
                                 @NonNull CurrenciesDataSource localDataSource) {
        mRemoteDataSource = checkNotNull(remoteDataSource);
        mLocalDataSource = checkNotNull(localDataSource);
    }

    public static CurrenciesRepository getInstance(CurrenciesDataSource remoteDataSource,
                                                   CurrenciesDataSource localDataSource) {
        if (sInstance == null) {
            sInstance = new CurrenciesRepository(remoteDataSource, localDataSource);
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public void getCurrencies(@NonNull final LoadCurrenciesCallback callback) {
        checkNotNull(callback);

        if (mCachedCurrencies != null && !mCacheIsDirty && isFetchDateCurrent(mCachedFetchDate)) {
            callback.onCurrenciesLoaded(mCachedCurrencies, mCachedFetchDate);
            return;
        }

        if (mCacheIsDirty) {
            getCurrenciesFromRemoteDataSource(callback);
        } else {
            mLocalDataSource.getCurrencies(new LoadCurrenciesCallback() {
                @Override
                public void onCurrenciesLoaded(List<Currency> currencies, Date date) {
                    refreshCache(currencies, date);
                    callback.onCurrenciesLoaded(currencies, date);
                    if (!isFetchDateCurrent(date)) {
                        getCurrenciesFromRemoteDataSource(callback);
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    getCurrenciesFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getCurrenciesFromRemoteDataSource(@NonNull final LoadCurrenciesCallback callback) {
        mRemoteDataSource.getCurrencies(new LoadCurrenciesCallback() {
            @Override
            public void onCurrenciesLoaded(List<Currency> currencies, Date date) {
                refreshCache(currencies, date);
                refreshLocalDataSource(currencies);
                callback.onCurrenciesLoaded(currencies, date);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<Currency> currencies) {
        mLocalDataSource.saveCurrencies(currencies);
    }

    private void refreshCache(List<Currency> currencies, Date date) {
        mCachedCurrencies = currencies;
        mCachedFetchDate = date;
        mCacheIsDirty = false;
    }

    private boolean isFetchDateCurrent(Date fetchDate) {
        if (fetchDate == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        return (now - fetchDate.getTime()) < (DateUtils.HOUR_IN_MILLIS * 12);
    }

    @Override
    public void saveCurrencies(@NonNull List<Currency> currency) {
        // Not required for read-only repo
    }

    @Override
    public void refreshCurrencies() {
        mCacheIsDirty = true;
    }
}
