package com.example.experimentskotlin.fragment.paging

import com.example.corenetwork.extension.disposedBy
import com.example.corenetwork.extension.subscribeOnMain
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.baseclasses.BaseViewModel
import com.example.experimentskotlin.util.adapters.paging.PageListLoadedCallback
import com.example.experimentskotlin.util.adapters.paging.PagedListSingleLayoutData

class PagingViewModel : BaseViewModel() {
    lateinit var pagedRecyclerViewData: PagedListSingleLayoutData<String, ArticleItem>
    fun setup() {
        pagedRecyclerViewData = PagedListSingleLayoutData(
                load = this::loadPage,
                areItemsTheSame = { oldItem, newItem ->
                    oldItem.id == newItem.id
                }
        )
    }

    private fun loadPage(page: String?, loaded: PageListLoadedCallback<String, ArticleItem>) {
        api.getArticlesList(page)
                .subscribeOnMain(
                        onNext = {
                            loaded(it.items, it.offset)
                        },
                        onError = this::showAlert
                )
                .disposedBy(compositeDisposable)
    }
}