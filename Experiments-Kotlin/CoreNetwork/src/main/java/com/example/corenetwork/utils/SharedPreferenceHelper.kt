package com.example.corenetwork.utils

import android.content.Context
import android.util.Log
import com.example.corenetwork.Api
import java.lang.reflect.Type

/**
 * класс хелпер для работы с SharedPreferences
 *
 * сериализует обекты через Gson
 */
class SharedPreferenceHelper(name: String, private val password: String? = null, context: Context) {
    //    private val prefsEncrypted by lazy {
//        EncryptedPreferences
//            .Builder(context)
//            .withPreferenceName(name)
//            .withEncryptionPassword(password)
//            .build()
//    }
    private val prefs by lazy { context.getSharedPreferences(name, Context.MODE_PRIVATE) }

    private fun prefsGetString(key: String): String? {
        return if (password == null)
            prefs.getString(key, null)
        else
            prefs.getString(key, null)
//            prefsEncrypted.getString(key, null)
    }

    private fun prefsEditPutString(key: String, value: String?) {
        if (password == null)
            prefs.edit().putString(key, value).apply()
        else
            prefs.getString(key, null)
//            prefsEncrypted.edit().putString(key, value).apply()
    }

    private fun prefsEditRemove(key: String) {
        if (password == null)
            prefs.edit().remove(key).apply()
        else
            prefs.getString(key, null)
//            prefsEncrypted.edit().remove(key).apply()
    }


    private val gson = Api.createGson()

    //region Primitives
    fun getString(key: String): String? {
        return prefsGetString(key)
    }

    fun putString(key: String, value: String?) {
        if (value != null)
            prefsEditPutString(key, value)
        else
            prefsEditRemove(key)
    }

    //endregion
    //region serialization
    fun <T> getValue(key: String, type: Type): T? {
        return try {
            val jsonString = prefsGetString(key)
            if (!jsonString.isNullOrBlank()) {
                gson.fromJson<T>(jsonString, type)
            } else {
                null
            }
        } catch (e: Throwable) {
            Log.e("SharedPreferenceHelper", "getValue", e)
            null
        }
    }

    fun <T> setValue(key: String, data: T?) {
        if (data != null) {
            val jsonString = gson.toJson(data)
            prefsEditPutString(key, jsonString)
        } else {
            prefsEditRemove(key)
        }
    }
    //endregion
}