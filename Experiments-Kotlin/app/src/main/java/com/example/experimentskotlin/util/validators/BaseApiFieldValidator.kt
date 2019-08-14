package com.example.experimentskotlin.util.validators

import androidx.databinding.ObservableField
import com.example.corenetwork.Constants
import com.example.corenetwork.extension.subscribeOnMain
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Базовый класс валидатора поля с помощью бекенда
 */

abstract class BaseApiFieldValidator<FIELD_DATA, VALIDATION_DATA>(
    internal val field: ObservableField<FIELD_DATA?>,
    private val fieldError: ObservableField<String?>,
    private val validationInfoIsVisible: ObservableField<Boolean>,
    private val validationInfoLoadingIsVisible: ObservableField<Boolean>,
    private val isSubmitButtonEnabled: ObservableField<Boolean>,
    private val emptyErrorMessage: String? = null
) {
    private var disposable: Disposable? = null
    private lateinit var observable: PublishSubject<FIELD_DATA>
    internal var lastValidationResult: VALIDATION_DATA? = null
    internal val fieldChangedCallback = object : androidx.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: androidx.databinding.Observable?, propertyId: Int) {
            fieldValueChanged(field.get())
        }
    }

    open fun setup() {
        field.addOnPropertyChangedCallback(fieldChangedCallback)
        recreateObservable()
    }

    open fun revalidate() {
        fieldValueChanged(field.get())
    }


    private fun recreateObservable() {
        hideLoading()
        disposable?.dispose()
        observable = PublishSubject.create()
        disposable = observable
            .map {
                reset()
                it
            }
            .debounce(Constants.DEBOUNCE_INPUT_TIME, TimeUnit.MILLISECONDS)
            .flatMap<DataMessagePair<VALIDATION_DATA>> { fieldValue ->
                mapValue(fieldValue)
            }
            .subscribeOnMain(
                onNext = {
                    setValidationResult(it)
                },
                onError = {
                    throw it
                }
            )
    }

    internal fun fieldValueChanged(data: FIELD_DATA?) {
        if (isNeedValidateFieldWithApi(data)) {
            try {
                showLoading()
                observable.onNext(data!!)
            } catch (it: Throwable) {
            }
        } else {
            val hasEmptyErrorMessage = isEmpty(data) && emptyErrorMessage != null
            if (hasEmptyErrorMessage)
                fieldError.set(emptyErrorMessage)
            recreateObservable()
            reset(!hasEmptyErrorMessage)
        }
    }

    fun reset(withErrorField:Boolean = true) {
        lastValidationResult = null
        if (withErrorField)
            fieldError.set(null)
        validationInfoIsVisible.set(false)
    }

    private fun setValidationResult(pair: DataMessagePair<VALIDATION_DATA>?) {
        hideLoading()
        val model = pair?.data
        lastValidationResult = model
        if (model != null) {
            fieldError.set(null)
            validationInfoIsVisible.set(true)
            setValidationViewData(model)
        } else {
            fieldError.set(pair?.message)
            validationInfoIsVisible.set(false)
        }
    }

    private fun showLoading() {
        validationInfoLoadingIsVisible.set(true)
        isSubmitButtonEnabled.set(false)
    }

    private fun hideLoading() {
        validationInfoLoadingIsVisible.set(false)
        isSubmitButtonEnabled.set(true)
    }

    data class DataMessagePair<T>(val data: T?, val message: String?)

    fun isEmpty():Boolean{
        return isEmpty(field.get())
    }

    open fun isEmpty(data: FIELD_DATA?): Boolean {
        return data != null
    }

    abstract fun isNeedValidateFieldWithApi(value: FIELD_DATA?): Boolean
    abstract fun isValid(): Boolean
    abstract fun setValidationViewData(data: VALIDATION_DATA)
    abstract fun mapValue(value: FIELD_DATA): Observable<DataMessagePair<VALIDATION_DATA>>
}