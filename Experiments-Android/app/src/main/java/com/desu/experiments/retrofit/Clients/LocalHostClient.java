package com.desu.experiments.retrofit.clients;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class LocalHostClient implements ClientInterface {

    @Override
    public Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl("http://localhost")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
