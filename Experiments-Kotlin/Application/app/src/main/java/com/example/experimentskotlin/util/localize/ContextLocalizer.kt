package com.example.experimentskotlin.util.localize

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Методы для перевода строк
 *
 * Позволяют использовать Restring на версиях Android < 5
 */
fun Context.getLocalizedString(@StringRes id: Int): String {
    return this.resources.getLocalizedString(id)
}

fun Fragment.getLocalizedString(@StringRes id: Int): String {
    return this.resources.getLocalizedString(id)
}

fun Resources.getLocalizedString(@StringRes id: Int): String {
    return try {
        val stringKey = getResourceEntryName(id)
        throw Error("Not implemented")
//        MyPhilologyRepositoryFactory.repository.getText(stringKey)!!.toString()
    } catch (e: Exception) {
        getString(id)
    }
}