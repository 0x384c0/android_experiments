package com.example.experimentskotlin.baseclasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corenetwork.Api
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    //Binding
    val showAlertBinding = MutableLiveData<Throwable>()
    val showAlertStringBinding = MutableLiveData<String>()
    val showLoadingBinding = MutableLiveData(false)

    open fun onCreate(){}
    open fun onResume(){}

    //Others
    var api = Api.getInstance()
    val compositeDisposable = CompositeDisposable()



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

}