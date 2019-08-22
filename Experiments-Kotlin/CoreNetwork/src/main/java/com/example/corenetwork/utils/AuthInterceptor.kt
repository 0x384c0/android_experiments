package com.example.corenetwork.utils

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Класс Interceptor
 *
 * Добавляет  Authorization Header к запросам
 */
class AuthInterceptor(private val authDataProvider:AuthDataProvider):Interceptor {
    private val exceptions = listOf("auth_urls")
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var skip = false
        val path = request.url.toUri().path.toString()
        exceptions.forEach { skip = skip || path.contains(it) }
        if (!skip) {
            val accessToken = authDataProvider.currentUser?.accessToken
            if (accessToken != null) {
                request = request
                    .newBuilder()
                    .addHeader("Authorization", "bearer $accessToken")
                    .build()
            }
        }
        return chain.proceed(request)
    }

}