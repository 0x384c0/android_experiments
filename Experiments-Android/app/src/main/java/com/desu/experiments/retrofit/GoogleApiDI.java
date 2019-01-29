package com.desu.experiments.retrofit;

import com.desu.experiments.retrofit.api.GoogleApi;
import com.desu.experiments.retrofit.clients.ClientInterface;

import javax.inject.Inject;

import retrofit.Retrofit;

public class GoogleApiDI {
    @Inject
    ClientInterface clientInterface;

    @Inject
    public GoogleApiDI() {
    }

    public GoogleApi getGoogleApi() {
        Retrofit retrofit = clientInterface.getClient();
        return retrofit.create(GoogleApi.class);
    }
}
