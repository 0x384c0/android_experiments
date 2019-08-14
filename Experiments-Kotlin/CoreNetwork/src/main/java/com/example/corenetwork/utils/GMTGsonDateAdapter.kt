package com.example.corenetwork.utils

import com.example.corenetwork.Constants
import com.google.gson.*
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Adapter для Retrofit
 * пытается парсить даты, перебирая известные форматы дат
 */
abstract class BaseGsonDateAdapter<T : Date> : JsonSerializer<T?>, JsonDeserializer<T?> {

    private val stringToDateFormats: List<DateFormat>
    private val dateToStringFormat: SimpleDateFormat

    abstract fun getTimezone(): TimeZone
    abstract fun parse(string: String?, dateFormat: DateFormat): T?

    init {
        stringToDateFormats = Constants.DATE_FORMATS.map {
            val dateFormat = SimpleDateFormat(it, Locale.US)
            dateFormat.timeZone = getTimezone()
            dateFormat
        }
        dateToStringFormat = SimpleDateFormat(Constants.SHORT_DATE_TIME_FORMAT, Locale.US)
        @Suppress("LeakingThis")
        dateToStringFormat.timeZone = getTimezone()
    }

    override fun serialize(src: T?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(dateToStringFormat.format(src))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): T? {
        for (dateFormat in stringToDateFormats) {
            try {
                return parse(json?.asString, dateFormat)
            } catch (e: ParseException) {
                continue
            }
        }
        return null
    }
}

class DefaultTimeZoneSimpleDateFormat(
    pattern: String,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = Constants.DEFAULT_TIME_ZONE
) : SimpleDateFormat(pattern, locale) {

    constructor(
        pattern: String,
        locale: Locale = Locale.getDefault()
    ) : this(
        pattern,
        locale,
        Constants.DEFAULT_TIME_ZONE
    )

    init {
        this.timeZone = timeZone
    }
}

/**
 * Adapter для Retrofit с часовый поясом сервера бака
 */
class DefaultTimeZoneGsonDateAdapter : BaseGsonDateAdapter<Date>() {
    override fun getTimezone(): TimeZone {
        return Constants.DEFAULT_TIME_ZONE
    }

    override fun parse(string: String?, dateFormat: DateFormat): Date? {
        return dateFormat.parse(string)
    }
}


/**
 * Adapter для Retrofit GMT
 */
class GMTGsonDateAdapter : BaseGsonDateAdapter<GMTTimeZoneDate>() {
    override fun getTimezone(): TimeZone {
        return TimeZone.getTimeZone("GMT")
    }

    override fun parse(string: String?, dateFormat: DateFormat): GMTTimeZoneDate? {
        return GMTTimeZoneDate(dateFormat.parse(string))
    }
}

/**
 * Тип даты с GMT
 * нужен для  GMTGsonDateAdapter
 */
class GMTTimeZoneDate(date: Date) : Date(date.time) {
    companion object {
        fun new(date: Date?): GMTTimeZoneDate? {
            return if (date != null)
                GMTTimeZoneDate(date)
            else
                null
        }
    }
}