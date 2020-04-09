package com.example.experimentskotlin.baseclasses

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
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

    override fun onDestroy() {
        super.onDestroy()
        viewModels.forEach { it.onDestroy() }
    }
    //endregion

    //region DataBinding
    protected abstract fun bindData()

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
        val vm = ViewModelProviders.of(this).get(vmClass)
        if (vm is BaseViewModel) {
            bindViewModel(vm)
            vm.setup(context!!)
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

    protected fun isLoading(): Boolean {
        return viewModels.map { it.showLoadingBinding.value }.contains(true)
    }

    private fun loadingIndicatorStateChanged() {
        val isNeedShowLoading = isLoading()
        try {
            if (isNeedShowLoading)
                showLoading()
            else
                hideLoading()
        } catch (e: Exception) {
        }
    }
    //endregion
}