package com.example.experimentskotlin.baseclasses

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.experimentskotlin.util.extensions.observe


/**
 * базовый класс для фрагмента с MVVMF
 */
abstract class BaseMVVMFragment<T : BaseViewModel> : BaseFragment() {
    abstract val viewModelClass: Class<T>


    //region LifeCycle
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(viewModelClass)
        bindData()
    }

    override fun onResume() {
        super.onResume()
        callOnCreateIfNeeded()
        viewModel.onResume()
    }
    //endregion

    //region DataBinding
    private lateinit var baseViewModel: BaseViewModel
    @Suppress("UNCHECKED_CAST")
    protected var viewModel: T
        get() = baseViewModel as T
        set(value) {
            baseViewModel = value
        }

    internal open fun bindData() {
        baseViewModel.showAlertBinding.observe(this)  { showAlert(it) }
        baseViewModel.showAlertStringBinding.observe(this) { showAlert(it) }
        baseViewModel.showLoadingBinding.observe(this) { showLoading() }
        baseViewModel.hideLoadingBinding.observe(this) { hideLoading() }
    }

    private var onCreateWasNotCalled = true
    private fun callOnCreateIfNeeded(){
        if (onCreateWasNotCalled) {
            viewModel.onCreate()
            onCreateWasNotCalled = false
        }
    }
    //endregion
}