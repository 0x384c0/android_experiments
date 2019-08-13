package com.example.corenetwork

import com.example.corenetwork.model.WikiaQueryResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WikiaApiInterface {
    @GET("api.php")
    fun query(
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("titles") titles: String
    ): Observable<WikiaQueryResponse.Response>

    companion object {
        fun create(): WikiaApiInterface {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient
                .Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://kancolle.fandom.com/")
                .client(client)
                .build()

            return retrofit.create(WikiaApiInterface::class.java)
        }
    }
}