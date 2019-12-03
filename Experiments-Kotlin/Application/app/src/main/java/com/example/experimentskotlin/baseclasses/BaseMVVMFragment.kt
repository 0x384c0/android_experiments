package com.example.experimentskotlin.baseclasses

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.experimentskotlin.util.extensions.observe


/**
 * базовый класс для фрагмента с MVVMF
 */
abstract class BaseMVVMFragment : BaseFragment() {

    //region LifeCycle
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()
    }

    override fun onResume() {
        super.onResume()
        callOnCreateIfNeeded()
        viewModels.forEach { it.onResume() }
    }
    //endregion

    //region DataBinding
    internal abstract fun bindData()

    private var onCreateWasNotCalled = true
    private fun callOnCreateIfNeeded() {
        if (onCreateWasNotCalled) {
            viewModels.forEach { it.onCreate() }
            onCreateWasNotCalled = false
        }
    }
    //endregion

    //region Internal
    private val viewModels = mutableListOf<BaseViewModel>()

    protected fun <T : ViewModel> getViewModel(vmClass: Class<T>): T {
        val vm = ViewModelProvider(this).get<T>(vmClass)
        if (vm is BaseViewModel) {
            bindViewModel(vm)
        }
        return vm
    }

    private fun bindViewModel(viewModel: BaseViewModel) {
        if (!viewModels.contains(viewModel))
            viewModels.add(viewModel)
        viewModel.showAlertBinding.observe(this) { showAlert(it) }
        viewModel.showAlertStringBinding.observe(this) { showAlert(it) }
        viewModel.showLoadingBinding.observe(this) { loadingIndicatorStateChanged() }
    }

    private fun loadingIndicatorStateChanged() {
        val isNeedShowLoading = viewModels.map { it.showLoadingBinding.value }.contains(true)
        if (isNeedShowLoading)
            showLoading()
        else
            hideLoading()
    }
    //endregion
}