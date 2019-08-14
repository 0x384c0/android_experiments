package com.example.corenetwork.extension

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.Fragment
import com.example.corenetwork.external.restring.Restring
import com.example.corenetwork.external.restring.RestringResources

/**
 * Методы для перевода строк
 *
 * Позволяют использовать Restring на версиях Android < 5
 */
fun Context.getLocalizedString(id: Int): String {
    if (this.resources !is RestringResources) {
        try {
            return Restring.wrapContext(this).getString(id)
        } catch (e: Exception) {
        }
    }
    return getString(id)
}

fun Resources.getLocalizedString(id: Int): String {
    if (this !is RestringResources) {
        try {
            return Restring.wrapResources(this).getString(id)
        } catch (e: Exception) {
        }
    }
    return getString(id)
}

fun Fragment.getLocalizedString(id: Int): String {
    if (this.resources !is RestringResources) {
        try {
            return Restring.wrapResources(this.resources).getString(id)
        } catch (e: Exception) {
        }
    }
    return getString(id)
}