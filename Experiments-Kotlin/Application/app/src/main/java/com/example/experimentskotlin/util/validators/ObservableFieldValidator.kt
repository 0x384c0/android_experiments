package com.example.experimentskotlin.util.validators

import androidx.databinding.ObservableField

/**
 * валидатор ObservableFieldValidator
 */
interface ObservableFieldValidator {
    fun validate(): Boolean
    fun isValid(): Boolean
    fun isEmpty(): Boolean
    fun reset()
    fun isSameField(field: ObservableField<*>): Boolean
}

/**
 * валидатор ObservableFieldValidator
 */
open class BaseObservableFieldValidator<T>(
    internal val field: ObservableField<T?>,
    internal val errorField: ObservableField<String?>?,
    internal val errorMessage: String?,
    internal val errorMessageHandler: (() -> String?) = { errorMessage },
    internal val validator: (T?) -> Boolean,
    internal val isEmptyValidator: (T?) -> Boolean
) : ObservableFieldValidator {
    override fun validate(): Boolean {
        val result = if (isValid()) {
            reset()
            true
        } else {
            if (!isAlreadyValidated())
                errorField?.set(errorMessageHandler())
            false
        }
        validateNeverCalled = false
        return result
    }

    internal var validateNeverCalled = true
    internal var previousValidateValue: T? = null
    open fun isAlreadyValidated(): Boolean {
        return if (validateNeverCalled)
            false
        else {
            val fieldValue = field.get()
            val alreadyValidated = fieldValue == previousValidateValue
            previousValidateValue = fieldValue
            alreadyValidated
        }
    }


    override fun isValid(): Boolean {
        return validator(field.get())
    }

    override fun isEmpty(): Boolean {
        return isEmptyValidator(field.get())
    }

    override fun reset() {
        errorField?.set(null)
        validateNeverCalled = true
    }

    override fun isSameField(field: ObservableField<*>): Boolean {
        return this.field === field
    }

}

/**
 * валидатор ObservableFieldValidator
 */
open class EmptyStringObservableFieldValidator(
    field: ObservableField<String?>,
    errorField: ObservableField<String?>?,
    errorMessage: String?

) : BaseObservableFieldValidator<String>(
    field = field,
    errorField = errorField,
    errorMessage = errorMessage,
    validator = { !it.isNullOrBlank() },
    isEmptyValidator = { field.get().isNullOrBlank() }
) {
    override fun isAlreadyValidated(): Boolean {
        return if (validateNeverCalled)
            false
        else {
            val fieldValue = field.get() ?: ""
            val alreadyValidated =
                fieldValue == previousValidateValue && !errorField?.get().isNullOrBlank()
            previousValidateValue = fieldValue
            alreadyValidated
        }
    }
}


/**
 * валидатор ObservableFieldValidator
 */
open class NullableObjectObservableFieldValidator<T>(
    field: ObservableField<T?>,
    errorField: ObservableField<String?>?,
    errorMessage: String?

) : BaseObservableFieldValidator<T>(
    field = field,
    errorField = errorField,
    errorMessage = errorMessage,
    validator = { it != null },
    isEmptyValidator = { it == null }
)


/**
 * валидатор ObservableFieldValidator
 */
class StringHandlerObservableFieldValidator(
    field: ObservableField<String?>,
    errorField: ObservableField<String?>?,
    errorMessage: String?,
    errorMessageHandler: (() -> String?) = { errorMessage },
    validator: (String?) -> Boolean
) : BaseObservableFieldValidator<String>(
    field = field,
    errorField = errorField,
    errorMessage = errorMessage,
    errorMessageHandler = errorMessageHandler,
    validator = { validator(it) },
    isEmptyValidator = { it.isNullOrBlank() }
) {
    override fun isAlreadyValidated(): Boolean {
        return if (validateNeverCalled)
            false
        else {
            val fieldValue = field.get() ?: ""
            val alreadyValidated =
                fieldValue == previousValidateValue && !errorField?.get().isNullOrBlank()
            previousValidateValue = fieldValue
            alreadyValidated
        }
    }
}