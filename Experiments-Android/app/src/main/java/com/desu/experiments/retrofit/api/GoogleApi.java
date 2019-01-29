package com.desu.experiments.retrofit.api;

import com.desu.experiments.model.GoogleApi.ImagesResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface GoogleApi {
    @GET("v1")
    Observable<ImagesResponse> get(
            @Query("q") String query,
            @Query("start") int startIndex
    );
}
