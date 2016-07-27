package com.neaniesoft.concurrency;

import android.content.Context;
import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.data.CurrenciesRepository;
import com.neaniesoft.concurrency.data.local.CurrenciesLocalDataSource;
import com.neaniesoft.concurrency.data.remote.CurrenciesRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 27/07/2016.
 */

public class Injection {

    public static CurrenciesRepository provideCurrenciesRepository(@NonNull Context context) {
        checkNotNull(context);
        return CurrenciesRepository.getInstance(
                CurrenciesRemoteDataSource.getInstance(),
                CurrenciesLocalDataSource.getInstance(context));
    }
}
