package com.example.experimentskotlin.util

import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.experimentskotlin.baseclasses.BaseFragment
import com.example.experimentskotlin.baseclasses.BaseViewModel
import com.example.experimentskotlin.util.extensions.observe

/**
 * Интерфейс для Fragment с SwipeRefresh
 */
interface SwipeRefreshFragmentInterface{
    fun getViewModel(): SwipeRefreshViewModelInterface
    fun getSwipeRefreshLayout(): SwipeRefreshLayout
    /**
     * должен вызываться после super.bindData()
     */
    fun bindSwipeRefreshLoading(fragment: BaseFragment, refreshHandler:()->Unit, hideLoadingHandler:(()->Unit)? = null){
        getSwipeRefreshLayout().setOnRefreshListener(refreshHandler)
        getViewModel().baseLoadingBinding.observe(fragment){
            fragment.showLoading()
        }
        val baseViewModel = (getViewModel() as? BaseViewModel)
        if (baseViewModel != null) {
            baseViewModel.showLoadingBinding.removeObservers(fragment)
            baseViewModel.showLoadingBinding.observe(fragment){
                getSwipeRefreshLayout().isRefreshing = true
            }
            baseViewModel.hideLoadingBinding.removeObservers(fragment)
            baseViewModel.hideLoadingBinding.observe(fragment){
                getSwipeRefreshLayout().isRefreshing = false
                fragment.hideLoading()
                hideLoadingHandler?.invoke()
            }
        }
    }
    fun handleShowAlert(){
        getSwipeRefreshLayout().isRefreshing = false
    }

}



/**
 * Интерфейс для ViewModel с SwipeRefresh
 */
interface SwipeRefreshViewModelInterface{
    var baseLoadingBinding: MutableLiveData<Unit>
    fun showBaseLoading(){
        baseLoadingBinding.postValue(Unit)
    }
}