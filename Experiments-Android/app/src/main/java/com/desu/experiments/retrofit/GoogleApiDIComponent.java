package com.desu.experiments.retrofit;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = GoogleApiDIModule.class)
public interface GoogleApiDIComponent {
    GoogleApiDI maker();
}