package com.desu.experiments.retrofit.clients;

import com.desu.experiments.Constants;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class GoogleApiClient implements ClientInterface {

    @Override
    public Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(Constants.GOOGLE_API_IMAGES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(googleApiHttpClient())
                .build();
    }


    OkHttpClient googleApiHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(chain -> {
            Request original = chain.request();
            HttpUrl url = original.httpUrl().newBuilder()
                    .addQueryParameter("alt", "json")
                    .addQueryParameter("searchType", "image")
                    .addQueryParameter("cx", Constants.GOOGLE_API_CX)
                    .addQueryParameter("key", Constants.GOOGLE_API_KEY)
                    .build();

            Request request = original.newBuilder()
                    .url(url)
                    .build();
            return chain.proceed(request);
        });
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        return client;
    }
}
