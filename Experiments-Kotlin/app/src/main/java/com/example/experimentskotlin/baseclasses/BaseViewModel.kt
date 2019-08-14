package com.example.experimentskotlin.baseclasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corenetwork.Api
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    //Binding
    val showAlertBinding: MutableLiveData<Throwable> = MutableLiveData()
    val showAlertStringBinding: MutableLiveData<String> = MutableLiveData()
    val showLoadingBinding: MutableLiveData<Unit> = MutableLiveData()
    val hideLoadingBinding: MutableLiveData<Unit> = MutableLiveData()

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
        showLoadingBinding.postValue(Unit)
    }

    fun hideLoading() {
        hideLoadingBinding.postValue(Unit)
    }

}