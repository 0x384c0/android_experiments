package com.example.corenetwork.extension

import com.example.corenetwork.utils.DefaultTimeZoneSimpleDateFormat
import java.util.*

fun Date.format(format:String, locale: Locale? = null):String{
    return DefaultTimeZoneSimpleDateFormat(format, locale ?: Locale.getDefault()).format(this)
}