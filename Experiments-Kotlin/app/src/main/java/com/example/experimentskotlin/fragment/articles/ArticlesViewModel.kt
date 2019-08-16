package com.example.experimentskotlin.fragment.articles

import androidx.lifecycle.MutableLiveData
import com.example.corenetwork.Constants
import com.example.corenetwork.extension.disposedBy
import com.example.corenetwork.extension.subscribeOnMain
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.baseclasses.BaseViewModel
import com.example.experimentskotlin.util.infinity_scroll.InfinityScrollManager
import com.example.experimentskotlin.util.infinity_scroll.PaginationManager

class ArticlesViewModel : BaseViewModel() {
    //region UI Binding
    var recyclerViewDataBinding = MutableLiveData<List<ArticleItem>>()
    //endregion

    //region LifeCycle
    override fun onResume() {
        super.onResume()
        paginationManager.onResume()
    }
    //endregion

    //region Others
    private var offset: String? = null

    fun refresh() {
        offset = null
        paginationManager.refresh()
    }
    //endregion

    //region Pagination
    private var canLoadMore = true
    val paginationManager = PaginationManager<ArticleItem, String>(
            dataBinding = recyclerViewDataBinding,
            getNextPage = { if (it == null) "null" else offset ?: "null"},
            getDataObservable = { page ->
                api.getArticlesList(if (page == "null") null else page)
                        .map {
                            canLoadMore = it.items.isNotEmpty()
                            offset = it.offset
                            it.items
                        }
            },
            subscribe = { observable ->
                observable.subscribeOnMain(
                        onNext = { result ->
                            recyclerViewDataBinding.postValue(result)
                            hideLoading()
                        },
                        onError = {
                            showAlert(it)
                        }
                )
            },
            showLoading = this::showLoading,
            isCanLoadMore = { canLoadMore }
    )
    //endregion

}