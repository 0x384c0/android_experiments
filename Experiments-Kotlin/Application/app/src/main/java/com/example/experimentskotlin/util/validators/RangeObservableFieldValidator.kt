package com.example.experimentskotlin.util.validators

import androidx.databinding.ObservableField

/**
 * валидатор числового поля диапазоном
 */
open class RangeObservableFieldValidator(
    field: ObservableField<String?>,
    errorField: ObservableField<String?>?,
    emptyErrorMessage: String?,
    private val getInvalidRangeString:() -> String?,
    private val getMin: () -> Double?,
    private val getMax: () -> Double?
) : EmptyStringObservableFieldValidator(
    field = field,
    errorField = errorField,
    errorMessage = emptyErrorMessage
) {
    override fun isValid(): Boolean {
        val isNotNull = super.isValid()
        return if (isNotNull)
            getInvalidNumberErrorMessage(field.get()!!) == null
        else
            isNotNull
    }

    override fun validate(): Boolean {
        val isNotNull = super.isValid()
        return if (isNotNull)
            validateNumber(field.get()!!)
        else
            super.validate()
    }

    private fun getInvalidNumberErrorMessage(text: String): String? {
        try {
            val number = text.toDouble()
            val min = getMin()!!
            val max = getMax()!!
            val valid = number in min..max
            if (!valid)
                return getInvalidRangeString()
        } catch (it: Throwable) {
        }
        return null
    }

    private fun validateNumber(text:String): Boolean {
        val invalidErrorMessage = getInvalidNumberErrorMessage(text)
        return if (invalidErrorMessage == null) {
            errorField?.set(null)
            true
        } else {
            errorField?.set(invalidErrorMessage)
            false
        }
    }
}