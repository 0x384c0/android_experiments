package com.example.corenetwork

import java.util.*

/**
 *  хранилище различных констант
 */
@Suppress("MemberVisibilityCanBePrivate")
class Constants {
    companion object {
        const val SERVER_BASE_URL = "https://kancolle.fandom.com/"
        const val WEEK_DATE_DATE_TIME_FORMAT = "EEE, dd MMM yyyy hh:mm:ss 'GMT'"
        const val SHORT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val DATE_FORMAT = "yyyy-MM-dd"
        val DATE_FORMATS = listOf(WEEK_DATE_DATE_TIME_FORMAT,SHORT_DATE_TIME_FORMAT)
        const val TIME_BEFORE_TOKEN_UPDATE_MIN = 2
        val LOCALES_MAP = mapOf("ru" to "ru-ru","en" to "en-us","ky" to "ky-kg")
        val LOCALES = LOCALES_MAP.keys.toTypedArray()
        val DEFAULT_LOCALE = LOCALES.first()
        const val DEBOUNCE_INPUT_TIME = 2000L
        const val FIRST_PAGE_NUMBER = 1
        const val PAGE_SIZE = 10
        val DEFAULT_TIME_ZONE = TimeZone.getTimeZone("Asia/Bishkek")
    }
}