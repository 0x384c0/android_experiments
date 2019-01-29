package com.desu.experiments.retrofit.api;


import com.desu.experiments.model.JSONResponse.JSONResponse;
import com.squareup.okhttp.ResponseBody;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface RetrofitApis {

    @GET("points")
    Observable<JSONResponse> getPointsRX();

    @GET("weather")
    Observable<Response<ResponseBody>> get(
            @Query("q") String query/*,
            @Query("AppId") String AppId*/
    );
}
