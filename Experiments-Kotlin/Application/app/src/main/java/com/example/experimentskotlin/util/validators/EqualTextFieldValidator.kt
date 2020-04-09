package com.example.experimentskotlin.util.validators

import androidx.databinding.ObservableField

/**
 * валидатор для полей с одинаковым текстом
 */
class EqualTextFieldValidator(
    field: ObservableField<String?>,
    errorField: ObservableField<String?>,
    emptyErrorMessage: String,
    private val otherField: ObservableField<String?>,
    private val mustBeEqualErrorMessage: String
) : EmptyStringObservableFieldValidator(
    field = field,
    errorField = errorField,
    errorMessage = emptyErrorMessage
) {
    override fun isSameField(field: ObservableField<*>): Boolean {
        return super.isSameField(field) || field === otherField
    }

    override fun isValid(): Boolean {
        val isNotNull = super.isValid()
        return if (isNotNull)
            getInvalidSumErrorMessage(field.get()!!) == null
        else
            isNotNull
    }

    override fun validate(): Boolean {
        val isNotNull = super.isValid()
        return if (isNotNull)
            validateText(field.get()!!)
        else
            super.validate()
    }


    private fun getInvalidSumErrorMessage(text: String): String? {
        try {
            if (text != otherField.get())
                return mustBeEqualErrorMessage
        } catch (it: Throwable) {
        }
        return null
    }

    private fun validateText(text: String): Boolean {
        val invalidErrorMessage = getInvalidSumErrorMessage(text)
        return if (invalidErrorMessage == null) {
            errorField?.set(null)
            true
        } else {
            errorField?.set(invalidErrorMessage)
            false
        }
    }
}