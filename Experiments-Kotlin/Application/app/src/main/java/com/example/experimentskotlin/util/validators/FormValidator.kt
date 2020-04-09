package com.example.experimentskotlin.util.validators

import android.os.Handler
import androidx.databinding.ObservableField
import com.example.experimentskotlin.UIConstants

/**
 * Класс хелпер для валидации формы
 */
class FormValidator(
    validators: List<ObservableFieldValidator>,
    autoValidateFields: List<ObservableField<*>>? = null
) {
    private var validators: MutableList<ObservableFieldValidator> = validators.toMutableList()

    val formValidator = this
    //region Public
    val propertyChangedCallback =
        object : androidx.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(
                sender: androidx.databinding.Observable?,
                propertyId: Int
            ) {
                if (formValidator.isEnabled())
                    onFieldChanged(sender as ObservableField<*>)
            }
        }

    /**
     * проверить перед отправкой
     */
    fun validateBeforeSubmit(): Boolean {
        return validateAllFields(true)
    }


    /**
     * все верно?
     */
    fun isAllValid(): Boolean {
        var isAllValid = true
        for (v in validators) {
            isAllValid = isAllValid && v.isValid()
        }
        return isAllValid
    }

    /**
     * сброс
     */
    fun reset() {
        Handler().postDelayed({
            for (v in validators) {
                v.reset()
            }
        }, UIConstants.FORM_VALIDATOR_RESET_DELAY_MILLISECONDS)
    }

    /**
     * проверить поле
     */
    fun <T> validateField(field: ObservableField<T>) {
        onFieldChanged(field)
    }

    /**
     * добавить валидатор
     */
    fun addValidator(validator: ObservableFieldValidator) {
        if (!validators.contains(validator))
            validators.add(validator)
    }

    /**
     * удалить валидатор
     */
    fun removeValidator(validator: ObservableFieldValidator) {
        validators.remove(validator)
    }

    /**
     * деактивация валидатора
     */
    fun disable() {
        isEnabled = false
    }

    /**
     * активация валидатора
     */
    fun enable() {
        isEnabled = true
    }
    //endregion

    //region Private
    /**
     * активирован валидатор?
     */
    private fun isEnabled(): Boolean {
        return isEnabled
    }

    /**
     * поле изменилось
     */
    private fun <T> onFieldChanged(field: ObservableField<T>) {
        for (v in validators) {
            if (v.isSameField(field)) {
                v.validate()
                return
            }
        }
    }

    @Suppress("LocalVariableName")
    /**
     * проверить все поля
     */
    private fun validateAllFields(showErrorIfAllEmpty: Boolean = false): Boolean {
        var isAllEmpty = true
        var isAllValid = true

        for (v in validators) {
            val vIsEmpty = v.isEmpty()
            val vIsValid = v.isValid()
            isAllEmpty = isAllEmpty && vIsEmpty
            isAllValid = isAllValid && vIsValid
        }

        if (isAllEmpty && !showErrorIfAllEmpty) {
            reset()
        } else {
            for (v in validators) {
                v.validate()
            }
        }
        return isAllValid
    }
    //endregion

    companion object {
        private var isEnabled = true
    }

    init {
        if (autoValidateFields != null) {
            for (v in autoValidateFields) {
                v.addOnPropertyChangedCallback(propertyChangedCallback)
            }
        }
    }
}
