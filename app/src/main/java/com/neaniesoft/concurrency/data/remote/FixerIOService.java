package com.neaniesoft.concurrency.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mdpearce on 23/07/2016.
 */

public interface FixerIOService {
    @GET("latest?base=USD")
    Call<FixerIOResponse> latestInUSD();
}
