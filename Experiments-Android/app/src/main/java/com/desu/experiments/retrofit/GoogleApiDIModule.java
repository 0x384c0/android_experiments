package com.desu.experiments.retrofit;

import com.desu.experiments.retrofit.clients.ClientInterface;
import com.desu.experiments.retrofit.clients.GoogleApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GoogleApiDIModule {
    @Provides
    @Singleton
    ClientInterface provideClientInterface(){
        return new GoogleApiClient();
    }
}
