package com.example.experimentskotlin.baseclasses

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corenetwork.Api
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel : ViewModel() {
    //region Binding
    val showAlertBinding = MutableLiveData<Throwable>()
    val showAlertStringBinding = MutableLiveData<String>()
    val showLoadingBinding = MutableLiveData(false)

    open fun onCreate() {}
    open fun onResume() {}
    //endregion

    //region Others
    protected lateinit var context: Context
    var api = Api.getInstance()
    val compositeDisposable = CompositeDisposable()

    fun setup(context: Context) {
        this.context = context
    }

    fun showAlert(e: Throwable) {
        showAlertBinding.postValue(e)
    }

    fun showAlert(text: String) {
        showAlertStringBinding.postValue(text)
    }

    fun showLoading() {
        showLoadingBinding.postValue(true)
    }

    fun hideLoading() {
        showLoadingBinding.postValue(false)
    }

    fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
    //endregion
}