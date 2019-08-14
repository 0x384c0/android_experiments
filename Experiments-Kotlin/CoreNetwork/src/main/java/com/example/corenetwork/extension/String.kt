package com.example.corenetwork.extension

/**
 * безопасное получения числа float с удалением пробелов
 */
fun String.toFloatSave():Float{
    return replace(" ","").toFloat()
}