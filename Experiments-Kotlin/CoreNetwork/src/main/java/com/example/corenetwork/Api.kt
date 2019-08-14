package com.example.corenetwork

import com.example.corenetwork.model.WikiaQueryResponse
import com.example.corenetwork.model.authsettings.UserInfo
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface Api {
    @GET("api.php")
    fun query(
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("titles") titles: String
    ): Observable<WikiaQueryResponse.Response>


    @GET("api.php/dictionaries")
    fun getDictionaries(localeSlug:String): Observable<ResourceDict>

    @POST("api.php/login")
    fun login(
        name: String,
        encryptedPassword: String
    ): Observable<UserInfo>

    @POST("api.php/changePass")
    fun changePass(
        oldPasswordEncrypted: String,
        newPasswordEncrypted: String
    ): Observable<UserInfo>

    @POST("api.php/refreshToken")
    fun refreshToken(refreshToken: String): Observable<UserInfo>

    companion object {
        private lateinit var instance: Api

        fun getInstance(): Api {
            return instance
        }

        fun init() {
            instance = create()
        }

        private fun create(): Api {
            val client = getClientBuilderWithAuth()
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.SERVER_BASE_URL)
                .client(client)
                .build()

            return retrofit.create(Api::class.java)
        }

        fun getClientBuilderWithAuth():OkHttpClient.Builder{

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient
                .Builder()
                .addInterceptor(logging)
        }
    }
}