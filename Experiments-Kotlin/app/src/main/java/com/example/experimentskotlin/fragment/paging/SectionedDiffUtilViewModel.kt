package com.example.experimentskotlin.fragment.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.corenetwork.extension.subscribeOnMain
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.baseclasses.BaseViewModel
import com.example.experimentskotlin.util.infinity_scroll.PaginationManager
import io.reactivex.Observable

class SectionedDiffUtilViewModel : BaseViewModel() {
    val recyclerViewDataBinding = MutableLiveData<List<ArticleItem>>()

    //region LifeCycle
    override fun onResume() {
        super.onResume()
        paginationManager.onResume()
    }
    //endregion

    //region Others
    fun refresh() {
        paginationManager.refresh()
    }
    //endregion


    //region Pagination
    private var canLoadMore = true
    val paginationManager = PaginationManager<ArticleItem, Int>(
            dataBinding = recyclerViewDataBinding,
            getNextPage = { if (it != null) it + 1 else 0 },
            getDataObservable = { page ->
                getDataObservable(page)
            },
            subscribe = { observable ->
                observable.subscribeOnMain(
                        onNext = { result ->
                            recyclerViewDataBinding.postValue(result)
                            hideLoading()
                        }
                )
            },
            showLoading = this::showLoading,
            isCanLoadMore = { canLoadMore }
    )
    //endregion

    //region Data Generator
    private fun getDataObservable(page: Int): Observable<List<ArticleItem>> {
        Log.e("getDataObservable", "$page")
        return Observable.just(getData(page))
    }

    private fun getData(page: Int): List<ArticleItem> {
        return getItemsInRange((page * 100)..((page + 1) * 100 - 1))
    }

    private fun getItemsInRange(range: IntRange): List<ArticleItem> {
        return range.toList().map { ArticleItem(it, it.toString(), it.toString(), it) }
    }
    //endregion
}