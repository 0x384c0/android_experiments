package com.example.experimentskotlin.helper

import androidx.databinding.Observable
import androidx.databinding.ObservableField

internal object CommonHelper {
    private val leadingRegex = Regex("^[0]+")
    private val zeroDotRegex = Regex("^0\\.")
    private val dotRegex = Regex("^\\.")
    @Suppress("UNCHECKED_CAST")
    private val leadingZerosValidator = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            try {
                val field = sender as? ObservableField<String?>
                if (field != null)
                    removeZerosIfNeeded(field)
            } catch (it: Throwable) {
            }
        }
    }

    fun addLeadingZerosValidator(field: ObservableField<String?>) {
        field.removeOnPropertyChangedCallback(leadingZerosValidator)
        field.addOnPropertyChangedCallback(leadingZerosValidator)
    }

    private fun removeZerosIfNeeded(field: ObservableField<String?>) {
        val string = field.get()
        if (string != null && leadingRegex.containsMatchIn(string) && !zeroDotRegex.containsMatchIn(string))
            field.set(removeZeros(string))
    }


    private fun removeZeros(text: String): String? {
        var newString = leadingRegex.replace(text, "")
        if (dotRegex.containsMatchIn(newString))
            newString = "0$newString"
        return newString
    }
}