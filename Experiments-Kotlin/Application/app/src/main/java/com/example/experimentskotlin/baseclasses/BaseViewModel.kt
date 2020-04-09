package com.example.experimentskotlin.baseclasses

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corenetwork.Api
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    //region Binding
    val showAlertBinding = MutableLiveData<Throwable>()
    val showAlertStringBinding = MutableLiveData<String>()
    val showLoadingBinding = MutableLiveData(false)
    val loadingShown = ObservableField(false)

    open fun onCreate() {}
    open fun onResume() {}
    open fun onDestroy() {
        compositeDisposable.dispose()
    }
    //endregion

    //region Others
    protected lateinit var context: Context
    protected var api = Api.getInstance()
    protected val compositeDisposable = CompositeDisposable()

    fun setup(context: Context) {
        this.context = context
    }

    fun showAlert(e: Throwable) {
        Log.e("BaseViewModel", "showAlert", e)
        showAlertBinding.postValue(e)
    }

    fun showAlert(text: String) {
        showAlertStringBinding.postValue(text)
    }

    fun showLoading() {
        loadingShown.set(true)
        showLoadingBinding.postValue(true)
    }

    fun hideLoading() {
        loadingShown.set(false)
        showLoadingBinding.postValue(false)
    }

    fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
    //endregion
}