package com.desu.experiments.retrofit;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = LocalHostDIModule.class)
public interface LocalHostDIComponent {
    GoogleApiDI maker();
}
