package com.example.corenetwork.model

import com.google.gson.Gson
import retrofit2.HttpException

data class ErrorResponse(
    val error: String?
) {
    fun getErrorMessage(): String? {
        return error
    }

    companion object {
        fun create(e: Throwable): ErrorResponse? {
            return try {
                val jsonString = (e as? HttpException)?.response()?.errorBody()?.string()
                Gson().fromJson(jsonString, ErrorResponse::class.java)
            } catch (e: Throwable) {
                null
            }
        }
    }
}
