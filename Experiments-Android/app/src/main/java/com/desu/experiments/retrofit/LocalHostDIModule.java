package com.desu.experiments.retrofit;

import com.desu.experiments.retrofit.clients.ClientInterface;
import com.desu.experiments.retrofit.clients.LocalHostClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalHostDIModule {
    @Provides
    @Singleton
    ClientInterface provideClientInterface() {
        return new LocalHostClient();
    }
}
