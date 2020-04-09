package com.example.corenetwork

import android.content.Context
import com.example.corenetwork.utils.SharedPreferenceHelper
import com.google.gson.reflect.TypeToken

class CacheManager(context: Context) :
    LocaleManager.DictionariesProvider {
    private val prefsHelper = SharedPreferenceHelper(
        name = "kg.kompanion.mpp.cache",
        context = context
    )

    //region DictionariesProvider
    override fun isHasLocaleDictionary(locale: String): Boolean {
        return !prefsHelper.getString("locale$locale").isNullOrBlank()
    }

    override fun getLocaleDictionary(locale: String): LocaleManager.LocaleDictionary? {
        return prefsHelper.getValue(
            key = "locale$locale",
            type = object : TypeToken<LocaleManager.LocaleDictionary>() {}.type
        )
    }

    override fun saveLocaleDictionary(localeDictionary: LocaleManager.LocaleDictionary) {
        prefsHelper.setValue("locale${localeDictionary.locale}", localeDictionary)
    }
    //endregion

    companion object {
        //singleton
        private lateinit var preferencesManager: CacheManager

        /**
         * Инициализация менеджера, должен вызваться после CacheManager
         */
        fun getInstance(): CacheManager {
            return preferencesManager
        }

        /**
         * @return singleton инстанс инстанс инициализированного менеджера в init()
         */
        fun init(context: Context) {//must be called first
            preferencesManager = CacheManager(context)
        }
    }
}